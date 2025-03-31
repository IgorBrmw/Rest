package com.example.IgorWebApp30.repository;

import com.example.IgorWebApp30.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);

}