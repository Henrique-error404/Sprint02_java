package com.gestaoestabelecimentos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurações básicas para melhor performance e segurança
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)          // Ignora campos nulos
                .setAmbiguityIgnored(true)         // Ignora mapeamentos ambíguos
                .setFieldMatchingEnabled(true);    // Permite mapeamento por campo

        return modelMapper;
    }
}