package ru.gb.stargame.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.ui.ScaledTouchUpButton;

public class ButtonExit extends ScaledTouchUpButton {
    public ButtonExit(TextureAtlas atlas, float pressScale, ActionListener actionListener) {
        super(atlas.findRegion("exit-button"), pressScale, actionListener);
    }

    @Override
    public void resize(Rect worldBounds) {
       // setBottom(worldBounds.getBottom()); // задаем нижнюю часть границы мира, метод наследуется по цепочке Rect - Sprite - ScaledTouchUpButton - ButtonExit
       // setRight(worldBounds.getRight());
        pos.set(worldBounds.pos.x, worldBounds.pos.y - 0.2f);
    }
}
