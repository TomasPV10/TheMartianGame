package Game.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameNPC implements Serializable {

    private final int id;

    private String name;

    private String examine;
    
    private String look;
    
    private Set<String> alias = new HashSet<>();
    
    private GameInventory inventory = new GameInventory();
    
    public GameNPC(int id) {
        this.id = id;
    }

    public GameNPC(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GameNPC(int id, String name, String examine) {
        this.id = id;
        this.name = name;
        this.examine = examine;
    }
    
    public GameNPC(int id, String name, String examine, String look) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look=look;
    }

    public GameNPC(int id, String name, String examine, String look, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look=look;
        this.alias = alias;
    }
    
    public GameNPC(int id, String name, String examine, String look, Set<String> alias, GameInventory inventory) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look=look;
        this.alias = alias;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }
    
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }
    
    public int getId() {
        return id;
    }
    
    public List<GameObject> getInventory() {
        return inventory.getList();
    }

    public void setInventory(GameInventory inventory) {
        this.inventory = inventory;
    }
    
    public void addInventory(GameObject o) {
        inventory.add(o);
    }
    
    public void removeFromInventory(int id) {
        inventory.remove(id);
    }
    
    public GameObject getFromInventory(int id) {
        return inventory.get(id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
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
        final GameNPC other = (GameNPC) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}