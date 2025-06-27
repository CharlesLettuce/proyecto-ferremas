package com.ferremas.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "jwt.secret=8QhM0yQk4+Z+WYy+gJXjzKxg4NK27fXoSrU0jKfKO/o="
})
class ProductoFlowIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void flujoProductoCompleto() throws Exception {

        /* registro rapido */
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"tester","password":"pass123"}
                """))
           .andExpect(status().isOk());

        /* login con token */
        MvcResult loginResult = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"tester","password":"pass123"}
                """))
           .andExpect(status().isOk())
           .andReturn();

        JsonNode body = mapper.readTree(loginResult.getResponse().getContentAsString());
        String jwt = body.get("token").asText();

        /* crear */
        mvc.perform(post("/api/productos")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"codigoSku":"TL-123",
                     "nombre":"Taladro",
                     "descripcion":"Taladro inalámbrico 18V",
                     "precioUnitario":54990,
                     "stock":15,
                     "categoria":"Herramientas"}
                """))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.nombre").value("Taladro"));

        /* verificar */
        mvc.perform(get("/api/productos")
                .header("Authorization", "Bearer " + jwt))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].nombre").value("Taladro"));
    }
}
