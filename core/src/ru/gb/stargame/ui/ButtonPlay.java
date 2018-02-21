package ru.gb.stargame.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.ui.ScaledTouchUpButton;

public class ButtonPlay extends ScaledTouchUpButton {
    public ButtonPlay(TextureAtlas atlas, float pressScale, ActionListener actionListener) {
        super(atlas.findRegion("play-button"), pressScale, actionListener);

    }

    @Override
    public void resize(Rect worldBounds) {
        //setBottom(worldBounds.getBottom());
        //setLeft(worldBounds.getLeft());
        pos.set(worldBounds.pos.x, worldBounds.pos.y - 0.1f);
    }
}
