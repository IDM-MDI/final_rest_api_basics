package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class GiftCertificateRepository implements JpaRepository<GiftCertificate, Long> {

    @Override
    public List<GiftCertificate> findAll() {
        return null;
    }

    @Override
    public List<GiftCertificate> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<GiftCertificate> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<GiftCertificate> findAllById(Iterable<Long> longs) {
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
    public void delete(GiftCertificate entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends GiftCertificate> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends GiftCertificate> S save(S entity) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<GiftCertificate> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends GiftCertificate> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<GiftCertificate> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public GiftCertificate getOne(Long aLong) {
        return null;
    }

    @Override
    public GiftCertificate getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends GiftCertificate> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends GiftCertificate> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends GiftCertificate> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends GiftCertificate, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
