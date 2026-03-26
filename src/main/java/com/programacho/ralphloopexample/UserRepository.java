package com.programacho.ralphloopexample;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
