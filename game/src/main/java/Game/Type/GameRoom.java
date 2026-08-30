package Game.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameRoom implements Serializable{

    private final int id;

    private String name;
    
    private String firstDescription;

    private String description;
    
    private String lockedDescription = null;

    private String look;

    private boolean visited = false;

    private GameRoom south = null;

    private GameRoom north = null;

    private GameRoom east = null;

    private GameRoom west = null;
    
    private boolean locked=false;
    
    private final List<GameObject> objects=new ArrayList<>();
    
    private final List<GameNPC> NPCs = new ArrayList<>();

    public GameRoom(int id) {
        this.id = id;
    }
    
    public GameRoom(int id, String name, boolean locked) {
        this.id = id;
        this.name = name;
        this.locked=locked;
    }

    public GameRoom(int id, String name, String firstDescription, String description, boolean locked) {
        this.id = id;
        this.name = name;
        this.firstDescription = firstDescription;
        this.description = description;
        this.locked=locked;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getFirstDescription() {
        return firstDescription;
    }

    public void setFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public GameRoom getSouth() {
        return south;
    }

    public void setSouth(GameRoom south) {
        this.south = south;
    }

    public GameRoom getNorth() {
        return north;
    }

    public void setNorth(GameRoom north) {
        this.north = north;
    }

    public GameRoom getEast() {
        return east;
    }

    public void setEast(GameRoom east) {
        this.east = east;
    }

    public GameRoom getWest() {
        return west;
    }

    public void setWest(GameRoom west) {
        this.west = west;
    }

    public List<GameObject> getObjects() {
        return objects;
    }    
    
    public List<GameNPC> getNPCs() {
        return NPCs;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameRoom other = (GameRoom) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public String getLockedDescription() {
        return lockedDescription;
    }

    public void setLockedDescription(String lockedDescription) {
        this.lockedDescription = lockedDescription;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    
}
