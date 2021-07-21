package com.mygdx.game.desktop.UI;

public interface InventorySlotObserver {
    void onNotify(final InventorySlot slot, SlotEvent event);

    public static enum SlotEvent{
        ADDED_ITEM,
        REMOVED_ITEM
    }
    // todo finish this
}
