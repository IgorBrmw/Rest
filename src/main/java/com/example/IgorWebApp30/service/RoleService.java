package com.example.IgorWebApp30.service;

import com.example.IgorWebApp30.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RoleService {
    Set<Role> getAllRoles();
    Set<Role> getRolesByIds(List<Long> ids);
}