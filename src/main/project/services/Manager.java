package project.services;

import project.models.Task;

import java.util.List;

public interface Manager<T extends Task> {
    List<T> get();

    T create(T task);

    T getById(int id);

    void delete();

    Task update(T task);

    void deleteById(int id);

    void fillMap(T task);
}
