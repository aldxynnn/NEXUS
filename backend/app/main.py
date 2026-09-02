from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from pydantic import BaseModel
import bcrypt
import requests
import json
import re

from .database import Base, engine, get_db
from .models import User, Task
from .schemas import (
    UserRegister,
    UserLogin,
    UserResponse,
    TaskCreate,
    TaskResponse
)

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="NEXUS API",
    version="1.0.0"
)


# =========================================================
# PASSWORD SECURITY
# =========================================================

def hash_password(password: str) -> str:
    """
    Hash password menggunakan bcrypt.
    bcrypt memiliki batas maksimal 72 byte.
    """

    password_bytes = password.encode("utf-8")

    if len(password_bytes) > 72:
        raise HTTPException(
            status_code=400,
            detail="Password maksimal 72 byte."
        )

    hashed = bcrypt.hashpw(
        password_bytes,
        bcrypt.gensalt()
    )

    return hashed.decode("utf-8")


def verify_password(
    plain_password: str,
    stored_password: str
) -> bool:
    """
    Verifikasi password.

    - Akun lama:
      password masih plaintext.
    - Akun baru:
      password sudah bcrypt.
    """

    # =====================================================
    # AKUN LAMA
    # Password masih plaintext
    # =====================================================

    if not stored_password.startswith("$2"):
        return plain_password == stored_password

    # =====================================================
    # AKUN BARU
    # Password sudah bcrypt
    # =====================================================

    try:
        password_bytes = plain_password.encode("utf-8")

        if len(password_bytes) > 72:
            return False

        return bcrypt.checkpw(
            password_bytes,
            stored_password.encode("utf-8")
        )

    except Exception as e:

        print(
            "BCRYPT VERIFY ERROR:",
            str(e)
        )

        return False


# =========================================================
# AI CONFIG
# =========================================================

AI_MODEL = "qwen3:8b"
OLLAMA_URL = "http://127.0.0.1:11434/api/chat"


# =========================================================
# AI MODELS
# =========================================================

class AIChatRequest(BaseModel):
    message: str


class AIChatResponse(BaseModel):
    response: str


class AIPlanRequest(BaseModel):
    goal: str


class AIPlanTask(BaseModel):
    title: str
    time: str
    duration: str
    category: str


class AIPlanResponse(BaseModel):
    tasks: list[AIPlanTask]


class AIInsightTask(BaseModel):
    title: str
    category: str
    time: str
    completed: bool
    day: str


class AIInsightsRequest(BaseModel):
    tasks: list[AIInsightTask]


class AIInsightCard(BaseModel):
    title: str
    value: str
    description: str


class AIInsightsResponse(BaseModel):
    insights: list[AIInsightCard]


# =========================================================
# JSON HELPER
# =========================================================

def extract_json(text: str):

    if not text:
        raise ValueError(
            "AI returned empty response"
        )

    # Remove Qwen thinking section
    text = re.sub(
        r"<think>.*?</think>",
        "",
        text,
        flags=re.DOTALL
    )

    text = text.strip()

    # Remove markdown code fences
    text = re.sub(
        r"^```(?:json)?\s*",
        "",
        text,
        flags=re.IGNORECASE
    )

    text = re.sub(
        r"\s*```$",
        "",
        text
    )

    text = text.strip()

    # Find JSON object
    start = text.find("{")
    end = text.rfind("}")

    if start == -1 or end == -1 or end <= start:
        raise ValueError(
            f"AI did not return valid JSON: {text[:500]}"
        )

    json_text = text[start:end + 1]

    return json.loads(json_text)


# =========================================================
# OLLAMA HELPER
# =========================================================

def call_ollama(
    messages: list,
    timeout: int = 180
):

    try:

        response = requests.post(
            OLLAMA_URL,
            json={
                "model": AI_MODEL,
                "messages": messages,
                "stream": False,
                "format": "json",
                "options": {
                    "temperature": 0.2,
                    "num_predict": 900
                },
                "keep_alive": "10m"
            },
            timeout=timeout
        )

        response.raise_for_status()

        data = response.json()

        content = data["message"]["content"]

        return content

    except requests.exceptions.ConnectionError:

        raise HTTPException(
            status_code=503,
            detail=(
                "Ollama tidak berjalan. "
                "Pastikan Ollama sedang aktif."
            )
        )

    except requests.exceptions.Timeout:

        raise HTTPException(
            status_code=504,
            detail=(
                "AI membutuhkan waktu terlalu lama "
                "untuk memberikan response."
            )
        )

    except requests.exceptions.HTTPError:

        raise HTTPException(
            status_code=500,
            detail=f"Ollama error: {response.text}"
        )

    except Exception as e:

        print(
            "OLLAMA ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=str(e)
        )


