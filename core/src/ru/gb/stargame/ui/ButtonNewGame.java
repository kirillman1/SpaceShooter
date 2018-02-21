package ru.gb.stargame.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.ui.ScaledTouchUpButton;

public class ButtonNewGame extends ScaledTouchUpButton {

    private static final float HEIGHT = 0.07f;
    private static final float TOP = 0.012f;
    private static final float PRESS_SCALE = 0.9f;


    public ButtonNewGame(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("new-game"), PRESS_SCALE, actionListener);
        setHeightProportion(HEIGHT);
        setTop(TOP);
    }
}
