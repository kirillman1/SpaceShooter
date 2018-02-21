package ru.gb.stargame.ship;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.math.Rnd;
import ru.gb.stargame.engine.utils.Regions;

public class EnemyEmitter {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MIDDLE_HEIGHT = 0.1f;
    private static final float ENEMY_MIDDLE_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_MIDDLE_BULLET_VY = -0.25f;
    private static final int ENEMY_MIDDLE_BULLET_DAMAGE = 5;
    private static final float ENEMY_MIDDLE_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_MIDDLE_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_BIG_BULLET_VY = -0.2f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 4.5f;
    private static final int ENEMY_BIG_HP = 15;

    private static final float ENEMY_BOSS_HEIGHT = 0.3f;
    private static final float ENEMY_BOSS_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_BOSS_BULLET_VY = -0.15f;
    private static final int ENEMY_BOSS_BULLET_DAMAGE = 20;
    private static final float ENEMY_BOSS_RELOAD_INTERVAL = 5f;
    private static final int ENEMY_BOSS_HP = 30;

    private final Vector2 enemySmallV = new Vector2(0,-0.2f); // скорость корабля
    private final Vector2 enemyMiddleV = new Vector2(0,-0.03f);
    private final Vector2 enemyBigV = new Vector2(0,-0.007f);
    private final Vector2 enemyBossV = new Vector2(0,-0.003f);

    private float generateTimer; // таймер, например мы хотим чтобы корабли создавались раз в 4 секунды
    private float generateInterval = 4f; // 4 секунды

    private final EnemyPool enemyPool;
    private Rect worldBounds;

    private final TextureRegion [] enemySmallRegion;
    private final TextureRegion [] enemyMiddleRegion;
    private final TextureRegion [] enemyBigRegion;
    //private final TextureRegion [] enemyBossRegion;
    private TextureRegion bulletRegion;

    private int stage;


    public EnemyEmitter(EnemyPool enemyPool, Rect worldBounds, TextureAtlas atlas) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        enemySmallRegion = Regions.split(atlas.findRegion("small"), 1, 2, 2);
        enemyMiddleRegion = Regions.split(atlas.findRegion("middle"), 1, 2, 2);
        enemyBigRegion = Regions.split(atlas.findRegion("big"), 1, 2, 2);
        //enemyBossRegion = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
        bulletRegion = atlas.findRegion("blaster");
    }

    public void setToNewGame(){
        stage = 1;
    }

    public void generateEnemy(float delta, int frags){
        stage = frags / 20 + 1; // сколько врагов надо убить чтобы новый уровень
        generateTimer += delta;
        if(generateInterval <= generateTimer){
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();

            float type = (float) Math.random();
            if (type < 0.65f){
                enemyShip.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_BULLET_DAMAGE * stage,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP * stage
                );
            } else if (type < 0.85f) {
                enemyShip.set(
                        enemyMiddleRegion,
                        enemyMiddleV,
                        bulletRegion,
                        ENEMY_MIDDLE_BULLET_HEIGHT,
                        ENEMY_MIDDLE_BULLET_VY,
                        ENEMY_MIDDLE_BULLET_DAMAGE * stage,
                        ENEMY_MIDDLE_RELOAD_INTERVAL,
                        ENEMY_MIDDLE_HEIGHT,
                        ENEMY_MIDDLE_HP * stage
                );
            } else if (type < 0.95f) {
                enemyShip.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_BULLET_DAMAGE * stage,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP * stage
                );
            } else {
                /*enemyShip.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_BULLET_DAMAGE * stage,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP * stage*/
            }


            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
            // чтобы корабль не вылезал обрезанным наполовину
            enemyShip.setBottom(worldBounds.getTop()); // чтбы выезжал сверху экрана
        }
    }

    public int getStage() {
        return stage;
    }
}
