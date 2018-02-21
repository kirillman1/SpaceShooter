package ru.gb.stargame.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.bullet.Bullet;
import ru.gb.stargame.bullet.BulletPool;
import ru.gb.stargame.engine.Sprite;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.explosion.Explosion;
import ru.gb.stargame.explosion.ExplosionPool;

/**
 * Базовый клас для кораблей
 */

public abstract class Ship extends Sprite {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    protected Vector2 v = new Vector2(); // скорость корабля
    protected Rect worldBounds; // границы мира

    protected int hp; // жизни корабля

    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected ExplosionPool explosionPool;

    protected Sound soundShoot;

    protected final Vector2 bulletV = new Vector2(); //скорость пули
    protected float bulletHeight; // высота пули
    protected int bulletDamage; // урон пули

    protected float reloadInterval; // время перезарядки
    protected float reloadTimer; // таймер для стрельбы

    public Ship(TextureRegion region, int rows, int cols, int frames, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) {
        super(region, rows, cols, frames);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.soundShoot = shootSound;
    }

    public Ship(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) { // конструктор для EnemyShip тк он будет брать параметры из пула
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.soundShoot = shootSound;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateTimer +=delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL){
            frame = 0;
        }
    }

    public int getHp() {
        return hp;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void damage(int damage){
        frame = 1; // смена анимации
        damageAnimateTimer = 0; // сброс таймера
        hp -= damage;
        if(hp < 0){
            hp = 0;
        }
        if (hp == 0){
            boom();
            setDestroyed(true);
        }
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, bulletDamage);
        soundShoot.play();
    }

    public void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(),pos);
    }
}
