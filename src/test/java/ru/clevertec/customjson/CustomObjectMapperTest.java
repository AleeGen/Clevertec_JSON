package ru.clevertec.customjson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.customjson.entity.Child;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomObjectMapperTest {

    private static CustomObjectMapper customMapper;
    private static Child child;
    private static Writer writer;

    @BeforeAll
    static void init() {
        customMapper = new CustomObjectMapper();
        child = FactoryEntity.getEntity();
        writer = new StringWriter();
    }

    @Test
    void generate() throws IllegalAccessException, IOException {
        String actual = customMapper.generate(child);
        new ObjectMapper().writeValue(writer, child);
        String expected = writer.toString();
        assertThat(actual).isEqualTo(expected);
    }
}