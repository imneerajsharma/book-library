package com.neeraj.book_library.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperConfigTest {

    @Test
    void shouldReturnModelMapperWithPrivateFieldAccess() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        assertNotNull(modelMapper);
        Configuration config = modelMapper.getConfiguration();
        assertTrue(config.isFieldMatchingEnabled());
        assertEquals(Configuration.AccessLevel.PRIVATE, config.getFieldAccessLevel());
    }
}
