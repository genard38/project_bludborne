package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.desktop.InventoryItem;

public class InventorySlotTarget extends DragAndDrop.Target {

    InventorySlot _targetSlot;
    public InventorySlotTarget(InventorySlot actor) {
        super(actor);
        _targetSlot = actor;

    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        InventoryItem sourceActor = (InventoryItem) payload.getDragActor();
        InventoryItem targetActor = _targetSlot.getTopInventoryItem(); //TODO  fix this
        InventorySlot sourceSlot = ((InventorySlotSource) source).getSourceSlot(); // TODO  fix this

        if(sourceActor == null){
            return;
        }

        //First, does the slot accept the source item type?
        if( !_targetSlot.doesAcceptItemUseType(sourceActor.getItemUseType())){ // TODO  fix this
            //Put item back where it came from, slot doesn't accept item
            sourceSlot.add(sourceActor);
            return;
        }


        if( ! _targetSlot.hasItem() ){ // TODO  fix this
            _targetSlot.add(sourceActor);
        }else{
            //If they aren't the same items or the items aren't stackable, then swap
            InventorySlot.swapSlots(sourceSlot, _targetSlot, sourceActor); // TODO  fix this
        }
    }
}
