package ru.gb.stargame.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.stargame.engine.Sprite;

public class MessageGameOver extends Sprite {

    private static final float HEIGHT = 0.2f;
    private static final float BOTTOM_MARGIN = 0.09f;

    public MessageGameOver(TextureAtlas atlas) {
        super(atlas.findRegion("game-over"));
        setHeightProportion(HEIGHT);
        setBottom(BOTTOM_MARGIN);
    }
}
