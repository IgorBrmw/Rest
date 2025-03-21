package com.example.IgorWebApp30.repository;

import com.example.IgorWebApp30.model.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @Override
    Optional<Role> findById(Long id);

    List<Role> findAllById(Long id);

}