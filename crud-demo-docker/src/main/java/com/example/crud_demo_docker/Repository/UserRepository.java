package com.example.crud_demo_docker.Repository;

import com.example.crud_demo_docker.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
