package com.example.reactive.controller;

import com.example.reactive.dto.StudentDto;
import com.example.reactive.dto.TutorDto;
import com.example.reactive.httpclient.TutorWebClient;
import com.example.reactive.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/university")
@RequiredArgsConstructor
public class UniversityController {

    private final StudentService studentService;
    private final TutorWebClient tutorWebClient;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<?> getAll() {
        Flux<StudentDto> students = studentService.findAllStudents();
        Flux<TutorDto> tutors = tutorWebClient.getTutors()
            .flatMapIterable(res -> res);
        return Flux.merge(students, tutors);
    }

}
