package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter @Setter
@NoArgsConstructor
public class OrderDto implements Serializable {
    private BigDecimal price;
    private Date purchaseTime;
}
