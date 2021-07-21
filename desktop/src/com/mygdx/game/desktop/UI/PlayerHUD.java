package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.desktop.audio.AudioObserver;
import com.mygdx.game.desktop.bludbourne.ComponentObserver;
import com.mygdx.game.desktop.bludbourne.Entity;
import com.mygdx.game.desktop.bludbourne.MapManager;

public class PlayerHUD implements Screen, AudioSubject, ProfileObserver, ComponentObserver, ConversationGraphObserver,StoreInventoryObserver, BattleObserver, InventoryObserver, StatusObserver {

    //todo fill this whole class


    private static final String INVENTORY_FULL = "Your inventory is full!";
    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;
    private Entity _player;
    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private ConversationUI _conversationUI;
    private StoreInventory _storeInventoryUI;
    private QuestUI _questUI;
    private BattleUI _battleUI;
    private Dialog _messageBoxUI;
    private Json _json;
    private MapManager _mapMgr;
    private Array<AudioObserver> _observers;
    private ScreenTransitionActor _transitionActor;
    private ShakeCamera _shakeCam;
    private ClockActor _clock;

    public PlayerHUD(Camera camera, Entity player, MapManager mapMgr){
        _camera = camera;
        _player = player;
        _mapMgr = mapMgr;
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);
        //_stage.setDebugAll(true);

        _observers = new Array<AudioObserver>();
        _transitionActor = new ScreenTransitionActor();

        _shakeCam = new ShakeCamera(0,0,30.0f);

        _json = new Json();
        _messageBoxUI = new Dialog("Message", Utility.STATUSUI_SKIN,"solidbackground"){

        }
    }



}
