package com.UserService.service;

import com.UserService.dto.*;
import com.UserService.exceptions.InvalidInputException;
import com.UserService.exceptions.UserAlreadyExistsException;
import com.UserService.exceptions.UserNotFoundException;
import com.UserService.model.User;
import com.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(SignupRequest request) throws UserAlreadyExistsException 
    {
    	// Email format validation
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }

        // Password strength validation
        if (request.getPassword().length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long");
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setWalletPoint(0.0); // Default wallet value

        userRepository.save(user);
    }
    

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid password.");
        }

        String token = jwtUtil.generateToken(user);
        return new LoginResponse(token, "Login successful");
    }
    

    public UserProfileDto getProfile(String email)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setWalletPoints(user.getWalletPoint());

        return dto;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Called in transaction service 
    public void updateWalletAfterTransaction(String email, double walletUsed, double cashback) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double updatedWallet = user.getWalletPoint() - walletUsed + cashback;
        user.setWalletPoint(updatedWallet);
        userRepository.save(user);
    }
    
    // Called in transaction service 
    public Double getUserWalletBalance(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getWalletPoint();
    }

//    public UserDto updateUser(Long id, User updatedUser) throws UserNotFoundException {
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        existingUser.setName(updatedUser.getName());
//        existingUser.setEmail(updatedUser.getEmail());
//        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        existingUser.setWalletPoint(updatedUser.getWalletPoint());
//
//        User savedUser = userRepository.save(existingUser);
//        return convertToDto(savedUser);
//    }
//
//
//    public void deleteUser(Long id) throws UserNotFoundException {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        userRepository.delete(user);
//    }

    public List<UserDto> createUsers(List<User> users) {
        return userRepository.saveAll(users)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto convertToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getWalletPoint()
        );
    }

    
}
