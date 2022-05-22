package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter @Setter
@NoArgsConstructor
public class OrderDto implements Serializable {
    @Min(1)
    private long id;

    private BigDecimal price;
    private Date purchaseTime;
    private GiftCertificateDto gift;

    private long giftId;
    private long userId;
}
