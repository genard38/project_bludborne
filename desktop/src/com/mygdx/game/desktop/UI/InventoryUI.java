package com.mygdx.game.desktop.UI;


import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.desktop.InventoryItem;
import com.mygdx.game.desktop.bludbourne.Component;
import com.mygdx.game.desktop.bludbourne.Entity;
import com.mygdx.game.desktop.bludbourne.Utility;

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


    private InventorySlotTooltip _inventorySlotTooltip;


    public InventoryUI() {
        super("Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        _observers = new Array<InventoryObserver>();

        _dragAndDrop = new DragAndDrop();
        _inventoryActors = new Array<Actor>();

        //create
        _inventorySlotTable = new Table();
        _inventorySlotTable.setName("Inventory_Slot_Table");

        _playerSlotsTable = new Table();
        _equipSlots = new Table();
        _equipSlots.setName("Equipment_Slot_Table");

        _equipSlots.defaults().space(10);
        _inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUSUI_SKIN);

        Label DPLabel = new Label("Defense: ", Utility.STATUSUI_SKIN);
        _DPValLabel = new Label(String.valueOf(_DPVal), Utility.STATUSUI_SKIN);

        Label APLabel = new Label("Attack: ", Utility.STATUSUI_SKIN);
        _APValLabel = new Label(String.valueOf(_APVal), Utility.STATUSUI_SKIN);

        Table labelTable = new Table();
        labelTable.add(DPLabel).align(Align.left);
        labelTable.add(_DPValLabel).align(Align.center);
        labelTable.row();
        labelTable.row();
        labelTable.add(APLabel).align(Align.left);
        labelTable.add(_APValLabel).align(Align.center);

        //TODO create class InventoryItem in com.mygdx.game package.
        InventorySlot headSlot = new InventorySlot(InventoryItem.ItemUseType.ARMOR_HELMET.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_helmet")));

        InventorySlot leftArmSlot = new InventorySlot(
                InventoryItem.ItemUseType.WEAPON_ONEHAND.getValue() |
                        InventoryItem.ItemUseType.WEAPON_TWOHAND.getValue() |
                        InventoryItem.ItemUseType.ARMOR_SHIELD.getValue() |
                        InventoryItem.ItemUseType.WAND_ONEHAND.getValue() |
                        InventoryItem.ItemUseType.WAND_TWOHAND.getValue() ,
                        new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_weapon"))
        );

        InventorySlot rightArmSlot = new InventorySlot(
                InventoryItem.ItemUseType.WEAPON_ONEHAND.getValue() |
                        InventoryItem.ItemUseType.WEAPON_TWOHAND.getValue() |
                        InventoryItem.ItemUseType.ARMOR_SHIELD.getValue() |
                        InventoryItem.ItemUseType.WAND_ONEHAND.getValue() |
                        InventoryItem.ItemUseType.WAND_TWOHAND.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_shield"))

        );

        InventorySlot chestSlot = new InventorySlot(
                InventoryItem.ItemUseType.ARMOR_CHEST.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_chest")));

        InventorySlot legsSlot = new InventorySlot(
                InventoryItem.ItemUseType.ARMOR_FEET.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_boot")));

        headSlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
        leftArmSlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
        rightArmSlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
        chestSlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
        legsSlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));

        headSlot.addObserver(this);
        leftArmSlot.addObserver(this);
        rightArmSlot.addObserver(this);
        chestSlot.addObserver(this);
        legsSlot.addObserver(this);

        _dragAndDrop.addTarget(new InventorySlotTarget(headSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(leftArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(rightArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(legsSlot));

        _playerSlotsTable.setBackground(new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))).getDrawable());

        //layout
        for (int i = 1; i <= _numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            _inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            inventorySlot.addListener(new ClickListener() {
                                          public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                              super.touchUp(event, x, y, pointer, button);
                                              if (getTapCount() == 2) {
                                                  InventorySlot slot = (InventorySlot) event.getListenerActor();
                                                  if (slot.hasItem()) { //TODO create hasItem method in Inventoryslot Class
                                                      InventoryItem item = slot.getTopInventoryItem();
                                                      if (item.isConsumable()) {
                                                          String itemInfo = item.getItemUseType() + Component.MESSAGE_TOKEN + item.getItemUseTypeValue();
                                                          InventoryUI.this.notify(itemInfo, InventoryObserver.InventoryEvent.ITEM_CONSUMED);
                                                          slot.recoverActor(item);
                                                          slot.remove(item);
                                                      }
                                                  }
                                              }
                                          }


                                      }


            );


            if (i % _lengthSlotRow == 0) {
                _inventorySlotTable.row();
            }

        }

        _equipSlots.add();
        _equipSlots.add(headSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add(leftArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(chestSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(rightArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add();
        _equipSlots.right().add(legsSlot).size(_slotWidth, _slotHeight);

        _playerSlotsTable.add(_equipSlots);
        _inventoryActors.add(_inventorySlotTooltip);

        this.add(_playerSlotsTable).padBottom(20);
        this.add(labelTable);
        this.row();
        this.add(_inventorySlotTable).colspan(2);
        this.row();
        this.pack();


    }

    public static void clearInventoryItems(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if (inventorySlot == null) continue;
            inventorySlot.clearAllInventoryItems(false);
        }
    }

    public static Array<InventoryItemLocation> removeInventoryItems(String name, Table inventoryTable) {
        Array<Cell> cells = inventoryTable.getCells();
        Array<InventoryItemLocation> items = new Array<>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            inventorySlot.removeAllInventoryItemsWithName(name);
        }
        return items;
    }

    public static void populateInventory(Table targetTable, Array<IventoryItemLocation> inventoryItems, DragAndDrop dragandrop, String defaultName, boolean desableNonDefaultItems) {
        clearInventoryItems(targetTable);

        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < inventoryItems.size; i++) {
            InventoryItemLocation itemLocation = inventoryItems.get(i); // todo resolve this
            InventoryItem.ItemTypeID itemtyeID = IventoryItem.ItemTypeID.valueOf(itemLocation.getItemTypeLocation()); // todo resolve this
            InventorySlot inventorySlot = ((InventorySlot) cells.get(itemLocation.getLocationIndex()).getActor());  // todo resolve this

            for (int index = 0; index < itemLocation.getNumberItemsAtLocation(); index++) {
                InventoryItem item = InventoryItemFactory.getInstance().getInvetoryItem(itemTypedID);
                String itemName = itemLocation.getItemNameProperty(); //todo resolve this
                if (itemName == null || itemName.isEmpty()) {
                    item.setName(defaultName);
                } else {
                    item.setName(itemName);
                }

                inventorySlot.add(item);
                if (item.getName().equalsIgnoreCase(defaultName)) {
                    dragandrop.addSource(new InventorySlotSource(inventorySlot, draganddrop));
                } else if (disableNonDefaultItems == false) {
                    dragandrop.addSource(new InventorySlotSource(inventorySlot, draganddrop));

                }
            }
        }
    }

    public static Array<InventoryItemLocation> getInventory(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if (numItems > 0) {
                items.add(new InventoryItemLocation(
                        i,
                        inventorySlot.getTopInventoryItem().getItemTypeID().toString(),
                        numItems,
                        inventorySlot.getTopInventoryItem().getName()));
            }
        }
        return items;
    }


    public static Array<InventoryItemLocation> getInventoryFiltered(Table targetTAble, String filterOutName) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            int numItems = inventorySlot.getTopInventoryItem().getName();
            if (numItems > 0) {

                if (topItemName.equalsIgnoreCase(filterOutName)) continue;
                //System.out.println("[i] " + i + " itemtype: " + inventorySlot.getTopInventoryItem().getItemTypeID().toString() + " numItems " + numItems);
                items.add(new InventoryItemLocation(
                        i,
                        inventorySlot.getTopInventoryItem().getItemTypeID().toStringI(),
                        numItems,
                        inventorySlot.getToInventoryItem().getName()));
            }
        }
        return items;

    }

    public static Array<InventoryItemLocation> getInventory(Table targetTAble, String name) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor);
            if (inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems(name);
            if (numItems > 0) {
                //System.out.println("[i] " + i + " itemtype: " + inventorySlot.getTopInventoryItem().getItemTypeID().toString() + " numItems " + numItems);
                items.add(new InventoryItemLocation(
                        i,
                        inventorySlot.getTopInventoryItem().getItemTypeID().toString(),
                        numItems,
                        name));

            }
        }
        return items;
    }

    public static Array<InventoryItemLocation> getInventoryFiletered(Table sourceTable, Table targetTable, String filterOutName) {
        Array<InventoryItemLocation> items = getInventoryFiltered(targetTable, filterOutName);
        Array<Cell> sourceCells = sourceTable.getCells();
        int index = 0;
        for (InventoryItemLocation item : items) {
            for (; index < sourceCells.size; index++) {
                InventorySlot inventorySlot = ((InventorySlot) sourceCells.get(index).getActor());
                if (inventorySlot == null) continue;
                int numItems = inventorySlot.getNumItems();
                if (numItems == 0) {
                    item.setLocationIndex(index);
                    //System.out.println("[index] " + index + " itemtype: " + item.getItemTypeAtLocation() + " numItems " + numItems);
                    index++;
                    break;
                }

            }
            if (index == sourceCells.size) {
                //System.out.println("[index] " + index + " itemtype: " + item.getItemTypeAtLocation() + " numItems " + item.getNumberItemsAtLocation());
                item.setLocationIndex(index - 1);
            }

        }


        return items;
    }
    
    
    public static void setInventoryItemNames(Table target){
       Array<Cell> cells = targetTable.getCells();
       for(int i = 0; i < cells.size; i++){
           InventorySlot inventorySlot = ((InventorySlot)cells.get(i).getActor());
           if(inventorySlot == null ) continue;
           inventorySlot.updateAllInventoryItemNames(names);
       }
    }

    public boolean doesInventoryHaveSpace(){
        Array<Cell> sourceCells = _inventorySlotTable.getCells();
        int index = 0;

        for(; index < sourceCells.size; index++){
            InventorySlot inventorySlot = ((InvenotorySlot) sourceCells.get(index).getActor());
            if(inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if(numItems == 0){
                return true;
            }else{
                index++;
            }
        }
        return false;
    }

    public void addEntityToInventory(Entity entity, String itemName){
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++){
            InventorySlot inventorySlot = ((InventorySlot) sourceCells.get(index).getActor());
            if(inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if(numItems == 0){
                inventoryItem inventoryItem = InventoryItemFactory.getInstance().getInventoryItem(InventoryItem.ItemTypeID.valueOf(entity.getEntityConfig().getItemTypeID()));
                inventoryItem.setName(itemName);
                invetorySlot.add(inventoryItem);
                _dragAndDrop.addSource(new InveotrySlotSource(inventorySlot, _dragAndDrop));
                break;
            }
        }
    }


    public void removeQuestItemFromInventory(String questID){
        Array<Cell> sourceCells = _inventorySlotTable.getCells();
        for(int index = 0; index < sourceCells.size; index++){
            InventorySlot inventorySlot = ((InventorySlot) sourceCells.get(index).getActor);
            if(inventorySlot == null) continue;
            InventoryItem item = inventorySlot.getTopInventoryItem();
            if(item == null) continue;
            String inventoryItemName = item.getName();
            if(inventoryItemName != null && inventoryItemName.equals(questID) ){
                inventorySlot.clearAllInventoryItems(false);
            }
        }
    }


    public Array<Actor> getInventoryActors(){
        return _inventoryActors;
    }


    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {
        switch(event){

            case ADDED_ITEM:
                Inventory addItem = slot.getTopInventoryItem();
                if( addItem == null) return;
                if( addItem.isIventoryItemOffensive() ){
                    _APVal += addItem.getItemUseTypeValue();
                    _APVal.setText(String.valueOf(_APVal));
                    notify(String.valueOf(_APVal), InventoryObserver.InventoryEvent.UPDATED_AP);

                    if(addItem.isInventoryItemOffenseiveWand() ){
                        notify(String.valueOf(addItem.getItemUseTypeValue()), InventoryObserver.InventoryEvent.ADD_WAND_AP);
                    }
                }else if(addItem.isInventoryItemDefensive() ){
                    _DPVal += addItem.getItemUseTypeValue();
                    _DPValLabel.setText(String.valueOf(_DPVal));
                    notify(String.valueOf(_DPVal), InventoryObserver.InventoryEvent.UPDATED_DP);
                }
                break;
            case REMOVED_ITEM:
                InventoryItem removeItem = slot.getTopInventoryItem();
                if( removeItem == null) return;
                if( removeItem.isInventoryItemOffensive() ){
                    _APVal -= removeItem.getItemUseTypeValue();
                    _APValLabel.setText(String.valueOf(_APVal));
                    notify(String.valueOf(_APVal), InventoryObserver.InventoryEvent.UPDATED_AP);

                    if( removeItem.isInventoryItemOffensiveWand() ) {
                        notify(String.valueOf(removeItem.getItemUseTypeValue()), InventoryObserver.InventoryEvent.REMOVE_WAND_AP);
                    }

                    }else if(removeItem.isInvenotryItemDefensive() ){
                        _DPVal -= removeItem.getItemUseTypeValue();
                        _DPValLabel.setText(String.valueOf(_DPVal));
                        notify(String.valueOf(_DPVal), InventoryObserver.InventoryEvent.UPDATED_DP);
                    }
                    break;
            default:
                break;


        }
    }


    @Override
    public void addObserver(InventoryObserver inventoryObserver) {
        _observers.add(inventoryObserver);
    }



    @Override
    public void removeObserver(InventoryObserver inventoryObserver) {
        _observers.removeValue(inventoryObserver ,true);
    }



    @Override
    public void removeAllObservers() {
        for(InventoryObservers observers: _observers){
            _observers.removeValue(observers, true);
        }
    }






    public DragAndDrop getDragAndDrop(){
        return _dragAndDrop;
    }

    public Table getInventorySlotTable(){
        return _inventorySlotTable;
    }

    public Table getEquipSlotTable(){
        return _equipSlots;
    }








    @Override
    public void notify(String value, InventoryObserver.InventoryEvent event) {
        for(InventoryObserver observer: _observers){
            observer.onNotify(value, event);
        }
    }





    public void resetEquipSlots(){
        _DPVal = 0;
        _APVal = 0;

        _DPValLabel.setText(String.valueOf(_DPVal));
        notify(String.valueOf(_DPVal), InventoryObserver.InventoryEvent.UPDATED_DP);

        _APValLabel.setText(String.valueOf(_APVal));
        notify(String.valueOf(_APVal), InventoryObserver.InventoryEvent.UPDATED_AP);
    }


}
