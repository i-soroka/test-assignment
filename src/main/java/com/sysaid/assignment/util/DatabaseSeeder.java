package com.sysaid.assignment.util;

import com.sysaid.assignment.domain.user.db.User;
import com.sysaid.assignment.domain.user.db.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    private final UserRepository repository;


    public DatabaseSeeder(UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        this.repository.save(
                new User(null, "root")
        );
    }
}
