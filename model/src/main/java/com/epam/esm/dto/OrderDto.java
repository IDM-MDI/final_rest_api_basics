package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class OrderDto implements Serializable {
    @Min(1)
    private long id;

    private BigDecimal price;
    private Date purchaseTime;
    private GiftCertificateDto gift;

    private long giftId;
    private long userId;
    private String status;
}
