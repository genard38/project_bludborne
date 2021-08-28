package com.mygdx.game.desktop.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.desktop.bludbourne.EntityConfig;
import com.mygdx.game.desktop.bludbourne.Utility;
import com.mygdx.game.desktop.dialog.Conversation;
import com.mygdx.game.desktop.dialog.ConversationChoice;
import com.mygdx.game.desktop.dialog.ConversationGraph;

import java.util.ArrayList;


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
        _dialogText.setWrap(true);
        _dialogText.setAlignment(Align.center);
        _listItems = new List<ConversationChoice>(Utility.STATUSUI_SKIN);//todo resolve this

        _closeButton = new TextButton("X", Utility.STATUSUI_SKIN);

        ScrollPane scrollPane = new ScrollPane(_listItems, Utility.STATUSUI_SKIN,"inventoryPane");
        scrollPane.setOverscroll(false,false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true,false  );
        scrollPane.setForceScroll(true,false);
        scrollPane.setScrollBarPositions(false, true);

        //layout
        this.add();
        this.add(_closeButton);
        this.row();

        this.defaults().expand().fill();
        this.add(_dialogText).pad(10,10,10,10);
        this.row();
        this.add(scrollPane).pad(10,10,10,10);


        //this.debug();

        //Listeners
        _listItems.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y){
                ConversationChoice choice = (ConversationChoice)_listItems.getSelected();
                if(choice == null) return;
                _graph.notify(_graph, choice.getConversationCommandEvent());
            }
        });

    }


    public TextButton getCloseButton(){
        return _closeButton;
    }

    public String getCurrentEntityID(){
        return _currentEntityID;
    }

    public void loadConversation(EntityConfig entityConfig){
        String fullFilenamePath = entityConfig.getConversationConfigPath(); //todo create method getConversationConfigPath();
        this.setTitle("");

        clearDialog();


        if(fullFilenamePath.isEmpty() || !Gdx.files.internal(fullFilenamePath).exists() ){
           Gdx.app.debug(TAG,"Conversation files does not exist!");
           return;
        }

        _currentEntityID = entityConfig.getEntityID();
        this.setTitle(entityConfig.getEntityID());

        ConversationGraph graph = _json.fromJson(ConversatioGraph.class, Gdx.files.internal(fullFilenamePath));
        setConversationGraph(graph);

    }

    public void setConversationGraph(ConversationGraph graph){
        if( _graph != null ) _graph.removeAllObservers();
        this._graph = graph;
        populateConversationDialog(_graph.getCurrentConversationID());
    }

    public ConversationGraph getCurrentConversationGraph(){
        return this._graph;
    }

    private void populateConversationDialog(String conversationID){
        clearDialog();

        Conversation conversation = _graph.getConversationByID(conversationID); //todo create Conversation
        if( conversation == null) return;
        _graph.setCurrentConversation(conversationID); //todo create method setCurrentConversation();
        _dialogText.setText(conversation.getDialog());
        ArrayList<ConversationChoice> choices = _graph.getCurrentChoices(); //todo create class ConversationChoice
        if( choices == null) return;
        _listItems.setItems(choices.toArray());// todo fix this
        _listItems.setSelectedIndex(-1);
    }

    private void clearDialog(){
        _dialogText.setText("");
        _listItems.clearItems();
    }


    public void setTitle(String title){
        this.title = title;
    }




}
