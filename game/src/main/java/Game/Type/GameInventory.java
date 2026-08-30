package Game.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class GameInventory implements Serializable{

    private List<GameObject> list = new ArrayList<>();

    public GameInventory() {
    }

    public GameInventory(List<GameObject> list) {
        this.list = list;
    }
    
    public List<GameObject> getList() {
        return list;
    }

    public void setList(List<GameObject> list) {
        this.list = list;
    }

    public void add(GameObject o) {
        list.add(o);
    }

    public void remove(GameObject o) {
        list.remove(o);
    }
    
    public GameObject get(int id) {
        GameObject obj = null;
        for (GameObject o : list) {
            if (o.getId() == id) {
                obj = o;
            }
        }
        return obj;
    }
    
    public void remove(int id) {
        List<GameObject> l = list;
        if (!l.isEmpty()) {
            Iterator<GameObject> it = l.iterator();
            while (it.hasNext()) {
                GameObject next = it.next();
                    if (next.getId() == id) {
                        it.remove();
                    }
            }
        }
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.list);
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
        final GameInventory other = (GameInventory) obj;
        if (!Objects.equals(this.list, other.list)) {
            return false;
        }
        return true;
    }
}
