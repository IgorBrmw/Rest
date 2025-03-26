package com.example.IgorWebApp30.service;


import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void saveUser(User user);

    void updateUser(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    void deleteUser(long id);

    String getRolesToString(Set<Role> roles);

    User getUserByUsername(String username);
}
