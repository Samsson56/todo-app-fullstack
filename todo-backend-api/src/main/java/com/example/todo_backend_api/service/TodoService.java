package com.example.todo_backend_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.todo_backend_api.entity.Todo;
import com.example.todo_backend_api.repository.TodoRepository;

@Service // Parent should be Bean type
public class TodoService {
	@Autowired // create repository instance(handled by spring)
	private TodoRepository todorepository;
	
	//create todo
	public Todo createTodo(Todo todo) {
		return todorepository.save(todo); // todo exist means update it/not exist means create it db.

	}
	//find by id
	public Todo getTodoById(Long id) {
		return todorepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
	}
	
	//find all todo
	public List<Todo> getTodos() {
		return todorepository.findAll();
	}
	
	public Page<Todo> getAllTodoPaged(int page, int size) {  //page number, page size
		Pageable pageable= PageRequest.of(page, size);
		return todorepository.findAll(pageable);
	}
	
	//update todo(replace if already exits/insert it in DB)
	public Todo updateTodo(Todo todo) {
		return todorepository.save(todo);
	}
	
	//no return for delete
	//delete todo by id
	public void deleteTodoById(long id) {
		todorepository.delete(getTodoById(id));
	}
	
	
}