# =========================================================
# ROOT
# =========================================================

@app.get("/")
def root():

    return {
        "message": "NEXUS API is running"
    }


# =========================================================
# HEALTH
# =========================================================

@app.get("/health")
def health():

    return {
        "status": "ok"
    }


# =========================================================
# REGISTER
# =========================================================

@app.post(
    "/auth/register",
    response_model=UserResponse
)
def register(
    data: UserRegister,
    db: Session = Depends(get_db)
):

    email = data.email.strip().lower()
    name = data.name.strip()

    if not name:

        raise HTTPException(
            status_code=400,
            detail="Name cannot be empty"
        )

    if not email:

        raise HTTPException(
            status_code=400,
            detail="Email cannot be empty"
        )

    if not data.password:

        raise HTTPException(
            status_code=400,
            detail="Password cannot be empty"
        )

    existing_user = (
        db.query(User)
        .filter(User.email == email)
        .first()
    )

    if existing_user:

        raise HTTPException(
            status_code=400,
            detail="Email already registered"
        )

    # =====================================================
    # HASH PASSWORD
    # =====================================================

    user = User(
        name=name,
        email=email,
        password=hash_password(
            data.password
        )
    )

    db.add(user)
    db.commit()
    db.refresh(user)

    return user


# =========================================================
# LOGIN
# =========================================================

@app.post(
    "/auth/login",
    response_model=UserResponse
)
def login(
    data: UserLogin,
    db: Session = Depends(get_db)
):

    email = data.email.strip().lower()

    user = (
        db.query(User)
        .filter(User.email == email)
        .first()
    )

    if not user:

        raise HTTPException(
            status_code=401,
            detail="Account not found"
        )

    # =====================================================
    # VERIFY PASSWORD
    # =====================================================

    if not verify_password(
        data.password,
        user.password
    ):

        raise HTTPException(
            status_code=401,
            detail="Incorrect password"
        )

    # =====================================================
    # AUTO MIGRATION PASSWORD LAMA
    # =====================================================
    #
    # Jika akun dibuat sebelum password hashing diterapkan,
    # password masih plaintext.
    #
    # Setelah login berhasil, otomatis ubah menjadi bcrypt.
    #

    if not user.password.startswith("$2"):

        user.password = hash_password(
            data.password
        )

        db.commit()
        db.refresh(user)

    return user


# =========================================================
# CREATE TASK
# =========================================================

@app.post(
    "/tasks/{user_id}",
    response_model=TaskResponse
)
def create_task(
    user_id: int,
    data: TaskCreate,
    db: Session = Depends(get_db)
):

    user = (
        db.query(User)
        .filter(User.id == user_id)
        .first()
    )

    if not user:

        raise HTTPException(
            status_code=404,
            detail="User not found"
        )

    task = Task(
        title=data.title,
        day=data.day,
        time=data.time,
        category=data.category,
        user_id=user_id
    )

    db.add(task)
    db.commit()
    db.refresh(task)

    return task


# =========================================================
# GET TASKS
# =========================================================

@app.get(
    "/tasks/{user_id}",
    response_model=list[TaskResponse]
)
def get_tasks(
    user_id: int,
    db: Session = Depends(get_db)
):

    return (
        db.query(Task)
        .filter(Task.user_id == user_id)
        .all()
    )


# =========================================================
# AI TEST
# =========================================================

@app.get("/ai/test")
def ai_test():

    try:

        response = requests.post(
            OLLAMA_URL,
            json={
                "model": AI_MODEL,
                "messages": [
                    {
                        "role": "user",
                        "content": "Say hello in Indonesian."
                    }
                ],
                "stream": False
            },
            timeout=120
        )

        response.raise_for_status()

        data = response.json()

        return {
            "status": "ok",
            "model": AI_MODEL,
            "response": data["message"]["content"]
        }

    except requests.exceptions.ConnectionError:

        return {
            "status": "error",
            "error": (
                "Ollama tidak berjalan. "
                "Jalankan Ollama terlebih dahulu."
            )
        }

    except requests.exceptions.Timeout:

        return {
            "status": "error",
            "error": (
                "Ollama terlalu lama memberikan response."
            )
        }

    except Exception as e:

        print(
            "OLLAMA TEST ERROR:",
            str(e)
        )

        return {
            "status": "error",
            "error": str(e)
        }


# =========================================================
# AI CHAT
# =========================================================

