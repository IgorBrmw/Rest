package com.example.IgorWebApp30.service;

import com.example.IgorWebApp30.model.Role;
import com.example.IgorWebApp30.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleServiceImpl() {
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @Transactional
    public Set<Role> getRolesByIds(List<Long> ids) {
        List<Role> roles = (List<Role>) roleRepository.findAllById(ids);
        return new HashSet<>(roles);
    }
}