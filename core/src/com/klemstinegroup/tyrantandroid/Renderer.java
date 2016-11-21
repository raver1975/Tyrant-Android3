package com.klemstinegroup.tyrantandroid;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.klemstinegroup.tyrant.*;

public class Renderer implements InputProcessor {

    // Game rendering
    SpriteBatch batch;
    public OrthographicCamera cam;
    MenuRenderer inv = new MenuRenderer();
    BitmapFont font;
    BitmapFont font1;

    static int scrollx;
    static int scrolly;
    private ArrayList animationElements = new ArrayList();
    private boolean animating = false;
    boolean invon = false;
    boolean menuon = true;
    public static Renderer instance;

    Texture textured = new Texture(512, 512, Pixmap.Format.RGBA8888);

    // constants
    public static int MAP_DISTANCE = 6;
    public static float percent = 0.001f;
    public static final int TILEWIDTH = 32;
    public static final int TILEHEIGHT = 32;
    static int WIDTH = 416 + 0;
    static int HEIGHT = 416 + 0;

    static Texture[] texture = new Texture[5];
    private int messageHeight;
    public float zoom = 1f;
    public final static int TEXTURE_CREATURES = 1;
    public final static int TEXTURE_EFFECTS = 2;
    public final static int TEXTURE_ITEMS = 3;
    public final static int TEXTURE_SCENERY = 4;//test
    public final static int TEXTURE_TILES = 0;

    //    private ButtonActor[] buttonList;
    private Stage stage;
    private int menuSelected = -1;


    // public final static int TEXTURE_GUI= 5;

    public Renderer() {

        instance = this;
        // cam
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.position.set(WIDTH / 2, HEIGHT / 2, 0);

        cam.update();

        // GL init
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // textures
        texture[TEXTURE_CREATURES] = new Texture(
                Gdx.files.internal("data/creature32.png"), false);
//        texture[TEXTURE_CREATURES].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        texture[TEXTURE_EFFECTS] = new Texture(
                Gdx.files.internal("data/effects32.png"), false);
//        texture[TEXTURE_EFFECTS].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        texture[TEXTURE_ITEMS] = new Texture(
                Gdx.files.internal("data/items32.png"), false);
//        texture[TEXTURE_ITEMS].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        texture[TEXTURE_SCENERY] = new Texture(
                Gdx.files.internal("data/scenery32.png"), false);
//        texture[TEXTURE_SCENERY].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        texture[TEXTURE_TILES] = new Texture(
                Gdx.files.internal("data/tiles32.png"), false);
//        texture[TEXTURE_TILES].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        // font
        font = new BitmapFont(Gdx.app.getFiles().getFileHandle("data/font.fnt",
                FileType.Internal), false);
        font1 = new BitmapFont(Gdx.app.getFiles().getFileHandle(
                "data/font1.fnt", FileType.Internal), false);

        // batch
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        InputMultiplexer imp=new InputMultiplexer();
//        imp.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
//            @Override
//            public boolean touchDown(float x, float y, int pointer, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean tap(float x, float y, int count, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean longPress(float x, float y) {
//                return false;
//            }
//
//            @Override
//            public boolean fling(float velocityX, float velocityY, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean pan(float x, float y, float deltaX, float deltaY) {
//                return false;
//            }
//
//            @Override
//            public boolean panStop(float x, float y, int pointer, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean zoom(float initialDistance, float distance) {
//                float zo =  distance/initialDistance;
//                System.out.println("zoom="+zo);
//                Renderer.instance.zoom = zo;
//                if (Renderer.instance.zoom < 1) Renderer.instance.zoom = 1;
//                Renderer.instance.cam.zoom = Renderer.instance.zoom;
//                Renderer.instance.cam.update();
//                Renderer.instance.MAP_DISTANCE = (int) (Renderer.instance.zoom * 6);
//                return true;
//            }
//
//            @Override
//            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
//                System.out.println("pinch");
//                return true;
//            }
//
//            @Override
//            public void pinchStop() {
//
//            }
//        }));
//        imp.addProcessor(this);
        Gdx.input.setInputProcessor(this);

        // engine
        Engine.instance = new Engine();


//Game.instance().initialize();
//        //create buttons
//        buttonList = new ButtonActor[1];
//        buttonList[0] = new ButtonActor(100, 100, 2, TEXTURE_TILES);
        stage = new Stage(new StretchViewport(cam.viewportWidth, cam.viewportHeight));
