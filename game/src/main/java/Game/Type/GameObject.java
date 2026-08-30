package Game.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameObject implements Serializable{

    private final int id;

    private String name;

    private String description;
    
    private String unreachableDescription = null;
    
    private String examine;
    
    private Set<String> alias = new HashSet<>();
    
    private int contained = -1;

    private boolean openable = false;

    private boolean pickupable = true;

    private boolean pushable = false;
    
    private boolean reachable = true;
    
    private boolean usable = false;
    
    private boolean equippable = false;
    
    private boolean equipped = false;

    private boolean open = false;

    private boolean push = false;
    
    private boolean dropped = false;

    public GameObject(int id) {
        this.id = id;
    }

    public GameObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GameObject(int id, String name, String description, String examine) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.examine = examine;
    }

    public GameObject(int id, String name, String description, String examine, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.examine = examine;
        this.alias = alias;
    }
    
    public GameObject(int id, String name, String description, String examine, Set<String> alias, int contained) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.examine = examine;
        this.alias = alias;
        this.contained = contained;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnreachableDescription() {
        return unreachableDescription;
    }

    public void setUnreachableDescription(String unreachableDescription) {
        this.unreachableDescription = unreachableDescription;
    }
    
    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public int getContained() {
        return this.contained;
    }
    
    public void setContained(int contained) {
        this.contained = contained;
    }
    
    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openable) {
        this.openable = openable;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }
    
    public boolean isEquippable() {
        return equippable;
    }

    public void setEquippable(boolean equippable) {
        this.equippable = equippable;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
    
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
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
        final GameObject other = (GameObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
