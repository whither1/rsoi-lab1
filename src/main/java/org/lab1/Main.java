package org.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.lab1"})
public class Main {
    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}