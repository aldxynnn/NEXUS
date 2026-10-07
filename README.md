# NEXUS

NEXUS is an AI-powered productivity Android application designed to help users organize tasks, manage focus sessions, and get intelligent productivity assistance.

## Features

- 🔐 User registration and login
- 📝 Personal task management
- ✅ Task completion tracking
- 🎯 Focus session / productivity timer
- 🤖 AI Chat assistant
- 📅 AI Planner for generating task plans
- 📊 AI Insights for productivity analysis
- 👤 User profile
- 💾 Local data persistence with Room
- 🌐 FastAPI backend with PostgreSQL
- 👥 Account-based user data

## Tech Stack

### Android

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- Room Database
- Retrofit
- OkHttp
- Gson

### Backend

- Python
- FastAPI
- SQLAlchemy
- PostgreSQL
- Uvicorn

### AI

- Ollama
- Qwen3 8B

## Architecture

```text
Android App
    │
    ├── Jetpack Compose UI
    ├── ViewModel
    ├── Room Database
    │
    └── Retrofit
            │
            ▼
        FastAPI Backend
            │
            ├── Authentication
            ├── Tasks
            ├── AI Chat
            ├── AI Planner
            └── AI Insights
                    │
                    ▼
              Ollama / Qwen3
```

## Requirements

To run the full project locally, install:

- Git
- Python 3.12+ recommended
- PostgreSQL
- Android Studio
- JDK 17
- Android SDK Platform 37

Ollama is only required when testing the AI features.

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/aldxynnn/NEXUS.git
cd NEXUS
```

### 2. Create the PostgreSQL database

Open PostgreSQL with an account that can create databases and run:

```sql
CREATE USER nexus WITH PASSWORD 'nexus';
CREATE DATABASE nexus OWNER nexus;
```

The credentials above are for the local development database only.

### 3. Configure the backend

From the project root:

**Windows PowerShell**

```powershell
cd backend
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
Copy-Item .env.example .env
```

**macOS / Linux**

```bash
cd backend
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
```

The default `.env.example` points to the local PostgreSQL database:

```env
DATABASE_URL=postgresql://nexus:nexus@localhost:5432/nexus
```

If your PostgreSQL username, password, host, or port is different, update `backend/.env`.

### 4. Start the backend

Keep the backend terminal running:

```bash
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Verify it is running by opening:

```text
http://127.0.0.1:8000/health
```

Expected response:

```json
{"status":"ok"}
```

### 5. Configure Android

Open the project root in Android Studio and let Gradle sync.

The app uses this default API URL for the **Android Emulator**:

```text
http://10.0.2.2:8000/
```

No extra configuration is required for the standard Android Emulator.

For a physical Android device, create or edit the root `local.properties` file (this file is ignored by Git) and add your computer's LAN IP:

```properties
NEXUS_BASE_URL=http://192.168.x.x:8000/
```

Make sure the phone and computer are on the same network and that Windows Firewall allows inbound TCP traffic on port 8000.

### 6. Build and run Android

In Android Studio, select an emulator/device and press **Run**.

You can also build the debug APK from the project root:

**Windows**

```powershell
.\gradlew.bat assembleDebug
```

**macOS / Linux**

```bash
./gradlew assembleDebug
```

### 7. Run the AI features (optional)

Install Ollama and make sure it is running.

Then pull the model used by NEXUS:

```bash
ollama pull qwen3:8b
```

Or start it directly:

```bash
ollama run qwen3:8b
```

NEXUS expects Ollama at:

```text
http://127.0.0.1:11434
```

If Ollama is not running, authentication and task features can still be tested, but AI Chat, AI Planner, and AI Insights will not work.

## Project Structure

```text
NEXUS/
├── app/                  # Android application
├── backend/              # FastAPI backend
│   ├── app/              # API source code
│   ├── .env.example      # Local environment template
│   └── requirements.txt  # Python dependencies
├── screenshots/          # README screenshots
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew / gradlew.bat
```

## Useful Endpoints

| Endpoint | Purpose |
|---|---|
| `GET /` | API status |
| `GET /health` | Health check |
| `POST /auth/register` | Register a user |
| `POST /auth/login` | Login |
| `POST /tasks/{user_id}` | Create a task |
| `GET /tasks/{user_id}` | Get user tasks |
| `POST /ai/chat` | AI chat |
| `POST /ai/plan` | Generate an AI plan |
| `POST /ai/insights` | Generate productivity insights |

FastAPI also exposes interactive API documentation at:

```text
http://127.0.0.1:8000/docs
```

## Troubleshooting

### `ModuleNotFoundError: No module named 'requests'`

Run:

```bash
pip install -r requirements.txt
```

The dependency is included in the repository requirements.

### `DATABASE_URL belum diatur di file .env`

Make sure `backend/.env` exists and contains a valid PostgreSQL connection string.

### Android cannot connect to the backend

For the standard Android Emulator, the API URL should be:

```text
http://10.0.2.2:8000/
```

Make sure the backend is started with:

```bash
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

For a physical device, use `NEXUS_BASE_URL` in `local.properties` and ensure the device can reach the computer on port 8000.

## Security Notes

- `backend/.env` is ignored by Git and should remain local.
- `backend/.env.example` contains only local development values.
- Do not commit production database credentials or API keys.
- The PostgreSQL credentials shown in this README are intended only for a local development database.

## Tampilan Aplikasi

### AI Chat

![NEXUS AI Chat](screenshots/ai-chat.png)

### Focus

![NEXUS Focus](screenshots/focus.png)
