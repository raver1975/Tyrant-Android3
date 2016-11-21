/*
 * Created on 16-Jan-2005
 *
 * By Mike Anderson
 */
package com.klemstinegroup.tyrant;

/**
 * @author Mike
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Animation extends BaseObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TEST=0;
	public static final int SHOT=1;
	public static final int EXPLOSION=2;
	public static final int SPARK=3;
	public static final int HIT=4;
	public static final int SEQUENCE=5;
	public static final int UNION=6;
	public static final int DELAY=7;
	public static final int SPRAY=8;
	
	public Animation(int type) {
		set("Type",type);
		set("StartTime",System.currentTimeMillis());
	}

	public static Animation test() {
		return new Animation(TEST);
	}

	
	public static Animation spark(int x, int y, int c) {
		Animation a=new Animation(SPARK);
		a.set("X",x);
		a.set("Y",y);
		a.set("C",c);
		a.set("LifeTime",500);
		return a;
	}
	
	public static Animation hit(int x, int y, int c) {
		Animation a=new Animation(HIT);
		
		// note: we are setting double values here
		a.set("X",x+0.5*(RPG.random()-0.5));
		a.set("Y",y+0.5*(RPG.random()-0.5));
		
		a.set("C",c);
		a.set("LifeTime",200);
		return a;
	}
	
	public static Animation explosion(int x, int y, int c, int r) {
		Animation a=new Animation(EXPLOSION);
		a.set("X",x);
		a.set("Y",y);
		a.set("Radius",r);
		a.set("C",c);
		a.set("LifeTime",500);
		return a;
	}
	
	public static Animation sequence(Animation a1, Animation a2) {
		Animation a=new Animation(SEQUENCE);
		a.set("Animation1",a1);
		a1.set("StartTime",a.getDouble("StartTime"));
		a.set("Animation2",a2);
		return a;
	}
	
	public static Animation union(Animation a1, Animation a2) {
		Animation a=new Animation(UNION);
		a.set("Animation1",a1);
		a1.set("StartTime",a.getDouble("StartTime"));
		a.set("Animation2",a2);
		a2.set("StartTime",a.getDouble("StartTime"));
		return a;
	}
	
	public static Animation delay(int millis) {
		Animation a=new Animation(DELAY);
		a.set("LifeTime",millis);
		return a;
	}
	
	public static Animation shot(int x1, int y1, int x2, int y2, int c, double speed) {
		Animation a=new Animation(SHOT);
		a.set("X1",x1);
		a.set("Y1",y1);
		a.set("X2",x2);
		a.set("Y2",y2);		
		a.set("C",c);
		a.set("LifeTime",RPG.max(1,(int)(1000.0*RPG.dist(x1,y1,x2,y2)/speed)));
		return a;
	}
	
	public static Animation spray(int x1, int y1, int x2, int y2, int c, double speed) {
		Animation a=new Animation(SPRAY);
		a.set("X1",x1);
		a.set("Y1",y1);
		a.set("X2",(x2+RPG.random()-0.5));
		a.set("Y2",(y2+RPG.random()-0.5));		
		a.set("C",c);
		a.set("LifeTime",RPG.max(1,(int)(1000.0*RPG.dist(x1,y1,x2,y2)/speed)));
		return a;
	}

	
	public boolean isExpired() {
		return getFlag("Expired");
	}
}
