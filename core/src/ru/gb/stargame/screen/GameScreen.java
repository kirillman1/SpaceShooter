package ru.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;
import ru.gb.stargame.Background;
import ru.gb.stargame.bullet.Bullet;
import ru.gb.stargame.bullet.BulletPool;
import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.Base2DScreen;
import ru.gb.stargame.engine.font.Font;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.math.Rnd;
import ru.gb.stargame.explosion.ExplosionPool;
import ru.gb.stargame.ship.EnemyEmitter;
import ru.gb.stargame.ship.EnemyPool;
import ru.gb.stargame.ship.EnemyShip;
import ru.gb.stargame.ship.MainShip;
import ru.gb.stargame.star.TrackingStar;
import ru.gb.stargame.ui.ButtonNewGame;
import ru.gb.stargame.ui.MessageGameOver;

public class GameScreen extends Base2DScreen implements ActionListener{

    private enum State {PLAYING, GAME_OVER}
    private State state;

    private static final int STAR_COUNT = 5;
    private static final float STAR_HEIGHT = 0.005f;
    private static final float FONT_SIZE = 0.02f;

    private Texture backgroundTexture;
    private Background background;
    private TrackingStar[] star;
    private TextureAtlas atlas;
    private MainShip mainShip;

    private final BulletPool bulletPool = new BulletPool();
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;

    private Sound soundExplosion;
    private Music music;
    private Sound soundLaser;
    private Sound soundBullet;

    private int frags;
    private Font font;
    private StringBuilder sbFrags = new StringBuilder(); // при стринге была  бы явная утечка памяти, тк надо изменять 60 раз в секунду
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbStage = new StringBuilder();
    private StringBuilder sbGameOver = new StringBuilder();

    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;


    public GameScreen(Game game) { super(game); }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        soundLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        soundBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/blaster.wav"));
        backgroundTexture = new Texture("textures/background.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        this.explosionPool = new ExplosionPool(atlas, soundExplosion);

        mainShip = new MainShip(atlas, bulletPool, explosionPool, worldBounds, soundLaser);

        this.enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, mainShip, soundBullet);
        this.enemyEmitter = new EnemyEmitter(enemyPool, worldBounds,atlas);

        star = new  TrackingStar[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new TrackingStar (atlas, Rnd.nextFloat(-0.05f, 0.05f), Rnd.nextFloat(-0.5f, -0.2f), STAR_HEIGHT, mainShip.getV());
        }
        this.messageGameOver = new MessageGameOver(atlas);
        this.buttonNewGame = new ButtonNewGame(atlas,this);
        this.font = new Font("font/font.fnt","font/font.png");
        this.font.setWordSize(FONT_SIZE);
        startNewGame();
    }

    public void printInfo(){
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbStage.setLength(0);
        font.draw(batch, sbFrags.append("Frags: ").append(frags), worldBounds.getLeft(), worldBounds.getTop() - 0.005f); // обычный стринг всё засрет
        font.draw(batch, sbHP.append("HP: ").append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - 0.005f, Align.center);
        font.draw(batch, sbStage.append("Stage: ").append(enemyEmitter.getStage()), worldBounds.getRight(), worldBounds.getTop() - 0.005f, Align.right);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(state == State.PLAYING) {
            checkCollisions();
        }
        deleteAllDestroyed();
        update(delta);
        draw();
    }

    public void checkCollisions(){
        // столкновение кораблей
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemy : enemyShipList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.setDestroyed(true);
                enemy.boom();
                mainShip.damage(mainShip.getHp());
                // mainShip.boom();
                // mainShip.setDestroyed(true);
                return;
            }
        }
        // нанесение урона вражескому кораблю
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemy : enemyShipList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.setDestroyed(true);
                    if (enemy.isDestroyed()){
                        frags++;
                        break;
                    }
                }

            }
        }

        // нанесение урона нашему кораблю
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.setDestroyed(true);
            }
        }
    }

    public void deleteAllDestroyed (){
        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    public void update(float delta){
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        explosionPool.updateActiveObjects(delta);

        switch (state){
            case PLAYING:
                bulletPool.updateActiveObjects(delta);
                enemyPool.updateActiveObjects(delta);
                mainShip.update(delta);
                enemyEmitter.generateEnemy(delta, frags);
                if (mainShip.isDestroyed()){
                    state = State.GAME_OVER;
                }
                break;

            case GAME_OVER:
                break;
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        printInfo();

        if (state == State.GAME_OVER){
            messageGameOver.draw(batch);
            //font.draw(batch, sbGameOver.append("Game Over"), worldBounds.pos.x, worldBounds.pos.y + 0.1f);
            buttonNewGame.draw(batch);
        } else {
            mainShip.draw(batch);
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
        }
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);

    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        soundExplosion.dispose();
        soundLaser.dispose();
        soundBullet.dispose();
        font.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        if(state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGame.touchDown(touch, pointer);
        }
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        if(state == State.PLAYING){
            mainShip.touchUp(touch,pointer);
        } else {
            buttonNewGame.touchUp(touch,pointer);
        }
    }

    private void startNewGame(){
        enemyEmitter.setToNewGame();
        state = State.PLAYING;
        frags = 0;
        mainShip.setToNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        //explosionPool.freeAllActiveObjects();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonNewGame){
            startNewGame();
        } else {
            throw new RuntimeException("Unknown src");
        }
    }
}
