package com.epam.esm.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    @Min(1)
    private long id;
    @Length(min = 2,max = 42)
    private String username;
    private String password;
    private List<OrderDto> orders;
    private StatusDto status;
}