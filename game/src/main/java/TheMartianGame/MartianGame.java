package TheMartianGame;

import Game.GameDescription;
import Game.Parser.ParserOutput;
import Game.Type.GameObject;
import Game.Type.GameEngine;
import Game.Type.CommandType;
import Game.Type.GameRoom;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import TheMartianGame.GraphicUserInterface.MartianGameFrame;

public class MartianGame extends GameDescription {

    CommandConfig commandNames;
    ObjectConfig objectParameters;
    CharacterConfig characterParameters;
    EnemyConfig enemyParameters;

    private int oxygenLevel = 100;
    private boolean doorAirlockUnlocked = false;
    private boolean fuseReplaced = false;
    private boolean greenhouseRepaired = false;
    private boolean terminalUnlocked = false;
    private boolean securitySystemOverridden = false;
    private boolean reactorCooled = false;
    
    private boolean airlockRefillUsed = false;
    private boolean supplyRefillUsed = false;
    private boolean medbayRefillUsed = false;
    
    private boolean roomExamined = false;

    private final Random random = new Random();
    private final String securityCode = "8492";

    public static final int ID_ROOM_OUTSIDE = 0;
    public static final int ID_ROOM_AIRLOCK = 1;
    public static final int ID_ROOM_SUBSTATION = 2;
    public static final int ID_ROOM_SUPPLY_DEPOT = 25;
    public static final int ID_ROOM_GREENHOUSE = 3;
    public static final int ID_ROOM_LAB = 4;
    public static final int ID_ROOM_MEDBAY = 45;
    public static final int ID_ROOM_SECURITY = 5;
    public static final int ID_ROOM_REACTORS = 6;
    public static final int ID_ROOM_COMMUNICATIONS = 7;
    public static final int ID_ROOM_TRAP = 99;

    private final int ID_OBJ_TETHER = 101;
    private final int ID_OBJ_FUSE = 102;
    private final int ID_OBJ_MULTITOOL = 103;
    private final int ID_OBJ_KEYCARD = 104;
    private final int ID_OBJ_COOLANT = 105;
    private final int ID_OBJ_O2_MINI = 106;

    public MartianGame(CommandConfig commandNames, ObjectConfig objectParameters, CharacterConfig characterParameters, EnemyConfig enemyParameters) {
        this.commandNames = commandNames;
        this.objectParameters = objectParameters;
        this.characterParameters = characterParameters;
        this.enemyParameters = enemyParameters;
    }

    public int getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(int oxygenLevel) { this.oxygenLevel = Math.max(0, Math.min(100, oxygenLevel)); }
    public String getSecurityCode() { return securityCode; }

    public String getSystemGuide() {
        return ">>> GUIDA OPERATIVA DI SISTEMA <<<\n" +
               "• SPOSTAMENTO TATTICO: Usa la pulsantiera direzionale per spostarti tra i moduli della base.\n" +
               "• 🔍 ESAMINA: Cerca oggetti, anomalie o analizza lo stato della stanza in cui ti trovi.\n" +
               "• ✋ PRENDI: Raccogli eventuali oggetti visibili per aggiungerli al tuo equipaggiamento.\n" +
               "• ⚡ RIPARA: Tenta di sistemare pannelli o dispositivi danneggiati nel modulo attuale.\n" +
               "• ⚙️ USA: Sfrutta lo strumento utile presente nel tuo inventario o i macchinari nell'area.\n" +
               "--------------------------------------\n" +
               "🫁 GESTIONE RISERVA DI OSSIGENO (O2):\n" +
               "• BARRA OSSIGENO: Situata nell'angolo in alto a destra dell'interfaccia.\n" +
               "• CONSUMO: Ogni movimento o danno all'Hub riduce l'ossigeno della tuta.\n" +
               "• RICARICHE: Puoi ricaricare l'O2 presso la Stazione Centrale dell'Airlock, nei moduli secondari (Deposito Scorte, Infermeria) o tramite bombole tascabili.\n" +
               "• SOGLIA CRITICA: Sotto il 25% scatta l'allarme visivo/sonoro di emergenza.\n" +
               "• GAME OVER: Se la barra raggiunge lo 0%, l'astronauta muore soffocato.\n" +
               "--------------------------------------\n\n";
    }

