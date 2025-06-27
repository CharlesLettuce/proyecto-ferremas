package com.ferremas.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
        "jwt.secret=8QhM0yQk4+Z+WYy+gJXjzKxg4NK27fXoSrU0jKfKO/o=",
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
class GreenIntegrationTest {

    @Autowired MockMvc mvc;

    @Test
    void registroYLogin_devuelveToken() throws Exception {
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"greenUser","password":"UN TEST EN VERDE PARA QUE NOSEDIGA JAJA"}
                """))
           .andExpect(status().isOk());

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"greenUser","password":"UN TEST EN VERDE PARA QUE NOSEDIGA JAJA"}
                """))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").exists());
    }
}
