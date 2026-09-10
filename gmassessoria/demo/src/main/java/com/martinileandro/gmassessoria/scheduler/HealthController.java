package com.martinileandro.gmassessoria.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/status")
    public String status(){
        jdbcTemplate.execute("SELECT 1");
        return "API e Banco de Dados operacionais!";
    }
}
