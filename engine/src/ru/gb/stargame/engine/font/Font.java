package ru.gb.stargame.engine.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * переделываем встроенный класс BitmapFont, чтобы расширить его функционал
 */
public class Font extends BitmapFont {

    public Font(String fontFile, String imageFile) {
        super(Gdx.files.internal(fontFile),  Gdx.files.internal(imageFile), false, false);
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setWordSize(float wordSize){
        getData().setScale(wordSize / getCapHeight());
    }

    public GlyphLayout draw (SpriteBatch batch, CharSequence str, float x, float y, int align){
        return super.draw(batch, str, x, y, 0f, align, false);
    }
}
