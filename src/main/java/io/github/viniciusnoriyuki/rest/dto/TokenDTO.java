package io.github.viniciusnoriyuki.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    private String login;
    private String token;
}
