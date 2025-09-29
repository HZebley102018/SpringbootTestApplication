package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	//Get users
	//curl http://localhost:8080/users    (ALL)
	@GetMapping
	public List<User> getUsers()
	{
		return userService.getAllUsers();
	}
	
	//Add user
	/*curl -X POST http://localhost:8080/users/id\
	 * -H "Content-type: application/json" \
	 * -d'{"firstName":" ","lastName":" ", "userName":" ","email":" "}'
	 */
	@PostMapping
	public User addUser(@RequestBody User user)
	{
		return userService.createUser(user);
	}
	
	//Delete user
	//curl -X DELETE http://localhost:8080/users/id (replace id with id)
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id)
	{
		userService.deleteUser(id);
	}

	//update user
	/*curl -X PUT http://localhost:8080/users/id\
	 * -H "Content-type: application/json" \
	 * -d'{"firstName":" ","lastName":" ", "userName":" ","email":" "}'
	 */
	
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id,
			@RequestBody User user)
	{
		return userService.updateUser(id, user);
	}
}
