package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController 
{
	@Autowired
	private final UserService userService;
	
	public UserController(UserService userService)
	{
		this.userService=userService;
	}
	
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
	//Add Admin
	@PostMapping("/create-admin")
	public ResponseEntity<User> createAdmin(@RequestBody User adminUser,
			@RequestParam String creatorUserName)
	{
		User created = userService.createAdmin(adminUser, creatorUserName);
		return ResponseEntity.ok(created);
	}
	
	
	//User registration
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user)
	{	
		User saved = userService.createUser(user);
		return ResponseEntity.ok(saved);
		
	}
	
	//Login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData)
	{
		String username = loginData.get("username");
		String password = loginData.get("password");
		
		return userService.getUserByUsername(username)
				.map(user ->
				{
					if (new BCryptPasswordEncoder().matches(password, user.getPassword())) 
					{
						return ResponseEntity.ok(user);
					}
					else
					{
						return ResponseEntity.status(401).body("Invalid password");
					}
				})
				.orElse(ResponseEntity.status(404).body("User not found"));
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
	
	//promote user to admin
	@PutMapping("/superuser/{id}")
	public User promoteUser(@PathVariable Long id, @RequestBody Map<String, String> payload)
	{
		String newRole = payload.get("role");
		return userService.promoteUser(id, newRole);
	}
}
