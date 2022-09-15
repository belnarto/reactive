package com.example.reactive.httpclient;

import com.example.reactive.dto.StudentDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StudentWebClient {

    WebClient client = WebClient.create("http://localhost:8080");

    public Mono<StudentDto> get(long id) {
        return client
            .get()
            .uri("/students/" + id)
            .headers(headers -> headers.setBasicAuth("user", "userpwd"))
            .retrieve()
            .bodyToMono(StudentDto.class);
    }

    public Flux<StudentDto> getAll() {
        return client.get()
            .uri("/students")
            .headers(headers -> headers.setBasicAuth("user", "userpwd"))
            .retrieve()
            .bodyToFlux(StudentDto.class);
    }

    public Flux<StudentDto> findByName(String name) {
        return client.get()
            .uri(uriBuilder -> uriBuilder.path("/students")
                .queryParam("name", name)
                .build())
            .headers(headers -> headers.setBasicAuth("user", "userpwd"))
            .retrieve()
            .bodyToFlux(StudentDto.class);
    }

    public Mono<StudentDto> create(StudentDto s)  {
        return client.post()
            .uri("/students")
            .headers(headers -> headers.setBasicAuth("admin", "adminpwd"))
            .body(Mono.just(s), StudentDto.class)
            .retrieve()
            .bodyToMono(StudentDto.class);
    }

    public Mono<StudentDto> update(StudentDto student)  {
        return client
            .put()
            .uri("/students/" + student.getId())
            .headers(headers -> headers.setBasicAuth("admin", "adminpwd"))
            .body(Mono.just(student), StudentDto.class)
            .retrieve()
            .bodyToMono(StudentDto.class);
    }

    public Mono<Void> delete(long id) {
        return client
            .delete()
            .uri("/students/" + id)
            .headers(headers -> headers.setBasicAuth("admin", "adminpwd"))
            .retrieve()
            .bodyToMono(Void.class);
    }
}
