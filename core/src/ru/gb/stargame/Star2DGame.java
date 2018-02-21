package ru.gb.stargame;

import com.badlogic.gdx.Game;

import ru.gb.stargame.screen.MenuScreen;

public class Star2DGame extends Game{

    public static boolean getAutoShoot() {
        return isAutoShoot;
    }

    private static boolean isAutoShoot;



    public Star2DGame(boolean isAutoShoot) {
        this.isAutoShoot = isAutoShoot;
    }

    /**
     * Устанавливаем текущий экран
     */
    @Override
    public void create() {
        setScreen(new MenuScreen( this));
    }
}
