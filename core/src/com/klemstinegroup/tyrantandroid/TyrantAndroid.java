package com.klemstinegroup.tyrantandroid;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

public class TyrantAndroid implements ApplicationListener {

    Renderer renderer;

    @Override
    public void create() {
        //InputMultiplexer imp = new InputMultiplexer();
        renderer = new Renderer();
        //imp.addProcessor(renderer);
        //Gdx.input.setInputProcessor(imp);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
        new Thread(new ThreadGroup("save"), new Runnable() {
            public void run() {
                Game.save(true);
                System.out.println("saved");
            }
        }, "save", 255000).start();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        renderer.cam.projection.setToOrtho2D(0, 0, width, height);
        renderer.cam.zoom = renderer.zoom;

        renderer.cam.update();
    }

    @Override
    public void render() {
        renderer.render();
    }


}
