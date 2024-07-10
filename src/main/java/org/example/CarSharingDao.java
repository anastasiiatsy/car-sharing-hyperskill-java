package org.example;

import java.util.List;

public interface CarSharingDao<T> {
    List<T> findAll();
    T findById(int id);
    boolean add(T element);
    boolean update(T element);
    void deleteById(int id);
    int getID(T element);
}
