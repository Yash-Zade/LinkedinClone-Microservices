package com.yash.linkedin.post_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public static ModelMapper modleMapper(){
        return new ModelMapper();
    }
}
