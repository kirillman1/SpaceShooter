package ru.gb.stargame.ship;

import com.badlogic.gdx.audio.Sound;

import ru.gb.stargame.bullet.BulletPool;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.pool.SpritesPool;
import ru.gb.stargame.explosion.ExplosionPool;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private final BulletPool bulletPool;
    private final ExplosionPool explosionPool;
    private final Rect worldBounds;
    private final MainShip mainShip;
    private final Sound soundBullet;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, MainShip mainShip, Sound soundBullet) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.mainShip = mainShip;
        this.soundBullet = soundBullet;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, explosionPool, worldBounds, mainShip, soundBullet);
    }


}