    @Override
    public void init() throws Exception {
        getCommands().add(new GameEngine(CommandType.NORD, commandNames.north()));
        getCommands().add(new GameEngine(CommandType.SOUTH, commandNames.south()));
        getCommands().add(new GameEngine(CommandType.EAST, commandNames.east()));
        getCommands().add(new GameEngine(CommandType.WEST, commandNames.west()));
        
        GameRoom rOutside = new GameRoom(ID_ROOM_OUTSIDE, "1. Esterno Hub", false);
        rOutside.setFirstDescription(
            "📖 TRAMA:\n" +
            "Anno 2046. Un'improvvisa tempesta di sabbia e una potente scossa sismica hanno\n" +
            "devastato l'Hub di ricerca 'Ares-3' su Marte. Il resto dell'equipaggio è stato\n" +
            "evacuato d'urgenza, ma sei rimasto bloccato all'esterno con i sistemi primari compromessi.\n\n" +
            "🎯 OBIETTIVO:\n" +
            "Raggiungere la Stanza 8 (Sala Comunicazioni Satellitare) per inviare il segnale\n" +
            "di soccorso prima che le riserve di aria si azzerino.\n\n" +
            "📍 STANZA 1: ESTERNO HUB\n" +
            "• DIREZIONE: ► EST (Airlock)"
        );

        GameRoom rAirlock = new GameRoom(ID_ROOM_AIRLOCK, "2. Airlock Principale", false);
        rAirlock.setFirstDescription(
            "📍 STANZA 2: AIRLOCK PRINCIPALE\n" +
            "• [STAZIONE O2 CENTRALE]: Ricarica TOTALE (100% O2) unica nel complesso.\n" +
            "• DIREZIONI: ◄ OVEST | ► EST (Cabina Elettrica)"
        );

        GameRoom rSubstation = new GameRoom(ID_ROOM_SUBSTATION, "3. Cabina Elettrica", false);
        rSubstation.setFirstDescription(
            "📍 STANZA 3: CABINA ELETTRICA\n" +
            "• PERCORSO OPZIONALE: Una botola a SUD porta al Deposito Scorte.\n" +
            "• DIREZIONI: ◄ OVEST | ► EST (Serra) | ▼ SUD (Deposito Scorte)"
        );

        GameRoom rSupplyDepot = new GameRoom(ID_ROOM_SUPPLY_DEPOT, "3B. Deposito Scorte (Opzionale)", false);
        rSupplyDepot.setFirstDescription(
            "📦 STANZA 3B: DEPOSITO SCORTE LOGISTICHE\n" +
            "• DEVIAZIONE SECONDARIA: Troverai risorse extra.\n" +
            "• [STAZIONE O2 SECONDARIA]: Ricarica Parziale (+30% O2).\n" +
            "• DIREZIONE: ▲ NORD (Torna alla Cabina Elettrica)"
        );

        GameRoom rGreenhouse = new GameRoom(ID_ROOM_GREENHOUSE, "4. Serra Idroponica", false);
        rGreenhouse.setFirstDescription(
            "📍 STANZA 4: SERRA IDROPONICA\n" +
            "• DIREZIONI: ◄ OVEST | ▼ SUD (Laboratorio Chimico)"
        );

        GameRoom rLab = new GameRoom(ID_ROOM_LAB, "5. Laboratorio Chimico", false);
        rLab.setFirstDescription(
            "📍 STANZA 5: LABORATORIO CHIMICO\n" +
            "• PERCORSO OPZIONALE: A EST si nota la porta dell'Infermeria di Emergenza.\n" +
            "• DIREZIONI: ▲ NORD | ◄ OVEST (Sicurezza) | ► EST (Infermeria) | ▼ SUD (??)"
        );

        GameRoom rMedbay = new GameRoom(ID_ROOM_MEDBAY, "5B. Infermeria di Emergenza (Opzionale)", false);
        rMedbay.setFirstDescription(
            "🏥 STANZA 5B: INFERMERIA DI EMERGENZA\n" +
            "• DEVIAZIONE SECONDARIA: Unita' medica di soccorso.\n" +
            "• [STAZIONE O2 MEDICINALE]: Ricarica Parziale (+50% O2).\n" +
            "• DIREZIONE: ◄ OVEST (Torna al Laboratorio)"
        );

        GameRoom rTrap = new GameRoom(ID_ROOM_TRAP, "⚠️ CONDOTTA DEPRESSURIZZATA", false);
        rTrap.setFirstDescription("💥 SEI ENTRATO NELLA CONDOTTA DANNEGGIATA! La pressione e' crollata a zero!");

        GameRoom rSecurity = new GameRoom(ID_ROOM_SECURITY, "6. Sala Sicurezza", false);
        rSecurity.setFirstDescription(
            "📍 STANZA 6: SALA SICUREZZA\n" +
            "• DIREZIONI: ► EST | ◄ OVEST (Reattore)"
        );

        GameRoom rReactors = new GameRoom(ID_ROOM_REACTORS, "7. Vano Reattore", false);
        rReactors.setFirstDescription(
            "📍 STANZA 7: VANO REATTORE\n" +
            "• DIREZIONI: ► EST | ◄ OVEST (Comunicazioni)"
        );

        GameRoom rComms = new GameRoom(ID_ROOM_COMMUNICATIONS, "8. Sala Comunicazioni", false);
        rComms.setFirstDescription(
            "🎉 STANZA 8: SALA COMUNICAZIONI SATELLITARE\n" +
            "🏆 VITTORIA! Sei riuscito a inviare il segnale di soccorso alla Terra!"
        );

        rOutside.setEast(rAirlock);
        rAirlock.setWest(rOutside); rAirlock.setEast(rSubstation);
        
        rSubstation.setWest(rAirlock); 
        rSubstation.setEast(rGreenhouse);
        rSubstation.setSouth(rSupplyDepot);
        
        rSupplyDepot.setNorth(rSubstation);

        rGreenhouse.setWest(rSubstation); 
        rGreenhouse.setSouth(rLab);
        
        rLab.setNorth(rGreenhouse); 
        rLab.setWest(rSecurity);
        rLab.setEast(rMedbay);
        rLab.setSouth(rTrap);

        rMedbay.setWest(rLab);

        rSecurity.setEast(rLab); rSecurity.setWest(rReactors);
        rReactors.setEast(rSecurity); rReactors.setWest(rComms);
        rComms.setEast(rReactors);

        getRooms().add(rOutside);
        getRooms().add(rAirlock);
        getRooms().add(rSubstation);
        getRooms().add(rSupplyDepot);
        getRooms().add(rGreenhouse);
        getRooms().add(rLab);
        getRooms().add(rMedbay);
        getRooms().add(rTrap);
        getRooms().add(rSecurity);
        getRooms().add(rReactors);
        getRooms().add(rComms);

        rOutside.getObjects().add(new GameObject(ID_OBJ_TETHER, "Cavo di Sicurezza"));
        rAirlock.getObjects().add(new GameObject(ID_OBJ_FUSE, "Fusibile 50A"));
        rSubstation.getObjects().add(new GameObject(ID_OBJ_COOLANT, "Cella Refrigerante"));
        rSupplyDepot.getObjects().add(new GameObject(ID_OBJ_O2_MINI, "Piccola Bombola O2 (+20%)"));
        rGreenhouse.getObjects().add(new GameObject(ID_OBJ_MULTITOOL, "Multitool Isolante"));
        rMedbay.getObjects().add(new GameObject(ID_OBJ_O2_MINI, "Piccola Bombola O2 (+20%)"));
        rSecurity.getObjects().add(new GameObject(ID_OBJ_KEYCARD, "Keycard Livello 4"));

        setCurrentRoom(rOutside);
    }

