package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController 
{
	private final UserRepository userRepository;
	
	public UserController(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	//Get users
	//curl http://localhost:8080/users    (ALL)
	//curl http://localhost:8080/users/{id}    (Single user)
	@GetMapping
	public List<User> getUsers()
	{
		return userRepository.findAll();
	}
	
	//Add user
	/*curl -X POST http://localhost:8080/users/id\
	 * -H "Content-type: application/json" \
	 * -d'{"firstName":" ","lastName":" ", "userName":" ","email":" "}'
	 */
	@PostMapping
	public User addUser(@RequestBody User user)
	{
		return userRepository.save(user);
	}
	
	//Delete user
	//curl -X DELETE http://localhost:8080/users/id (replace id with id)
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id)
	{
		userRepository.deleteById(id);
	}

	//update user
	/*curl -X PUT http://localhost:8080/users/id\
	 * -H "Content-type: application/json" \
	 * -d'{"firstName":" ","lastName":" ", "userName":" ","email":" "}'
	 */
	
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id,
			@RequestBody User userDetails)
	{
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setUserName(userDetails.getUserName());
		user.setEmail(userDetails.getEmail());

		return userRepository.save(user);
	}
}
