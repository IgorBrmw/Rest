package com.example.IgorWebApp30.service;

import com.example.IgorWebApp30.model.Role;

import java.util.List;
import java.util.Set;


public interface RoleService {
    Iterable<Role> getAllRoles();
    Set<Role> getRolesByIds(List<Long> ids);
    Role getRoleByName(String roleName);
}