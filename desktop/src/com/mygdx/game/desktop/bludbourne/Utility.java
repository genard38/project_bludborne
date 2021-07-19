package com.mygdx.game.desktop.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;



public class Utility {

    public static final AssetManager _assetManager = new AssetManager();


    private static final String TAG = Utility.class.getSimpleName();


    public static InternalFileHandleResolver _filePathResolver = new InternalFileHandleResolver();


    public static void unloadAsset(String assetFilenamePath){
        //once the asset manager is done loading
        if(_assetManager.isLoaded(assetFilenamePath)){
            _assetManager.unload(assetFilenamePath);
        }else{
            Gdx.app.debug(TAG,"Asset is not loaded; Nothing to unload: "
                    + assetFilenamePath);

        }
    }





    public static float loadCompleted(){
        return _assetManager.getProgress();
    }

    public static int numberAssetsQueued(){
        return _assetManager.getQueuedAssets();
    }

    public static boolean updateAssetLoading(){
        return _assetManager.update();
    }

    public static boolean isAssetLoaded(String filename){
        return _assetManager.isLoaded(filename);
    }



    public static void loadMapAsset (String mapFilenamePath) {
        if (mapFilenamePath == null || mapFilenamePath.isEmpty()) {
            return;
        }


        //load asset
        if (_filePathResolver.resolve(mapFilenamePath).exists()) {
            _assetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver));
            _assetManager.load(mapFilenamePath, TiledMap.class);
            //Until we add loading screen,
            //just block until we load the map
            _assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Map doesn’t exist!: " + mapFilenamePath);
        }
    }







    public static TiledMap getMapAsset(String mapFilenamePath){
        TiledMap map = null;
        // once the asset manager is done loading
        if(_assetManager.isLoaded(mapFilenamePath) ){
            map = _assetManager.get(mapFilenamePath,TiledMap.class);
        } else {
            Gdx.app.debug(TAG,  "Map is not loaded: " + mapFilenamePath );
        }
        return map;
    }



    public static void loadTextureAsset(String textureFilenamePath){
        if(textureFilenamePath == null || textureFilenamePath.isEmpty()){
            return;
        }

        //load asset
        if(_filePathResolver.resolve(textureFilenamePath).exists() ){
            _assetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));

            _assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen
            //just block until we load the map
            _assetManager.finishLoadingAsset(textureFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: "+ textureFilenamePath);
        }
    }


    public static Texture getTextureAsset(String textureFilenamePath){
        Texture texture = null;

        //once the asset manager is done loading
        if(_assetManager.isLoaded(textureFilenamePath)){
            texture = _assetManager.get(textureFilenamePath,Texture.class);
        }else{
            Gdx.app.debug(TAG,"Texture is not loaded: "+
                    textureFilenamePath);
        }
        return texture;
    }

    public static void loadMusicAsset(String musicFilenamePath) {
        if(musicFilenamePath == null || musicFilenamePath.isEmpty()){
            return;
        }

        if (_assetManager.isLoaded(musicFilenamePath)) {
            return;
        }

        //load asset
        if(_filePathResolver.resolve(musicFilenamePath).exists() ){ // if exists
            _assetManager.setLoader(Music.class, new MusicLoader(_filePathResolver));
            _assetManager.load(musicFilenamePath, Music.class);
            //Until we add loading screen, just block until we load the map
            _assetManager.finishLoadingAsset(musicFilenamePath);
            Gdx.app.debug(TAG,"Music loaded!: " + musicFilenamePath);
        }
        else{
            Gdx.app.debug(TAG,"Music doesn't exist!:" + musicFilenamePath);
        }
    }



    public static void loadSoundAsset(String soundFilenamePath) {
        if(soundFilenamePath == null || soundFilenamePath.isEmpty() ){
            return;
        }
        if( _assetManager.isLoaded(soundFilenamePath)){
            return;
        }

        //load asset
        if(_filePathResolver.resolve(soundFilenamePath).exists()){
            _assetManager.setLoader(Sound.class, new SoundLoader(_filePathResolver));
            _assetManager.load(soundFilenamePath, Sound.class);
            //Until we add loading screen, just block until we load the map
            _assetManager.finishLoadingAsset(soundFilenamePath);
            Gdx.app.debug(TAG, "Sound loaded!: " + soundFilenamePath );

        } else {
            Gdx.app.debug(TAG, "Sound doesn't exist!: " + soundFilenamePath);
        }
    }







    public static Music getMusicAsset(String fullFilePath) {

    }

    public static Sound getSoundAsset(String fullFilePath) {
    }
}

