package ru.geek.financial_assistant.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geek.financial_assistant.library.GetRequestFinam;

@Configuration
public class Config {

    @Bean
    public GetRequestFinam getRequestFinam(){
        return new GetRequestFinam();
    }

}
