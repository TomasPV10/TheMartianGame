package Game.Parser;

import Game.Type.GameCharacter;
import Game.Type.EnemyEntity;
import Game.Type.GameNPC;
import Game.Type.GameObject;
import Game.Type.GameEngine;
import java.util.List;

public class Parser {

    private int checkForCommand(String token, List<GameEngine> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equals(token) || commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    private int checkForObject(String token, List<GameObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getName().equals(token) || objects.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }
    
    private int checkForCharacter(String token, List<GameNPC> NPCs) {
        for (int i = 0; i < NPCs.size(); i++) {
            if (NPCs.get(i) instanceof GameCharacter && (NPCs.get(i).getName().equals(token) || NPCs.get(i).getAlias().contains(token))) {
                return i;
            }
        }
        return -1;
    }
    
    private int checkForEnemy(String token, List<GameNPC> NPCs) {
        for (int i = 0; i < NPCs.size(); i++) {
            if (NPCs.get(i) instanceof EnemyEntity && (NPCs.get(i).getName().equals(token) || NPCs.get(i).getAlias().contains(token))) {
                return i;
            }
        }
        return -1;
    }

    public ParserOutput parse(String command, List<GameEngine> commands, List<GameObject> objects, List<GameObject> inventory, List<GameNPC> NPCs) {
        boolean flagExtraWords = false;
        String cmd = command.toLowerCase().trim();
        String[] tokens = cmd.split("\\s+");
        if (tokens.length > 0) {
            int inCommand = checkForCommand(tokens[0], commands);
            if (inCommand > -1) {
                if (tokens.length > 1) {
                    int inObj1 = -1;
                    int inObj2 = -1;
                    int inInvObj1 = -1;
                    int inInvObj2 = -1;
                    int inCharacter1 = -1;
                    int inCharacter2 = -1;
                    int inEnemy1 = -1;
                    int inEnemy2 = -1;
                    
                    for (int i=1; i<tokens.length; i++) {
                        if (inObj1<0 && inInvObj1<0 && inCharacter1<0 && inEnemy1<0) {
                            inObj1 = checkForObject(tokens[i], objects);
                            if (inObj1<0) {
                                inInvObj1 = checkForObject(tokens[i], inventory);
                                if (inInvObj1<0) {
                                    inCharacter1 = checkForCharacter(tokens[i], NPCs);
                                    if (inCharacter1<0) {
                                        inEnemy1 = checkForEnemy(tokens[i], NPCs);
                                        if (inEnemy1<0){
                                            flagExtraWords = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            inObj2 = checkForObject(tokens[i], objects);
                            if (inObj2<0) {
                                inInvObj2 = checkForObject(tokens[i], inventory);
                                if (inInvObj2<0) {
                                    inCharacter2 = checkForCharacter(tokens[i], NPCs);
                                    if (inCharacter2<0) {
                                        inEnemy2 = checkForEnemy(tokens[i], NPCs);
                                        if (inEnemy2<0) {
                                            flagExtraWords = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                   
                    if (inObj1 > -1 && inObj2 < 0 && inInvObj2 < 0 && inCharacter2 < 0 && inEnemy2 < 0) {                 //oggetto
                        return new ParserOutput(commands.get(inCommand), objects.get(inObj1), null, null, null, null, null, null, null, flagExtraWords);
                    } else if (inObj1 > -1 && inObj2 > -1) {                                                              //oggetto, oggetto
                        return new ParserOutput(commands.get(inCommand), objects.get(inObj1), null, null, null, objects.get(inObj2), null, null, null, flagExtraWords);
                    } else if (inObj1 > -1 && inInvObj2 > -1) {                                                           //oggetto, inventario
                        return new ParserOutput(commands.get(inCommand), objects.get(inObj1), null, null, null, null, inventory.get(inInvObj2), null, null, flagExtraWords);
                    } else if (inObj1 > -1 && inCharacter2 > -1) {                                                        //oggetto, personaggio
                        return new ParserOutput(commands.get(inCommand), objects.get(inObj1), null, null, null, null, null, (GameCharacter)NPCs.get(inCharacter2), null, flagExtraWords);
                    } else if (inObj1 > -1 && inEnemy2 > -1) {                                                            //oggetto, nemico
                        return new ParserOutput(commands.get(inCommand), objects.get(inObj1), null, null, null, null, null, null, (EnemyEntity)NPCs.get(inEnemy2), flagExtraWords);
                    
                    } else if (inInvObj1 > -1 && inObj2 < 0 && inInvObj2 < 0 && inCharacter2 < 0 && inEnemy2 < 0) {       //inventario            
                        return new ParserOutput(commands.get(inCommand), null, inventory.get(inInvObj1), null, null, null, null, null, null, flagExtraWords);
                    } else if (inInvObj1 > -1 && inObj2 > -1) {                                                           //inventario, oggetto
                        return new ParserOutput(commands.get(inCommand), null, inventory.get(inInvObj1), null, null, objects.get(inObj2), null, null, null, flagExtraWords);
                    } else if (inInvObj1 > -1 && inInvObj2 > -1) {                                                        //inventario, inventario
                        return new ParserOutput(commands.get(inCommand), null, inventory.get(inInvObj1), null, null, null, inventory.get(inInvObj2), null, null, flagExtraWords);
                    } else if (inInvObj1 > -1 && inCharacter2 > -1) {                                                     //inventario, personaggio
                        return new ParserOutput(commands.get(inCommand), null, inventory.get(inInvObj1), null, null, null, null, (GameCharacter)NPCs.get(inCharacter2), null, flagExtraWords);
                    } else if (inInvObj1 > -1 && inEnemy2 > -1) {                                                         //inventario, nemico
                        return new ParserOutput(commands.get(inCommand), null, inventory.get(inInvObj1), null, null, null, null, null, (EnemyEntity)NPCs.get(inEnemy2), flagExtraWords);
                    
                    } else if (inCharacter1 > -1 && inObj2 < 0 && inInvObj2 < 0 && inCharacter2 < 0 && inEnemy2 < 0) {    //personaggio
                        return new ParserOutput(commands.get(inCommand), null, null, (GameCharacter)NPCs.get(inCharacter1), null, null, null, null, null, flagExtraWords);
                    } else if (inCharacter1 > -1 && inObj2 > -1) {                                                        //personaggio, oggetto
                        return new ParserOutput(commands.get(inCommand), null, null, (GameCharacter)NPCs.get(inCharacter1), null, objects.get(inObj2), null, null, null, flagExtraWords);
                    } else if (inCharacter1 > -1 && inInvObj2 > -1) {                                                     //personaggio, inventario
                        return new ParserOutput(commands.get(inCommand), null, null, (GameCharacter)NPCs.get(inCharacter1), null, null, inventory.get(inInvObj2), null, null, flagExtraWords);
                    } else if (inCharacter1 > -1 && inCharacter2 > -1) {                                                  //personaggio, personaggio
                        return new ParserOutput(commands.get(inCommand), null, null, (GameCharacter)NPCs.get(inCharacter1), null, null, null, (GameCharacter)NPCs.get(inCharacter2), null, flagExtraWords);
                    } else if (inCharacter1 > -1 && inEnemy2 > -1) {                                                      //personaggio, nemico
                        return new ParserOutput(commands.get(inCommand), null, null, (GameCharacter)NPCs.get(inCharacter1), null, null, null, null, (EnemyEntity)NPCs.get(inEnemy2), flagExtraWords);
                    
                    } else if (inEnemy1 > -1 && inObj2 < 0 && inInvObj2 < 0 && inCharacter2 < 0 && inEnemy2 < 0) {        //nemico
                        return new ParserOutput(commands.get(inCommand), null, null, null, (EnemyEntity)NPCs.get(inEnemy1), null, null, null, null, flagExtraWords);
                    } else if (inEnemy1 > -1 && inObj2 > -1) {                                                            //nemico, oggetto
                        return new ParserOutput(commands.get(inCommand), null, null, null, (EnemyEntity)NPCs.get(inEnemy1), objects.get(inObj2), null, null, null, flagExtraWords);
                    } else if (inEnemy1 > -1 && inInvObj2 > -1) {                                                         //nemico, inventario
                        return new ParserOutput(commands.get(inCommand), null, null, null, (EnemyEntity)NPCs.get(inEnemy1), null, inventory.get(inInvObj2), null, null, flagExtraWords);
                    } else if (inEnemy1 > -1 && inCharacter2 > -1) {                                                      //nemico, personaggio
                        return new ParserOutput(commands.get(inCommand), null, null, null, (EnemyEntity)NPCs.get(inEnemy1), null, null, (GameCharacter)NPCs.get(inCharacter2), null, flagExtraWords); 
                    } else if (inEnemy1 > -1 && inEnemy2 > -1) {                                                          //nemico, nemico
                        return new ParserOutput(commands.get(inCommand), null, null, null, (EnemyEntity)NPCs.get(inEnemy1), null, null, null, (EnemyEntity)NPCs.get(inEnemy2), flagExtraWords);
                    
                    } else {
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, null, null, null, null, flagExtraWords);
                    }
                } else {
                    return new ParserOutput(commands.get(inCommand), null, flagExtraWords);
                }
            } else {
                return new ParserOutput(null, null, flagExtraWords);
            }
        } else {
            return null;
        }
    }

}
