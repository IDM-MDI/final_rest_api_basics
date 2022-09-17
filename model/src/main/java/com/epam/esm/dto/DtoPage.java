package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DtoPage<T> extends RepresentationModel<DtoPage<T>> {
    private List<T> content;
    private ResponseDto response;
    private int size;
    private int numberOfPage;
    private String sortBy;
    private String direction;
    private boolean hasNext;
    private String param;
    private ControllerType type;
}