    public String move(String direction) {
        GameRoom next = null;
        if (direction.equalsIgnoreCase("NORD")) next = getCurrentRoom().getNorth();
        if (direction.equalsIgnoreCase("SUD")) next = getCurrentRoom().getSouth();
        if (direction.equalsIgnoreCase("EST")) next = getCurrentRoom().getEast();
        if (direction.equalsIgnoreCase("OVEST")) next = getCurrentRoom().getWest();

        if (next == null) return "❌ PERCORSO NON DISPONIBILE: Parete cieca verso " + direction + ".";

        if (next.getId() == ID_ROOM_TRAP) {
            setCurrentRoom(next);
            setOxygenLevel(0);
            setEnd(true);
            return "💥 ERRORE FATALE! Sei entrato in una zona sismica non pressurizzata. La tuta si e' squarciata!\n== GAME OVER ==";
        }

        if (getCurrentRoom().getId() == ID_ROOM_OUTSIDE && direction.equalsIgnoreCase("EST") && !doorAirlockUnlocked) {
            return "🔒 AIRLOCK SIGILLATO: Prendi il 'Cavo di Sicurezza' e usa 'USA'.";
        }
        if (getCurrentRoom().getId() == ID_ROOM_SUBSTATION && direction.equalsIgnoreCase("EST") && !fuseReplaced) {
            return "🔒 NESSUNA CORRENTE: Usa 'RIPARA' per posizionare il Fusibile.";
        }
        if (getCurrentRoom().getId() == ID_ROOM_GREENHOUSE && direction.equalsIgnoreCase("SUD") && !greenhouseRepaired) {
            return "🔒 SERRA ALLAGATA: Raccogli il Multitool e usa 'RIPARA'.";
        }
        if (getCurrentRoom().getId() == ID_ROOM_LAB && direction.equalsIgnoreCase("OVEST") && !terminalUnlocked) {
            return "🔒 BLOCCO SICUREZZA: Digita il PIN 8492 nel terminale.";
        }
        if (getCurrentRoom().getId() == ID_ROOM_SECURITY && direction.equalsIgnoreCase("OVEST") && !securitySystemOverridden) {
            return "🔒 ALLARME ATTIVO: Usa 'USA' sulla consolle della Stanza 6.";
        }
        if (getCurrentRoom().getId() == ID_ROOM_REACTORS && direction.equalsIgnoreCase("OVEST") && !reactorCooled) {
            return "⚠️ SURRISCALDAMENTO: Usa la Cella Refrigerante ('USA') prima di proseguire.";
        }

        setCurrentRoom(next);
        roomExamined = false;
        oxygenLevel -= 10;

        String randomEventMsg = triggerRandomHubDamage();

        if (oxygenLevel <= 0) {
            setEnd(true);
            return "⚠️ OSSIGENO ESAURITO! Sei svenuto nell'Hub.\n== GAME OVER ==";
        }

        if (next.getId() == ID_ROOM_COMMUNICATIONS) {
            setEnd(true);
        }

        return "Ti sposti a " + direction + ".\n\n" + next.getFirstDescription() + randomEventMsg;
    }

