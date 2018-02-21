package ru.gb.stargame.engine.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.Sprite;

public class ScaledTouchUpButton extends Sprite {

    private float pressScale; // определяет насколько уменьшился или увеличился
    private int pointer; //номер пальца который нажал на кнопку на тачскрине
    private boolean pressed; //нажата ли кнопка в данный момент
    private final ActionListener actionListener;

    public ScaledTouchUpButton(TextureRegion region, float pressScale, ActionListener actionListener) {
        super(region);
        this.pressScale = pressScale;
        this.actionListener = actionListener;
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if(pressed || !isMe(touch)){
            return;
        }
        this.pointer = pointer;
        scale = pressScale;
        pressed = true;
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if(this.pointer != pointer || !pressed){
            return;
        }
        if(isMe(touch)){
            actionListener.actionPerformed(this);
        }
        pressed = false;
        scale = 1f;
    }
}
