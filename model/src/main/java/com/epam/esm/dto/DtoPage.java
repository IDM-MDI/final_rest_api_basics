package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class DtoPage<T> extends RepresentationModel<DtoPage<T>> {
    private List<T> content;
    private int size;
    private int numberOfPage;
    private String sortBy;
}