@app.post(
    "/ai/chat",
    response_model=AIChatResponse
)
def ai_chat(
    data: AIChatRequest
):

    message = data.message.strip()

    if not message:

        raise HTTPException(
            status_code=400,
            detail="Message cannot be empty"
        )

    print("========================================")
    print("NEXUS AI REQUEST")
    print("Message:", message)
    print("Model:", AI_MODEL)
    print("========================================")

    try:

        response = requests.post(
            OLLAMA_URL,
            json={
                "model": AI_MODEL,
                "messages": [
                    {
                        "role": "system",
                        "content": """
You are Nexus AI, the AI assistant inside the NEXUS productivity app.

Your job is to help users with:
- productivity
- task management
- daily planning
- scheduling
- prioritization
- focus sessions
- time management
- studying
- work organization

Rules:
- Respond naturally like a helpful AI assistant.
- Use Indonesian when the user speaks Indonesian.
- Use English when the user speaks English.
- Be practical and concise.
- Give clear step-by-step advice when useful.
- When making a plan, make it realistic.
- Do not pretend to access information that was not provided.
- Do not mention that you are running through Ollama.
- You are Nexus AI.
"""
                    },
                    {
                        "role": "user",
                        "content": message
                    }
                ],
                "stream": False
            },
            timeout=180
        )

        response.raise_for_status()

        result = response.json()

        ai_response = (
            result["message"]["content"]
            .strip()
        )

        print(
            "NEXUS AI RESPONSE SUCCESS"
        )

        return AIChatResponse(
            response=ai_response
        )

    except requests.exceptions.ConnectionError:

        print(
            "OLLAMA CONNECTION ERROR"
        )

        raise HTTPException(
            status_code=503,
            detail=(
                "Ollama tidak berjalan. "
                "Pastikan Ollama sedang aktif."
            )
        )

    except requests.exceptions.Timeout:

        print(
            "OLLAMA TIMEOUT"
        )

        raise HTTPException(
            status_code=504,
            detail=(
                "AI membutuhkan waktu terlalu lama "
                "untuk menjawab."
            )
        )

    except requests.exceptions.HTTPError as e:

        print(
            "OLLAMA HTTP ERROR:",
            str(e)
        )

        print(
            "Response:",
            response.text
        )

        raise HTTPException(
            status_code=500,
            detail=f"Ollama error: {response.text}"
        )

    except Exception as e:

        print(
            "OLLAMA ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=str(e)
        )


# =========================================================
# AI PLANNER
# =========================================================

@app.post(
    "/ai/plan",
    response_model=AIPlanResponse
)
def ai_plan(
    data: AIPlanRequest
):

    goal = data.goal.strip()

    if not goal:

        raise HTTPException(
            status_code=400,
            detail="Goal cannot be empty"
        )

    print("========================================")
    print("NEXUS AI PLANNER")
    print("Goal:", goal)
    print("Model:", AI_MODEL)
    print("========================================")

    system_prompt = """
You are Nexus AI Planner inside a productivity application.

Your job is to create a realistic daily task plan based ONLY on the user's goal.

Rules:
- Create 4 to 6 tasks.
- Tasks must be practical and achievable.
- Arrange tasks in chronological order.
- Use realistic times.
- Avoid overlapping times.
- Use categories such as Focus, Study, Work, Planning, Review, Break, Personal.
- Keep task titles short and actionable.
- Use Indonesian if the user speaks Indonesian.
- Use English if the user speaks English.
- Do not add explanations.
- Do not use markdown.
- Return ONLY valid JSON.

The JSON MUST have exactly this structure:

{
  "tasks": [
    {
      "title": "string",
      "time": "HH:MM",
      "duration": "30 min",
      "category": "Focus"
    }
  ]
}
"""

    user_prompt = f"""
Create a realistic daily plan for this goal:

{goal}

Return ONLY JSON.
"""

    try:

        content = call_ollama(
            [
                {
                    "role": "system",
                    "content": system_prompt
                },
                {
                    "role": "user",
                    "content": user_prompt
                }
            ],
            timeout=180
        )

        print(
            "RAW PLANNER RESPONSE:",
            content
        )

        parsed = extract_json(content)

        tasks = parsed.get(
            "tasks",
            []
        )

        if not isinstance(
            tasks,
            list
        ):

            raise ValueError(
                "Planner tasks is not a list"
            )

        clean_tasks = []

        for task in tasks[:6]:

            if not isinstance(
                task,
                dict
            ):
                continue

            title = str(
                task.get(
                    "title",
                    ""
                )
            ).strip()

            time = str(
                task.get(
                    "time",
                    ""
                )
            ).strip()

            duration = str(
                task.get(
                    "duration",
                    ""
                )
            ).strip()

            category = str(
                task.get(
                    "category",
                    "General"
                )
            ).strip()

            if not title:
                continue

            clean_tasks.append(
                AIPlanTask(
                    title=title,
                    time=time or "--:--",
                    duration=(
                        duration
                        or "30 min"
                    ),
                    category=(
                        category
                        or "General"
                    )
                )
            )

        if not clean_tasks:

            raise ValueError(
                "AI did not generate any valid tasks"
            )

        print(
            "PLANNER SUCCESS:",
            len(clean_tasks),
            "tasks"
        )

        return AIPlanResponse(
            tasks=clean_tasks
        )

    except HTTPException:

        raise

    except json.JSONDecodeError as e:

        print(
            "PLANNER JSON ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=(
                "AI Planner menghasilkan "
                "format yang tidak valid."
            )
        )

    except Exception as e:

        print(
            "PLANNER ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=str(e)
        )


