package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;


@Service
public class UserService 
{
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	//create User
	public User createUser(User user)
	{
		try
		{
			logger.info("Creating user: {}", user.getUserName());
			return userRepository.save(user);
			
		}
		catch (Exception e)
		{
			logger.error("Error creating user: {}", e.getMessage(), e);
			return null;
		}
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
	
	//Update Users
	public User updateUser(Long id, User updatedUser)
	{
		logger.info("Updating yser with ID {}", id);
		return userRepository.findById(id)
				.map(user -> {						user.setFirstName(updatedUser.getFirstName());
					user.setLastName(updatedUser.getLastName());
					user.setUserName(updatedUser.getUserName());
					user.setEmail(updatedUser.getEmail());
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
}
