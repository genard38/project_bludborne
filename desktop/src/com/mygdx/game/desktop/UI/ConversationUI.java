package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.desktop.bludbourne.Utility;

public class ConversationUI extends Window {
    private static final String TAG = ConversationUI.class.getSimpleName();

    private Label _dialogText;
    private List _listItems;
    private ConversationGraph _graph; // TODO create this class
    private String _currentEntityID;

    private TextButton _closeButton;

    private Json _json;
    private String title;

    public ConversationUI(){
        super("dialog", Utility.STATUSUI_SKIN, "solidbackground");
        _json = new Json();
        _graph = new ConversationGraph();

        //create
        _dialogText = new Label("No Conversation", Utility.STATUSUI_SKIN);
        //TODO continue here

    }
}
