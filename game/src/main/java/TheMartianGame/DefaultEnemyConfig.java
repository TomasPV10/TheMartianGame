package TheMartianGame;

import java.io.Serializable;

public class DefaultEnemyConfig implements EnemyConfig, Serializable {
    @Override
    public String cameraName() {
        return "telecamera";
    }
    @Override
    public String[] cameraAliases() {
        return new String[]{"telecamere"};
    }
    
    @Override
    public String guard1Name() {
        return "guardia";
    }
    @Override
    public String[] guard1Aliases() {
        return new String[]{"guardiano","sorvegliante","custode","sentinella"};
    }
    
    @Override
    public String guard2Name() {
        return "guardia";
    }
    @Override
    public String[] guard2Aliases() {
        return new String[]{"guardiano","sorvegliante","custode","sentinella"};
    }
}
