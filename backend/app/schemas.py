from pydantic import BaseModel, EmailStr


class UserRegister(BaseModel):
    name: str
    email: EmailStr
    password: str


class UserLogin(BaseModel):
    email: EmailStr
    password: str


class UserResponse(BaseModel):
    id: int
    name: str
    email: EmailStr

    class Config:
        from_attributes = True


class TaskCreate(BaseModel):
    title: str
    day: str = "Today"
    time: str = ""
    category: str = "General"


class TaskResponse(BaseModel):
    id: int
    title: str
    completed: bool
    day: str
    time: str
    category: str

    class Config:
        from_attributes = True