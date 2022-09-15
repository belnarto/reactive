package com.example.reactive.controller;

import com.example.reactive.dto.TutorDto;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final Random random = new Random();

    @GetMapping()
    public List<TutorDto> getStudentBooks() {
        return random.ints(0, 101)
            .limit(10)
            .distinct()
            .mapToObj(i -> new TutorDto(UUID.randomUUID(), "title" + i))
            .collect(Collectors.toList());
    }

}
