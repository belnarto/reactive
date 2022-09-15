package com.example.reactive.converter;

import com.example.reactive.dto.StudentDto;
import com.example.reactive.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter {

    public StudentDto toDto(StudentEntity entity) {
        if (entity == null) {
            return null;
        }
        return StudentDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .address(entity.getAddress())
            .build();
    }

    public StudentEntity toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }
        return StudentEntity.builder()
            .name(dto.getName())
            .address(dto.getAddress())
            .build();
    }
}
