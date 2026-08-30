package TheMartianGame;

import java.io.Serializable;

public class DefaultObjectConfig implements ObjectConfig, Serializable {
    @Override
    public String gunName() {
        return "pistola";
    }
    @Override
    public String[] gunAliases() {
        return new String[]{"ferro"};
    }
    
    @Override
    public String gunAmmoName() {
        return "munizioni";
    }
    @Override
    public String[] gunAmmoAliases() {
        return new String[]{"colpi","proiettili"};
    }

    @Override
    public String fingerName() {
        return "dito";
    }
    @Override
    public String[] fingerAliases() {
        return new String[]{"braccio","mano","mani","braccia","dita","indice","pollice","medio","anulare","mignolo"};
    }

    @Override
    public String knifeName() {
        return "coltello";
    }
    @Override
    public String[] knifeAliases() {
        return new String[]{"lama","coltellino","pugnale"};
    }
    
    @Override
    public String glassesName() {
        return "occhiali";
    }
    @Override
    public String[] glassesAliases() {
        return new String[]{"visore"};
    }
    
    @Override
    public String fingerContainerName() {
        return "riconoscitore";
    }
    @Override
    public String[] fingerContainerAliases() {
        return new String[]{"lettore","analizzatore","sensore","fessura","buco"};
    }
    
    @Override
    public String cameraControllerName() {
        return "telecamere";
    }
    @Override
    public String[] cameraControllerAliases() {
        return new String[]{"sorveglianza","videosorveglianza","video","computer","monitor","telecamera"};
    }
    
    @Override
    public String diamondName() {
        return "diamante";
    }
    @Override
    public String[] diamondAliases() {
        return new String[]{"gioiello","tesoro"};
    }
    
    @Override
    public String firstAidName() {
        return "kit";
    }
    @Override
    public String[] firstAidAliases() {
        return new String[]{"cassetta","pronto","cura"};
    }
    
    @Override
    public String buttonName() {
        return "bottone";
    }
    @Override
    public String[] buttonAliases() {
        return new String[]{"pulsante","interruttore","tasto"};
    }
}
