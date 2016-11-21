package com.klemstinegroup.tyrant;

public class Aura {
	public static void init() {
		Thing t;
		
		t=Lib.extend("base aura","base special");
		t.set("IsAura",1);
		t.set("IsWishable",1);
		t.set("IsInvisible",1);
		t.set("Image",145);
		Lib.add(t);
		
		t=Lib.extend("speed aura","base aura");
		{
			Modifier m=Modifier.bonus("Speed",10);
			m.set("ApplyMessage","You feel nimble and quick");
			t.add("LocationModifiers",m);
		}
		Lib.add(t);
		
		t=Lib.extend("luck aura","base aura");
		{
			Modifier m=Modifier.bonus("Luck",20);
			m.set("ApplyMessage","You feel lucky");
			t.add("LocationModifiers",m);
		}
		Lib.add(t);
	}
}
