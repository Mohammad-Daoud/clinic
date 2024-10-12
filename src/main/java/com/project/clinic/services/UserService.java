package com.project.clinic.services;
import com.project.clinic.models.User;
import com.project.clinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public boolean isPasswordValid(String username, String password) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return false;
            }
            return passwordEncoder.matches(password, user.getPassword());
        }catch (Exception e){
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }


    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            return;
        user.setPassword(newPassword);
        saveUser(user);
    }

    public List<User> findAllUsers() {
         return userRepository.findAll().stream().filter(user-> !user.getUsername().equals(getCurrentUsername())).toList();
    }
}
