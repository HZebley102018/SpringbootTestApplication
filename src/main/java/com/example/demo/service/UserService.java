package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserService 
{
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder =passwordEncoder;
	}
	
	//create User -- hash password
	public User createUser(User user)
	{
		try
		{
			logger.info("Creating user: {}", user.getUserName());
			//hash password
			if (user.getPassword() != null) 
			{
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			
			//set default role
			if(user.getRole() == null || user.getRole().isEmpty()) 
			{
				user.setRole("USER");
			}
			return userRepository.save(user);
			
		}
		catch (Exception e)
		{
			logger.error("Error creating user: {}", e.getMessage(), e);
			return null;
		}
	}
	
	//Create admin (SUPERUSER ONLY)
	public User createAdmin(User adminUser, String requestingUserName)
	{
		Optional<User> creatorOpt = userRepository.findByUserName(requestingUserName);
		
		if(creatorOpt.isEmpty())
		{
			throw new RuntimeException("Requesting user not found.");
		}
		
		User creator = creatorOpt.get();
		if(!"SUPERUSER".equalsIgnoreCase(creator.getRole()))
		{
			throw new RuntimeException("Only a SUPERUSER can create admins.");
		}
		
		logger.info("SUPERUSER {} is creating ADMIN {}", creator.getUserName(), adminUser.getUserName());

		if(adminUser.getPassword() != null)
		{
			adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
		}
		adminUser.setRole("ADMIN");
		
		return userRepository.save(adminUser);
	}
	
	//Get Users
	public List<User> getAllUsers()
	{
		try
		{
			logger.info("Fetching all users");
			return userRepository.findAll();
		}
		catch(Exception e)
		{
			logger.error("Error fetching users: {}", e.getMessage());
			return null;
		}
	}
	
	//Look up username
	public Optional<User> getUserByUsername(String username)
	{
		return userRepository.findByUserName(username);
	}
	
	//Update Users
	public User updateUser(Long id, User updatedUser)
	{
		logger.info("Updating user with ID {}", id);
		return userRepository.findById(id)
				.map(user -> {						user.setFirstName(updatedUser.getFirstName());
					user.setLastName(updatedUser.getLastName());
					user.setUserName(updatedUser.getUserName());
					user.setEmail(updatedUser.getEmail());
					if(user.getPassword() != null && !user.getPassword().isBlank())
						{
							user.setPassword(passwordEncoder.encode(user.getPassword()));
						};
					return userRepository.save(user);
				})
				.orElseThrow(() -> new RuntimeException("User not found."));	
	}
	
	//delete Users
	public void deleteUser(Long id)
	{
		logger.warn("Deleting user with ID {}", id);
		userRepository.deleteById(id);
	}

	public User promoteUser(Long id, String newRole) 
	{
		return userRepository.findById(id).map(user ->
		{
			logger.info("Promoting user {} to role {}", user.getUserName(),newRole);
			user.setRole(newRole);
			return userRepository.save(user);
		})
		.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}
}
