package com.example.reactive.controller;

import com.example.reactive.dto.StudentDto;
import com.example.reactive.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentDto>> getStudent(@PathVariable Long id) {
        return studentService.findStudentById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StudentDto> listStudents(@RequestParam(name = "name", required = false) String name) {
        return studentService.findStudentsByName(name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<StudentDto> addNewStudent(@RequestBody StudentDto student) {
        return studentService.addNewStudent(student);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<StudentDto>> updateStudent(@PathVariable Long id, @RequestBody StudentDto student) {
        return studentService.updateStudent(id, student)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable Long id) {
        return studentService.findStudentById(id)
            .flatMap(s ->
                studentService.deleteStudent(id)
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
            )
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
