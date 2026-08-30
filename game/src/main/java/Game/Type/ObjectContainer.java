package Game.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectContainer extends GameObject {

    private List<GameObject> list = new ArrayList<>();

    public ObjectContainer(int id) {
        super(id);
    }

    public ObjectContainer(int id, String name) {
        super(id, name);
    }

    public ObjectContainer(int id, String name, String description, String examine) {
        super(id, name, description, examine);
    }

    public ObjectContainer(int id, String name, String description, String examine, Set<String> alias) {
        super(id, name, description, examine, alias);
    }
    
    public ObjectContainer(int id, String name, String description, String examine, Set<String> alias, int contained) {
        super(id, name, description, examine, alias, contained);
    }

    public List<GameObject> getList() {
        return list;
    }

    public void setList(List<GameObject> list) {
        this.list = list;
    }

    public void add(GameObject o) {
        list.add(o);
        o.setContained(this.getId());
    }

    public void remove(GameObject o) {
        list.remove(o);
        o.setContained(-1);
    }

}
