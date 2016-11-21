package com.klemstinegroup.tyrantandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.klemstinegroup.tyrant.Action;
import com.klemstinegroup.tyrant.Animation;
import com.klemstinegroup.tyrant.Armour;
import com.klemstinegroup.tyrant.BaseObject;
import com.klemstinegroup.tyrant.Being;
import com.klemstinegroup.tyrant.Coin;
import com.klemstinegroup.tyrant.Hero;
import com.klemstinegroup.tyrant.Item;
import com.klemstinegroup.tyrant.LevelMap;
import com.klemstinegroup.tyrant.Lib;
import com.klemstinegroup.tyrant.Map;
import com.klemstinegroup.tyrant.Point;
import com.klemstinegroup.tyrant.Portal;
import com.klemstinegroup.tyrant.RPG;
import com.klemstinegroup.tyrant.Spell;
import com.klemstinegroup.tyrant.Text;
import com.klemstinegroup.tyrant.Thing;
import com.klemstinegroup.tyrant.ThingOwner;
import com.klemstinegroup.tyrant.Weapon;
import com.klemstinegroup.tyrant.Wish;

public class Game extends BaseObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static Thing actor;
    public static Game instance = new Game();
    static Thing hero;
    public static boolean over;
    public static final int LINELENGTH = 45;
    static HashMap<ThingOwner, String> temp = new HashMap<ThingOwner, String>();

    public static Thing hero() {
        return Game.hero;
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            try {
                throw new AssertionError();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initialize() {

        Lib library = (Lib) get("Library");
        if (library != null) {
            Lib.setInstance(library);
        } else {
            library = Lib.instance();
            if (library == null) {
                throw new Error("No library in Game.initialize()");
            }
            set("Library", library);
        }
    }

    public static Thing createHero() {
        String race = null;
        String profession = null;
        race = "human";
        profession = "wizard";

        // get list of races
        String[] raceherostrings = Hero.heroRaces();
        String[] racedescriptions = Hero.heroRaceDescriptions();

        if (race == null) {
            // Debug mode only
            // have escaped, so choose randomly
            race = raceherostrings[RPG.r(raceherostrings.length)];
            String[] herostrings = Hero.heroProfessions(race);
            profession = herostrings[RPG.r(herostrings.length)];

        }

        // get list of possible professions
        String[] professionstrings = Hero.heroProfessions(race);
        String[] professiondescriptions = Hero.heroProfessionDescriptions(race);

        Thing h = Hero.createHero("QuickTester", race, profession);

        // hero name and history display
        String name = "QuickTester";
        Hero.setHeroName(h, name);
        System.out.println("Hero created");
        hero = h;
        return h;
    }

    /**
     * Temporary access method for Game.instance
     *
     * @return
     */
    public static Game instance() {
        return instance;
    }

    public static void message(String string) {
        System.out.println(string);
        Engine.messages += "\n" + string;
        Engine.messageLog.add(0, "\n" + string);
        while (Engine.messageLog.size() > 100) {
            Engine.messageLog.remove(Engine.messageLog.size() - 1);
        }
    }

    public static void warn(String string) {
        System.out.println(string);
        // Engine.messages+="\n"+"\""+string+"\"";
    }

    public static int level() {
        // Map m=hero.getMap();
        // if (m!=null) return m.getLevel();
        return hero() == null ? 1 : hero().getLevel();
    }

    // animates a shot from (x1,y1) to (x2,y2)
    public void doShot(int x1, int y1, int x2, int y2, int c, double speed) {
        Animation a1 = Animation.shot(x1, y1, x2, y2, c, speed);
        Animation a2 = Animation.spark(x2, y2, c);

        Renderer.instance.addAnimation(Animation.sequence(a1, a2));
    }

    // animates a shot from (x1,y1) to (x2,y2)
    public void doBreath(int x1, int y1, int x2, int y2, int c, double speed) {
        for (int i = 0; i < 20; i++) {
            Animation a0 = Animation.delay(i * 50);
            Animation a1 = Animation.spray(x1, y1, x2, y2, c, speed);
            Renderer.instance.addAnimation(Animation.sequence(a0, a1));
        }
    }

    // animates a shot from (x1,y1) to (x2,y2)
    public void doSpellShot(int x1, int y1, int x2, int y2, int c,
                            double speed, int r) {
        Animation a1 = Animation.shot(x1, y1, x2, y2, c, speed);
        Animation a2;
        if (r == 0) {
            a2 = Animation.spark(x2, y2, c);
        } else {
            a2 = Animation.explosion(x2, y2, c, r);
        }
        Renderer.instance.addAnimation(Animation.sequence(a1, a2));
    }

    public void doDamageMark(int tx, int ty, int c) {
        Renderer.instance.addAnimation(Animation.hit(tx, ty, c));
    }

    // makes an explosion of the specified style and radius
    public void doExplosion(int x, int y, int c, int r) {
        if (r <= 0) {
            doSpark(x, y, c);
            return;
        }
        Renderer.instance.addAnimation(Animation.explosion(x, y, c, r));
    }

    public void doSpark(int x, int y, int c) {
        Renderer.instance.addAnimation(Animation.spark(x, y, c));
    }

    public static int getNumber(String string, int total) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static void selectString(String message, String[] rs,
                                    boolean highlight) {
        Engine.inputStrings = rs;
        Renderer.instance.inv.type = MenuRenderer.TYPE_STRINGS;
        Renderer.instance.inv.highlight = highlight;

        Renderer.instance.inv.drawString = message;
        Renderer.instance.inv(true);
        // setupScrollPane(string, rs);
    }

    public static void selectSale(String message, Thing[] things) {
        Engine.inputThings = things;
        Renderer.instance.inv.type = MenuRenderer.TYPE_SALE;
        Renderer.instance.inv.drawString = message;
        Renderer.instance.inv(true);
        // setupScrollPane(message, rs);
    }

    public static void reveal(Map map) {
        Engine.instance.map.setAllVisible();
        LevelMap.reveal(map);
    }

    public static void quotedMessage(String pick) {
        System.out.println(pick);
        Engine.messages += "\n" + "\"" + pick + "\"";
        Engine.messageLog.add(0, "\n\"" + pick + "\"");
        while (Engine.messageLog.size() > 100) {
            Engine.messageLog.remove(Engine.messageLog.size() - 1);
        }

    }

    public static Thing selectItem(String message, Thing owner) {
        return selectItem(message, owner.getItems());
    }

    public static Thing selectItem(String message, Thing[] things) {
        return selectItem(message, things, false);
    }

    public static Thing selectItem(String message, Thing[] things,
                                   boolean rememberFilter) {
        Item.tryIdentify(Game.hero(), things);
        String[] rs = new String[things.length];
        for (int r = 0; r < things.length; r++) {
            rs[r] = Game.instance.getItemText(things[r], false);
        }
        Engine.inputThings = things;
        Renderer.instance.inv.type = MenuRenderer.TYPE_THINGS;
        Renderer.instance.inv.drawString = message;
        Renderer.instance.inv(true);
        // setupScrollPane(message, rs);
        return null;
    }

    public String getItemText(Thing t, boolean ttt) {
        Thing h = Game.hero();

        if (t.getFlag("IsSpell")) {
            return Spell.selectionString(h, t, LINELENGTH);
        }

        String s = t.getName(null);

        if (t.getStat("HPS") < t.getStat("HPSMAX")) {
            s = "damaged " + s;
        }

        if (t.getFlag("IsWeapon") && Item.isIdentified(t)) {
            s = s + "  " + Weapon.statString(t);
        }

        if (t.getFlag("IsArmour") && Item.isIdentified(t)) {
            s = s + "  " + Armour.statString(t);
        }

        String ws = Lib.wieldDescription(t.y);
        if ((t.place == h) && (ws != null)) {
            ws = "(" + ws + ")";
        } else {
            ws = "";
        }

        if (!ttt) {
            s = Text.centrePad(s, ws, LINELENGTH);
            s = s + Text.centrePad("  ", t.getWeight() / 100 + "s", 7);
        } else {

            s = Text.centrePad(s, ws, LINELENGTH - 15);

            if (!t.getFlag("IsMoney"))
                s = s
                        + Text.centrePad("  ", Coin.valueString(Item.shopValue(
                        t, Game.hero(), Engine.inputThing)), 20);
        }
        return s;
    }

    /**
     * Gets the map sorage HashMap
     *
     * @return
     */
    public HashMap getMapStore() {
        HashMap h = (HashMap) Game.instance().get("MapStore");
        if (h == null) {
            h = new HashMap();
            Game.instance().set("MapStore", h);
        }
        return h;
    }

    public static Point getDirection(Action action) {
        Point direction = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        if (action == Action.MOVE_N) {
            direction.x = 0;
            direction.y = -1;
        }
        if (action == Action.MOVE_S) {
            direction.x = 0;
            direction.y = 1;
        }
        if (action == Action.MOVE_W) {
            direction.x = -1;
            direction.y = 0;
        }
        if (action == Action.MOVE_E) {
            direction.x = 1;
            direction.y = 0;
        }
        if (action == Action.MOVE_NW) {
            direction.x = -1;
            direction.y = -1;
        }
        if (action == Action.MOVE_NE) {
            direction.x = 1;
            direction.y = -1;
        }
        if (action == Action.MOVE_SW) {
            direction.x = -1;
            direction.y = 1;
        }
        if (action == Action.MOVE_SE) {
            direction.x = 1;
            direction.y = 1;
        }
        if (action == Action.MOVE_NOWHERE) {
            direction.x = 0;
            direction.y = 0;
        }
        return direction.x == Integer.MIN_VALUE ? null : direction;
    }

    // transport to location of particular map
    public static void enterMap(Map m, int tx, int ty) {
        m.addThing(Game.hero(), tx, ty);
        Game.message(m.getEnterMessage());

        // update highest reached level if necessary
        if (hero().getStat(RPG.ST_SCORE_BESTLEVEL) < m.getLevel()) {
            hero().set(RPG.ST_SCORE_BESTLEVEL, m.getLevel());
        }

        // Game.hero.set("APS",0);
    }

    public void clearMessageList() {
        // TODO Auto-generated method stub

    }

    /**
     * Adds a thing to a map, storing it in a temporary queue if the map is not
     * yet created
     *
     * @param t       The thing to add
     * @param mapName The map name
     */
    public void addMapObject(Thing t, String mapName) {
        addMapObject(t, mapName, 0, 0);
    }

    public void addMapObject(Thing t, String mapName, int x, int y) {
        t.remove();
        t.x = x;
        t.y = y;

        Map map = (Map) getMapStore().get(mapName);
        if (map == null) {
            ArrayList al = getMapObjectList(mapName);
            al.add(t);
        } else {
            addMapObject(t, map);
        }
    }

    private void addMapObject(Thing t, Map map) {
        if ((t.x == 0) && (t.y == 0)) {
            map.addThing(t);
        } else {
            map.addThing(t, t.x, t.y);
        }
    }

    public void addMapObjects(Map map) {
        ArrayList obs = getMapObjectList(map.getString("HashName"));
        for (Iterator it = obs.iterator(); it.hasNext(); ) {
            Thing t = (Thing) it.next();
            addMapObject(t, map);
        }
        obs.clear();
    }

    public Map createWorld() {
        set("MapStore", null);
        return Portal.getMap("karrain", 1, 0);
    }

    private HashMap getMapObjectStore() {
        HashMap h = (HashMap) Game.instance().get("MapObjectStore");
        if (h == null) {
            h = new HashMap();
            Game.instance().set("MapObjectStore", h);
        }
        return h;
    }

    private ArrayList getMapObjectList(String mapName) {
        HashMap h = getMapObjectStore();
        ArrayList al = (ArrayList) h.get(mapName);
        if (al == null) {
            al = new ArrayList();
            h.put(mapName, al);
        }
        return al;
    }

    // has same effect as pressing stipulated direction key
    public static void simulateDirection(int dx, int dy) {
        switch (dy) {
            case -1:
                switch (dx) {
                    case 1:
                        simulateKey('9');
                        return;
                    case 0:
                        simulateKey('8');
                        return;
                    case -1:
                        simulateKey('7');
                        return;
                }
            case 0:
                switch (dx) {
                    case 1:
                        simulateKey('6');
                        return;
                    case 0:
                        simulateKey('5');
                        return;
                    case -1:
                        simulateKey('4');
                        return;
                }
            case 1:
                switch (dx) {
                    case 1:
                        simulateKey('3');
                        return;
                    case 0:
                        simulateKey('2');
                        return;
                    case -1:
                        simulateKey('1');
                        return;
                }
        }

        return;
    }

    public static void registerDeath(Thing t) {
        Being.registerKill(Game.actor, t);
    }

    public static void simulateKey(char c) {
        Engine.update(c);
    }

    public static void getAllTree(ThingOwner t) {
        if (temp.get(t) == null && t != null)
            temp.put(t, " ");
        else
            return;
        try {
            Thing tt = (Thing) t;
            try {
                getAllTree(tt.place);
            } catch (Exception e) {
                System.out.println("Error1: " + t + " " + e);
            }
            try {
                getAllTree(tt.next);
            } catch (Exception e) {
                System.out.println("Error2: " + t + " " + e);
            }
            try {
                getAllTree(tt.owner());
            } catch (Exception e) {
                System.out.println("Error3: " + t + " " + e);
            }
            try {
                getAllTree(tt.getMap());
            } catch (Exception e) {
                System.out.println("Error4: " + t + " " + e);
            }
            try {
                if (tt.inv != null) {
                    for (int r = 0; r < tt.inv.length; r++) {
                        getAllTree(tt.inv[r]);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error5: " + t + " " + e);
            }
        } catch (Exception e) {
        }

        try {
            Map mm = (Map) t;
            Thing[] ob = mm.getObjects();
            try {
                for (int r = 0; r < ob.length; r++) {

                    getAllTree(ob[r]);
                }
            } catch (Exception e) {
                System.out.println("Error7: " + t + " " + e);
            }
            try {
                Thing[] ot = mm.getThings();
                for (int r = 0; r < ot.length; r++) {
                    getAllTree(ot[r]);
                }
            } catch (Exception e) {
                System.out.println("Error8: " + t + " " + e);
            }
            try {
                Object[] op = mm.getAllPortals().toArray();
                for (int r = 0; r < op.length; r++) {
                    //System.out.println("----------------------"+(Thing)op[r]);
                    getAllTree((Thing) op[r]);
                    getAllTree((Map) ((Thing) op[r]).get("PortalTargetMap"));
                    //getAllTree(Portal.getTargetMap(((Thing)op[r])));

                }
            } catch (Exception e) {
                System.out.println("Error9: " + t + " " + e);
            }

            try {
                getAllTree(mm.getMap());
            } catch (Exception e) {
                System.out.println("Error10: " + t + " " + e);
            }
        } catch (Exception e) {
        }

    }

    public static boolean save(boolean auto) {
        if (Game.hero == null) return false;
        temp = new HashMap<ThingOwner, String>();
        getAllTree(Hero.worldMap());
        getAllTree(Game.hero);
        ArrayList<BaseObject> all = new ArrayList<BaseObject>();
        for (Iterator iter = temp.keySet().iterator(); iter.hasNext(); ) {
            BaseObject key = (BaseObject) iter.next();
            all.add(key);
        }
        System.out.println(all.size());
        try {
            // ------------------------------------------------------------
            // all.remove(Game.instance);
            for (int r = 0; r < all.size(); r++) {
                BaseObject b = all.get(r);
                String b1 = b.getClass().toString();
                if (b.place == null)
                    b.placeInt = -1;
                else
                    b.placeInt = all.indexOf(b.place);
            }
            int h = all.indexOf(Game.hero());

            all.get(h).heroFlag = true;
            // ------------------------------------------------------------
            Game.instance.compressAllData();
            if (auto) Serializer.serialize("TyrantA.sav", all);
            else Serializer.serialize("TyrantB.sav", all);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean restore(boolean auto) {
        try {
            Engine.running = false;
            // Game.instance().initialize();
            // Renderer.percent = 0f;
            ArrayList<BaseObject> all;
            if (auto) all = (ArrayList<BaseObject>) Serializer
                    .deserialize("TyrantA.sav", false);
            else all = (ArrayList<BaseObject>) Serializer
                    .deserialize("TyrantB.sav", false);
            Game.hero = null;
            // -----------------------------------------------------------------
            if (all != null) {
                for (int r = 0; r < all.size(); r++) {
                    BaseObject b = all.get(r);
                    String b1 = b.getClass().toString();
                    if (b.placeInt > -1)
                        b.place = (ThingOwner) all.get(b.placeInt);
                    if (all.get(r).heroFlag)
                        Game.hero = (Thing) all.get(r);
                }
            }
            // -----------------------------------------------------------------
            Engine.update();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Engine.running = true;
        }
        return true;
    }

    /**
     * @param hero The hero to set.
     */
    public void setHero(Thing hero) {
        Game.instance().hero = hero;
        hero.set("Game", instance());
    }

    public void compressAllData() {
        HashMap hs = new HashMap();

        HashMap store = getMapStore();
        Set keySet = getMapStore().keySet();

        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            Map m = (Map) store.get(it.next());
            compressMapData(hs, m);
        }
    }

    private void compressMapData(HashMap hs, Map m) {
        Thing[] ts = m.getThings();
        for (int i = 0; i < ts.length; i++) {
            ts[i].compressData(hs);
        }
    }

    public void gameOver() {
        Wish.makeWish("identification", 100);
        Game.message("");

        Thing h = Game.hero();

        String outcome = getDeathString(h);

        String story = null;
        int sc = 0;
        if (h != null) sc = h.getStat("Score");
        String score = Integer.toString(sc);
        String level = "loser";
        if (h != null)level= Integer.toString(h.getLevel());

        if ((h!=null&&!h.isDead())) {
            story = "You have defeated The Tyrant!\n"
                    + "\n"
                    + "Having saved the world from such malevolent evil, you are crowned as the new Emperor of Daedor, greatly beloved by all the people of the Earth.\n"
                    + "\n"
                    + "You rule an Empire of peace and prosperity, and enjoy a long and happy life.\n"
                    + "\n" + "Hurrah for Emperor " + h.getString("HeroName")
                    + "!!\n";

        } else {
            story = "\n"
                    + "It's all over...... "
                    + outcome
                    + "\n"
                    + "\n"
                    + "You have failed in your adventures and died a hideous death.\n"
                    + "\n" + "You reached level " + level + "\n"
                    + "Your score is " + score + "\n";
        }

        Game.message("GAME OVER - " + outcome);

        // display the final story
        Game.over = true;
        Engine.input = ' ';

        if (h==null)return;
        Thing[] inc = h.getItems();
        String rs = "Your Inventory:\n\n";
        for (int r = 0; r < inc.length; r++) {
            rs += inc[r].getName() + "\n";
        }
        String killData = Hero.reportKillData();
        story += "\n" + rs + "\n" + killData;
        System.out.println(story);
        story = Engine.stringCompress(story.split("\n"), false);
        String[] g = story.substring(1).split("\n");
        Game.selectString("Game Over", g, false);
    }

    private String getDeathString(Thing h) {
        if (h == null) return "NULL";
        if (h.getStat("HPS") <= 0) {
            Thing t = h.getThing("Killer");
            if (t == null) {
                return "Killed by divine power";
            }
            t.remove();

            String killer = t.getAName();
            if (t.getFlag("IsEffect"))
                killer = t.name();

            if (killer.equals("you"))
                killer = "stupidity";
            return "Killed by " + killer;
        }

        return "Defeated The Tyrant";
    }
}
