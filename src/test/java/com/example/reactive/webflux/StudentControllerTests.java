package com.example.reactive.webflux;

import com.example.reactive.dto.StudentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTests {

    @Autowired
    WebTestClient webClient;

    @Test
    @WithMockUser(roles = "USER")
    void test_getStudents() {
        webClient.get().uri("/students")
            .header(HttpHeaders.ACCEPT, "application/json")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(StudentDto.class);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddNewStudent() {
        StudentDto newStudent = new StudentDto();
        newStudent.setId(1L);
        newStudent.setName("some name");
        newStudent.setAddress("an address");

        webClient.post().uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newStudent), StudentDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.name").isEqualTo(newStudent.getName())
            .jsonPath("$.address").isEqualTo(newStudent.getAddress());
    }

}
