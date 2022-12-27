package com.dranoj.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dranoj.api.model.dto.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

}