//        imp.addProcessor(stage);
//        stage.addActor(buttonList[0]);


    }

    public void inv(boolean b) {
        if (b) {
            Gdx.input.setCatchBackKey(true);
            inv.reset();
            invon = true;
            Gdx.input.setInputProcessor(inv);

        } else {
            Gdx.input.setCatchBackKey(false);
            invon = false;
            Gdx.input.setInputProcessor(Renderer.this);
        }
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);

        if (Engine.instance.map != null && Engine.running && !Game.over) {
            batch.begin();
            renderMap();
//            stage.act(Gdx.graphics.getDeltaTime());
//            stage.draw();
            batch.setColor(1f, 1f, 1f, 1f);
            if (Engine.cursor)
                drawCursor(Engine.curx, Engine.cury);
            if (animating)
                renderAnimation();

            batch.end();
        }

        cam.zoom = 1;
        cam.update();

        batch.begin();
        batch.setProjectionMatrix(cam.combined);
        font.getData().setScale(1);
        if (Engine.running && !invon && !Engine.cursor) {

            renderLevelMap();

            renderStats();
            if (menuon && !Engine.cursor)
                renderActionButtons();

        }

        if (percent > 0)
            renderPercent();


        else if (Game.over)
            renderDead();
        else if (Engine.messages != null && !invon) {
            renderMessages();
        }

        if (invon)
            inv.render();


        // renderButtons(Gdx.graphics.getDeltaTime());

        batch.end();
        cam.zoom = zoom;
        cam.update();
        batch.setProjectionMatrix(cam.combined);

    }

    public boolean selectMenu(int x, int y, boolean up) {
        cam.zoom = 1;
        cam.update();
        Vector3 vec = new Vector3(x, y, 0);
        cam.unproject(vec);
        cam.zoom = zoom;
        cam.update();
        int worldx = (int) (vec.x) - WIDTH / 2;
        int worldy = (int) (vec.y) - HEIGHT / 2;
        if (vec.y < messageHeight && up && !Engine.cursor) {
            messageHeight = -1;
            Engine.messages = "";
            return true;
        }
        float dx = ((float) worldx / (float) TILEWIDTH);
        float dy = -((float) worldy / (float) TILEHEIGHT);
        System.out.println(dx + "," + dy + " : world=" + worldx + "," + worldy);
        int bb = (int) ((dy + 3.5f) / 9.5f * 13f);

        menuSelected = -1;
        if (dx < -5.5f) {
            if (dy > -3.5f && dy < 6f) {
                System.out.println("touched=" + bb);
                menuSelected = bb;
                if (up) {
                    Engine.input = 'h';
                    Engine.inputSelected = bb;
                    Engine.update();
                    menuSelected = -1;
                }
                return true;
            }
        }
        if (dx > 5.5f) {
            if (dy > -3.5f && dy < 6f) {
                System.out.println("touched=" + bb);
                menuSelected = bb + 13;
                if (up) {
                    Engine.input = 'h';
                    Engine.inputSelected = menuSelected;
                    Engine.update();
                    menuSelected = -1;
                }
                return true;
            }
        }
        return false;
    }

    private void renderActionButtons() {
        int r1 = 0;
        int r2 = 0;
        for (int r = 0; r < 13; r++) {
            r1 = r + 13;
            r2 = r;


            //right menu
            font.setColor(1, 1, 1, 1);
            if (r1 == menuSelected) {
                batch.setColor(1f, 0f, 0f, 1f);

            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }
//            batch.setColor(font.getColor());
            font.getData().setScale(1f);
            glyphLayout.setText(font, Engine.menu[r1]);

            float x1 = WIDTH - TILEWIDTH;
            float x2 = WIDTH  - 5 -TILEWIDTH- glyphLayout.width;
            float y = HEIGHT / 2 - (r - 4) * TILEHEIGHT * 6 / 8;
            batch.draw(Engine.trs[r1], x1, y, TILEWIDTH * 5 / 8, TILEHEIGHT * 5 / 8);
            if (Engine.helpOverlay || r1 == menuSelected) {
                font.setColor(Color.RED);
                font.draw(batch, Engine.menu[r1], x2 - 1, y - 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r1], x2 - 1, y + 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r1], x2 + 1, y - 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r1], x2 + 1, y + 1 + TILEHEIGHT / 2 + 2);
                font.setColor(Color.YELLOW);
                font.draw(batch, Engine.menu[r1], x2, y + TILEHEIGHT / 2 + 2);

            }

            //left menu
            if (r2 == menuSelected)
                batch.setColor(1f, 0f, 0f, 1f);
            else
                batch.setColor(1f, 1f, 1f, 1f);
