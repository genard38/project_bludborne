package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.desktop.bludbourne.Utility;

public class StatusUI extends Window implements StatusSubject {


    private static final String LEVEL_TABLE_CONFIG = "scripts/level_table.json";
    private Image _hpBar;
    private Image _mpBar;
    private Image _xpBar;
    private ImageButton _inventoryButton;
    private ImageButton _questButton;
    private Array<StatusObserver> _observers;
    private Array<LevelTable> _levelTables;
    //Attributes
    private int _levelVal = -1;
    private int _goldVal = -1;
    private int _hpVal = -1;
    private int _mpVal = -1;
    private int _xpVal = 0;

    private int _xpCurrentMax = -1;
    private int _hpCurrentMax = -1;
    private int _mpCurrentMax = -1;

    private Label _hpValLabel;
    private Label _mpValLabel;
    private Label _xpValLabel;
    private Label _levelValLabel;
    private Label _goldValLabel;

    private float _barWidth = 0;
    private float _barHeight = 0;

    public StatusUI(){
        super("status", Utility.STATUSUI_SKIN);

        _levelTables = LevelTable.getLevelTables(LEVEL_TABLE_CONFIG);

        _observers = new Array <StatusObserver>();

        //groups
        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        _hpBar = new Image (Utility.STATUSUI_TEXTUREATLAS.findRegion("HP_Bar"));
        Image bar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        _mpBar = new Image(Utility.STATUS_TEXTUREATLAS.findRegion("MP_Bar"));
        Image bar2 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));

        _barWidth = _hpBar.getWidth();
        _barHeight = _hpBar.getHeight();

        //labels
        Label hpLabel = new Label(" hp: ", Utility.STATUSUI_SKIN);
        _hpValLabel = new Label(String.valueOf(_hpVal), Utility.STATUSUI_SKIN);
        Label mpLabel = new Label(" mp: ", Utility.STATUSUI_SKIN);
        _mpValLabel = new Label(String.valueOf(_mpVal), Utility.STATUSUI_SKIN);
        Label xpLabel = new Label( " xp: ", Utility.STATUSUI_SKIN);
        _xpValLabel = new Label(String.valueOf(_xpVal), Utility.STATUSUI_SKIN);
        Label levelLabel = new Label(" lv: ", Utility.STATUSUI_SKIN);
        _levelValLabel = new Label(String.valueOf(_levelVal), Utility.STATUSUI_SKIN);
        Label goldLabel = new Label( " gp: ", Utility.STATUS_SKIN);
        _goldValLabel = new Label(String.valueOf(_goldVal), Utility.STATUSUI_SKIN);


        //buttons
        _inventoryButton = new ImageButton(Utility.STATUSUI_SKIN,"inventory-button");
        _inventoryButton.getImageCell().size(32,32);
















    }


    @Override
    public void addObserver(StatusObserver statusObserver) {

    }

    @Override
    public void removeObserver(StatusObserver statusObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(int value, StatusObserver.StatusEvent event) {

    }

    //pag natapos na punta na sa ibang mga redlines

    // todo fill this



}
