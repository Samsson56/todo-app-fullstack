package com.example.todo_backend_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo_backend_api.entity.User;
import com.example.todo_backend_api.repository.UserRepository;

@Service // Parent should be Bean type
public class UserService {
	@Autowired // create repository instance(handled by spring)
	private UserRepository Userrepository;
	
	//create User
	public User createUser(User user) {
		return Userrepository.save(user); // User exist means update it/not exist means create it db.

	}
	//find by id
	public User getUserById(Long id) {
		return Userrepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}
}
