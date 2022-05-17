package com.epam.esm.hateoas;

public interface HateoasDTO<DTO> {
    void addLinks(DTO dto);
}
