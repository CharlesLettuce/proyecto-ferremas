package com.ferremas.backend.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceTest {

    private static final int NUM_REQUESTS = 50;
    private final RestTemplate rest = new RestTemplate();

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    @BeforeEach
    void setupUser() {
        try {
            rest.postForEntity(
                baseUrl() + "/register",
                Map.of("username", "carlos", "password", "123123"),
                String.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            // usuario ya existe → ignoramos
        }
    }

    @Test
    void performanceUnderLoad() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(10);
        CompletionService<Long> cs = new ExecutorCompletionService<>(exec);

        for (int i = 0; i < NUM_REQUESTS; i++) {
            cs.submit(() -> {
                long start = System.currentTimeMillis();
                ResponseEntity<String> resp = rest.postForEntity(
                    baseUrl() + "/login",
                    Map.of("username","carlos","password","123123"),
                    String.class
                );
                assertEquals(200, resp.getStatusCodeValue(),
                    "Login falló con código " + resp.getStatusCodeValue());
                return System.currentTimeMillis() - start;
            });
        }

        long total = 0;
        for (int i = 0; i < NUM_REQUESTS; i++) {
            try {
                total += cs.take().get();
            } catch (ExecutionException ex) {
                fail("Request failed: " + ex.getCause());
            }
        }
        exec.shutdown();

        long avg = total / NUM_REQUESTS;
        assertTrue(avg < 300,
            "Tiempo medio demasiado alto: " + avg + " ms (meta < 300 ms)");

        // --- Resumen amigable para capturar en pantalla ---
        System.out.println("==============================================");
        System.out.println("PerformanceTest: OK");
        System.out.printf("  Peticiones: %d%n", NUM_REQUESTS);
        System.out.printf("  Latencia promedio: %d ms%n", avg);
        System.out.println("  Cumple meta (<300 ms): " + (avg < 300));
        System.out.println("==============================================");
    }
}
