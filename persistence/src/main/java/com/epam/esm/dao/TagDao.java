package com.epam.esm.task.dao;

import com.epam.esm.task.entity.impl.Tag;
import com.epam.esm.task.exception.DaoException;

import java.util.List;

public interface TagDao extends CrdDao<Tag,Long>{
    List<Long> createWithList(List<Tag> tagList) throws DaoException;
    Tag findByName(String name) throws DaoException;
}
