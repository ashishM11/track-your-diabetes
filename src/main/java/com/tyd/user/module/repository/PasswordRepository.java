package com.tyd.user.module.repository;

import com.tyd.user.module.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password,Long> {
}
