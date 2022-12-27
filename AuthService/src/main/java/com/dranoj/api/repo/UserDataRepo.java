package com.dranoj.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dranoj.api.model.dto.UserData;

@Repository
public interface UserDataRepo  extends JpaRepository<UserData, Long>{

	Optional<UserData> findByUsername(String username);

}
