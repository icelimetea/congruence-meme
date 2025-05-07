package kek.ui.util;

import javax.swing.*;
import java.util.*;

public final class ArrayListModel<E> extends AbstractListModel<E> {
    private final List<E> elements;

    public ArrayListModel() {
        elements = new ArrayList<>();
    }

    public ArrayListModel(Collection<E> items) {
        elements = new ArrayList<>(items);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    public List<E> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public void add(E element) {
        elements.add(element);
        fireIntervalAdded(this, elements.size() - 1, elements.size() - 1);
    }

    public void remove(int[] indexes) {
        int off = 0;

        Arrays.sort(indexes);

        for (int idx : indexes) {
            elements.remove(idx - off);
            fireIntervalRemoved(this, idx - off, idx - off);
            off++;
        }
    }
}
