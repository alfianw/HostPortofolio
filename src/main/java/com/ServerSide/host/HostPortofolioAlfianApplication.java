package com.ServerSide.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HostPortofolioAlfianApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostPortofolioAlfianApplication.class, args);
        System.err.println("Server is Running");
    }
}
