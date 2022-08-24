package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDto extends RepresentationModel<UserDto> {
    @Min(1)
    private long id;
    @Length(min = 2,max = 42)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;
    private List<OrderDto> orders;
    private String status;
    private List<RoleDto> roles;
    private String jwt;
}