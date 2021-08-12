package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.desktop.bludbourne.Utility;

public class InventorySlot extends Stack implements InventorySlotSubject {


    //All slots have this default image
    private Stack _defaultBackground;
    private Image _customBackgroundDecal;
    private Label _numItemsLabel;
    private int _numItemsVal = 0;
    private int _filterItemType;

    private Array<InventorySlotObserver> _observers;


    public InventorySlot() {
        _filterItemType = 0;// filter nothing;
        _defaultBackground = new Stack();
        _observers = new Array<InventorySlotObserver>();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))); // todo create this field in Utility

        _numItemsLabel = new Label(String.valueOf(_numItemsVal), Utility.STATUSUI_SKIN, "inventory-item-count"); // todo create this field in Utility
        _numItemsLabel.setAlignment(Align.bottomRight);
        _numItemsLabel.setVisible(false);

        _defaultBackground.add(image);

        _defaultBackground.setName("background");
        _numItemsLabel.setName("numitems");

        this.add(_defaultBackground);
        this.add(_numItemsLabel);



    }

    public InventorySlot(int filterItemType, Image customBackgroundDecal){
        this();
        _filterItemType = filterItemType;
        _customBackgroundDecal = customBackgroundDecal;
        _defaultBackground.add(_customBackgroundDecal);
    }


    public void decrementItemCount(boolean sendRemoveNotification){
        _numItemsVal --;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if( _defaultBackground.getChildren().size == 1){
            _defaultBackground.add(_customBackgroundDecal);
        }// todo continue filling here

    }


    @Override
    public void addObserver(InventorySlotObserver inventorySlotObserver) {

    }

    @Override
    public void removeObserver(InventorySlotObserver inventorySlotObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(InventorySlot slot, InventorySlotObserver.SlotEvent event) {

    }


    public void recoverActor(InventoryItem item) {
    }
}
