package com.example.demo1.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    static {
        Dotenv dotenv = Dotenv.configure()
                .load();
        System.setProperty("DATASOURCE_URL", dotenv.get("DATASOURCE_URL"));
        System.setProperty("DATASOURCE_USERNAME", dotenv.get("DATASOURCE_USERNAME"));
        System.setProperty("DATASOURCE_PASSWORD", dotenv.get("DATASOURCE_PASSWORD"));
        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
    }

}