    private String triggerRandomHubDamage() {
        if (getCurrentRoom().getId() == ID_ROOM_OUTSIDE || getCurrentRoom().getId() == ID_ROOM_COMMUNICATIONS) {
            return "";
        }

        int chance = random.nextInt(100);
        if (chance < 35) {
            int severity = random.nextInt(10);
            if (severity < 5) {
                oxygenLevel -= 8;
                return "\n\n⚠️ DANNO HUB LIEVE! Micro-perdita nelle guarnizioni! (-8% O2)";
            } else if (severity < 8) {
                oxygenLevel -= 15;
                return "\n\n🚨 DANNO HUB MEDIO! Caduta di pannelli dal soffitto e perdita di pressione! (-15% O2)";
            } else {
                oxygenLevel -= 25;
                return "\n\n💥 DANNO HUB GRAVE! Esplosione di un tubo primario dell'aria! (-25% O2)";
            }
        }
        return "";
    }

    public String executeExamine() {
        roomExamined = true;
        if (getCurrentRoom().getObjects().isEmpty()) {
            return "🔍 ESAME COMPLETATO: Ispezionando l'area non trovi alcun oggetto utile a terra.";
        }
        StringBuilder sb = new StringBuilder("🔍 ESAME COMPLETATO: Ispezionando attentamente l'area hai individuato:\n");
        for (GameObject obj : getCurrentRoom().getObjects()) {
            sb.append(" • ").append(obj.getName()).append("\n");
        }
        sb.append("Ora puoi usare l'azione PRENDI per raccogliere quanto trovato.");
        return sb.toString();
    }

    public String executePickUp() {
        if (!roomExamined) {
            return "❌ NON PUOI RACCOGLIERE NULLA: Prima devi ESAMINARE la stanza per individuare gli oggetti!";
        }
        if (!getCurrentRoom().getObjects().isEmpty()) {
            GameObject obj = getCurrentRoom().getObjects().remove(0);
            getInventory().add(obj);

            switch (obj.getId()) {
                case ID_OBJ_TETHER:
                    return "✅ Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Serve per agganciarti alla porta dell'Airlock nella Stanza 1 e procedere verso EST. Premere 'USA'.";
                case ID_OBJ_FUSE:
                    return "✅ Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Serve per ripristinare la corrente nella Cabina Elettrica (Stanza 3). Premere 'RIPARA' nella Stanza 3.";
                case ID_OBJ_COOLANT:
                    return "✅ Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Serve per raffreddare il nocciolo del reattore nella Stanza 7. Premere 'USA' nel Vano Reattore.";
                case ID_OBJ_MULTITOOL:
                    return "✅ Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Serve per riparare i circuiti allagati della Serra Idroponica (Stanza 4). Premere 'RIPARA' nella Stanza 4.";
                case ID_OBJ_KEYCARD:
                    return "✅ Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Ti permette di sbloccare i comandi di sicurezza nella Stanza 6. Premere 'USA' nella Sala Sicurezza.";
                case ID_OBJ_O2_MINI:
                    return "📦 Hai raccolto: " + obj.getName() + "\n💡 UTILIZZO: Puo' essere usata in qualsiasi momento dal tasto 'USA' per ricaricare istantaneamente +20% di Ossigeno.";
                default:
                    return "✅ Hai raccolto: " + obj.getName();
            }
        }
        return "Nessun altro oggetto da raccogliere in questa zona.";
    }

