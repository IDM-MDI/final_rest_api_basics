package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @Length(min = 2,max = 42)
    private String name;
    private List<GiftCertificateDto> orders;
}