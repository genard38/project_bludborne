package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.desktop.audio.AudioSubject;
import com.mygdx.game.desktop.bludbourne.ComponentObserver;
import com.mygdx.game.desktop.bludbourne.Entity;

public class PlayerHUD implements Screen, AudioSubject, ProfileObserver, ComponentObserver, ConversationGraphObserver,StoreInventoryObserver, BattleObserver, InventoryObserver, StatusObserver { //TODO create all this interface

    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;
    private Entity _player;

    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private ConversationUI _conversationUI;
    //TODO continue here

}
