package com.tyd.user.module.repository;

import com.tyd.user.module.model.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<User,Long> {
    Optional<User> findByUserEmail(String emailOrMobile);

    Optional<User> findByUserMobile(String emailOrMobile);
}
