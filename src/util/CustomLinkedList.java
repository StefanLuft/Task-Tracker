package util;

import java.util.ArrayList;

public class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public ArrayList<T> getTasks() {
        ArrayList<T> nodeList = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            nodeList.add(current.data);
            current = current.next;
        }
        return nodeList;
    }

    public Node<T> linkLast(T element) {
        Node<T> oldTail = tail;
        Node<T> newNode = new Node<>(oldTail, element, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    public void removeNode(Node<T> node) {
        Node<T> prev = node.prev;
        Node<T> next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
    }
}
