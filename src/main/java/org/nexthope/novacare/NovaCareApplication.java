package org.nexthope.novacare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NovaCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaCareApplication.class, args);
    }

}
