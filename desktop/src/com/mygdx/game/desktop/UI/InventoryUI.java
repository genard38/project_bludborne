package com.mygdx.game.desktop.UI;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class InventoryUI extends Window implements InventorySubject, InventorySlotObserver {

    public final static int _numSlots = 50;
    public static final String PLAYER_INVENTORY = "Player_Inventory";
    public static final String STORE_INVENTORY = "Store_Inventory";
    private final int _slotWidth = 52;
    private final int _slotHeight = 52;
    private int _lengthSlotRow = 10;
    private Table _inventorySlotTable;
    private Table _playerSlotsTable;
    private Table _equipSlots;
    private DragAndDrop _dragAndDrop;
    private Array<Actor> _inventoryActors;
    private Label _DPValLabel;
    private int _DPVal = 0;
    private Label _APValLabel;
    private int _APVal = 0;
    private Array<InventoryObserver> _observers;

    private InventorySlotTooltip _inventorySlotTooltip; //


    public InventoryUI() {
        super(title, skin, styleName);
    }

    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {

    }

    @Override
    public void addObserver(InventoryObserver inventoryObserver) {

    }

    @Override
    public void removeObserver(InventoryObserver inventoryObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(String value, InventoryObserver.InventoryEvent event) {

    }
}
