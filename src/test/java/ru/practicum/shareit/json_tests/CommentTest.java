package ru.practicum.shareit.json_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import java.io.IOException;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    public void testDeserialization() throws IOException {

        String json = "{\"created\": \"2022-08-01T16:14:47\"}";
        LocalDateTime expect = LocalDateTime.of(2022, 8, 1, 16, 14, 47);

        CommentDto commentDto = jacksonTester.parseObject(json);
        Assertions.assertEquals(expect, commentDto.getCreated());

    }

    @Test
    public void testSerialization() throws IOException {

        CommentDto commentDto = new CommentDto();
        commentDto.setCreated(LocalDateTime.of(2022, 8, 1, 16, 14, 47));

        JsonContent<CommentDto> json = jacksonTester.write(commentDto);

        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2022-08-01T16:14:47");

    }


}
