package com.example.reactive.service;

import com.example.reactive.converter.StudentConverter;
import com.example.reactive.dto.BookDto;
import com.example.reactive.dto.StudentDto;
import com.example.reactive.entity.StudentEntity;
import com.example.reactive.repo.StudentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    public Flux<StudentDto> findStudentsByName(String name) {
        return (name != null)
            ? studentRepository.findByName(name).map(studentConverter::toDto)
            : studentRepository.findAll().map(studentConverter::toDto);
    }

    public Mono<StudentDto> findStudentById(Long id) {
        return studentRepository.findById(id).map(studentConverter::toDto);
    }

    public Flux<StudentDto> findAllStudents() {
        Flux<List<BookDto>> bookFlux = Flux.range(1, Integer.MAX_VALUE)
            .map(i -> List.of(new BookDto(UUID.randomUUID(), "bookTitle" + i)));

        return studentRepository.findAll()
            .map(studentConverter::toDto)
            .zipWith(bookFlux, (s, b) -> {
                s.setBooks(b);
                return s;
            });
    }

    public Mono<StudentDto> addNewStudent(StudentDto student) {
        return studentRepository.save(studentConverter.toEntity(student)).map(studentConverter::toDto);
    }

    public Mono<StudentDto> updateStudent(Long id, StudentDto student) {
        return studentRepository.findById(id)
            .flatMap(s -> {
                StudentEntity entity = studentConverter.toEntity(student);
                entity.setId(id);
                return studentRepository.save(entity);
            })
            .map(studentConverter::toDto);

    }

    public Mono<Void> deleteStudent(Long id) {
        return studentRepository.deleteById(id);
    }
}
