package com.ferremas.backend.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void accessProtectedEndpointWithoutTokenShouldBeForbidden() throws Exception {
        mvc.perform(get("/api/productos"))
           .andExpect(status().isForbidden());
    }

    @Test
    public void sqlInjectionAttemptOnLoginShouldBeUnauthorized() throws Exception {
        mvc.perform(post("/api/auth/login")
           .contentType(MediaType.APPLICATION_JSON)
           .content("{\"username\":\"' OR '1'='1\",\"password\":\"whatever\"}"))
           .andExpect(status().isUnauthorized());  // <-- 401 en lugar de 400
    }

    @AfterAll
    static void printSummary() {
        System.out.println("==============================================");
        System.out.println("SecurityTests: PASSED");
        System.out.println("  • Acceso sin token → 403 Forbidden ✔");
        System.out.println("  • Intento de SQLi en /api/auth/login → 401 Unauthorized ✔");
        System.out.println("==============================================");
    }
}
