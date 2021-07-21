package com.mygdx.game.desktop.UI;

public interface InventoryObserver {
    void onNotify(final String value, InventoryEvent event);

    public static enum InventoryEvent{
        UPDATED_AP,
        UPDATED_DP,
        ITEM_CONSUMED,
        ADD_WAND_AP,
        REMOVE_WAND_AP,
        NONE
    }
}
