package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
@Data
@Getter @Setter
@NoArgsConstructor
public class TagDto {
    @Length(min = 2,max = 42)
    private String name;
}
