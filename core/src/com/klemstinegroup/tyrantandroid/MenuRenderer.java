package com.klemstinegroup.tyrantandroid;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.klemstinegroup.tyrant.Thing;

public class MenuRenderer implements InputProcessor {
	int selected = -2;
	int scrollx = 0;
	int scrolly = 0;
	int touchx = 0;
	int touchy = 0;
	int worldx = 0;
	int worldy = 0;
	int rowtouched = -3;
	public String drawString = "";
	int size = 0;
	int max = 0;
	private boolean dragged = false;
	int type = -1;
	private int basex = 0;
	private int basey = 0;
	private int max1;
	public boolean highlight;
	public static final int TYPE_THINGS = 0;
	public static final int TYPE_STRINGS = 1;
	public static final int TYPE_SALE = 2;

	public void reset() {
		selected = -2;
		scrollx = 0;
		scrolly = 0;
		touchx = 0;
		touchy = 0;
		worldx = 0;
		worldy = 0;
		basex = 0;
		basey = 0;
		rowtouched = -3;
		String drawString = "";
		size = 0;
		max = 0;
		dragged = false;
		if (type == TYPE_THINGS || type == TYPE_SALE)
			size = Engine.inputThings.length;
		if (type == TYPE_STRINGS)
			size = Engine.inputStrings.length;
	}

