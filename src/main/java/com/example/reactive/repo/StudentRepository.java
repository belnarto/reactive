package com.example.reactive.repo;

import com.example.reactive.entity.StudentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface StudentRepository extends R2dbcRepository<StudentEntity, Long> {

    Flux<StudentEntity> findByName(String name);
}
