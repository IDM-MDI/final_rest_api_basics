package com.epam.esm.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class ResponseDto {
    private int code;
    private String text;
}
