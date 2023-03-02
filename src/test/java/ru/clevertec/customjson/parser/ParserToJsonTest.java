package ru.clevertec.customjson.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.customjson.entity.builder.impl.ChildBuilder;
import ru.clevertec.customjson.entity.builder.impl.ComplexBuilder;
import ru.clevertec.customjson.entity.builder.impl.ParentBuilder;
import ru.clevertec.customjson.exception.ParserException;

import java.io.IOException;

class ParserToJsonTest {
    private static ParserToJson parser;
    private static ParentBuilder parent;
    private static ChildBuilder child;
    private static ComplexBuilder complex;

    @BeforeAll
    static void init() {
        parser = new ParserToJson();
    }

    @Test
    void checkParseShouldReturnExpected() {

    }
}