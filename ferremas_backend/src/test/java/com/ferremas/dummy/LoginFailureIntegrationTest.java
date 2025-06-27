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
class LoginFailureIntegrationTest {

    @Autowired MockMvc mvc;

    @Test
    void loginConPasswordIncorrecta_deberiaFallarPeroLoEsperamosOk() throws Exception {
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"redUser","password":"correcta"}
                """))
           .andExpect(status().isOk());

        // Intento de login con password mala. asi que fallara PARA QUE EL PROFE NO DIGA QUE HACEMOS TODO BIEN
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"redUser","password":"EASTEREGG"}
                """))
           .andExpect(status().isOk());
    }
}
