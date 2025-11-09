package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
		logger.info("Entering and exiting getUsers()");
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
		logger.info("Entering and exiting addUser()");
		return userService.createUser(user);
	}
	//Add Admin
	@PostMapping("/create-admin")
	public ResponseEntity<User> createAdmin(@RequestBody User adminUser,
			@RequestParam String creatorUserName)
	{
		logger.info("Entering createAdmin()");
		User created = userService.createAdmin(adminUser, creatorUserName);
		logger.info("Exiting createAdmin()");
		return ResponseEntity.ok(created);
	}
	
	
	//User registration
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user)
	{	
		logger.info("Entering register()");
		User saved = userService.createUser(user);
		logger.info("Exiting register()");
		return ResponseEntity.ok(saved);
		
	}
	
	//Login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData)
	{
		logger.info("Entering login()");
		String username = loginData.get("username");
		String password = loginData.get("password");
		
		return userService.getUserByUsername(username)
				.map(user ->
				{
					if (new BCryptPasswordEncoder().matches(password, user.getPassword())) 
					{
						logger.info("Exiting login()");
						return ResponseEntity.ok(user);
					}
					else
					{
						logger.info("Exiting login()");
						return ResponseEntity.status(401).body("Invalid password");
					}
				})
				.orElse(ResponseEntity.status(404).body("User not found"));
	}
	//Delete user
	//curl -X DELETE http://localhost:8080/users/id (replace id with id)
	@DeleteMapping("/superuser/{id}")
	public void deleteUser(@PathVariable Long id)
	{
		logger.info("Entering and exiting deleterUser()");
		userService.deleteUser(id);
	}

	//update user
	/*curl -X PUT http://localhost:8080/users/id\
	 * -H "Content-type: application/json" \
	 * -d'{"firstName":" ","lastName":" ", "userName":" ","email":" "}'
	 */
	
	@PutMapping("/superuser/update/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser)
	{
		logger.info("Entering and exiting updateUser()");
		return userService.getUserById(id)
				.map(user ->
				{
					user.setUserName(updatedUser.getUserName());
					user.setEmail(updatedUser.getEmail());
					userService.saveUser(user);
					return ResponseEntity.ok(user);
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	//promote user to admin
	@PutMapping("/superuser/{id}")
	public User promoteUser(@PathVariable Long id, @RequestBody Map<String, String> payload)
	{
		logger.info("Entering promoteUser()");
		String newRole = payload.get("role");
		logger.info("Exiting promoteUser()");
		return userService.promoteUser(id, newRole);
	}
}
