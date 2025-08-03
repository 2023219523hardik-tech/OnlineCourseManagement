package com.ocms.user.service;

import com.ocms.common.exception.ResourceNotFoundException;
import com.ocms.common.exception.UnauthorizedException;
import com.ocms.user.dto.LoginDto;
import com.ocms.user.dto.UserRegistrationDto;
import com.ocms.user.entity.User;
import com.ocms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // HashMap for storing user data in memory for quick access
    private final Map<String, User> userCache = new HashMap<>();
    
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setRole(registrationDto.getRole());
        
        User savedUser = userRepository.save(user);
        
        // Add to cache
        userCache.put(savedUser.getUsername(), savedUser);
        
        return savedUser;
    }
    

    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User getUserByUsername(String username) {
        // Check cache first
        User cachedUser = userCache.get(username);
        if (cachedUser != null) {
            return cachedUser;
        }
        
        // If not in cache, get from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        // Add to cache
        userCache.put(username, user);
        
        return user;
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }
    
    public User updateUser(Long id, UserRegistrationDto updateDto) {
        User user = getUserById(id);
        
        if (updateDto.getFirstName() != null) {
            user.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            user.setLastName(updateDto.getLastName());
        }
        if (updateDto.getEmail() != null && !updateDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        
        // Update cache
        userCache.put(updatedUser.getUsername(), updatedUser);
        
        return updatedUser;
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
        
        // Remove from cache
        userCache.remove(user.getUsername());
    }
    
    public void clearUserCache() {
        userCache.clear();
    }
    
    public Map<String, User> getUserCache() {
        return new HashMap<>(userCache);
    }
}