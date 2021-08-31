package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.desktop.InventoryItem;
import com.mygdx.game.desktop.audio.AudioManager;
import com.mygdx.game.desktop.audio.AudioObserver;
import com.mygdx.game.desktop.audio.AudioSubject;
import com.mygdx.game.desktop.bludbourne.ComponentObserver;
import com.mygdx.game.desktop.bludbourne.Entity;
import com.mygdx.game.desktop.bludbourne.MapManager;
import com.mygdx.game.desktop.bludbourne.Utility;
import com.mygdx.game.desktop.dialog.ConversationGraphObserver;

public class PlayerHUD implements Screen, AudioSubject, ProfileObserver, ComponentObserver, ConversationGraphObserver,StoreInventoryObserver, BattleObserver, InventoryObserver, StatusObserver { //TODO create all this interface

    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;
    private Entity _player;

    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private ConversationUI _conversationUI;
    private static final String INVENTORY_FULL = "Your inventory is full! ";
    private StoreInventoryUI _storeInventoryUI;
    private QuestUI _questUI;
    private BattleUI _battleUI;
    private Dialog _messageBoxUI;
    private Json _json;
    private MapManager _mapMgr;
    private Array<AudioObserver> _observers;
    private ScreenTransitionActor _transitionActor;
    private ShakeCamera _shakeCam;
    private ClockActor _clock;

    public PlayerHUD(OrthographicCamera camera, Entity player, MapManager mapMgr) {
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
            {
                button("OK");
                text(INVENTORY_FULL);
            }
            @Override
            protected void result(final Object object){
                cancel();
                setVisible(false);
            }
        };

        _clock = new ClockActor("0", Utility.STATUSUI_SKIN); // TODO create class ClockActor
        _clock.setPosition(_stage.getWidth()- _clock.getWidth(),0); // todo resolve this
        _clock.setRateOfTime(60);
        _clock.setVisible(true);

        _messageBoxUI.setVisible(false);
        _messageBoxUI.pack();
        _messageBoxUI.setPosition(_stage.getWidth()/ 2 - _messageBoxUI.getWidth() / 2, _stage.getHeight() / 2 - _messageBoxUI.getHeight() / 2);

        _statusUI = new StatusUI();
        _statusUI.setVisible(true);
        _statusUI.setPosition(0,0);
        _statusUI.setKeepWithinStage(false);
        _statusUI.setMovable(false);

        _inventoryUI = new InventoryUI();
        _inventoryUI.setKeepWithinStage(false);
        _inventoryUI.setMovable(false);
        _inventoryUI.setVisible(false);
        _inventoryUI.setPosition(_statusUI.getWidth(), 0);

        _conversationUI = new ConversationUI();
        _conversationUI.setMovable(true);
        _conversationUI.setVisible(false);
        _conversationUI.setPosition(_stage.getWidth()/2,0);
        _conversationUI.setWidth(_stage.getWidth() / 2);
        _conversationUI.setHeight(_stage.getHeight() / 2);

        _storeInventoryUI = new StoreInventoryUI();
        _storeInventoryUI.setMovable(false);
        _storeInventoryUI.setVisible(false);
        _storeInventoryUI.setPosition(0,0);

        _questUI = new QuestUI();
        _questUI.setMovable(false);
        _questUI.setVisible(false);
        _questUI.setKeepWithinStage(false);
        _questUI.setPosition(0,_stage.getHeight() / 2);
        _questUI.setWidth(_stage.getWidth());
        _questUI.setHeight(_stage.getHeight() /2 );

        _battleUI = new BattleUI();
        _battleUI.setMovable(false);
        //removes all listeners including ones that handle focus
        _battleUI.clearListeners();
        _battleUI.setVisible(false);

        _stage.addActor(_battleUI);
        _stage.addActor(_questUI);
        _stage.addActor(_storeInventoryUI);
        _stage.addActor(_conversationUI);
        _stage.addActor(_messageBoxUI);
        _stage.addActor(_statusUI);
        _stage.addActor(_inventoryUI);
        _stage.addActor(_clock);

        _battleUI.validate();
        _questUI.validate();
        _storeInventoryUI.validate();
        _conversationUI.validate();
        _messageBoxUI.validate();
        _statusUI.validate();
        _inventoryUI.validate();
        _clock.validate();

