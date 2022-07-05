package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
}
