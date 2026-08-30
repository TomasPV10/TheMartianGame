package TheMartianGame;

import java.io.Serializable;

public class DefaultCharacterConfig implements CharacterConfig, Serializable {
    @Override
    public String HectorName() {
        return "hector";
    }
    @Override
    public String[] HectorAliases(){
        return new String[]{"ettore","autista","l'autista","amico","compagno"};
    }
}
