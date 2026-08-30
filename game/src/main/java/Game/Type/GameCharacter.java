package Game.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GameCharacter extends GameNPC {
    
    private boolean firstDialogueFlag;
    
    private String firstDialogue;
    
    private Map<String,String> dialogues = new HashMap<>();
    
    public GameCharacter(int id) {
        super(id);
        firstDialogueFlag = true;
    }

    public GameCharacter(int id, String name) {
        super(id, name);
        firstDialogueFlag = true;
    }

    public GameCharacter(int id, String name, String examine) {
        super(id, name, examine);
        firstDialogueFlag = true;
    }
    
    public GameCharacter(int id, String name, String examine, String look) {
        super(id, name, examine, look);
        firstDialogueFlag = true;
    }

    public GameCharacter(int id, String name, String examine, String look, Set<String> alias) {
        super(id, name, examine, look, alias);
        firstDialogueFlag = true;
    }
    
    public GameCharacter(int id, String name, String examine, String look, Set<String> alias, GameInventory inventory, boolean firstDialogueFlag, String firstDialogue, Map<String,String> dialogues) {
        super(id, name, examine, look, alias, inventory);
        this.firstDialogueFlag = firstDialogueFlag;
        this.firstDialogue = firstDialogue;
        this.dialogues = dialogues;
    }

    public boolean isFirstDialogue() {
        return firstDialogueFlag;
    }

    public void setFirstDialogueFlag(boolean firstDialogue) {
        this.firstDialogueFlag = firstDialogue;
    }

    public String getFirstDialogue() {
        return firstDialogue;
    }

    public void setFirstDialogue(String firstDialogue) {
        this.firstDialogue = firstDialogue;
    }
    
    public Map<String, String> getDialogues() {
        return dialogues;
    }

    public void setDialogues(Map<String, String> dialogues) {
        this.dialogues = dialogues;
    }
    
    public void putDialogue(String key, String value) {
        dialogues.put(key, value);
    }
    
    public String getDialogue(String key) {
        return dialogues.get(key);
    }
    
    public boolean containsDialogue(String key) {
        return dialogues.containsKey(key);
    }
}