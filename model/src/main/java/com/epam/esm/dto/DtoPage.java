package com.epam.esm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class DtoPage<T> extends RepresentationModel<DtoPage<T>> {
    private List<T> content;
    private int size;
    private int numberOfPage;
    private String sortBy;
}
