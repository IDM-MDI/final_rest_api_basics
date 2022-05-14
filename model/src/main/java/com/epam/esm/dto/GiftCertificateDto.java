package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter @Setter
@NoArgsConstructor
public class GiftCertificateDto {
    @Length(min = 2,max = 42)
    private String name;
    private String description;
    @Min(1)
    private BigDecimal price;
    @Min(1)
    private int duration;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime create_date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime update_date;

    private List<TagDto> tags;
}