//            batch.setColor(font.getColor());
            font.getData().setScale(1f);
            float x = 5 + TILEWIDTH * 3 / 4;
            y = HEIGHT / 2 - (r - 4) * TILEHEIGHT * 6 / 8;
            batch.draw(Engine.trs[r2], 5, y, TILEWIDTH * 5 / 8, TILEHEIGHT * 5 / 8);
            if (Engine.helpOverlay || r2 == menuSelected) {
                font.setColor(Color.RED);
                font.draw(batch, Engine.menu[r2], x - 1, y - 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r2], x - 1, y + 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r2], x + 1, y - 1 + TILEHEIGHT / 2 + 2);
                font.draw(batch, Engine.menu[r2], x + 1, y + 1 + TILEHEIGHT / 2 + 2);

                font.setColor(Color.YELLOW);
                font.draw(batch, Engine.menu[r2], x, y + TILEHEIGHT / 2 + 2);
            }


            font.getData().setScale(1f);
            font.setColor(1f, 1f, 1f, 1f);
            batch.setColor(font.getColor());
        }

    }

    private void renderLevelMap() {
        int sc = 2;
        Pixmap pm = LevelMap.instance().getMapView(Engine.instance.map);
        if (pm == null)
            return;
        textured.draw(pm, 0, 0);
        TextureRegion region = new TextureRegion(textured, 0, 0, pm.getWidth(),
                pm.getHeight());
        batch.setColor(1f, 1f, 1f, 0.8f);
        batch.draw(region, (WIDTH - 5) - pm.getWidth() * sc,
                (HEIGHT - 25) - pm.getHeight() * sc, pm.getWidth() * sc,
                pm.getHeight() * sc);
        cam.update();

    }

    public void renderMessages() {
        if (Engine.messages.length() < 4)
            return;
        String[] temp = Engine.messages.split("\n");
        String temp1 = "";
        font.getData().setScale(1f);
        for (int r = 1; r < temp.length; r++) {
            if (temp[r].equals("")) {
                temp1 += "\n";
                continue;
            }
            int wi = temp[r].length();
            glyphLayout.setText(font, temp[r].substring(0, wi));

            while (glyphLayout.width > WIDTH - 30) {
                wi = wi - 1;
                glyphLayout.setText(font, temp[r].substring(0, wi));
            }
            boolean flag = false;
            while (temp[r].length() > wi) {

                int cut = temp[r].substring(0, wi).lastIndexOf(" ") + 1;
                temp1 = temp1 + "\n" + (flag ? " " : "-")
                        + temp[r].substring(0, cut);
                temp[r] = temp[r].substring(cut);
                flag = true;
            }
            temp1 += "\n" + (flag ? " " : "-") + temp[r];
        }
        String[] temp3 = temp1.split("\n");
        // font.setScale(1.25f);
        batch.setColor(0f, 0f, 0.3f, 1f);
        int h = 0;
        messageHeight = (int) (((temp3.length - 1) * (font.getLineHeight()
                * font.getScaleX() + 2)) + 13);
        batch.draw(texture[TEXTURE_TILES], 0, h, 0, h, WIDTH, messageHeight, 1,
                1, 0, 0, 288, 1, 1, false, false);
        // batch.draw(texture[TEXTURE_TILES], 0, h, WIDTH, messageHeight, 0,
        // 288,
        // 1, 1);
        batch.setColor(1f, 1f, 1f, 1f);
        font.setColor(1f, 1f, 1f, 1f);
        for (int r = 1; r < temp3.length; r++) {
            font.draw(batch, temp3[temp3.length - r], 8,
                    h + 7 + (r * (font.getLineHeight() * font.getScaleX() + 2)));
        }
    }

    public void renderDead() {
        font.getData().setScale(7);
        font.draw(batch, "YOU", 80, 380);
        font.draw(batch, "ARE", 80, 260);
        font.draw(batch, "DEAD!", 30, 140);
    }

    private static GlyphLayout glyphLayout = new GlyphLayout();

    public void renderPercent() {
        //percent += 0.00001f;
        //String percentt = Float.toString(percent * 100);
        //if (percentt.length() > 5)
        //	percentt = percentt.substring(0, 5);
        font.getData().setScale(5);
        glyphLayout.setText(font, "Rogue");

        font.draw(batch, "Rogue",
                WIDTH / 2 - glyphLayout.width / 2, HEIGHT + 5);
        font.getData().setScale(3f);
        //font.draw(batch, percentt + "%",
        //		WIDTH / 2 - font.getBounds(percentt + "%").width / 2, 60);
        batch.setColor(.8f, 0f, 1f, 1f);
        batch.draw(texture[TEXTURE_TILES], 5, 30, 5, 30, (WIDTH - 10) * percent, 30, 1,
                1, 0, 0, 288, 1, 1, false, false);
        batch.setColor(1f, 1f, 1f, 1f);
        glyphLayout.setText(font, "loading...");
        font.draw(batch, "loading...", WIDTH / 2
                - glyphLayout.width / 2, 120);
        batch.draw(texture[TEXTURE_CREATURES], WIDTH / 2 - 100,
                HEIGHT / 2 - 90, WIDTH / 2 - 100, HEIGHT / 2 - 90, 200, 200, 1,
                1, 0, 2 * TILEWIDTH, 6 * TILEHEIGHT, TILEWIDTH, TILEHEIGHT,
                false, false);
    }

    public void renderMap() {
        scrollx = Game.hero().x;
        scrolly = Game.hero().y;
        drawTiles(Game.hero().x - MAP_DISTANCE, Game.hero().y - MAP_DISTANCE,
                Game.hero().x + MAP_DISTANCE, Game.hero().y + MAP_DISTANCE);

    }

    public void dispose() {
        texture[0].dispose();
        texture[1].dispose();
        texture[2].dispose();
        texture[3].dispose();
        texture[4].dispose();
        batch.dispose();
        font.dispose();
        font1.dispose();
        textured.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SLASH:
                Engine.update(Action.AGAIN);
                break;
            case Input.Keys.DPAD_LEFT:
                Engine.update('4');
                break;
            case Input.Keys.DPAD_RIGHT:
                Engine.update('6');
                break;
            case Input.Keys.DPAD_UP:
                Engine.update('8');
                break;
            case Input.Keys.DPAD_DOWN:
                Engine.update('2');
                break;
            case Input.Keys.DPAD_CENTER:
                Engine.update(Action.EXIT);
                break;
            case Input.Keys.MENU:
                Engine.update(Action.HELP);
                break;
            case Input.Keys.A:
                Engine.update(Action.APPLY_SKILL);
                break;
            case Input.Keys.B:
                Engine.update(Action.WAND);
                break;
            case Input.Keys.C:
                Engine.update(Action.CHAT);
                break;
            case Input.Keys.D:
                Engine.update(Action.DROP);
                break;
            case Input.Keys.E:
                Engine.update(Action.EAT);
                break;
            case Input.Keys.F:
                Engine.update(Action.FIRE);
                break;
            case Input.Keys.G:
                Engine.update(Action.GIVE);
                break;
            case Input.Keys.H:
                Engine.update(Action.HELP);
                break;
            case Input.Keys.I:
                Engine.update(Action.INVENTORY);
                break;
            case Input.Keys.J:
                Engine.update(Action.JUMP);
                break;
            case Input.Keys.K:
                Engine.update(Action.KICK);
                break;
            case Input.Keys.L:
                Engine.update(Action.LOOK);
                break;
            case Input.Keys.M:
                Engine.update(Action.MESSAGES);
                break;
            case Input.Keys.N:
                Engine.shiftdown = !Engine.shiftdown;
                break;
            case Input.Keys.O:
                Engine.update(Action.OPEN);
                break;
            case Input.Keys.P:
                Engine.update(Action.PICKUP);
                break;
            case Input.Keys.Q:
                Engine.update(Action.QUAFF);
                break;
            case Input.Keys.R:
                Engine.update(Action.READ);
                break;
            case Input.Keys.S:
                Engine.update(Action.SEARCH);
                break;
            case Input.Keys.T:
                Engine.update(Action.THROW);
                break;
            case Input.Keys.U:
                Engine.update(Action.USE);
                break;
            case Input.Keys.V:
                Engine.update(Action.VIEW_STATS);
                break;
            case Input.Keys.W:
                Engine.update(Action.WIELD);
                break;
            case Input.Keys.X:
                Engine.update(Action.EXIT);
                break;
            case Input.Keys.Y:
                Engine.update(Action.PRAY);
                break;
            case Input.Keys.Z:
                Engine.update(Action.ZAP);
                break;
            case Input.Keys.NUM_0:
                break;
            case Input.Keys.NUM_1:
                Engine.update('1');
                break;
            case Input.Keys.NUM_2:
                Engine.update('2');
                break;
            case Input.Keys.NUM_3:
                Engine.update('3');
                break;
            case Input.Keys.NUM_4:
                Engine.update('4');
                break;
            case Input.Keys.NUM_5:
                Engine.update('5');
                break;
            case Input.Keys.NUM_6:
                Engine.update('6');
                break;
            case Input.Keys.NUM_7:
                Engine.update('7');
                break;
            case Input.Keys.NUM_8:
                Engine.update('8');
                break;
            case Input.Keys.NUM_9:
                Engine.update('9');
                break;
            case Input.Keys.POUND:
                break;
            case Input.Keys.COMMA:
                zoom -= .5f;
                if (zoom < 1)
                    zoom = 1;
                cam.zoom = zoom;
                cam.update();
                MAP_DISTANCE = (int) (zoom * 6);
                Engine.update();
                break;
            case Input.Keys.PERIOD:
                zoom += .5f;
                cam.zoom = zoom;
                MAP_DISTANCE = (int) (zoom * 6);
                cam.update();
                Engine.update();
                break;
            case Input.Keys.SEMICOLON:
                break;
            case Input.Keys.SPACE:
                Engine.update(Action.WAIT);
                break;
            case Input.Keys.BACK:
                break;
            case Input.Keys.ESCAPE:
                break;

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        select(x, y, false);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        select(x, y, true);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (stage.touchDragged(x, y, pointer)) return true;
        select(x, y, false);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

//	@Override
//	public boolean touchMoved(int x, int y) {
//		// TODO Auto-generated method stub
//		return false;
//	}

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    public void select(int x, int y, boolean up) {
        if (!Engine.running)
            return;
        Vector3 vec = new Vector3(x, y, 0);
        cam.unproject(vec);
        int worldx = (int) (vec.x) - WIDTH / 2;
        int worldy = (int) (vec.y) - HEIGHT / 2;
        float dx = ((float) worldx / (float) TILEWIDTH);
        float dy = -((float) worldy / (float) TILEHEIGHT);
        // Engine.cursor=true;
        if (x < 200 && y < 100) {
        Action a=Action.HELP;
            Engine.update(a);
        }
        if (selectMenu(x, y, up))
            return;
        Engine.curx = (int) (scrollx + dx + .5f);
        Engine.cury = (int) (scrolly + dy + .5f);
        if (!Engine.cursor && up) {
            if (worldx >= -16 && worldy >= -16 && worldx <= 16 && worldy <= 16)
                Engine.update(Action.WAIT);
            int rx = Engine.curx - scrollx;
            int ry = Engine.cury - scrolly;
            if (rx == 0 && ry == 0)
                return;
            dx = -1;
            if (ry < (2 * rx))
                dx++;
            if (ry > (-2 * rx))
                dx++;
            dy = -1;
            if (rx < (2 * ry))
                dy++;
            if (rx > (-2 * ry))
                dy++;
            Game.simulateDirection((int) dx, (int) (dy));
        }
        if (Engine.cursor && up) {
            Engine.inputSelected = -1;
            Engine.update();
        }

        // System.out.println(worldx + " " + worldy + " " + Engine.instance.curx
        // + " "
        // + Engine.instance.cury);
    }

    // draws tiles in box (x1,y1)-->(x2,y2) to back buffer
    public void drawTiles(int x1, int y1, int x2, int y2) {
        // Game.warn("(" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ")");

        // draw the specified area in buffer
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                drawTile(x, y);
            }
        }
    }

    // draws tile at (x,y)
    // includes logic for mapping tile number to image
    // draw blank if outside map area
    public void drawTile(int x, int y) {
        Thing h = Game.hero();

        int m = Engine.instance.map.getTile(x, y);
        if (!Engine.instance.map.isDiscovered(x, y))
            m = 0;
        // if (!Engine.instance.map.isDiscovered(x, y))
        // m = 0;
        if (Engine.instance.map.isVisible(x, y))
            batch.setColor(1f, 1f, 1f, 1f);
        else
            batch.setColor(.5f, .5f, .5f, 1f);

        int tile = m & 65535;

        int px = (x - scrollx + MAP_DISTANCE) * TILEWIDTH + WIDTH / 2
                - ((MAP_DISTANCE * 2 + 1) * TILEWIDTH / 2);
        int py = (MAP_DISTANCE * 2 - (y - scrolly) - MAP_DISTANCE) * TILEHEIGHT
                + HEIGHT / 2 - ((MAP_DISTANCE * 2 + 1) * TILEHEIGHT / 2);

        switch (tile) {
            case 0: // blank
                break;

            // default method
            default: {
                int image;
                if (Engine.instance.map.isDiscovered(x, y + 1)
                        && Tile.filling[Engine.instance.map.getTile(x, y + 1) & 65535]) {
                    image = Tile.imagefill[tile];
                } else {
                    image = Tile.images[tile];
                }
                int sx = (image % 20) * TILEWIDTH;
                int sy = (image / 20) * TILEHEIGHT;
                batch.draw(texture[TEXTURE_TILES], px, py, sx, sy, TILEWIDTH,
                        TILEHEIGHT);

                if ((Tile.borders[tile] > 0)) {
                    if ((x > 0) && (Engine.instance.map.getTile(x - 1, y) != m))
                        batch.draw(texture[TEXTURE_SCENERY], px, py, 0,
                                16 * TILEHEIGHT, TILEWIDTH, TILEHEIGHT);
                    if ((x < (Engine.instance.map.width - 1))
                            && (Engine.instance.map.getTile(x + 1, y) != m))
                        batch.draw(texture[TEXTURE_SCENERY], px, py, TILEWIDTH,
                                16 * TILEHEIGHT, TILEWIDTH, TILEHEIGHT);
                    if ((y > 0) && (Engine.instance.map.getTile(x, y - 1) != m))
                        batch.draw(texture[TEXTURE_SCENERY], px, py, 2 * TILEWIDTH,
                                16 * TILEHEIGHT, TILEWIDTH, TILEHEIGHT);
                    if ((y < (Engine.instance.map.height - 1))
                            && (Engine.instance.map.getTile(x, y + 1) != m))
                        batch.draw(texture[TEXTURE_SCENERY], px, py, 3 * TILEWIDTH,
                                16 * TILEHEIGHT, TILEWIDTH, TILEHEIGHT);
                }

                break;
            }
            // end of switch
        }

        if (Engine.instance.map.isVisible(x, y)) {
            drawThings(x, y);
        } else if ((x == h.x) && (y == h.y)) {
            drawThing(x, y, h, batch);
        }
    }

    public void drawThing(int x, int y, Thing t, SpriteBatch batch) {
        if (!t.isInvisible()) {
            int i = t.getImage();
            int sx = (i % 20) * TILEWIDTH;
            int sy = (i / 20) * TILEHEIGHT;

            int px = (x - scrollx + MAP_DISTANCE) * TILEWIDTH + WIDTH / 2
                    - ((MAP_DISTANCE * 2 + 1) * TILEWIDTH / 2);
            int py = (MAP_DISTANCE * 2 - (y - scrolly) - MAP_DISTANCE)
                    * TILEHEIGHT + HEIGHT / 2
                    - ((MAP_DISTANCE * 2 + 1) * TILEHEIGHT / 2);

            Object source = t.get("ImageSource");
            int im = TEXTURE_ITEMS;
            if (source.equals("Tiles"))
                im = TEXTURE_TILES;
            if (source.equals("Scenery"))
                im = TEXTURE_SCENERY;
            if (source.equals("Creatures"))
                im = TEXTURE_CREATURES;
            if (source.equals("Items"))
                im = TEXTURE_ITEMS;
            if (source.equals("Effects"))
                im = TEXTURE_EFFECTS;

            batch.draw(texture[im], px, py, sx, sy, TILEWIDTH, TILEHEIGHT);

            if (t.getFlag("IsBeing")) {
                double health = Being.getHealth(t);
                batch.setColor(0f, 0f, 0f, .9f);
                batch.draw(texture[TEXTURE_TILES], px + 20, py + 27, 12, 4, 0,
                        288, 1, 1);
                if (t.isHostile(Game.hero()))
                    batch.setColor(1f, 0f, 0f, .5f);
                else
                    batch.setColor(0f, 1f, 0f, .5f);
                batch.draw(texture[TEXTURE_TILES], px + 21, py + 28, px + 21,
                        py + 28, (int) (10 * health), 2, 1, 1, 0, 0, 288, 1, 1,
                        false, false);
                // batch.draw(texture[TEXTURE_TILES], px + 21, py + 28,
                // (int) (10 * health), 2, 0, 288, 1, 1);
                batch.setColor(1f, 1f, 1f, 1f);
            }

        }
    }

    // Draw all visible objects on map to back buffer
    // side effect: sorts map objects in increasing z-order
    private void drawThings(int x, int y) {

        Thing head = Engine.instance.map.sortZ(x, y);
        int numberOfThings = 0;
        if (head == null)
            return;

        do {
            drawThing(x, y, head, batch);
            head = head.next;
            numberOfThings++;
        } while (head != null);

    }

	/*
     * public void addAnimation(Animation a) { synchronized (animationElements)
	 * { if (!animating) { animating=true; // Game.warn("Animation start"); }
	 * animationElements.add(a); } }
	 */

    // draws cursor at given location to buffer
    public void drawCursor(int x, int y) {

        int px = (x - scrollx + MAP_DISTANCE) * TILEWIDTH + WIDTH / 2
                - ((MAP_DISTANCE * 2 + 1) * TILEWIDTH / 2);
        int py = (MAP_DISTANCE * 2 - (y - scrolly) - MAP_DISTANCE) * TILEHEIGHT
                + HEIGHT / 2 - ((MAP_DISTANCE * 2 + 1) * TILEHEIGHT / 2);
        int sx = 6 * TILEWIDTH;
        int sy = 0 * TILEHEIGHT;

        batch.draw(texture[TEXTURE_EFFECTS], px, py, sx, sy, TILEWIDTH,
                TILEHEIGHT);

    }

    public void addAnimation(Animation a) {
        synchronized (animationElements) {
            if (!animating) {
                animating = true;
                // Game.warn("Animation start");
            }
            animationElements.add(a);
        }
    }

    public void renderAnimation() {
        synchronized (animationElements) {
            Iterator it = animationElements.iterator();
            while (it.hasNext()) {
                Animation ae = (Animation) it.next();
                drawAnimation(ae);

                // remove finished animation parts
                if (ae.isExpired()) {
                    it.remove();
                }
            }
            if (animationElements.size() == 0) {
                // Game.warn("Animation stop");
                animating = false;
            }
        }
    }

    public void drawImage(double x, double y, int image) {
        double px = (x - scrollx + MAP_DISTANCE) * TILEWIDTH + WIDTH / 2
                - ((MAP_DISTANCE * 2 + 1) * TILEWIDTH / 2);
        double py = (MAP_DISTANCE * 2 - (y - scrolly) - MAP_DISTANCE)
                * TILEHEIGHT + HEIGHT / 2
                - ((MAP_DISTANCE * 2 + 1) * TILEHEIGHT / 2);
        int sx = (image % 20) * TILEWIDTH;
        int sy = TILEHEIGHT * (image / 20);

        batch.draw(texture[TEXTURE_EFFECTS], (int) px, (int) py, sx, sy,
                TILEWIDTH, TILEHEIGHT);
    }

    public void drawAnimation(Animation ae) {
        double start = ae.getDouble("StartTime");
        double now = System.currentTimeMillis();
        double step = now - start;
        double progress = 0.0; // assume complete
        int life = ae.getStat("LifeTime");
        if (life > 0) {
            progress = step / life;
            // 10secs max for any animation
            if (life > 10000) {
                Game.warn("Animation over 10 seconds long expired");
                progress = 1.0;
            }

            // expire if complete
            if (progress >= 1.0) {
                ae.set("Expired", true);
                return;
            }
        }

        switch (ae.getStat("Type")) {

            case Animation.SPARK: {
                int x = ae.getStat("X");
                int y = ae.getStat("Y");
                int c = ae.getStat("C");
                c = (c / 20) * 20 + (int) (progress * 6);
                drawImage(x, y, c);

                break;
            }

            case Animation.EXPLOSION: {
                int x = ae.getStat("X");
                int y = ae.getStat("Y");
                int r = ae.getStat("Radius");
                int c = ae.getStat("C");
                c = (c / 20) * 20 + (int) (progress * 6);

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        double ds = ((dx == 0) || (dy == 0)) ? 1.0 : 1.414;
                        double tx = x + (r * progress * dx / ds);
                        double ty = y + (r * progress * dy / ds);
                        drawImage(tx, ty, c);
                    }
                }

                break;
            }

            case Animation.SHOT: {
                int x1 = ae.getStat("X1");
                int y1 = ae.getStat("Y1");
                int x2 = ae.getStat("X2");
                int y2 = ae.getStat("Y2");
                int c = ae.getStat("C");

                drawImage(x1 + progress * (x2 - x1), y1 + progress * (y2 - y1), c);

                break;
            }

            case Animation.HIT: {
                double x = ae.getDouble("X");
                double y = ae.getDouble("Y");
                int c = ae.getStat("C");

                drawImage(x, y, c);

                break;
            }

            case Animation.SEQUENCE: {
                Animation a1 = (Animation) ae.get("Animation1");
                drawAnimation(a1);
                if (a1.isExpired()) {
                    Animation a2 = (Animation) ae.get("Animation2");
                    ae.set("Animation1", a2);
                    ae.set("Animation2", null);
                    if (a2 == null) {
                        ae.set("Expired", true);
                    } else {
                        a2.set("StartTime", now);
                    }
                }

                break;
            }

            case Animation.UNION: {
                Animation a1 = (Animation) ae.get("Animation1");
                Animation a2 = (Animation) ae.get("Animation2");

                boolean expired = true;
                if (!a1.isExpired()) {
                    drawAnimation(a1);
                    expired = a1.isExpired();
                }
                if (!a2.isExpired()) {
                    drawAnimation(a2);
                    expired = a2.isExpired();
                }

                ae.set("Expired", expired);

                break;
            }

            case Animation.SPRAY: {
                int x1 = ae.getStat("X1");
                int y1 = ae.getStat("Y1");
                double x2 = ae.getDouble("X2");
                double y2 = ae.getDouble("Y2");
                int c = ae.getStat("C");

                drawImage(x1 + progress * (x2 - x1), y1 + progress * (y2 - y1),
                        (c / 20) * 20 + (int) (progress * 6));

                break;
            }

            case Animation.DELAY: {
                // do nothing
            }
        }

    }

    public void renderStats() {
        Thing h = Game.hero();
        if (h == null)
            return;
        String location = (Engine.instance.map == null) ? "Disoriented"
                : Engine.instance.map.getDescription();
        int hp = h.getStat(RPG.ST_HPS);
        int hpm = h.getStat(RPG.ST_HPSMAX);
        int xp = h.getStat(RPG.ST_EXP);
        int xpm = Hero.calcXPRequirement(h.getStat("Level") + 1);
        // int mp = h.getStat(RPG.ST_MPS);
        // int mpm = h.getStat(RPG.ST_MPSMAX);
        // float pow = ((float) mp) / mpm;

        StringList sl = new StringList();

        Thing[] atts = h.getFlaggedContents("IsEffect");
        for (int i = 0; i < atts.length; i++) {
            sl.add(Text.capitalise(atts[i].getString("EffectName")));
        }

        Thing w1 = h.getWielded(RPG.WT_MAINHAND);
        Thing w2 = h.getWielded(RPG.WT_SECONDHAND);
        if ((w1 != null) && Item.isDamaged(w1))
            sl.add("Damaged weapon");
        if ((w2 != null) && Item.isDamaged(w2))
            sl.add("Damaged weapon");
        if ((w1 != null) && (!w1.getFlag("IsWeapon")))
            w1 = null;
        if ((w2 != null) && (!w2.getFlag("IsWeapon")))
            w2 = null;
        if ((w1 == null) && (w2 == null))
            sl.add("Unarmed");

        Thing[] fs = h.getFlaggedContents("IsBeing");

        int followers = 0;
        int pursuers = 0;
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isHostile(h)) {
                pursuers++;
            } else {
                followers++;
            }
        }
        if (followers > 0) {
            sl.add(Integer.toString(followers)
                    + ((followers > 1) ? " companions" : " companion"));
        }
        if (pursuers > 0) {
            sl.add(Integer.toString(pursuers)
                    + ((pursuers > 1) ? " enemies in pursuit"
                    : " enemy in pusuit"));
        }

        if (Hero.hasHungerString(h)) {
            String hunger = Text.capitalise(Hero.hungerString(h));
            sl.add(hunger);
        }

        sl = sl.compress();
        sl = sl.compact(21, ", ");
        String hps = hp + "/" + hpm;
        String xps = xp + "/" + xpm;
        // String mps = mp + "/" + mpm;

        int u = HEIGHT - 71;
        batch.setColor(1f, 1f, 1f, 1f);
        // batch.draw(texture[TEXTURE_TILES],5,u,105,HEIGHT-5-u,0,288,1,1);
        // batch.draw(texture[TEXTURE_SCENERY], 5, u,105,HEIGHT-5-u, 0,
        // 17 * TILEHEIGHT, TILEWIDTH*10, TILEHEIGHT*10);
        batch.draw(texture[TEXTURE_SCENERY], 5, u, 5, u, 110, HEIGHT - 5 - u,
                1, 1, 0, 0, 17 * TILEHEIGHT, TILEWIDTH * 10, TILEHEIGHT * 10,
                false, false);
        batch.setColor(0f, 0f, 0f, 1f);
        batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 26, 10, HEIGHT - 26,
                100, 16, 1, 1, 0, 0, 288, 1, 1, false, false);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 26, 100, 16, 0, 288,
        // 1,
        // 1);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, 100, 16, 0, 288,
        // 1,
        // 1);
        batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, 10, HEIGHT - 26,
                100, 16, 1, 1, 0, 0, 288, 1, 1, false, false);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, 100, 16, 0, 288,
        // 1,
        // 1);
        batch.setColor(1f, 0f, 0f, 1f);
        batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 26, 10, HEIGHT - 26, hp
                * 100 / hpm, 16, 1, 1, 0, 0, 288, 1, 1, false, false);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 26, hp * 100 / hpm,
        // 16,
        // 0, 288, 1, 1);
        batch.setColor(0f, 0f, 1f, 1f);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, mp * 100 / mpm,
        // 16,
        // 0, 288, 1, 1);
        batch.setColor(0f, 1f, 0f, 1f);
        batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, 10, HEIGHT - 26, xp
                * 100 / xpm, 16, 1, 1, 0, 0, 288, 1, 1, false, false);
        // batch.draw(texture[TEXTURE_TILES], 10, HEIGHT - 46, xp * 100 / xpm,
        // 16,
        // 0, 288, 1, 1);
        glyphLayout.setText(font, location);
        font.draw(batch, location, WIDTH - glyphLayout.width - 5,
                HEIGHT - 7);
        font.draw(batch, hps, 10, HEIGHT - 10);
        // font.draw(batch, mps, 10, HEIGHT - 30);
        font.draw(batch, xps, 10, HEIGHT - 30);
        font.draw(batch, "Level: " + Game.hero().getLevel(), 10, HEIGHT - 50);
        // font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", WIDTH - 30,
        // 20);
        for (int i = 0; i < sl.getCount(); i++) {
            font.draw(batch, sl.getString(i), 10, HEIGHT - 70 - (i * 20));
        }
    }
}