# =========================================================
# AI INSIGHTS
# =========================================================

@app.post(
    "/ai/insights",
    response_model=AIInsightsResponse
)
def ai_insights(
    data: AIInsightsRequest
):

    print("========================================")
    print("NEXUS AI INSIGHTS")
    print("Tasks:", len(data.tasks))
    print("Model:", AI_MODEL)
    print("========================================")

    task_data = []

    for task in data.tasks[:50]:

        task_data.append(
            {
                "title": task.title,
                "category": task.category,
                "time": task.time,
                "completed": task.completed,
                "day": task.day
            }
        )

    task_json = json.dumps(
        task_data,
        ensure_ascii=False,
        indent=2
    )

    system_prompt = """
You are Nexus AI Insights inside a productivity application.

Your job is to analyze the user's actual task data and provide useful productivity insights.

Important:
- Use ONLY the task data provided.
- Never invent activities that are not present.
- Never claim access to information outside the supplied task data.
- Analyze completion patterns, task categories, timing, workload, and productivity patterns.
- If there is not enough data, clearly say that the data is insufficient.
- Use Indonesian when appropriate.
- Keep each insight concise and useful.
- Return exactly 4 insight cards.
- Do not use markdown.
- Return ONLY valid JSON.

The JSON MUST have exactly this structure:

{
  "insights": [
    {
      "title": "PRODUCTIVITY PATTERN",
      "value": "string",
      "description": "string"
    },
    {
      "title": "BEST FOCUS WINDOW",
      "value": "string",
      "description": "string"
    },
    {
      "title": "TASK COMPLETION",
      "value": "string",
      "description": "string"
    },
    {
      "title": "RECOMMENDATION",
      "value": "string",
      "description": "string"
    }
  ]
}
"""

    user_prompt = f"""
Analyze this user's NEXUS task data:

{task_json}

Create exactly 4 useful productivity insights.

Return ONLY JSON.
"""

    try:

        content = call_ollama(
            [
                {
                    "role": "system",
                    "content": system_prompt
                },
                {
                    "role": "user",
                    "content": user_prompt
                }
            ],
            timeout=180
        )

        print(
            "RAW INSIGHTS RESPONSE:",
            content
        )

        parsed = extract_json(content)

        insights = parsed.get(
            "insights",
            []
        )

        if not isinstance(
            insights,
            list
        ):

            raise ValueError(
                "Insights is not a list"
            )

        clean_insights = []

        for insight in insights[:4]:

            if not isinstance(
                insight,
                dict
            ):
                continue

            title = str(
                insight.get(
                    "title",
                    "INSIGHT"
                )
            ).strip()

            value = str(
                insight.get(
                    "value",
                    ""
                )
            ).strip()

            description = str(
                insight.get(
                    "description",
                    ""
                )
            ).strip()

            if not value and not description:
                continue

            clean_insights.append(
                AIInsightCard(
                    title=(
                        title
                        or "INSIGHT"
                    ),
                    value=(
                        value
                        or "—"
                    ),
                    description=(
                        description
                        or "Tidak ada detail."
                    )
                )
            )

        if not clean_insights:

            raise ValueError(
                "AI did not generate insights"
            )

        print(
            "INSIGHTS SUCCESS:",
            len(clean_insights),
            "cards"
        )

        return AIInsightsResponse(
            insights=clean_insights
        )

    except HTTPException:

        raise

    except json.JSONDecodeError as e:

        print(
            "INSIGHTS JSON ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=(
                "AI Insights menghasilkan "
                "format yang tidak valid."
            )
        )

    except Exception as e:

        print(
            "INSIGHTS ERROR:",
            str(e)
        )

        raise HTTPException(
            status_code=500,
            detail=str(e)
        )