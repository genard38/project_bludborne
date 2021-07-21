package com.mygdx.game.desktop.UI;

public interface StatusObserver {
    void onNotify(final int value, StatusEvent event);

    public static enum StatusEvent{
        UPDATED_GP,
        UPDATED_LEVEL,
        UPDATED_HP,
        UPDATED_MP,
        UPDATED_XP,
        LEVELED_UP
    }
}
