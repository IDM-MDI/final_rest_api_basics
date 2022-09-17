package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String mainImage;

    private boolean haveImage;

    private String status;
}
