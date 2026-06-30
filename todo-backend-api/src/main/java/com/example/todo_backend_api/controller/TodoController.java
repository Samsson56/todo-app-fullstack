package com.example.todo_backend_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_backend_api.entity.Todo;
import com.example.todo_backend_api.service.TodoService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/todo")
@Slf4j  //for logging
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	@PostMapping("/create")
	ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
		Todo createdTodo=todoService.createTodo(todo);
		return new ResponseEntity<>(createdTodo,HttpStatus.CREATED); //201
	}
	
	//find by ID
	//see this in swagger-ui/index.html
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Todo retrieved successfully!!"),
			@ApiResponse(responseCode = "404",description = "Todo was not found.")
	})
	@GetMapping("/{id}")
	ResponseEntity<Todo> getTodoById(@PathVariable long id) {
		try {
			Todo todo=todoService.getTodoById(id);
			return new ResponseEntity<>(todo,HttpStatus.OK);
			
		} catch(RuntimeException e){
			//log.warn(""); log.error(""), log.warn("");
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	//find All todo
	@GetMapping
	ResponseEntity<List<Todo>> getTodos(){
		return new ResponseEntity<List<Todo>>(todoService.getTodos(),HttpStatus.OK); //200
	}
	
	//Pagination
	@GetMapping("/page")
	ResponseEntity<Page<Todo>> getTodoPaged(@RequestParam int page,@RequestParam int size){
		return new ResponseEntity<>(todoService.getAllTodoPaged(page,size),HttpStatus.OK);
	}
	
	@PutMapping
	ResponseEntity<Todo> updateTodoById(@RequestBody Todo todo) {
		return new ResponseEntity<>(todoService.updateTodo(todo),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	void deleteTodoById(@PathVariable long id) {
		todoService.deleteTodoById(id);
	}
	
}
