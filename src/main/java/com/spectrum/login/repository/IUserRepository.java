package com.spectrum.login.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.login.model.Role;
import com.spectrum.login.model.User;

import java.util.List;
import java.util.Optional;



@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findAllByEmail(String email);
    
    Optional<User> findByEmployeeId(Long employeeId); // Add this method

}
