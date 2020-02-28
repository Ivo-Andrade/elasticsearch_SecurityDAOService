package br.com.looplex.dao;

import java.util.List;

public interface IDao<T, PK> {

    void create(T newInstance);

    List<T> findAll();

    T findById(PK id);

    T update(T transientObject);

    void delete(T persistentObject);

}