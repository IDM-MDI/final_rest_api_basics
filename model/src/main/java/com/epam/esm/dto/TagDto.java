package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;

@Data
@Getter @Setter
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    @Min(1)
    private long id;
    @Length(min = 2,max = 42)
    private String name;
}
