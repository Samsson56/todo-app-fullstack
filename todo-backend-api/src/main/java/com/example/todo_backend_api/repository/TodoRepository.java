package com.example.todo_backend_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo_backend_api.entity.Todo;

//speaks to DB
public interface TodoRepository extends JpaRepository<Todo,Long> {
	
}
