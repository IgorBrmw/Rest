package com.example.IgorWebApp30.service;



import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.model.User;
import com.example.IgorWebApp30.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User getUserByUsername(String username) {
     return userRepository.getUserByUsername(username);
    }

    @Override
    @Transactional
    public String getRolesToString(Set<Role> roles) {
        String rolesString = roles.stream().map(Role::getName).collect(Collectors.joining(","));
        return rolesString;
    }
}
