package Game;

import Game.Parser.ParserOutput;
import Game.Type.GameObject;
import Game.Type.GameEngine;
import Game.Type.GameInventory;
import Game.Type.GameRoom;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GameDescription implements Serializable{
    
    private boolean end = false;
    
    private final List<GameRoom> rooms = new ArrayList<>();

    private final List<GameEngine> commands = new ArrayList<>();

    private final GameInventory inventory = new GameInventory();

    private GameRoom currentRoom;
    
    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
    
    public List<GameRoom> getRooms() {
        return rooms;
    }

    public List<GameEngine> getCommands() {
        return commands;
    }

    public GameRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(GameRoom currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<GameObject> getInventory() {
        return inventory.getList();
    }

    public abstract void init() throws Exception;

    public abstract String nextMove(ParserOutput p);
    
    public abstract void save() throws FileNotFoundException, IOException, ClassNotFoundException;
    
    public abstract GameDescription load() throws FileNotFoundException, IOException, ClassNotFoundException;
}