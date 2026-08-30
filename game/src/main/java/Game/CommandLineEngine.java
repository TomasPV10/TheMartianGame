package Game;

import TheMartianGame.MartianGame;
import TheMartianGame.DefaultCharacterConfig;
import TheMartianGame.DefaultCommandNames;
import TheMartianGame.DefaultEnemyConfig;
import TheMartianGame.DefaultObjectConfig;
import Game.Parser.Parser;
import Game.Parser.ParserOutput;
import Game.Type.CommandType;
import java.io.IOException;
import java.util.Scanner;

public class CommandLineEngine {

    private GameDescription game;

    private final Parser parser;

    public CommandLineEngine(GameDescription game) {
        this.game = game;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        parser = new Parser();
    }

    public void run() {
        System.out.println("\n////   " + game.getCurrentRoom().getName() + "   ////");
        System.out.println(game.getCurrentRoom().getFirstDescription());
        game.getCurrentRoom().setVisited(true);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory(), game.getCurrentRoom().getNPCs());
            if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                if (p.getObject1() != null || p.getInvObject1() != null || p.getCharacter1() != null || p.getEnemy1() != null || p.hasExtraWords()) {
                    System.out.println("Forse intendevi: esci");
                } else {
                    System.out.println("Addio!");
                    break;
                }
                
            } else if(p.getCommand() != null && p.getCommand().getType()==CommandType.SAVE){
                if (p.getObject1() != null || p.getInvObject1() != null || p.getCharacter1() != null || p.getEnemy1() != null || p.hasExtraWords()) {
                    System.out.println("Forse intendevi: salva");
                } else {
                    try {
                           game.save();
                           System.out.println("Salvataggio partita completato.");
                    } catch(IOException | ClassNotFoundException e) {
                        System.out.println("Si e' verificata un'eccezione.");
                    }
                }
                
            }else if(p.getCommand() != null && p.getCommand().getType() == CommandType.LOAD) {
                if (p.getObject1() != null || p.getInvObject1() != null || p.getCharacter1() != null || p.getEnemy1() != null || p.hasExtraWords()) {
                    System.out.println("Forse intendevi: carica");
                } else {
                    try {
                        game = game.load();
                        System.out.println("Caricamento partita completato.");
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println("Si e' verificata un'eccezione.");
                    } 
                }
            }
           
            else{
                System.out.println(game.nextMove(p));
                if (game.isEnd()) {
                    System.exit(0);
                }
                System.out.println("================================================");
            } 
        }
    }

    public static void main(String[] args) {
        CommandLineEngine engine = new CommandLineEngine(new MartianGame(new DefaultCommandNames(), new DefaultObjectConfig(), new DefaultCharacterConfig(), new DefaultEnemyConfig()));
        engine.run();
    }

}