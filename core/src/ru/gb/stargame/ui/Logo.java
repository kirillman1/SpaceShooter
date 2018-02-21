package ru.gb.stargame.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.stargame.engine.Sprite;

public class Logo extends Sprite {
    private static final float HEIGHT = 0.3f;
    private static final float BOTTOM_MARGIN = 0.09f;

    public Logo(TextureAtlas atlas) {
        super(atlas.findRegion("logo"));
        setHeightProportion(HEIGHT);
        setBottom(BOTTOM_MARGIN);
    }
}
