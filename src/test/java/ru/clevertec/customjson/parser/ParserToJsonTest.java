package ru.clevertec.customjson.parser;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.customjson.entity.Child;
import ru.clevertec.customjson.entity.Complex;
import ru.clevertec.customjson.entity.Parent;
import ru.clevertec.customjson.entity.builder.impl.ChildBuilder;
import ru.clevertec.customjson.entity.builder.impl.ComplexBuilder;
import ru.clevertec.customjson.entity.builder.impl.ParentBuilder;
import ru.clevertec.customjson.exception.ParserException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ParserToJsonTest {
    private static ParserToJson parser;
    private static ObjectMapper mapper;

    @BeforeAll
    static void init() {
        parser = new ParserToJson();
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Test
    void checkParseShouldParsePrimitive() throws ParserException, IOException {
        Parent parent = ParentBuilder.aParent().build();
        String result = parser.parse(parent);
        Parent actual = mapper.readValue(result, Parent.class);
        assertAll(
                () -> assertThat(actual.isABoolean()).isEqualTo(parent.isABoolean()),
                () -> assertThat(actual.getAByte()).isEqualTo(parent.getAByte()),
                () -> assertThat(actual.getAShort()).isEqualTo(parent.getAShort()),
                () -> assertThat(actual.getAnInt()).isEqualTo(parent.getAnInt()),
                () -> assertThat(actual.getALong()).isEqualTo(parent.getALong()),
                () -> assertThat(actual.getADouble()).isEqualTo(parent.getADouble()),
                () -> assertThat(actual.getAFloat()).isEqualTo(parent.getAFloat()),
                () -> assertThat(actual.getAChar()).isEqualTo(parent.getAChar())
        );
    }

    @Test
    void checkParseShouldParseStringArrayCollectionExtend() throws ParserException, IOException {
        Child child = ChildBuilder.aChild().build();
        String result = parser.parse(child);
        Child actual = mapper.readValue(result, Child.class);
        assertAll(
                () -> assertThat(actual.isABoolean()).isEqualTo(child.isABoolean()),
                () -> assertThat(actual.getAByte()).isEqualTo(child.getAByte()),
                () -> assertThat(actual.getAShort()).isEqualTo(child.getAShort()),
                () -> assertThat(actual.getAnInt()).isEqualTo(child.getAnInt()),
                () -> assertThat(actual.getALong()).isEqualTo(child.getALong()),
                () -> assertThat(actual.getADouble()).isEqualTo(child.getADouble()),
                () -> assertThat(actual.getAFloat()).isEqualTo(child.getAFloat()),
                () -> assertThat(actual.getAChar()).isEqualTo(child.getAChar()),
                () -> assertThat(actual.getString()).isEqualTo(child.getString()),
                () -> assertThat(actual.getDate()).isEqualTo(child.getDate()),
                () -> assertThat(actual.getDoubles()).isEqualTo(child.getDoubles()),
                () -> assertThat(actual.getStringList()).isEqualTo(child.getStringList()),
                () -> assertThat(actual.getIntegerSet()).isEqualTo(child.getIntegerSet())
        );
    }

    @Test
    void checkParseShouldParseComplex() throws ParserException, IOException {
        Complex complex = ComplexBuilder.aComplex().build();
        String result = parser.parse(complex);
        Complex actual = mapper.readValue(result, Complex.class);
        assertAll(
                () -> assertThat(actual.getBooleans()).isEqualTo(complex.getBooleans()),
                () -> assertThat(actual.getListList()).isEqualTo(complex.getListList()),
                () -> assertThat(actual.getSets()).isEqualTo(complex.getSets()),
                () -> assertThat(actual.getListSet()).isEqualTo(complex.getListSet()),
                () -> assertThat(actual.getChild()).isEqualTo(complex.getChild())
        );
    }

}