/*
 * Created on 26-Jul-2004
 *
 * By Mike Anderson
 */
package com.klemstinegroup.tyrant;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.klemstinegroup.tyrantandroid.Game;


/**
 * @author Mike
 *
 * Implements static methods for handling level maps
 */
public class LevelMap implements java.io.Serializable {
	private static final long serialVersionUID = 3545517309273125684L;
    // current pixels
	private int[] pixels=null;
	private int[] currentMemory=null;
	private Map lastMap=null;
	private HashMap mapMemory=new HashMap();
	transient Pixmap pix;

	public static LevelMap instance() {
		LevelMap l=(LevelMap)Game.instance.get("MapMemory");
		if (l==null) {
			l=new LevelMap();
			Game.instance.set("MapMemory",l);
		}
		return l;
	}
	
	private HashMap getMapMemory() {
		return mapMemory;
	}
	
	private int[] getMapMemory(Map m) {
		HashMap h=getMapMemory();
		int[] memory=(int[])h.get(m);
		if ((memory==null)&&(m==lastMap)) {
			memory=currentMemory;
		}
		if (memory==null) {
			memory=new int[m.width*m.height];
			if (!m.getFlag("ForgetMap")) {
				h.put(m,memory);
			} 
		}
		currentMemory=memory;
		lastMap=m;
		return memory;
	}
	
	public static void reveal(Map map) {
		int w=map.width;
		int h=map.height;
		int[] mem= instance().getMapMemory(map);

		for (int y=0;y<h; y++) {
			for (int x=0; x<w; x++) {
				setMapColor(map,mem,x,y);
			}
		}		
	}

	public static void forget(Map map,int chance) {
		if (map==null) return;
		int[] mem= instance().getMapMemory(map);

		for (int i=0;i<mem.length; i++) {
			if (RPG.r(100)<chance) mem[i]=0;
		}		
	}
	
	public Pixmap getMapView(Map map) {
		if (map==null)return null;
		int w=map.width;
		int h=map.height;
		
		int[] mem= getMapMemory(map);

		Thing he=Game.hero();
		int r3=Being.calcViewRange(he);

		for (int y=RPG.max(0,he.y-r3);y<RPG.min(he.y+r3+1,h); y++) {
			for (int x=RPG.max(0,he.x-r3); x<RPG.min(he.x+r3+1,w); x++) {
				if (map.isVisibleChecked(x+y*w)) {
					setMapColor(map,mem,x,y);
				}
			}
		}
		
		if ((pixels==null)||(pixels.length!=mem.length)) {
			pixels=new int[mem.length];
		}
		
		System.arraycopy(mem,0,pixels,0,mem.length);
		
		for (int y=RPG.max(0,he.y-r3);y<RPG.min(he.y+r3+1,h); y++) {
			for (int x=RPG.max(0,he.x-r3); x<RPG.min(he.x+r3+1,w); x++) {
				updateMapColor(map,pixels,x,y);
			}
		}
		pix=new Pixmap(w, h, Pixmap.Format.RGBA8888);
		int count=0;
		for (int r1=0;r1<h;r1++){
			for (int r2=0;r2<w;r2++){
				int pixelCol=pixels[count];
				//int a = (pixelCol >>> 24) & 0xff;
				int r = (pixelCol >>> 16) & 0xff;
				int g = (pixelCol >>> 8) & 0xff;
				int b = pixelCol & 0xff;
				float a=1f;
				if (r+g+b==0)a=0f;
				pix.setColor((float)r/255f,(float)g/255f,(float)b/255f,a);
				
				pix.drawRectangle(r2,r1, 1,1);
				count++;
			}
		}

		return pix;
	}
	
	/**
	 * This method modifiers map memory colour 
	 * Use this for temporary map colours
	 * e.g. Beings, Hero on radar
	 * 
	 * @param map
	 * @param pixels
	 * @param x
	 * @param y
	 */
	private static void updateMapColor(Map map, int[] pixels,int x, int y) {
		int i=x+map.width*y;
		if (map.isVisibleChecked(i)) {
			int c=pixels[i];
			c=c+0x00101010;


			Thing t=map.getObjectsChecked(i);
			while (t!=null) {

				
				if (t.getFlag("IsMobile")) {
					if (t.isHero()) {
						int cc=0x00FF8000;
						pixels[i]=cc;
						return;
					} else if (AI.isHostile(Game.hero(),t)) {
						c=0x00D00000;
					} else {
						c=0x0000D020;
					}
				}
				
				
				
				t=t.next;
			}
			
			pixels[i]=c;
		}
	}
	
	/**
	 * This method sets map memory colour according to map contents
	 * 
	 * @param map
	 * @param pixels
	 * @param x
	 * @param y
	 */
	private static void setMapColor(Map map, int[] pixels,int x, int y) {
		
		int tile=map.getTile(x,y);
		int c=Tile.getMapColour(tile);

		
		Thing t=map.getObjects(x,y);
		while (t!=null) {
			int mc=t.getStat("MapColour");
			if (mc>0) {
				c=mc;
			}
			t=t.next;
		}
		
		pixels[x+map.width*y]=c;
	}
	
}