    public String executeRepair() {
        int id = getCurrentRoom().getId();
        
        if (id == ID_ROOM_AIRLOCK && !airlockRefillUsed) {
            airlockRefillUsed = true;
            setOxygenLevel(100);
            return "🫁 STAZIONE O2 CENTRALE: Il serbatoio della tuta e' stato ricaricato completamente al 100%!";
        }
        if (id == ID_ROOM_SUPPLY_DEPOT && !supplyRefillUsed) {
            supplyRefillUsed = true;
            setOxygenLevel(getOxygenLevel() + 30);
            return "🫁 SERBATOIO AUX DEPOSITO: Ricarica parziale eseguita (+30% O2)!";
        }
        if (id == ID_ROOM_MEDBAY && !medbayRefillUsed) {
            medbayRefillUsed = true;
            setOxygenLevel(getOxygenLevel() + 50);
            return "🫁 EROGATORE MEDICO: Ricarica parziale avanzata eseguita (+50% O2)!";
        }

        if (id == ID_ROOM_SUBSTATION) {
            boolean hasFuse = getInventory().stream().anyMatch(o -> o.getId() == ID_OBJ_FUSE);
            if (hasFuse) {
                fuseReplaced = true;
                return "⚡ Fusibile sostituito! Alimentazione ripristinata verso EST.";
            }
            return "❌ Manca il Fusibile 50A!";
        }
        if (id == ID_ROOM_GREENHOUSE) {
            boolean hasMulti = getInventory().stream().anyMatch(o -> o.getId() == ID_OBJ_MULTITOOL);
            if (hasMulti) {
                greenhouseRepaired = true;
                return "⚙️ Serra stabilizzata! Strada aperta a SUD.";
            }
            return "❌ Ti serve il Multitool Isolante!";
        }
        return "Nulla da riparare qui.";
    }

    public String executeUse() {
        int id = getCurrentRoom().getId();
        
        if (id == ID_ROOM_AIRLOCK && !airlockRefillUsed) {
            airlockRefillUsed = true;
            setOxygenLevel(100);
            return "🫁 STAZIONE O2 CENTRALE: Tuta ricaricata al 100%!";
        }
        if (id == ID_ROOM_SUPPLY_DEPOT && !supplyRefillUsed) {
            supplyRefillUsed = true;
            setOxygenLevel(getOxygenLevel() + 30);
            return "🫁 SERBATOIO AUX DEPOSITO: Tuta ricaricata del +30%!";
        }
        if (id == ID_ROOM_MEDBAY && !medbayRefillUsed) {
            medbayRefillUsed = true;
            setOxygenLevel(getOxygenLevel() + 50);
            return "🫁 EROGATORE MEDICO: Tuta ricaricata del +50%!";
        }

        if (id == ID_ROOM_OUTSIDE) {
            boolean hasTether = getInventory().stream().anyMatch(o -> o.getId() == ID_OBJ_TETHER);
            if (hasTether) {
                doorAirlockUnlocked = true;
                return "⚓ Cavo agganciato! Airlock sbloccato a EST.";
            }
        }
        if (id == ID_ROOM_SECURITY) {
            securitySystemOverridden = true;
            return "💻 Consolle resettata! Porta OVEST del Reattore aperta.";
        }
        if (id == ID_ROOM_REACTORS) {
            boolean hasCoolant = getInventory().stream().anyMatch(o -> o.getId() == ID_OBJ_COOLANT);
            if (hasCoolant) {
                reactorCooled = true;
                return "❄️ Reattore raffreddato! Accesso aperto alla Stanza 8 (OVEST).";
            }
        }

        GameObject miniO2 = getInventory().stream().filter(o -> o.getId() == ID_OBJ_O2_MINI).findFirst().orElse(null);
        if (miniO2 != null) {
            getInventory().remove(miniO2);
            setOxygenLevel(getOxygenLevel() + 20);
            return "🫁 BOMBOLA EXTRA USATA: Riserva O2 +20%.";
        }

        return "Nessun meccanismo azionabile qui.";
    }

    public boolean enterPin(String pin) {
        if (getCurrentRoom().getId() == ID_ROOM_LAB && pin.trim().equals(securityCode)) {
            terminalUnlocked = true;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MartianGameFrame().setVisible(true));
    }

    @Override public String nextMove(ParserOutput p) { return ""; }
    @Override public void save() throws FileNotFoundException, IOException, ClassNotFoundException {}
    @Override public GameDescription load() throws FileNotFoundException, IOException, ClassNotFoundException { return null; }
}