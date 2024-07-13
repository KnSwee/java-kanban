package project.controller;

import project.controller.api.HistoryManager;
import project.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, HistoryLinkedList.Node<Task>> history = new HashMap<>();
    private final HistoryLinkedList<Task> list = new HistoryLinkedList<>();

    @Override
    public <T extends Task> T add(T task) {
        if (history.containsKey(task.getID())) {
            remove(task.getID());
        }
        history.put(task.getID(), list.linkLast(task.copy()));
        return task;
    }

    @Override
    public void remove(int id) {
        if (!getHistory().isEmpty()) {
            HistoryLinkedList.Node<Task> deletedNode = history.remove(id);
            if (deletedNode == null) {
                return;
            }
            if (list.size == 1) {
                list.head = null;
                list.tail = null;
            } else if (deletedNode == list.tail) {
                list.tail = deletedNode.prev;
                list.tail.next = null;
            } else if (deletedNode == list.head) {
                list.head = deletedNode.next;
                list.head.prev = null;
            } else {
                deletedNode.prev.next = deletedNode.next;
                deletedNode.next.prev = deletedNode.prev;

            }
            deletedNode.next = null;
            deletedNode.prev = null;
            list.size--;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        HistoryLinkedList.Node<Task> current = list.head;
        for (int i = 0; i < list.size; i++) {
            historyList.add(current.data);
            current = current.next;
        }
        return historyList;
    }

    public void clearHistory() {
        history.clear();
        list.clearList();
    }

    private static class HistoryLinkedList<T extends Task> {

        public Node<T> head;
        public Node<T> tail;
        private int size = 0;

        public Node<T> linkLast(T task) {
            if (head == null) {
                head = new Node<>(task);
                tail = head;
            } else {
                Node<T> prevTail = tail;
                tail = new Node<>(task);
                tail.prev = prevTail;
                prevTail.next = tail;
            }
            size++;
            return tail;
        }

        public void clearList() {
            head = null;
            tail = null;
            size = 0;
        }

        public int size() {
            return size;
        }

        private static class Node<T extends Task> {

            public T data;
            public Node<T> next;
            public Node<T> prev;

            public Node(T data) {
                this.data = data;
                this.next = null;
                this.prev = null;
            }
        }

    }
}
