package com.mygdx.game.desktop.bludbourne;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Hashtable;


////the change yowfasdfaf
// thehahfahdfadfafahfafafaf


//the second changes

// the third changes and fourth changes


public class MapManager {
    public final static float UNIT_SCALE = 1 / 16f;
    private static final String TAG = MapManager.class.getSimpleName();
    //maps
    private final static String TOP_WORLD = "TOP_WORLD";
    private final static String TOWN = "TOWN";
    private final static String CASTLE_OF_DOOM = "CASTLE_OF_DOOM";
    //Map layers
    private final static String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
    private final static String MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    private final static String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
    private final static String PLAYER_START = "PLAYER_START";
    //All maps for the game
    private Hashtable <String, String> _mapTable;
    private Hashtable <String, Vector2> _playerStartLocationTable;
    private Camera _camera;
    private Vector2 _playerStartPositionRect;
    private Vector2 _closestPlayerStartPosition;
    private Vector2 _convertedUnits;
    private Vector2 _playerStart;
    private TiledMap _currentMap = null;
    private String _currentMapName;
    private MapLayer _collisionLayer = null;
    private MapLayer _portalLayer = null;
    private MapLayer _spawnsLayer = null;
    private Entity _currentSelectedEntity = null;

    private Entity _player;

    public MapManager() {
        _playerStart = new Vector2(0, 0);
        _mapTable = new Hashtable();

        // make sure that the strings match.
        _mapTable.put(TOP_WORLD, "maps./topworld.tmx");
        _mapTable.put(TOWN, "maps/town.tmx");
        _mapTable.put(CASTLE_OF_DOOM, "maps/castle_of_doom.tmx");

        _playerStartLocationTable = new Hashtable();
        _playerStartLocationTable.put(TOP_WORLD, _playerStart.cpy());

        _playerStartPositionRect = new Vector2(0, 0);
        _closestPlayerStartPosition = new Vector2(0, 0);
        _convertedUnits = new Vector2(0, 0);

    }
    public void unregisterCurrentMapEntityObservers(){
        if(_currentMap != null){
            Array<Entity> entities = _currentMap.getMapEntities();  // todo fix me
            for(Entity entity: entities){
                entity.unregisterObservers(); // todo fix me
            }
        }
    }







    /* this method is a straightforward helper method
       that verifies that the string passed in is a valid
       and checks to see whether the asset exists;
     */
    public void loadMap(String mapName) {
        _playerStart.set(0, 0);

        String mapFullPath = _mapTable.get(mapName);

        if (mapFullPath == null || mapFullPath.isEmpty()) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        if (_currentMap != null) {
            _currentMap.dispose();
        }


        Utility.loadMapAsset(mapFullPath);
        if (Utility.isAssetLoaded(mapFullPath)) {
            _currentMap = Utility.getMapAsset(mapFullPath);
            _currentMapName = mapName;
        } else {
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        _collisionLayer = _currentMap.getLayers().get(MAP_COLLISION_LAYER);
        if (_collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer!");
        }

        _portalLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);

        if (_portalLayer == null) {
            Gdx.app.debug(TAG, "No portal layer!");
        }

        _spawnsLayer = _currentMap.getLayers().get(MAP_SPAWNS_LAYER);


        if (_spawnsLayer == null) {
            Gdx.app.debug(TAG, "No spawn layer! ");
        } else {
            Vector2 start = _playerStartLocationTable.get(_currentMapName);
            if (start.isZero()) {
                setClosestStartPosition(_playerStart);
                start = _playerStartLocationTable.get(_currentMapName);
            }
            _playerStart.set(start.x, start.y);
        }

        Gdx.app.debug(TAG, "Player Start: ( " + _playerStart.x +
                "," + _playerStart.y + ")");
    }





    public TiledMap getCurrentMap() {
        if (_currentMap == null) {
            _currentMapName = TOWN;
            loadMap(_currentMapName);
        }
        return _currentMap;
    }



    public MapLayer getCollisionLayer() {
        return _collisionLayer;
    }

    public MapLayer getPortalLayer() {
        return _portalLayer;
    }


    public Vector2 getPlayerStartUnitScaled() {
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x * UNIT_SCALE,_playerStart.y * UNIT_SCALE);
        return playerStart;
    }


    private void setClosestStartPosition(final Vector2 position) {
        //Get last known position on this map
        _playerStartPositionRect.set(0, 0);
        _closestPlayerStartPosition.set(0, 0);
        float shortestDistance = 0f;

        //Go through all player start positions and choose closest to
        // last known position
        for (MapObject object : _spawnsLayer.getObjects()) {
            if (object.getName().equalsIgnoreCase(PLAYER_START)) {
                ((RectangleMapObject) object).getRectangle().getPosition(_playerStartPositionRect);
                float distance = position.dst2(_playerStartPositionRect);

                if (distance < shortestDistance || shortestDistance == 0) {
                    _closestPlayerStartPosition.set(_playerStartPositionRect);
                    shortestDistance = distance;

                }
            }
        }

        _playerStartLocationTable.put(_currentMapName, _closestPlayerStartPosition.cpy());
    }


    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        if (UNIT_SCALE <= 0)
            return;


        _convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE);
        setClosestStartPosition(_convertedUnits);
    }

    public Array<Entity> getCurrentMapEntities() {
        return _currentMap.getMapEntities(); // todo fix me
    }
    


    public void setCurrentSelectedMapEntity(Entity currentSelectedEntity) {
        this._currentSelectedEntity = currentSelectedEntity;

    }

    public Camera getCamera (){
        return _camera;
    }

    public void setCamera (Camera camera){
        this._camera =camera;
    }


    public void setPlayer(Entity entity) {
        this._player = entity;
    }
}

















