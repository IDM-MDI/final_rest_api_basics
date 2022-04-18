package com.epam.esm.task.dao;

import com.epam.esm.task.entity.impl.ManyToMany;
import com.epam.esm.task.entity.impl.Tag;
import com.epam.esm.task.exception.DaoException;

import java.util.List;

public interface ManyToManyDao {
    List<ManyToMany> findByGiftId(long id) throws DaoException;
    List<ManyToMany> findByTagId(long id) throws DaoException;
    void create(long giftId,List<Long> tagList) throws DaoException;
    void update(long giftId,List<Tag> tagList) throws DaoException;
    void deleteByGiftId(long id) throws DaoException;
    void deleteByTagId(long id) throws DaoException;
}