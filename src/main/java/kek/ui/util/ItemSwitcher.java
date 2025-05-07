package kek.ui.util;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class ItemSwitcher<E> implements ItemListener {
    private E previousItem;

    @Override
    public void itemStateChanged(ItemEvent e) {
        switch (e.getStateChange()) {
            case ItemEvent.DESELECTED -> previousItem = (E) e.getItem();
            case ItemEvent.SELECTED -> {
                onItemReplace(previousItem, (E) e.getItem());
                previousItem = null;
            }
        }
    }

    protected abstract void onItemReplace(E previous, E current);
}
