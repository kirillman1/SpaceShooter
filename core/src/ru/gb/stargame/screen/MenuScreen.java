package ru.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.gb.stargame.Background;
import ru.gb.stargame.engine.ActionListener;
import ru.gb.stargame.engine.Base2DScreen;
import ru.gb.stargame.engine.font.Font;
import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.math.Rnd;
import ru.gb.stargame.star.Star;
import ru.gb.stargame.ui.ButtonExit;
import ru.gb.stargame.ui.ButtonPlay;
import ru.gb.stargame.ui.Logo;

/**
 * Экран который отображает меню
 */

public class MenuScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 15;
    private static final float STAR_HEIGHT = 0.005f;
    private static final float BUTTON_HEIGHT = 0.07f;
    private static final float BUTTON_PRESS_SCALE = 0.90f;

    private static final float FONT_SIZE = 0.065f;

    private Logo logo;

    private Texture backgroundTexture;
    private Background background;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private Star[] star;
    private TextureAtlas atlas;
    //private Font font;
    //private StringBuilder sbLogo = new StringBuilder();

    public MenuScreen(Game game) {
        super(game);
    }

    /**
     * Здесь всё инициализируем
     */
    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("textures/background.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        buttonExit = new ButtonExit(atlas, BUTTON_PRESS_SCALE, this);
        buttonExit.setHeightProportion(BUTTON_HEIGHT);
        buttonPlay = new ButtonPlay(atlas, BUTTON_PRESS_SCALE,this);
        buttonPlay.setHeightProportion(BUTTON_HEIGHT);
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(atlas, Rnd.nextFloat(-0.05f, 0.05f), Rnd.nextFloat(-0.5f, -0.2f), STAR_HEIGHT);
        }
        //this.font = new Font("font/font.fnt","font/font.png");
        //this.font.setWordSize(FONT_SIZE);
        logo = new Logo(atlas);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta){
        //star.update(delta);
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        //sbLogo.setLength(0);
        //font.draw(batch, sbLogo.append("Star").append(System.getProperty("line.separator")).append("Shooter"), worldBounds.pos.x, worldBounds.getTop() - 0.2f, Align.center);

        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
        //font.dispose();
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch, pointer);
        buttonExit.touchDown(touch, pointer);
        buttonPlay.touchDown(touch,pointer);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        buttonExit.touchUp(touch, pointer);
        buttonPlay.touchUp(touch,pointer);
    }

    @Override
    public void actionPerformed(Object src) {
        if(src == buttonExit){
            Gdx.app.exit();
        } else if(src == buttonPlay){
            game.setScreen(new GameScreen(game));
        } else {
            throw new RuntimeException("Unknown src" + src);
        }
    }
}
