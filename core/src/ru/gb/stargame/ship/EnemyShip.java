package ru.gb.stargame.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.gb.stargame.bullet.BulletPool;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.explosion.ExplosionPool;

public class EnemyShip extends Ship{

    private enum State {
        DESCENT, FIGHT
    }

    private MainShip mainShip; // ссылка на игровой корабль чтобы с ним взаимодействовать.
    private State state;
    private Vector2 descentV = new Vector2(0, -0.15f); // скорость выезда из за экрана
    private Vector2 v0 = new Vector2();

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, MainShip mainShip, Sound shootSound) {
        super(bulletPool, explosionPool, worldBounds, shootSound);
        this.mainShip = mainShip;
        this.v.set(v0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta); // заставляем двигаться



        switch (state){
            case FIGHT:
                reloadTimer += delta;
                if(reloadTimer >= reloadInterval){
                    reloadTimer = 0;
                    shoot();
                }
                if(getBottom() < worldBounds.getBottom()){ // вылетает за нижнюю часть экрана
                    boom();
                    mainShip.damage(bulletDamage); // наносится уроен как если бы попала пуля от этого корабля
                    setDestroyed(true);
                }
                break;

            case DESCENT:
                if (getTop() <= worldBounds.getTop()){
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        v.set(descentV);
        state = State.DESCENT;
        reloadTimer = reloadInterval;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }
}