        //add tooltips to the stage
        Array<Actor> actors = _inventoryUI.getInventoryActors();
        for(Actor actor: actors){
            _stage.addActor(actor);
        }

        Array<Actor> storeActors = _storeInventoryUI.getInventoryActors();
        for(Actor actor : storeActors){
            _stage.addActor(actor);
        }

        _stage.addActor(_transitionActor);
        _transitionActor.setVisible(false);

        //Observers
        _player.registerObserver(this);
        _statusUI.addObserver(this);
        _storeInventoryUI.addObserver(this);
        _inventoryUI.addObserver(_battleUI.getCurrentState());
        _inventoryUI.addObserver(this);
        _battleUI.getCurrentState().addObserver(this);
        this.addObserver(AudioManager.getInstance());

        //Listeners
        ImageButton inventoryButton = StatusUI.getInventoryButton(); //todo resolve this
        inventoryButton.addListener((ClickListener) clicked(event,x,y){ //todo resolve this
            _inventoryUI.setVisible(_inventoryUI.isVisible() ? false: true);
        });


        ImageButton questButton = _statusUI.getQuestButton();
        questButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                _questUI.setVisible(_questUI.isVisible() ? false : true);
            }
        });




        _conversationUI.getCloseButton().addListener((ClickListener) clicked(event, x, y){
            _conversationUI.setVisible(false);
            _mapMgr.clearCurrentSelectedMapEntity();
        });



        _storeInventoryUI.getCloseButton().addListener((ClickListener) clicked(event,x, y){
            _storeInventoryUI.savePlayerInventory();
            _storeInventoryUI.cleanupStoreInventory();
            _storeInventoryUI.setVisible(false);
            _mapMgr.clearCurrentSelectedMapEntity();
        });


        //Music/Sound Loading
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_BATTLE);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_LEVEL_UP_FANFARE);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_COIN_RUSTLE);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_CREATURE_PAIN);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_PLAYER_PAIN);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_PLAYER_WAND_ATTACK);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_EATING);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.SOUND_DRINKING);

    }

    public Stage getStage(){
        return _stage;
    }
    
    public ClockActor.TimeOfDay getCurrentTimeOfDay(){
        return _clock.getCurrentTimeOfDay();
    }

    public void updateEntityObservers(){
        _mapMgr.unregisterCurrentMapEntityObservers();
        _questUI.initQuests(_mapMgr);
        _mapMgr.unregisterCurrentMapEntityObservers(this);
    }

    public void addTransitionToScreen(){
        _transitionActor.setVisible(true);
        _stage.addAction(
                Action.sequence(
                        Actions.addAction(ScreenTransitionAction.transition(ScreenTransition.ScreenTransitionType.FADE_IN,1), _transitionActor)));
    }

    public void OnNotify(ProfileManager profileManager, ProfileEvent event){
        switch(event){
            case PROFILE_LOADED:
                boolean firstime = profileManager.getIsNewProfile();

                if( firstime ) {
                    InventoryUI.clearInventoryItems(_inventoryUI.getInventorySlotTable());
                    InventoryUI.clearInventoryItems(_inventoryUI.getEquipSlotTable());
                    _inventoryUI.resetEquipSlots();

                    _questUI.setQuests(new Array<QuestGraph>());

                    //add default items if first time
                    Array<InventoryItem.ItemTypeID> items = _player.getEntityConfig().getInventory();
                    Array<InventoryItemLocation> itemLocations = new Array<>();
                    for (int i = 0; i < items.size; i++) {
                        itemLocations.add(InventoryItemLocation(i, items.get(i).toString(), 1, InventoryUI.PLAYER_INVENTORY));

                    }
                    InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), itemLocations, _inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);
                    profileManager.setProperty("playerInventory", InventoryUI.getInventory(_inventoryUI.getInventorySlotTable()));

                    //start the player with some money
                    _statusUI.setGoldValue(20);
                    _statusUI.setStatusForLevel(1);

                    _clock.setTotalTime(60 * 60 * 12); //start at noon
                    profileManager.setProperty("currentTime", _clock.getTotalTime());
                }else{
                    int goldVal = profileManager.getProperty("currentPlayerGP", Integer.class);

                    Array<InventoryItemLocation> inventory = profileManager.getProperty("playerInventory", Array.class);
                    InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(),inventory,_inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);

                    Array<InventoryItemLocation> equipInventory = profileManager.getProperty("playerEquipInventory", Array.class); //todo create class InventoryItemLocation
                    if( equipInventory != null && equipInventory.size > 0){
                        _inventoryUI.resetEquipSlots();
                        InventoryUI.populateInventory(_inventoryUI.getEquipSlotTable(), equipInventory, _inventoryUI.getDragAndDrop(),InventoryUI.PLAYER_INVENTORY,false);
                    }

                    Array<QuestGraph> quests = profileManager.getProperty("playerQuests", Array.class);
                    _questUI.setQuest(quests);

                    int xpMaxVal = profileManager.getProperty("currentPlayerXPMax",Integer.class);
                    int xpVal = profileManager.getProperty("currentPlayerXP", Integer.class);

                    int hpMaxVal = profileManager.getProperty("currentPlayerHPMax", Integer.class);
                    int hpVal = profileManager.getProperty("currentPlayerHP", Integer.class);

                    int mpMaxVal = profileManager.getProperty("currentPlayerMPMax", Integer.class);
                    int mpVal = profileManager.getProperty("currentPlayerMP", Integer.class );

                    int levelVal = profileManager.getProperty("currentPlayerLevel", Integer.class);


                    //set the current max values first
                    _statusUI.setXPValueMax(xpMaxVal);
                    _statusUI.setHPValueMax(hpMaxVal);
                    _statusUI.setMPValueMax(mpMaxVal);

                    _statusUI.setXPValue(xpVal);
                    _statusUI.setHPValue(hpVal);
                    _statusUI.setMPValue(mpVal);

                    //then add in current values
                    _statusUI.setGoldValue(goldVal);
                    _statusUI.setLevelValue(levelVal);

                    float totalTime = profileManager.getProperty("currentTime", Float.class);
                    _clock.setTotalTime(totalTime);
                }
                break;

            case SAVING_PROFILE:
                profileManager.setProperty("playerQuests", _questUI.getQuests());
                profileManager.setProperty("playerInventory", InventoryUI.getInventory(_inventoryUI.getInventorySlotTable()));
                profileManager.setProperty("playerEquipInventory", InventoryUI.getInventory(_inventoryUI.getEquipSlotTable()));
                profileManager.setProperty("currentPlayerGP", _statusUI.getGoldValue());
                profileManager.setProperty("currentPlayerLevel", _statusUI.getLevelValue());
                profileManager.setProperty("currentPlayerXP", _statusUI.getXPValue());
                profileManager.setProperty("currentPlayerXPMax", _statusUI.getXPValueMax());
                profileManager.setProperty("currentPlayerHP", _statusUI.getHPValue() );
                profileManager.setProperty("currentPlayerHPMax", _statusUI.getHPValueMax() );
                profileManager.setProperty("currentPlayerMP", _statusUI.getMPValue() );
                profileManager.setProperty("currentPlayerMPMax", _statusUI.getMPValueMax() );
                profileManager.setProperty("currentTime", _clock.getTotalTime());
                break;
            case CLEAR_CURRENT_PROFILE:
                profileManager.setProperty("playerQuests", new Array<QuestGraph>());
                profileManager.setProperty("playerInventory", new Array<InventoryItemLocation>());
                profileManager.setProperty("playerEquipInventory", new Array<InventoryItemLocation>());
                profileManager.setProperty("currentPlayerGP", 0 );
                profileManager.setProperty("currentPlayerLevel",0 );
                profileManager.setProperty("currentPlayerXP", 0 );
                profileManager.setProperty("currentPlayerXPMax", 0 );
                profileManager.setProperty("currentPlayerHP", 0 );
                profileManager.setProperty("currentPlayerHPMax", 0 );
                profileManager.setProperty("currentPlayerMP", 0 );
                profileManager.setProperty("currentPlayerMPMax", 0 );
                profileManager.setProperty("currentTime", 0);
                break;
            default:
                break;






        }
    }
    
    
    
    
    //TODO continue here

}
