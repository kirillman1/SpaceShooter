package ru.gb.stargame.engine.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ru.gb.stargame.engine.Sprite;

/**
 * Можем передать любой объект наследующийся от Sprite
 * @param <T>
 */

public abstract class SpritesPool <T extends Sprite> {

    protected List<T> activeObjects = new LinkedList<T>(); // список активных объектов. LinkedList так как может освободиться объект из середины и будет лютое смещение если бы был ArrayList

    protected List<T> freeObjects = new ArrayList<T>(); // список свободных объектов

    protected abstract T newObject(); // T это экземпляр класса наследующегося от Sprite

    public T obtain(){ // берет объекты из пула обектов
        T object;
        if(freeObjects.isEmpty()){
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        debugLog();
        return object;
    }

    public void updateActiveObjects(float delta) {
        for (int i = 0; i < activeObjects.size(); i++) {
            activeObjects.get(i).update(delta);
        }
    }

    public void drawActiveObjects(SpriteBatch batch){
        for (int i = 0; i < activeObjects.size(); i++) {
            activeObjects.get(i).draw(batch);
        }
    }

    public void freeAllDestroyedActiveObjects(){
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.setDestroyed(false);
            }
        }
    }

    public void free (T object){
        if (!activeObjects.remove(object)){
            throw new RuntimeException("Попытка удаления несуществующего объекта");
        }
        freeObjects.add(object);
    }

    public void freeAllActiveObjects(){
        freeObjects.addAll(activeObjects);
        activeObjects.clear();
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    protected void debugLog(){

    }

}
