package com.epam.esm.service.crud;

public interface CRUDDesignPattern<E,D> extends CreatDesignPattern<E,D>,
                                           ReadDesignPattern<E,D>,
                                           UpdateDesignPattern<E,D>,
                                           DeleteDesignPattern {
}
