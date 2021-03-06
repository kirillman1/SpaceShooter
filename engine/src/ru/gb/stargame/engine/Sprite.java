package ru.gb.stargame.engine;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.stargame.engine.math.Rect;
import ru.gb.stargame.engine.utils.Regions;

public class Sprite extends Rect{

    protected float angle; // угол
    protected float scale = 1f; // масштаб
    protected TextureRegion[] regions;
    protected int frame; // номер текущего региона из массива регионов, то есть кадр

    protected boolean isDestroyed;
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }


    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException("region is null");
        }
        regions = new TextureRegion[1]; // универсальный суперкласс
        regions[0] = region;
    }

    public Sprite (TextureRegion region, int rows, int cols, int frames){
        this.regions = Regions.split(region,rows,cols,frames);
    }

    public Sprite (){

    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame], // текущий регион
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scale, scale, // масштаб по x и y
                angle // угол вращения
        );
    }

    public void setWithProportion(float width) {
        setWidth(width);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setHeight(width / aspect);
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void resize(Rect worldBounds) {

    }

    protected void touchDown(Vector2 touch, int pointer) {

    }

    protected void touchUp(Vector2 touch, int pointer) {

    }

    protected void touchDragged(Vector2 touch, int pointer) {

    }

    public void update(float delta) {

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