	@Override
	public boolean keyDown(int keycode) {
		int py = scrolly-(rowtouched + 1) * 30
		- 30;
		System.out.println(py+" "+scrolly);
		switch (keycode) {
		case Input.Keys.DPAD_LEFT:
			scrollx+=20;
			break;
		case Input.Keys.DPAD_RIGHT:
			scrollx-=20;
			break;
		case Input.Keys.DPAD_UP:
			rowtouched--;
			if (py>-90)scrolly-=30;
			if (rowtouched<0)rowtouched=0;
			break;
		case Input.Keys.DPAD_DOWN:
			rowtouched++;
			if (py<-Renderer.instance.HEIGHT+30)scrolly+=30;
			if (rowtouched>=size)rowtouched=size-1;
			if (rowtouched<0)rowtouched=0;
			break;
		case Input.Keys.DPAD_CENTER:
		case Input.Keys.ENTER:
			doSelect();
			break;
		}
		checkScrolls();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.ESCAPE
				|| keycode == Input.Keys.BACK) {
			if (Game.over) {
				Game.over = false;
				Game.hero = null;
				Engine.instance.start();
			}
			if (Engine.input != 'P' && Engine.input != 'R') {
				Renderer.instance.inv(false);
				selected = -21;
				rowtouched = -21;
				Engine.input = ' ';
				Engine.inputSelected = -1;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public void convert(int x, int y) {
		Renderer.instance.cam.zoom = 1;
		Renderer.instance.cam.update();
		Vector3 vec = new Vector3(x, y, 0);
		Renderer.instance.cam.unproject(vec);
		worldx = (int) (vec.x);// - Renderer.instance.WIDTH / 2;
		worldy = (int) (vec.y);// - Renderer.instance.HEIGHT / 2;
		Renderer.instance.cam.zoom = Renderer.instance.zoom;
		Renderer.instance.cam.update();

	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		touchx = x;
		touchy = y;
		basex = x;
		basey = y;
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (dragged) {
			dragged = false;
			return true;
		}
		convert(x, y);
		// if (worldy>0&&worldy>50)Renderer.instance.inv(false);
		rowtouched = (Renderer.instance.HEIGHT - (worldy - scrolly)) / 30 - 1;
		if (rowtouched == selected) {doSelect();return true;}
		selected = rowtouched;
		return false;
	}

	private void doSelect() {

			if (Game.over) {
				Renderer.instance.inv(false);
				selected = -21;
				rowtouched = -21;
				Engine.input = ' ';
				Engine.inputSelected = -1;
				Engine.instance.start();
			}
			if (rowtouched < size && rowtouched > -1) {
				Renderer.instance.inv(false);
				Engine.inputSelected = rowtouched;
				selected = -21;
				rowtouched = -21;
				Engine.instance.update();
			} else if (Engine.input != 'P' && Engine.input != 'R') {
				Renderer.instance.inv(false);
				selected = -21;
				rowtouched = -21;
				Engine.input = ' ';
				Engine.inputSelected = -1;
			}
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (Math.abs(basex - x) > 20 || Math.abs(basey - y) > 20) {
			scrollx = scrollx - (touchx - x);
			scrolly = scrolly + touchy - y;
		}
checkScrolls();

		if (Math.abs(basex - x) > 20 || Math.abs(basey - y) > 20) {
			basex = -20;
			basey = -20;
			dragged = true;
			selected = -2;
			rowtouched = -3;
		}
		touchx = x;
		touchy = y;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	private void checkScrolls() {
		if (scrollx > 0)
			scrollx = 0;
		if (scrolly < 0)
			scrolly = 0;
		if (scrolly > 30 * (size - 12))
			scrolly = 30 * (size - 12);
		if (size < 12)
			scrolly = 0;
		if (scrollx < -max + Renderer.instance.WIDTH && max != 0) {
			if (Renderer.instance.WIDTH < max)
				scrollx = -(max - Renderer.instance.WIDTH);
			else
				scrollx = 0;
		}
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

	public void render() {
		Renderer.instance.batch.setColor(0f, 0f, 0.3f, .8f);
		Renderer.instance.batch.draw(
				Renderer.instance.texture[Renderer.TEXTURE_TILES], 0, 0, 0, 0,
				Renderer.instance.WIDTH, Renderer.instance.HEIGHT, 1, 1, 0, 0,
				288, 1, 1, false, false);
		Renderer.instance.font1.setColor(.0f, 1f, 1f, 1f);
		Renderer.instance.font1.draw(Renderer.instance.batch, drawString,
				scrollx + 37, scrolly + Renderer.instance.HEIGHT - 1);
		Renderer.instance.font1.setColor(1f, 1f, 1f, 1f);
		max1 = 0;
		Renderer.instance.font.getData().setScale(.5f);
		for (int r = 0; r < size; r++) {
			if (type == TYPE_THINGS || type == TYPE_SALE) {
				Thing t = Engine.inputThings[r];
				if (t.containsKey("Image")) {
					int image = t.getStat("Image");
					int sx = (image % 20) * Renderer.instance.TILEWIDTH;
					int sy = (image / 20) * Renderer.instance.TILEHEIGHT;
					int px = scrollx + 5;
					int py = scrolly + Renderer.instance.HEIGHT - (r + 1) * 30
							- 30;
					Object source = t.get("ImageSource");
					int im = Renderer.TEXTURE_ITEMS;
					if (source.equals("Tiles"))
						im = Renderer.TEXTURE_TILES;
					if (source.equals("Scenery"))
						im = Renderer.TEXTURE_SCENERY;
					if (source.equals("Creatures"))
						im = Renderer.TEXTURE_CREATURES;
					if (source.equals("Items"))
						im = Renderer.TEXTURE_ITEMS;
					if (source.equals("Effects"))
						im = Renderer.TEXTURE_EFFECTS;

					if (r == rowtouched) {
						Renderer.instance.batch.setColor(.5f, 0f, .5f, 1f);
						Renderer.instance.batch
								.draw(Renderer.instance.texture[Renderer.instance.TEXTURE_TILES],
										0, py, Renderer.instance.WIDTH, 30, 0,
										288, 1, 1);
					}
					Renderer.instance.batch.setColor(1f, 1f, 1f, 1f);
					Renderer.instance.batch.draw(Renderer.instance.texture[im],
							px, py, sx, sy, Renderer.instance.TILEWIDTH,
							Renderer.instance.TILEHEIGHT);
					Renderer.instance.font1.getData().setScale(1f);
					String g = Game.instance.getItemText(t, type == TYPE_SALE);
					if (g.length() * 12 + 50 > max1)
						max1 = g.length() * 12 + 50;
					Renderer.instance.font1.draw(Renderer.instance.batch, g,
							px + 32, py + 29);
				}
			} else if (type == TYPE_STRINGS) {
				int px = scrollx + 5;
				int py = scrolly + Renderer.instance.HEIGHT - (r + 1) * 30 - 30;
				Renderer.instance.batch.setColor(.5f, 0f, .5f, 1f);
				if (r == rowtouched && highlight) {
					Renderer.instance.batch.draw(
							Renderer.instance.texture[Renderer.TEXTURE_TILES],
							0, py, 0, py, Renderer.instance.WIDTH, 30, 1, 1, 0,
							0, 288, 1, 1, false, false);
				}
				Renderer.instance.batch.setColor(1f, 1f, 1f, 1f);
				Renderer.instance.font1.getData().setScale(1f);
				if (Engine.inputStrings[r].length() * 12 > max1)
					max1 = Engine.inputStrings[r].length() * 12;
				Renderer.instance.font1.draw(Renderer.instance.batch,
						Engine.inputStrings[r], px, py + 29);
			}
		}
		max = max1;
		// Renderer.instance.font1.setScale(2f);
		// Renderer.instance.font1.draw(Renderer.instance.batch,"Cancel",Renderer.instance.WIDTH/2-Renderer.instance.font1.getBounds("Cancel").width/2,10);

	}

}
