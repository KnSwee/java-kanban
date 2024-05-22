package project.controller.api;

import project.models.Task;

import java.util.ArrayList;

public interface HistoryManager {

    /**
     * Добавляет таск в историю просмотров
     *
     * @param task Задача, добавляемая в историю
     * @return Добавленный в историю таск
     */
    <T extends Task> T add(T task);

    /**
     * Возвращает историю просмотров
     *
     * @return Список из 10 последних вызванных задач (с дублями)
     */
    ArrayList<Task> getHistory();
}
