from sqlalchemy import Column, Integer, String, Boolean, ForeignKey
from sqlalchemy.orm import relationship

from .database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False)
    email = Column(String(255), unique=True, index=True, nullable=False)
    password = Column(String(255), nullable=False)

    tasks = relationship(
        "Task",
        back_populates="user",
        cascade="all, delete-orphan"
    )


class Task(Base):
    __tablename__ = "tasks"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(255), nullable=False)
    completed = Column(Boolean, default=False)
    day = Column(String(50), default="Today")
    time = Column(String(20), default="")
    category = Column(String(100), default="General")

    user_id = Column(
        Integer,
        ForeignKey("users.id"),
        nullable=False
    )

    user = relationship(
        "User",
        back_populates="tasks"
    )