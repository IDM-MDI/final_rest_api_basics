package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GiftTagRepository implements JpaRepository<GiftTag,Long> {
    @Override
    public <S extends GiftTag> S save(S entity) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends GiftTag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends GiftTag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<GiftTag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public GiftTag getOne(Long aLong) {
        return null;
    }

    @Override
    public GiftTag getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends GiftTag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends GiftTag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends GiftTag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends GiftTag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends GiftTag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends GiftTag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends GiftTag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<GiftTag> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<GiftTag> findAll() {
        return null;
    }

    @Override
    public List<GiftTag> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<GiftTag> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends GiftTag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Page<GiftTag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(GiftTag entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends GiftTag> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
