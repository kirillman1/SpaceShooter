package ru.gb.stargame.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.engine.math.MatrixUtils;
import ru.gb.stargame.engine.math.Rect;

/**
 * База для всех экранов, от него они будут наследоваться
 */

public class Base2DScreen implements Screen, InputProcessor{
    protected Game game;
    private Rect screenBounds; // границы области рисования в пикселях
    protected Rect worldBounds; // границы проекции мировых координат
    private Rect glBounds; // дефолтные границы проекции мир - gl
    protected Matrix4 worldToGL; // так как в libGDX по умолчанию 3D, даже когда 2D
    protected Matrix3 screenToWorld;
    protected SpriteBatch batch; // переносим из MenuScreen

    private final Vector2 touch = new Vector2(); //вектор который хранит координаты нажатия кнопки мыши

    public Base2DScreen(Game game) {

        this.game = game;

    }

    @Override
    public void show() {  // отображается само окно
        System.out.println("show");
        Gdx.input.setInputProcessor(this);
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0,0,1f,1f); // точка (0, 0) это середина в мировой системе координат
        this.worldToGL = new Matrix4();
        this.screenToWorld = new Matrix3();
        if (batch != null){
            throw new RuntimeException("batch != null, повторная установка screen без dispose");
        }
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width = " + width + " height = " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0); // установили квадрат screen в начальную точку (0, 0) слева внизу
        screenBounds.setBottom(0);

        float aspect = width / (float) height; // соотношение сторон, будем использовать при ресайзе

        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);

        MatrixUtils.calcTransitionMatrix(worldToGL,worldBounds,glBounds);
        batch.setProjectionMatrix(worldToGL);

        MatrixUtils.calcTransitionMatrix(screenToWorld,screenBounds,worldBounds);
        resize(worldBounds);
    }

    protected void resize(Rect worldBounds) {  // в прошлом методе resize данные приходят в пикселях и их нужно преобразовывать, а здесь сразу в границах мира

    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        batch.dispose();
        batch = null;
    }


    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped character = " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX,screenBounds.getHeight() - screenY).mul(screenToWorld); // координаты переходят из пикселей в мировую систему координат
        System.out.println("touchDown X = " + touch.x + " Y = " + touch.y);
        touchDown(touch, pointer);
        return false;
    }

    protected void touchDown(Vector2 touch, int pointer) { // pointer это номер пальца

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX,screenBounds.getHeight() - screenY).mul(screenToWorld); // координаты переходят из пикселей в мировую систему координат
        System.out.println("touchUp X = " + touch.x + " Y = " + touch.y);
        touchUp(touch, pointer);
        return false;
    }

    protected void touchUp(Vector2 touch, int pointer) { // pointer это номер пальца

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX,screenBounds.getHeight() - screenY).mul(screenToWorld); // координаты переходят из пикселей в мировую систему координат
        System.out.println("touchDragged X = " + touch.x + " Y = " + touch.y);
        touchDragged(touch, pointer);
        return false;
    }

    protected void touchDragged(Vector2 touch, int pointer) { // pointer это номер пальца

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
