package project.services;

import project.models.Task;

import java.util.ArrayList;

public interface Manager<T extends Task> {
    ArrayList<T> get();

    T create(T task);

    T getById(int ID);

    void delete();

    void update(T task);

    void deleteById(int ID);

    Class<Task> getType();
}
