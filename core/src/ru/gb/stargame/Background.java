package ru.gb.stargame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.stargame.engine.Sprite;
import ru.gb.stargame.engine.math.Rect;

public class Background extends Sprite {

    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}
