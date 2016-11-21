package com.klemstinegroup.tyrantandroid;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import static com.klemstinegroup.tyrantandroid.Renderer.*;


/**
 * Created by Paul on 7/3/2016.
 */
public class ButtonActor extends Actor {

    int x;
    int y;
    float ddx;
    float ddy;
    TextureRegion tr;

    public ButtonActor(int x, int y, int i,int map){
        this(x,y,ButtonActor.getRegion(i,map));
    }

    public ButtonActor(int x, int y, TextureRegion tr){
        this.x=x;
        this.y=y;
        this.tr=tr;

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdown");
                ButtonActor.this.ddy=x;
                ButtonActor.this.ddy=y;
                return true;
            }
            public boolean touchDragged(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdrag");
                setX(getX()+ddx-x);
                setY(getY()+ddy-x);
                return true;
            }

        });

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tr, x, y);
    }
    public static TextureRegion getRegion(int i, int src){
        int sx = (i % 20) * TILEWIDTH;
        int sy = (i / 20) * TILEHEIGHT;
        TextureRegion tetr = new TextureRegion(texture[src], sx, sy, TILEWIDTH, TILEHEIGHT);
        return tetr;
    }


    float dx,dy;
    public boolean touchDown (float x, float y, int pointer) {
        dx=x;
        dy=y;
        return true;
    }


}
