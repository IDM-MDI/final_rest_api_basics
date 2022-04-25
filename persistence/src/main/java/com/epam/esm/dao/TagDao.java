package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface TagDao extends CrdDao<Tag,Long>{
    List<Long> createWithList(List<Tag> tagList) throws DaoException;
    Tag findByName(String name) throws DaoException;
}
