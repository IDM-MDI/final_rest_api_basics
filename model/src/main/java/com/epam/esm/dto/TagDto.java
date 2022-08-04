package com.epam.esm.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class TagDto extends RepresentationModel<TagDto> {
    @Min(1)
    private Long id;
    @Length(min = 2,max = 42)
    private String name;
    private String status;
}
