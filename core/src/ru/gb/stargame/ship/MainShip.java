package ru.gb.stargame.ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.Star2DGame;
import ru.gb.stargame.bullet.BulletPool;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.explosion.ExplosionPool;

public class MainShip extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;

    private Vector2 v0 = new Vector2(0.5f, 0);
    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean pressedUp;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound soundLaser) {
        super(atlas.findRegion("mainship"), 1, 2, 2, bulletPool, explosionPool, worldBounds, soundLaser);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletRegion = atlas.findRegion("laser");
        this.bulletHeight = 0.01f;
        this.bulletV.set(0,0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
    }

    public Vector2 getV() {
        return v;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public void setToNewGame(){
        pos.x = worldBounds.pos.x;
        this.bulletHeight = 0.01f;
        this.bulletV.set(0,0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;

        hp = 100;
        setDestroyed(false);
    }

    @Override
    public void update(float delta) { // движение происходит  методе update
        super.update(delta);
        pos.mulAdd(v,delta);
        if(Star2DGame.getAutoShoot() || pressedUp) {
            reloadTimer += delta;
            if (reloadTimer >= reloadInterval){
                reloadTimer = 0.0f;
                shoot();
            }
        }

        if(getRight() > worldBounds.getRight()){
            setRight(worldBounds.getRight());
            stop();
        }
        if(getLeft() < worldBounds.getLeft()){
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    public void keyDown(int keycode) {
        if(!Star2DGame.getAutoShoot()){
            switch (keycode){
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    pressedLeft = true;
                    moveLeft();
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    pressedRight = true;
                    moveRight();
                    break;
                case Input.Keys.UP:
                    pressedUp = true;
                    shoot();
                    break;
            }
        } else {
            switch (keycode) {
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    pressedLeft = true;
                    moveLeft();
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    pressedRight = true;
                    moveRight();
                    break;
            }
        }
    }


    public void keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if(pressedRight){
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if(pressedLeft){
                    moveLeft();

                } else {
                    stop();
                }
                break;
            case Input.Keys.UP:
                pressedUp = false;
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (worldBounds.pos.x > touch.x) {
            if (leftPointer != INVALID_POINTER) return;
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) return;
            rightPointer = pointer;
            moveRight();
        }
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) moveRight(); else stop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) moveLeft(); else stop();
        }
    }

    private void moveRight(){
        v.set(v0);
    }

    private void moveLeft(){
        v.set(v0).rotate(180);
    }

    private void stop(){
        v.setZero();
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getTop() < getBottom()
                || bullet.getBottom() > pos.y
        );
    }
}
