package Game.Parser;

import Game.Type.GameCharacter;
import Game.Type.EnemyEntity;
import Game.Type.GameObject;
import Game.Type.GameEngine;

public class ParserOutput{

    private GameEngine command;

    private GameObject object1 = null;
    private GameObject invObject1 = null;
    private GameCharacter character1 = null;
    private EnemyEntity enemy1 = null;
    
    private GameObject object2 = null;
    private GameObject invObject2 = null;
    private GameCharacter character2 = null;
    private EnemyEntity enemy2 = null;
    
    private boolean extraWords;

    public ParserOutput(GameEngine command, GameObject object, boolean extraWords) {
        this.command = command;
        this.object1 = object;
        this.extraWords = extraWords;
    }

    public ParserOutput(GameEngine command, GameObject object1, GameObject invObject1, GameCharacter character1, EnemyEntity enemy1, GameObject object2, GameObject invObject2, GameCharacter character2, EnemyEntity enemy2, boolean extraWords) {
        this.command = command;
        this.object1 = object1;
        this.invObject1 = invObject1;
        this.character1 = character1;
        this.enemy1 = enemy1;
        this.object2 = object2;
        this.invObject2 = invObject2;
        this.character2 = character2;
        this.enemy2 = enemy2;
        this.extraWords = extraWords;
    }

    public GameEngine getCommand() {
        return command;
    }

    public void setCommand(GameEngine command) {
        this.command = command;
    }

    public GameObject getObject1() {
        return object1;
    }
    
    public GameObject getObject2() {
        return object2;
    }

    public void setObject1(GameObject object) {
        this.object1 = object;
    }
    
    public void setObject2(GameObject object) {
        this.object2 = object;
    }

    public GameObject getInvObject1() {
        return invObject1;
    }
    
    public GameObject getInvObject2() {
        return invObject2;
    }

    public void setInvObject1(GameObject invObject) {
        this.invObject1 = invObject;
    }
    
    public void setInvObject2(GameObject invObject) {
        this.invObject2 = invObject;
    }

    public GameCharacter getCharacter1() {
        return character1;
    }

    public void setCharacter1(GameCharacter character1) {
        this.character1 = character1;
    }

    public GameCharacter getCharacter2() {
        return character2;
    }

    public void setCharacter2(GameCharacter character2) {
        this.character2 = character2;
    }
    
    public EnemyEntity getEnemy1() {
        return enemy1;
    }

    public void setEnemy1(EnemyEntity enemy1) {
        this.enemy1 = enemy1;
    }

    public EnemyEntity getEnemy2() {
        return enemy2;
    }

    public void setEnemy2(EnemyEntity enemy2) {
        this.enemy2 = enemy2;
    }
    
    public boolean hasExtraWords() {
        return extraWords;
    }

    public void setExtraWords(boolean extraWords) {
        this.extraWords = extraWords;
    }
}
