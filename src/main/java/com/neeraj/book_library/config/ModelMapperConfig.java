package com.neeraj.book_library.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for providing a singleton ModelMapper bean.
 * This bean enables object mapping between DTOs and Entities using ModelMapper
 * and is available throughout the application context.
 */
@Slf4j
@Configuration
public class ModelMapperConfig {

    /**
     * Provides a shared and customized ModelMapper bean for the application.
     *
     * @return a configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);
        return mapper;
    }

    /**
     * Logs a message after the configuration class is initialized.
     */
    @PostConstruct
    public void logBeanInitialization() {
        log.info("✅ ModelMapper bean initialized and configured with PRIVATE field access.");
    }
}

//package com.neeraj.book_library.config;
//
//import org.modelmapper.ModelMapper;
//import org.modelmapper.config.Configuration.AccessLevel;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Configuration class for providing a singleton ModelMapper bean.
// * This bean enables object mapping between DTOs and Entities using ModelMapper
// * and is available throughout the application context.
// */
//@Configuration
//public class BookMapperConfig {
//
//    /**
//     * Provides a shared and customized ModelMapper bean for the application.
//     *
//     * @return a configured ModelMapper instance
//     */
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration()
//                .setFieldMatchingEnabled(true)
//                .setFieldAccessLevel(AccessLevel.PRIVATE);
//        return mapper;
//    }
//}
