package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.crud.CRUDDesignPattern;

public interface TagService extends CRUDDesignPattern<Tag, TagDto> {
}
