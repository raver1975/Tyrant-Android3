package com.klemstinegroup.tyrantandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.klemstinegroup.tyrant.AI;
import com.klemstinegroup.tyrant.Action;
import com.klemstinegroup.tyrant.Armour;
import com.klemstinegroup.tyrant.Being;
import com.klemstinegroup.tyrant.Coin;
import com.klemstinegroup.tyrant.Combat;
import com.klemstinegroup.tyrant.Damage;
import com.klemstinegroup.tyrant.Describer;
import com.klemstinegroup.tyrant.Description;
import com.klemstinegroup.tyrant.Door;
import com.klemstinegroup.tyrant.Event;
import com.klemstinegroup.tyrant.Food;
import com.klemstinegroup.tyrant.GameHandler;
import com.klemstinegroup.tyrant.Gods;
import com.klemstinegroup.tyrant.Hero;
import com.klemstinegroup.tyrant.IActionHandler;
import com.klemstinegroup.tyrant.Item;
import com.klemstinegroup.tyrant.KeyEvent;
import com.klemstinegroup.tyrant.Lib;
import com.klemstinegroup.tyrant.Map;
import com.klemstinegroup.tyrant.Movement;
import com.klemstinegroup.tyrant.Point;
import com.klemstinegroup.tyrant.Portal;
import com.klemstinegroup.tyrant.Potion;
import com.klemstinegroup.tyrant.Quest;
import com.klemstinegroup.tyrant.RPG;
import com.klemstinegroup.tyrant.RangedWeapon;
import com.klemstinegroup.tyrant.Recipe;
import com.klemstinegroup.tyrant.Rune;
import com.klemstinegroup.tyrant.Secret;
import com.klemstinegroup.tyrant.Skill;
import com.klemstinegroup.tyrant.Spell;
import com.klemstinegroup.tyrant.Text;
import com.klemstinegroup.tyrant.Thing;
import com.klemstinegroup.tyrant.Time;
import com.klemstinegroup.tyrant.Trap;
import com.klemstinegroup.tyrant.Weapon;
import com.klemstinegroup.tyrant.Wish;

import static com.klemstinegroup.tyrantandroid.Renderer.TEXTURE_CREATURES;
import static com.klemstinegroup.tyrantandroid.Renderer.TEXTURE_ITEMS;
import static com.klemstinegroup.tyrantandroid.Renderer.TEXTURE_SCENERY;

public class Engine {

    public static boolean helpOverlay = false;
    public Map map;
    public static int curx = -10000;
    public static int cury = -10000;
    public static boolean cursor = false;
    public static Engine instance;
    public static boolean shiftdown = false;
    public static String messages;
    public static char input = ' ';
    public static Thing inputThing = null;
    public static int inputSelected = -1;
    public static Thing[] inputThings = null;
    public static String[] inputStrings = null;
    boolean exit = false;
    public static Thing lastUsed = null;

    public static ArrayList<String> messageLog = new ArrayList<String>();
    public static boolean running = false;
    protected GameHandler gameHandler = new GameHandler();
    private List actionHandlers;
    public static Thing portal;
    public static Thing portal1;
    static String race = "human";
    static String profession = "witch";

    public static String[] menu = { "Kick","Chat", "Drink", "Drop", "Eat",
            "Equip", "Exit", "Fire", "Give", "Inv", "Jump", "Look", "Magic", "Wait",
            "Message", "Open", "Pick Up", "Pray", "Read", "Search", "Skill",
            "Stats", "Steal", "Use", "Throw",  "Zap"};
    static TextureRegion[] trs = new TextureRegion[menu.length];

    static {
        int cnt = 0;
        for (String s : menu) {
            trs[cnt++] = ButtonActor.getRegion(cnt, 0);
        }
        cnt=0;
        trs[cnt++] = ButtonActor.getRegion(360, TEXTURE_ITEMS);  //kick
        trs[cnt++] = ButtonActor.getRegion(424, Renderer.TEXTURE_CREATURES); //chat
        trs[cnt++] = ButtonActor.getRegion(249, TEXTURE_ITEMS);  //drink
        trs[cnt++] = ButtonActor.getRegion(108, TEXTURE_ITEMS);  //drop
        trs[cnt++] = ButtonActor.getRegion(220, TEXTURE_ITEMS);  //eat
        trs[cnt++] = ButtonActor.getRegion(163, TEXTURE_ITEMS);  //equip
        trs[cnt++] = ButtonActor.getRegion(4, Renderer.TEXTURE_SCENERY);  //exit
        trs[cnt++] = ButtonActor.getRegion(124, TEXTURE_ITEMS);  //fire
        trs[cnt++] = ButtonActor.getRegion(370, TEXTURE_ITEMS);  //give
        trs[cnt++] = ButtonActor.getRegion(142, TEXTURE_ITEMS);  //inv
        trs[cnt++] = ButtonActor.getRegion(66, Renderer.TEXTURE_SCENERY);  //jump
        trs[cnt++] = ButtonActor.getRegion(261, TEXTURE_ITEMS);  //look
        trs[cnt++] = ButtonActor.getRegion(163, Renderer.TEXTURE_SCENERY);  //Magict

//        trs[13] = ButtonActor.getRegion(372, TEXTURE_CREATURES);  //help
        trs[cnt++] = ButtonActor.getRegion(40, TEXTURE_SCENERY);  //wait
        trs[cnt++] = ButtonActor.getRegion(285, TEXTURE_ITEMS);  //message
        trs[cnt++] = ButtonActor.getRegion(148, Renderer.TEXTURE_SCENERY);  //open
        trs[cnt++] = ButtonActor.getRegion(140, TEXTURE_ITEMS);  //pickup
        trs[cnt++] = ButtonActor.getRegion(240, Renderer.TEXTURE_SCENERY);  //pray
        trs[cnt++] = ButtonActor.getRegion(283, TEXTURE_ITEMS);  //read
        trs[cnt++] = ButtonActor.getRegion(188, TEXTURE_ITEMS);  //search
        trs[cnt++] = ButtonActor.getRegion(384, TEXTURE_ITEMS);  //skill
        trs[cnt++] = ButtonActor.getRegion(189, TEXTURE_ITEMS);  //stats
        trs[cnt++] = ButtonActor.getRegion(69, TEXTURE_ITEMS);  //steal
        trs[cnt++] = ButtonActor.getRegion(20, TEXTURE_SCENERY);  //use
        trs[cnt++] = ButtonActor.getRegion(88, TEXTURE_ITEMS);  //throw
        trs[cnt++] = ButtonActor.getRegion(289, TEXTURE_ITEMS);  //zap
    }

    public void createHero() {
        Game.hero = Hero.createHero("Hero", race, profession);
        Hero.setHeroName(Game.hero, "Hero");
        Being.utiliseItems(Game.hero());
        Game.hero.flattenProperties();
        messages = "";
        map = Portal.getMap("karrain", 1, 0);
        Game.hero.set("WorldMap", map);
        Thing port = map.find("tutorial inn");
        Map tm = Portal.getTargetMap(port);
        Game.instance.enterMap(tm, tm.getEntrance().x, tm.getEntrance().y);
        portal = Portal.create("traveler portal");
        Portal.setDestination(portal, tm, tm.getEntrance().x,
                tm.getEntrance().y);
        //Game.hero.addThing(Spell.create("Magic Shovel"));
        Game.hero.addThing(Lib.create("[IsMagicItem]"));
        for (int r = 0; r < Game.hero().getItems().length; r++) {
            Item.identify(Game.hero().getItems()[r]);
        }
        String m = Engine.messages;
        Engine.update();
        messageLog = new ArrayList();
        Game.instance.message(m);
        running = true;
        String sg = "\n(Drag to scroll, double-tap to exit.)\n\n"
                + Game.hero().getString("HeroHistory");
        Game.selectString("Hero History",
                Engine.stringCompress(sg.split("\n"), false).substring(1)
                        .split("\n"), false);
    }

    public void start() {
        new Thread(new ThreadGroup("start"), new Runnable() {
            public void run() {

                Game.instance().initialize();

                // get list of races
                // Game.hero=null;
                if (Game.hero == null) {
                    try {
                        Game.restore(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Renderer.percent = 0f;
                if (Game.hero == null)
                    pickRace();
                else {
                    Thing port = Hero.worldMap().find("tutorial inn");
                    Map tm = Portal.getTargetMap(port);
                    portal = Portal.create("traveler portal");
                    Portal.setDestination(portal, tm, tm.getEntrance().x,
                            tm.getEntrance().y);

                    Engine.instance.map = Game.hero.getMap();

                    Engine.update(Action.NONE);
                    Game.message("You feel like you just woke up.");
                }
                running = true;
                Game.over = false;
            }
        }, "start", 255000) {
        }.start();

    }

    protected void pickRace() {
        String[] raceherostrings = Hero.heroRaces();
        String[] racedescriptions = Hero.heroRaceDescriptions();
        String[] racee = new String[raceherostrings.length];
        for (int r = 0; r < raceherostrings.length; r++) {
            racee[r] = Text.centrePad(raceherostrings[r], "- ", 15)
                    + racedescriptions[r];

        }
        Game.selectString("What is your race?", racee, true);
        Gdx.input.setCatchBackKey(false);
        input = 'R';
    }

    public Engine() {
        start();
    }

    static public void update(char c) {
        update(new KeyEvent(c));
    }

    static public void update() {
        Engine.update(Action.NONE);
    }

    static public void update(KeyEvent e) {
        instance.update1(instance.convertEventToAction(e));
    }

    public static void update(Action action) {
        instance.update1(action);
    }

    public void update1(Action action) {
        // System.out.println(action+" "+input);

        try {
            if (cursor) {
                if (action != Action.NONE) {

                    Point p = Game.getDirection(action);
                    if (p != null) {
                        Engine.instance.curx += p.x;
                        Engine.instance.cury += p.y;
                    }
                    return;
                } else {
                    inputSelected = 0;
                    cursor(false);
                }
            }
            if (action != Action.AGAIN && action != Action.HELP
                    && action != Action.WAIT && action != Action.NONE) {
//                lastAction = action;
            }

            messages = null;

            if (input != ' ') {
                doInput(action);
                return;
            }

            Thing h = Game.hero();

            if (tryTick(h, action, shiftdown))
                ensureHerosNotDead();
            if (Game.hero != null)
                if (Game.hero().isRunning())
                    update1(action);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void doInput(Action action) { // inputs
        Game.actor = Game.hero();
        System.out.println(":" + input + ":");
        switch (input) {
            case 'h':
                if (inputSelected > -1) {
                    input = ' ';
                    String inputString = "";
                    if (inputSelected < menu.length) inputString = Engine.menu[inputSelected];
                    else inputString = Engine.inputStrings[inputSelected];
                    if (inputString.equals("Again"))
                        tryTick(Game.hero(), Action.AGAIN, shiftdown);
                    if (inputString.equals("Skill"))
                        tryTick(Game.hero(), Action.APPLY_SKILL, shiftdown);
                    if (inputString.equals("Magic"))
                        tryTick(Game.hero(), Action.ZAP, shiftdown);
                    if (inputString.equals("Chat")) {
                        tryTick(Game.hero(), Action.CHAT, shiftdown);
                    }
                    if (inputString.equals("Drop"))
                        tryTick(Game.hero(), Action.DROP, shiftdown);
                    if (inputString.equals("Eat"))
                        tryTick(Game.hero(), Action.EAT, shiftdown);
                    if (inputString.equals("Exit")) {
                        tryTick(Game.hero(), Action.EXIT, shiftdown);
                    }
                    if (inputString.equals("Fire"))
                        tryTick(Game.hero(), Action.FIRE, shiftdown);
                    if (inputString.equals("Give"))
                        tryTick(Game.hero(), Action.GIVE, shiftdown);
                    if (inputString.equals("Inv")) {
                        tryTick(Game.hero(), Action.INVENTORY, shiftdown);
                    }
                    if (inputString.equals("Help")) {
                        Engine.helpOverlay = !Engine.helpOverlay;
//                        tryTick(Game.hero(), Action.HELP, shiftdown);
                    }
                    if (inputString.equals("Jump")) {
                        tryTick(Game.hero(), Action.JUMP, shiftdown);
                    }
                    if (inputString.equals("Look"))
                        tryTick(Game.hero(), Action.LOOK, shiftdown);
                    if (inputString.equals("Kick")) {
                        tryTick(Game.hero(), Action.KICK, shiftdown);
                    }
                    if (inputString.equals("Message"))
                        tryTick(Game.hero(), Action.MESSAGES, shiftdown);

                    if (inputString.equals("Open")) {
                        tryTick(Game.hero(), Action.OPEN, shiftdown);
                    }
                    if (inputString.equals("Drink"))
                        tryTick(Game.hero(), Action.QUAFF, shiftdown);
                    if (inputString.equals("Pick Up")) {
                        tryTick(Game.hero(), Action.PICKUP, shiftdown);
                    }
                    if (inputString.equals("Read"))
                        tryTick(Game.hero(), Action.READ, shiftdown);
                    if (inputString.equals("Run")) {
                        Engine.instance.shiftdown = !Engine.instance.shiftdown;
                    }
                    if (inputString.equals("Search")) {
                        tryTick(Game.hero(), Action.SEARCH, shiftdown);
                    }
                    if (inputString.equals("Steal")) {
                        tryTick(Game.hero(), Action.STEAL, shiftdown);
                    }
                    if (inputString.equals("Use"))
                        tryTick(Game.hero(), Action.USE, shiftdown);
                    if (inputString.equals("Throw"))
                        tryTick(Game.hero(), Action.THROW, shiftdown);
                    if (inputString.equals("Quests")) {
                        tryTick(Game.hero(), Action.SHOW_QUESTS, shiftdown);
                    }
                    if (inputString.equals("Stats")) {
                        tryTick(Game.hero(), Action.VIEW_STATS, shiftdown);
                    }

                    if (inputString.equals("Zap"))
                        tryTick(Game.hero(), Action.WAND, shiftdown);
                    if (inputString.equals("Wait"))
                        tryTick(Game.hero(), Action.WAIT, shiftdown);
                    if (inputString.equals("Equip"))
                        tryTick(Game.hero(), Action.WIELD, shiftdown);
                    if (inputString.equals("Pray")) {
                        tryTick(Game.hero(), Action.PRAY, shiftdown);
                    }
                    if (inputString.equals("Zoom In")) {
                        zoomin();

                    }
                    if (inputString.equals("Zoom Out")) {
                       zoomout();
                    }
                    if (inputString.equals("Load Game"))
                        update(Action.LOAD_GAME);
                    if (inputString.equals("Save Game"))
                        update(Action.SAVE_GAME);
                    if (inputString.equals("Restart")) {
                        Game.hero().set("HPS", -1);
                        Engine.death();
                    }

                }

                break;
            case 'A':
                if (inputSelected > -1) {
                    input = ' ';

                    Thing r = inputThings[inputSelected];
                    String ingredients = r.getString("Ingredients");
                    int ic = Recipe.checkIngredients(Game.hero, ingredients);
                    if (ic <= 0) {
                        Game.message("You do not have the necessary ingredients!");
                        Game.message("You need " + ingredients);

                    } else {
                        Recipe.removeIngredients(Game.hero, ingredients);
                        // TODO: skill checks

                        Thing t = Recipe.recipeResult(r);
                        Game.message("You make " + t.getAName());
                        Item.identify(t);
                        Game.hero.addThing(t);
                    }
                }
                break;
            case '0': // sale
                if (inputSelected > -1) {
                    input = '0';
                    Thing[] things = Game.hero().getItems();
                    Item.tryIdentify(inputThing, things);

                    Thing gift = things[inputSelected];
                    inputSelected = -1;
                    Thing h = Game.hero();
                    if (gift == null) {
                        break;
                    }// Sanity check for elusive bug
                    if (!gift.getFlag("IsMoney")) {

                        int total = gift.getStat("Number");
                        int count = 1;
                        if (total > 1) {
                            // count =
                            // Game.getNumber("Sell how many (Enter=all)?",total);
                            // count =
                            // Game.selectSaleNumber("Sell how many (Enter=All)? ",
                            // h,
                            // t, total);
                            count = 1;
                        }
                        int value = Item.shopValue(gift, h, inputThing, count);

                        if (!((gift.y > 0) && (!h.clearUsage(gift.y)))) {

                            Game.message("You sell your "
                                    + gift.getName(Game.hero(), count) + " to "
                                    + inputThing.getTheName());
                            if (count > 0) {
                                gift.remove(count);
                            }
                            Coin.addMoney(h, value);
                            things = Game.hero().getItems();
                            Item.tryIdentify(inputThing, things);

                        }
                    }
                    Game.selectItem("Select an item to sell:", things);
                }

                break;
            case 'j':
                input = ' ';
                Point p = Game.getDirection(action);
                if ((p != null) && ((p.x != 0) || (p.y != 0)))
                    doJump1(Game.hero(), p.x * 5, p.y * 5);
                break;

            case 'l':
                if (inputSelected > -1) {
                    doLookPoint(new Point(curx, cury));
                    input = ' ';
                    cursor(false);
                }
                input = ' ';
                break;
            case 'a': // apply skill
                if (inputSelected > -1) {
                    input = ' ';
                    ArrayList al = Skill.getList(Game.hero());
                    String a = al.get(inputSelected).toString();
                    Thing h = Game.hero();
                    if (a.equals(Skill.THROWING)) {
                        doThrow(h);
                    } else if (a.equals(Skill.ARCHERY)) {
                        doFire(h);
                    } else if (a.equals(Skill.CASTING)) {
                        doZap(h, true);
                    } else {
                        Skill.apply(h, a);
                    }

                }
                break;
            case 'g':
                Point p7 = Game.getDirection(action);
                inputThing = map.getFlaggedObject(Game.hero().x + p7.x,
                        Game.hero().y + p7.y, "IsGiftReceiver");
                Game.selectItem("Select item to give:", Game.hero().getItems());
                input = 'G';
                break;

            case 'G':
                doGive1(inputThing, inputThings[inputSelected]);
                input = ' ';
                break;
            case 'f':
                if (inputSelected > -1) {
                    doFire1();
                    input = ' ';
                    cursor(false);
                }
                break;
            case 'S':
                if (inputSelected > -1) {
                    input = ' ';
                    int cost = Game.hero().getStat(RPG.ST_SKILLPOINTSSPENT) * 100
                            * Game.hero().getLevel();
                    cost = (int) (cost / (1.0 + 0.3 * Game.hero().getStat(
                            Skill.TRADING)));
                    String s = inputStrings[inputSelected];
                    if (s != null) {
                        Skill.train(Game.hero(), s);
                        Coin.removeMoney(Game.hero(), cost);
                    }
                }
                break;
            case 'Y':
                Thing h = Game.hero;
                Map map1 = h.getMap();
                int level = h.getStat(Skill.PICKPOCKET);
                Point p16 = Game.getDirection(action);
                if ((p16 != null) && ((p16.x != 0) || (p16.y != 0))) {
                    Thing b = map1.getFlaggedObject(h.x + p16.x, h.y + p16.y,
                            "IsMobile");

                    if (b != null) {
                        int skill = h.getStat(RPG.ST_SK) * level;

                        if (RPG.test(skill, b.getStat(RPG.ST_SK), h, b)) {
                            Thing[] stuff = b.getItems();
                            if (stuff.length > 0) {
                                Thing nick = stuff[RPG.r(stuff.length)];
                                if ((nick.y == 0) && !nick.getFlag("IsTheftProof")) {
                                    h.addThingWithStacking(nick);
                                    Game.message("You steal " + nick.getAName());
                                    Item.steal(h, nick);
                                } else {
                                    Game.message("You almost manage to steal "
                                            + nick.getAName());
                                }
                            } else {
                                Game.message("You find nothing worth stealing");
                            }
                        } else if (RPG.test(skill, b.getStat(RPG.ST_IN), h, b)) {
                            Game.message("You are unable to steal anything");
                        } else {
                            Game.message(b.getTheName() + " spots you!");
                            AI.turnNasty(b);
                        }
                    }
                }
                input = ' ';
                break;
            case 'M':
                Point p17 = Game.getDirection(action);
                if ((p17 != null) && ((p17.x != 0) || (p17.y != 0))) {
                    Thing trap = Game.hero.getMap().getFlaggedObject(Game.hero.x + p17.x,
                            Game.hero.y + p17.y, "IsTrap");
                    if ((trap != null) && trap.isVisible(Game.hero())) {
                        Trap.tryDisarm(Game.hero, trap);
                    }
                }
                input = ' ';
                break;
            case 'k':
                Point p6 = Game.getDirection(action);
                if ((p6 != null) && ((p6.x != 0) || (p6.y != 0)))
                    Combat.kick(Game.hero(), p6.x, p6.y);
                input = ' ';
                break;
            case 'z':
                if (inputSelected > -1)
                    input = ' ';
                doZap1(inputThings[inputSelected]);
                inputSelected = -1;
                break;
            case 's':
                Point p1 = Game.getDirection(action);
                if (p1 != null) {
                    Spell.castInDirection(inputThing, Game.hero(), p1.x, p1.y);
                    input = ' ';
                    inputSelected = -1;
                }

                break;
            case '1': // cast at item
                if (inputSelected > -1) {
                    input = ' ';
                    Spell.castAtObject(inputThing, Game.hero(),
                            inputThings[inputSelected]);
                }
                inputSelected = -1;
                break;
            case '2': // cast at location
                if (inputSelected > -1) {
                    inputSelected = -1;
                    if (map.isVisible(curx, cury)) {
                        Spell.castAtLocation(inputThing, Game.hero(), map, curx,
                                cury);
                    } else {
                        Game.message("You cannot see to focus your power");
                    }
                    input = ' ';
                    cursor(false);
                }
                break;
            case '3': // dip potion
                if (inputSelected > -1) {
                    Thing it = inputThings[inputSelected];
                    inputThing.remove(1);
                    if (it.getStat("Number") > 1) {
                        it = it.separate(RPG.d(4));
                    }

                    Potion.dip(Game.hero(), it, inputThing);
                    input = ' ';
                }
                break;
            case '6': // sacrifice
                if (inputSelected > -1) {
                    Gods.sacrifice(Game.hero(), inputThings[inputSelected]);
                    input = ' ';
                }
                break;
            case '7': // well
                if (inputSelected > -1) {
                    Thing gift = inputThings[inputSelected];
                    gift = gift.remove(1);
                    Game.message("You drop " + gift.getTheName() + " into "
                            + inputThing.getTheName());
                    Game.message("Splooosh!");

                    // need coin or valuable item
                    int value = (Item.value(gift) > 10000) ? 1 : 0;
                    if (gift.getName().equals("strange rock")) {
                        // while ((Game.hero().getLevel())<49) {
                        // Hero.gainExperience(Hero.calcXPRequirement(Game.hero().getLevel()+1));
                        Thing f = Lib.create("[IsBeing]", Game.hero.getLevel());

                        Game.hero.getMap().addThing(f, Game.hero.x - 1,
                                Game.hero.y - 1, Game.hero.x + 1, Game.hero.y + 1);
                        AI.setFollower(f, Game.hero);
                    }
                    if (gift.getFlag("IsCoin"))
                        value = 1;

                    if ((value > 0)
                            && (RPG.r(100) < inputThing.getStat("WishChance"))) {
                        Wish.doWish();
                    }

                    // no more wish chances!
                    inputThing.set("WishChance", 0);
                    input = ' ';

                }
                break;
            case '5': // scribe rune
                if (inputSelected > -1) {
                    Thing user = Game.hero();
                    int skill = user.getStat(Skill.RUNELORE) - 1;
                    String rname = inputStrings[inputSelected];
                    // String rname = "AIT";
                    rname = rname + " runestone";

                    inputThing.remove(1);

                    Thing r = Lib.create(rname);
                    int lev = r.getStat("RuneIndex");
                    int prob = RPG.middle(
                            0,
                            50
                                    - 3
                                    * lev
                                    + (user.getStat("CR") * (skill - lev) / RPG
                                    .round(Math.pow(1.15, lev))), 100);
                    Game.warn("Success chance = " + prob);
                    if (RPG.r(100) < prob) {
                        Item.identify(r);
                        user.message("You successfully scribe " + r.getAName());
                        user.addThingWithStacking(r);
                    } else {
                        user.message("You fail to scribe the " + rname
                                + " correctly");
                    }

                    input = ' ';
                }
                break;
            case '4':// scribe rune
                if (inputSelected > -1) {

                    input = ' ';
                    Thing t = inputThing;
                    Thing it = inputThings[inputSelected];

                    if (it.getStat("Number") > 1) {
                        it = it.separate(RPG.d(4));
                    }

                    String at = t.getString("AlterationType");

                    if (at == null) {
                        Game.message("Nothing seems to happen");
                        Game.warn("No AlterationType for " + t.name());
                        break;
                    }

                    if (!it.getFlag(at)) {
                        Game.message("You do not seem to be able to use "
                                + t.getTheName() + " with " + it.getTheName());
                        break;
                    }

                    t.remove(1);

                    if (it.getFlag("IsArtifact")) {
                        Game.message(it.getTheName() + " seems totally unaffected");
                        break;
                    }

                    int rc = Rune.countExistingRunes(it);
                    if (rc > 0) {
                        int des = Rune.destroyExistingRunes(it, 50);
                        if (des > 0)
                            Game.message("Some existing runes are destroyed!");
                    }

                    if (t.handles("OnApply")) {
                        Game.message(it.getTheName()
                                + " shines brightly for a second");
                        Event de = new Event("Apply");
                        de.set("Target", it);
                        de.set("User", Game.hero());
                        t.handle(de);

                    }

                }

                break;
            case 'o':
                Point p3 = Game.getDirection(action);
                if ((p3 != null) && ((p3.x != 0) || (p3.y != 0))) {
                    Thing t = map.getFlaggedObject(Game.hero().x + p3.x,
                            Game.hero().y + p3.y, "IsOpenable");
                    if (t != null) {
                        Door.useDoor(Game.hero(), t);
                        Game.hero().incStat("APS", -Being.actionCost(Game.hero()));
                    }
                }
                input = ' ';
                break;
            case 'R':
                if (inputSelected > -1) {
                    // get list of possible professions
                    String[] raceherostrings = Hero.heroRaces();
                    Engine.race = (String) raceherostrings[inputSelected];
                    String[] professionstrings = Hero.heroProfessions(race);
                    String[] professiondescriptions = Hero
                            .heroProfessionDescriptions(race);
                    String[] prof = new String[professionstrings.length];
                    for (int r = 0; r < professionstrings.length; r++) {
                        prof[r] = Text.centrePad(professionstrings[r], "- ", 15)
                                + professiondescriptions[r];

                    }
                    inputSelected = -1;
                    input = 'P';
                    Engine.inputStrings = prof;
                    Renderer.instance.inv.size = Engine.inputStrings.length;
                    Renderer.instance.inv.drawString = "What is your profession?";
                    Renderer.instance.inv(true);
                    Gdx.input.setCatchBackKey(false);
                }
                break;
            case 'P':
                if (inputSelected > -1) {
                    input = ' ';
                    String[] professionstrings = Hero.heroProfessions(race);
                    System.out.println(professionstrings.length);
                    Engine.profession = professionstrings[inputSelected];
                    createHero();

                }
                break;
            case 't':
                if (inputSelected > -1) {
                    cursor(true);
                    Game.message("Throw: select location");
                    inputThing = inputThings[inputSelected];
                    input = 'T';
                    inputSelected = -1;
                }
                break;
            case 'T':
                if (inputSelected > -1) {
                    doThrow(Game.hero(), inputThing);
                    input = ' ';
                    cursor(false);
                    inputSelected = -1;
                }
                break;
            case 'e':
                if (inputSelected > -1) {
                    input = ' ';
                    doEat1(inputThings[inputSelected]);
                }
                break;
            case 'u':
                if (inputSelected > -1) {
                    input = ' ';
                    doUse1(inputThings[inputSelected]);
                }
                break;
            case 'q':
                if (inputSelected > -1) {
                    input = ' ';
                    doQuaff1(inputThings[inputSelected]);
                }
                break;
            case 'd':
                if (inputSelected > -1) {
                    input = ' ';
                    doDrop1(inputThings[inputSelected]);
                    inputSelected = -1;
                }
                break;
            case 'r':
                if (inputSelected > -1) {
                    input = ' ';
                    doRead1(inputThings[inputSelected]);
                }
                break;

            case 'c':
                input = ' ';
                Point p4 = Game.getDirection(action);
                Thing person = map.getFlaggedObject(Game.hero().x + p4.x,
                        Game.hero().y + p4.y, "IsIntelligent");

                person = map.getNearby("IsIntelligent", Game.hero().x,
                        Game.hero().y, 1);
                if (person == null || person.equals(Game.hero())) {
                    if (!doOpen(Game.hero())) {
                        Game.message("You mumble to yourself");
                    }
                    return;
                }
                if (AI.isHostile(person, Game.hero())) {
                    Game.message(person.getTheName() + " is attacking you!");
                    return;
                }

                if (person.handles("OnChat")) {
                    doIntroduction(person);
                    Event e = new Event("Chat");
                    e.set("Target", Game.hero());
                    person.handle(e);
                } else if (person.equals(Game.hero())) {
                    Game.message("You mumble to yourself");
                } else if (person.getFlag("IsIntelligent")) {
                    doIntroduction(person);
                    Game.message("You chat with " + person.getTheName()
                            + " for some time");

                } else {
                    Game.message("You can't talk to " + person.getTheName());
                }
                Game.hero().incStat("APS", -200);
                break;
            case 'i':
                if (inputSelected > -1) {
                    input = ' ';
                    inputThing = inputThings[inputSelected];
                    Game.message(Item.inspect(inputThing));
                }
                break;
            case 'w':
                if (inputSelected > -1)
                    input = ' ';
                doWield1(inputThings[inputSelected]);
                break;
        }
    }

    private void zoomin() {
        Renderer.instance.zoom -= .5f;
        if (Renderer.instance.zoom < 1)
            Renderer.instance.zoom = 1;
        Renderer.instance.cam.zoom = Renderer.instance.zoom;
        Renderer.instance.cam.update();
        Renderer.instance.MAP_DISTANCE = (int) (Renderer.instance.zoom * 6);
    }
    private void zoomout(){
        Renderer.instance.zoom += .5f;
        Renderer.instance.MAP_DISTANCE = (int) (Renderer.instance.zoom * 6);
        Renderer.instance.cam.zoom = Renderer.instance.zoom;
        Renderer.instance.cam.update();
    }

    private void doTest() {
        // System.out.println("Saving");
        // Game.save();
        System.out.println("Loading");
        Game.restore(false);

    }

    public void addActionHandler(IActionHandler actionHandler) {
        if (actionHandlers == null)
            actionHandlers = new LinkedList();
        actionHandlers.add(actionHandler);
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * Answer true if the action is executed, false otherwise.
     */
    public boolean tryTick(Thing thing, Action action, boolean isShiftDown) {
        if (action == null)
            return false;
        performAction(thing, action, isShiftDown);

        endTurn();
        return true;
    }

    private void ensureHerosNotDead() {
        // ensure we are back on the GameScreen
        // has the hero died?
        if ((Game.hero == null) || (Game.hero().place == null) || Game.hero().getStat("HPS") <= 0) {
            death();
        }
    }

    public static void death() {
        Game.message("You have died.....");
        Game.instance.gameOver();
        Game.hero = null;
        Serializer.delete("TyrantA.sav");
        Game.over = true;
        // Game.hero = null;
        Engine.running = false;
        Engine.instance.map = null;
        input = ' ';
        inputSelected = -1;
        // Lib.instance = null;
        Game.instance = new Game();
    }

    public Action convertEventToAction(KeyEvent keyEvent) {
        // if(rejectEvent(keyEvent)) return null;
        return gameHandler.actionFor(keyEvent);
    }

    private KeyEvent getUserInput(KeyEvent k2) {
        Game.instance().clearMessageList();
        // get key, or repeat if running
        if (Game.hero().isRunning())
            return k2;
        return new KeyEvent(' ');
    }

    public void performAction(Thing thing, Action action, boolean isShiftDown) {
        if (Game.over)
            return;
        Game.actor = thing;
        if (actionHandlers != null) {
            for (Iterator iter = actionHandlers.iterator(); iter.hasNext(); ) {
                IActionHandler actionHandler = (IActionHandler) iter.next();
                if (actionHandler.handleAction(thing, action, isShiftDown))
                    return;
            }
        }

        // movement
        if (action == Action.WAIT)
            doWait(thing);
        else if (action.isMovementKey())
            gameHandler.doDirection(thing, action, isShiftDown);
        else if (action == Action.EXIT)
            doExit(thing);

            // information
        else if (action == Action.HELP)
            doHelp();
        else if (action == Action.SHOW_QUESTS)
            doShowQuests();
        else if (action == Action.INVENTORY)
            doInventory(thing);
            // else if (action == Action.MESSAGES)
            // doMessages();
        else if (action == Action.VIEW_STATS)
            doViewStats(thing);

            // display
            // else if (action == Action.ZOOM_OUT)
            // doZoom(25);
            // else if (action == Action.ZOOM_IN)
            // doZoom(-25);

            // save and load games
        else if (action == Action.SAVE_GAME) {
            Game.message("Saving renderer...");
            new Thread(new ThreadGroup("save"), new Runnable() {
                public void run() {
                    Game.save(false);
                    Game.message("Saved Game");
                }
            }, "save", 255000).start();

        }

        // quit , TODO:need to test how this works in applet mode
        else if (action == Action.QUIT_GAME) {
            death();
        } else if (action == Action.LOAD_GAME) {
            Game.message("Loading renderer...");
            new Thread(new ThreadGroup("load"), new Runnable() {
                public void run() {
                    Game.restore(false);
                    Game.message("Loaded renderer.");
                    System.out.println("saved");
                }
            }, "load", 255000).start();
            return;
        }
        // else if (action == Action.DEBUG)
        // doDebugKeys(thing);

        // dom't perform renderer actions if on world map
        else if (thing != null && thing.getMap().getFlag("IsWorldMap")) {
            thing.message("You must enter an area by using \"exit\" first!");
            Renderer.instance.inv(false);
            cursor(false);
            return;
        }

        // renderer actions
        else if (action == Action.AGAIN) {
            //            doAgain();
        }

        else if (action == Action.APPLY_SKILL)
            doApplySkill(thing);
        else if (action == Action.CHAT)
            doChat(thing);
        else if (action == Action.DROP)
            doDrop(thing, false);
        else if (action == Action.DROP_EXTENDED)
            doDrop(thing, true);
        else if (action == Action.EAT)
            doEat(thing);
        else if (action == Action.FIRE)
            doFire(thing);
        else if (action == Action.GIVE)
            doGive(thing);
        else if (action == Action.JUMP)
            doJump(thing);
        else if (action == Action.SAVE_GAME) {
            Game.save(false);
        } else if (action == Action.LOAD_GAME) {
            Game.restore(false);
        } else if (action == Action.KICK)
            doKick(thing);
        else if (action == Action.LOOK)
            doLook();
        else if (action == Action.OPEN)
            doOpen(thing);
        else if (action == Action.MESSAGES)
            doMessages();
        else if (action == Action.PICKUP)
            doPickup(thing, false);
        else if (action == Action.PICKUP_EXTENDED)
            doPickup(thing, true);
        else if (action == Action.QUAFF)
            doQuaff(thing);
        else if (action == Action.READ)
            doRead(thing);
        else if (action == Action.SEARCH)
            doSearch(thing);
        else if (action == Action.STEAL) {
            doPickup(thing, true);

        } else if (action == Action.WAND)
            doWand(thing);
        else if (action == Action.THROW)
            doThrow(thing);
        else if (action == Action.USE)
            doUse(thing);
        else if (action == Action.WIELD)
            doWield(thing);
        else if (action == Action.PRAY || action == Action.PRAY2)
            doPray(thing, action);
        else if (action == Action.ZAP)
            doZap(thing, true);
        else if (action == Action.ZAP_AGAIN)
            doZap(thing, false);
    }

//    private void doAgain() {
//        if (lastAction == Action.ZAP) {
//            doZap1(lastUsed);
//            return;
//        }
//        if (lastAction == Action.DROP) {
//            doDrop1(lastUsed);
//            return;
//        }
//        if (lastAction == Action.USE || lastAction == Action.WAND) {
//            doUse1(lastUsed);
//            return;
//        }
//        if (lastAction == Action.FIRE) {
//            doFire1();
//            return;
//        }
//        update1(lastAction);
//
//    }

    private void doMessages() {
        String temp4 = "";
        for (int r1 = 0; r1 < messageLog.size(); r1++) {
            String[] temp = Engine.messageLog.get(r1).split("\n");

            temp4 += stringCompress(temp, true);
        }
        String[] temp3 = temp4.substring(1).split("\n");
        Game.selectString("Messages", temp3, false);

    }

    private static GlyphLayout glyphLayout = new GlyphLayout();

    public static String stringCompress(String[] temp, boolean f1) {
        String temp1 = "";
        for (int r = 1; r < temp.length; r++) {
            if (temp[r].equals("")) {
                temp1 += "\n";
                continue;
            }
            int wi = temp[r].length();
            float ggg = 0;
            if (f1) {
                glyphLayout.setText(Renderer.instance.font, temp[r].substring(0, wi));

            } else {
                glyphLayout.setText(Renderer.instance.font1, temp[r].substring(0, wi));
            }
            ggg = glyphLayout.width;
//			int ggg = f1 ? Renderer.instance.font.getBounds(temp[r].substring(
//					0, wi)).width : Renderer.instance.font1.getBounds(temp[r]
//					.substring(0, wi)).width;
			while (ggg > Renderer.WIDTH - 10) {
                wi = wi - 1;
                if (f1) {
                    glyphLayout.setText(Renderer.instance.font, temp[r].substring(0, wi));

                } else {
                    glyphLayout.setText(Renderer.instance.font1, temp[r].substring(0, wi));
                }
                ggg=glyphLayout.width;
            }

            ggg = 0;
            if (f1) {
                glyphLayout.setText(Renderer.instance.font, temp[r].substring(0, wi));

            } else {
                glyphLayout.setText(Renderer.instance.font1, temp[r].substring(0, wi));
            }
            ggg = glyphLayout.width;
//				ggg = f1 ? Renderer.instance.font.getBounds(temp[r].substring(
//						0, wi)).width : Renderer.instance.font1
//						.getBounds(temp[r].substring(0, wi)).width;
//			}
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
        return temp1;
    }

    public Point getTargetLocation() {
        return getTargetLocation(Game.hero());
    }

    public Point getTargetLocation(Thing a) {
        if (a == null)
            a = Game.hero();
        return getTargetLocation(a.getMap(), new Point(a.x, a.y));
    }

    // get location, initially place crosshairs at start
    public Point getTargetLocation(Map m, Point start) {
        System.out.println("Engine.getTarget " + curx + " , " + cury);
        return new Point(curx, cury);

    }

    public void castSpell(Thing h, Thing s) {
        if (s == null)
            return;

        Map map = h.getMap();
        input = ' ';
        switch (s.getStat("SpellTarget")) {
            case Spell.TARGET_SELF:
                Spell.castAtSelf(s, h);
                break;
            case Spell.TARGET_DIRECTION: {
                Game.message("Select Direction:");
                input = 's';
                inputThing = s;
                break;
            }
            case Spell.TARGET_LOCATION:
                Game.message("Select Location:");
                input = '2';
                inputThing = s;
                Engine.cursor(true);
                break;
            case Spell.TARGET_ITEM:
                Game.selectItem("Select an item:", h.getItems());
                input = '1';
                inputThing = s;
                break;
        }
    }

    public void endTurn() {
        try {
            // make sure hero is not responsible for these actions
            Game.actor = null;

            Thing h = Game.hero();

            while (h!=null&&h.getStat("APS") < 0) {
                int apsElapsed = -h.getStat("APS");

                // advance renderer time and perform all actions
                int timeElapsed = apsElapsed * 100 / h.getStat("Speed");
                Time.advance(timeElapsed);

                h.incStat("APS", apsElapsed);
            }

            // special calculation for encumberance
            Being.calcEncumberance(h);

            // other updates
            h.set("IsFrozen", 0);

            if (h.getMap() != map) {
                // Game.warn("Map switch");
                map = h.getMap();
                if (map == null)
                    return;
                Game.enterMap(map, h.x, h.y);
            }

            gameHandler.calculateVision(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static char getKey(KeyEvent e) {
        char k = Character.toLowerCase(e.getKeyChar());

        // handle key conversions
        if (e.getKeyCode() == KeyEvent.VK_UP)
            k = '8';
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            k = '2';
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            k = '4';
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            k = '6';
        if (e.getKeyCode() == KeyEvent.VK_HOME)
            k = '7';
        if (e.getKeyCode() == KeyEvent.VK_END)
            k = '1';
        if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
            k = '9';
        if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
            k = '3';
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            k = 'Q';
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            k = 'Q';
        if (e.getKeyCode() == KeyEvent.VK_F1)
            k = '?';
        if (e.getKeyCode() == KeyEvent.VK_F2)
            k = '(';
        if (e.getKeyCode() == KeyEvent.VK_F3)
            k = ')';
        if (e.getKeyCode() == KeyEvent.VK_F4)
            k = '*';
        if (e.getKeyCode() == KeyEvent.VK_F5)
            k = ':';
        if (e.getKeyCode() == KeyEvent.VK_TAB)
            k = '\t';
        return k;
    }

    public void doWait(Thing h) {
        Game.message("You wait a moment.");
        h.incStat("APS", -50);
    }

    public void doApplySkill(Thing h) {

        ArrayList al = Skill.getList(h);
        String[] sks = (String[]) al.toArray(new String[al.size()]);
        Game.selectString("Your skills", sks, true);
        input = 'a';

    }

    public void doIntroduction(Thing person) {
        String intro = (String) person.get("Introduction");
        if (intro != null) {
            Game.message(intro);
            person.set("Introduction", null);
        }
    }

    public void doChat(Thing h) {

        Thing person = null;
        if (map.countNearby("IsIntelligent", h.x, h.y, 1) > 2) {
            Game.message("Chat: select direction");
            input = 'c';
            // person = map
            // .getFlaggedObject(h.x + p.x, h.y + p.y, "IsIntelligent");
        } else {
            try {
                person = map.getNearby("IsIntelligent", h.x, h.y, 1);
                if (person == null || person.equals(Game.hero())) {
                    if (!doOpen(h)) {
                        Game.message("You mumble to yourself");
                    }
                    return;
                }
                if (AI.isHostile(person, h)) {
                    Game.message(person.getTheName() + " is attacking you!");
                    return;
                }

                if (person.handles("OnChat")) {
                    doIntroduction(person);
                    Event e = new Event("Chat");
                    e.set("Target", h);
                    person.handle(e);
                } else if (person.equals(Game.hero())) {
                    Game.message("You mumble to yourself");
                } else if (person.getFlag("IsIntelligent")) {
                    doIntroduction(person);
                    Game.message("You chat with " + person.getTheName()
                            + " for some time");

                } else {
                    Game.message("You can't talk to " + person.getTheName());
                }
                h.incStat("APS", -200);

            } catch (Exception anyex) {
                anyex.printStackTrace();
            }
        }

    }

    public void doDrop(Thing h, boolean ext) {
        input = 'd';
        Game.selectItem("Select item to drop:", h.getItems());
    }

    public void doDrop1(Thing o) {
        Thing h = Game.hero();
        input = ' ';
        try {
            // while (o != null) {

            Being.tryDrop(h, o);
            // }
        } catch (Error e) {
        }
        lastUsed = o;
    }

    public void doEat(Thing h) {
        Game.selectItem("Select item to eat:", h.getFlaggedContents("IsEdible"));
        input = 'e';

    }

    public void doEat1(Thing h) {
        if (h != null)
            Food.eat(Game.hero(), h);
    }

    public void doFire(Thing h) {
        input = 'f';
        Game.message("Fire: select location");
        cursor(true);
    }

    public void doFire1() {
        Thing w = Game.hero().getWielded(RPG.WT_RANGEDWEAPON);
        Thing m = Game.hero().getWielded(RPG.WT_MISSILE);
        if (w == null)
            Game.message("You must first equip an appropriate missile weapon.");
        Renderer.instance.inv(false);
        Engine.cursor(false);
        // else if
        // (m==null)Game.message("You must equip ammunition for your weapon.");

        if (w != null) {
            RangedWeapon.useRangedWeapon(w, Game.hero());
        }
    }

    public void doGive(Thing h) {
        Thing mobile = null;
        if (map.countNearby("IsGiftReceiver", h.x, h.y, 1) == 0) {
            Game.message("There is nobody to receive your gift");
            Renderer.instance.inv(false);
            Engine.cursor(false);
            return;
        }
        if (map.countNearby("IsGiftReceiver", h.x, h.y, 1) > 1) {
            Game.message("Give: select direction");
            input = 'g';
        } else {
            mobile = map.getNearby("IsGiftReceiver", h.x, h.y, 1);
            input = 'G';
            Game.selectItem("Select item to give:", Game.hero().getItems());
            inputThing = mobile;
        }

    }

    public void doGive1(Thing mobile, Thing gift) {
        Thing h = Game.hero();
        // select mobile to give to

        if ((mobile != null) && (!h.isHostile(mobile))) {
            if (gift != null) {

                // can't give a cursed item
                if ((gift.y > 0) && (!h.clearUsage(gift.y)))
                    return;

                int total = gift.getStat("Number");
                if (total > 1) {
                    int n = Game
                            .getNumber("Give how many (Enter=All)? ", total);
                    n = 1;
                    if (n > 0) {
                        gift = gift.separate(n);
                    }
                }

                mobile.give(h, gift);

                // restack nicely if not taken
                if (gift.place == h)
                    gift.restack();

                h.incStat("APS", -100); // make it quick
            }
        } else {
            Game.message("There is nobody to receive your gift");
        }

    }

    private void doInventory(Thing h) {
        Thing t = Game.selectItem("Your inventory:", h.getItems());
        input = 'i';
    }

    private void doJump(Thing h) {
        Game.message("Jump: select location");
        input = 'j';
    }

    private void doJump1(Thing h, int x, int y) {
        if (h != null) {
            Movement.jump(h, h.x + x, h.y + y);

        }
    }

    private void doKick(Thing h) {
        Game.message("Kick: select direction");
        input = 'k';
    }

    private void doLook() {
        Game.message("Look: select location");
        input = 'l';
        inputSelected = -1;
        cursor(true);
    }

    // You can pass null Points ( pun intended )
    // to this method
    private void doLookPoint(Point p) {
        if (p != null) {
            Game.message("You see:");
            if (map.isVisible(p.x, p.y)) {
                Thing t = map.getObjects(p.x, p.y);
                while ((t != null) && t.isVisible(Game.hero())) {
                    String st = "";
                    st = st + t.getDescription().getDescriptionText();
                    if (t.getFlag("IsBeing")) {
                        st = st + " ("
                                + Text.capitalise(Damage.describeState(t))
                                + " damage)";
                    }
                    if (t.isHostile(Game.hero())) {
                        st = st + " (Hostile) ";
                    }
                    Game.message(st);
                    t = t.next;
                }
            }
        }
    }

    private void doMagicLook(Thing t) {
        // char c = Game.showData(t.report());
        //
        // if (c == 'e')
        // doEdit(t);
    }

    // Open/close ( toggle ) openable things such as
    // doors, chests, portcullises etc.
    // returns a boolean, because really, if you try to 'c'hat
    // with a door, you might just as well open/close it ;)
    private boolean doOpen(Thing h) {
        Thing t = null;
        if (map.countNearby("IsOpenable", h.x, h.y, 1) >= 2) {
            Game.message("Select direction");
            input = 'o';
        } else {
            try {
                t = map.getNearby("IsOpenable", h.x, h.y, 1);
                if (t != null) {
                    Door.useDoor(h, t);
                    h.incStat("APS", -Being.actionCost(h));
                    return true;
                }
            } catch (Exception anyex) {
                anyex.printStackTrace();
            }
        }

        return false;
    }

    private void doPickup(Thing h, boolean ext) {

        if (h.getInventoryWeight() >= Being.maxCarryingWeight(h)) {
            Game.message("You cannot carry any more!");
            Renderer.instance.inv(false);
            Engine.cursor(false);
            return;

        }

        Thing[] th = map.getObjects(h.x, h.y, h.x, h.y, "IsItem");
        boolean all = false;

        if (ext && (th.length > 1)) {
            while (th.length > 0) {
                Thing t = Game.selectItem("Select items to pick up:", th);
                if (t == null) {
                    break;
                }

                Being.tryPickup(h, t, ext);
                th = map.getObjects(h.x, h.y, h.x, h.y, "IsItem");
            }
        } else {
            for (int i = 0; i < th.length; i++) {
                Thing t = th[i];

                if ((!all) && (th.length > 1)) {
                    char c;
                    if (i == (th.length - 1)) {
                        // -we are about to select last item
                        c = 'y';
                    } else {
                        c = 'y';
                    }
                    if (c == 'n')
                        continue;
                    if (c == 'q') {
                        break;
                    }
                    if (c == 'a')
                        all = true;
                }

                Being.tryPickup(h, t, ext);
            }
        }

    }

    /**
     * Called when the player presses "q" to quaff a potion
     *
     * @param h The hero
     */
    private void doQuaff(Thing h) {
        Thing o = Game.selectItem("Select item to drink:",
                h.getFlaggedContents("IsDrinkable"));
        input = 'q';
    }

    private void doQuaff1(Thing h) {
        if (h != null) {
            Potion.drink(Game.hero(), h);
        }

    }

    private void doRead(Thing h) {
        input = 'r';
        Thing o = Game.selectItem("Select item to read:",
                h.getFlaggedContents("IsReadable"));
    }

    private void doRead1(Thing o) {
        Thing h = Game.hero();
        int skill = h.getStat(Skill.LITERACY);
        if (skill > 0) {
            if (o != null) {
                // cost depends on reading ability
                h.incStat("APS", -600 / skill);

                o = o.separate(1);
                h.message("You read " + o.getTheName());
                if (o.handles("OnRead")) {
                    Event e = new Event("Read");
                    e.set("Reader", h);
                    e.set("Target", h);
                    o.handle(e);
                }
                if (o.place == h)
                    o.restack();
            }
        } else {
            h.message("You can't read!");
        }
    }

    private void doSearch(Thing h) {
        Game.message("Searching...");
        h.incStat("APS", -200);
        Secret.searchAround();
    }

    private void doThrow(Thing h) {
        Thing o = Game.selectItem("Throw Item:", h.getItems());
        input = 't';
    }

    private void doThrow(Thing h, Thing o) {
        if (o != null) {
            // get wield position
            int wt = o.y;

            o = o.separate(1);

            // can't throw cursed worn item
            if ((o.y > 0) && (!h.clearUsage(o.y)))
                return;

            // get initial target
            Thing f = map.findNearestFoe(h);
            if ((f != null) && (!map.isVisible(f.x, f.y)))
                f = null;
            // get user target selection
            Point p = getTargetLocation(f);

            if (p != null) {
                o = o.remove(1);
                Being.throwThing(h, o, p.x, p.y);
            }

            if (o.place == h) {
                o = o.restack();
                h.setUsage(o, wt);
            }

        }
    }

    private void doWand(Thing h) {
        Game.selectItem("Zap Wand:", Game.hero().getFlaggedContents("IsWand"));
        inputThing = h;
        input = 'u';
//        lastAction = Action.USE;
    }

    private void doUse(Thing h) {
        Game.selectItem("Use Item:", h.getUsableContents());
        inputThing = h;
        input = 'u';
    }

    private void doUse1(Thing o) {
        // if (o.getFlag("IsPotion")) {
        // Game.message("You must quaff the " + o.name());
        // }
        Game.message("You use the " + o.name());
        if (o != null) {
            o = o.separate(1);
            System.out.println(inputThing.name() + " using " + o.name());
            Item.use(inputThing, o);
            o.restack();
            lastUsed = o;
        }
    }

    private String statString(String s) {
        int bs = Game.hero().getBaseStat(s);
        int ms = Game.hero().getStat(s) - bs;
        return Text.centrePad(new Integer(bs).toString(), ((ms >= 0) ? "(+"
                + ms : "(" + ms)
                + ")", 8);
    }

    /**
     * Caled when the player presses "v" to view stats
     */
    private void doViewStats(Thing h1) {
        String[] string = new String[200];
        Thing h = Game.hero();
        int xx = -1;
        string[++xx] = Text.capitalise(h.getString("Race")) + " "
                + Text.capitalise(h.getString("Profession"));
        String god = h.getString("Religion");
        string[++xx] = "You worship " + ((god == null) ? "no god" : god);
        string[++xx] = "Encumberance    : "
                + RPG.middle(0, h.getStat(RPG.ST_ENCUMBERANCE), 100) + "%";
        string[++xx] = "Wealth          : "
                + Integer.toString(Coin.getMoney(h));
        ++xx;
        string[++xx] = "SK: " + statString(RPG.ST_SK);
        string[++xx] = "ST: " + statString(RPG.ST_ST);
        string[++xx] = "AG: " + statString(RPG.ST_AG);
        string[++xx] = "TG: " + statString(RPG.ST_TG);
        string[++xx] = "IN: " + statString(RPG.ST_IN);
        string[++xx] = "WP: " + statString(RPG.ST_WP);
        string[++xx] = "CH: " + statString(RPG.ST_CH);
        string[++xx] = "CR: " + statString(RPG.ST_CR);
        ++xx;
        string[++xx] = "Hit Points    : " + h.getStat(RPG.ST_HPS) + "/"
                + h.getStat(RPG.ST_HPSMAX);
        string[++xx] = "Magic Points  : " + h.getStat(RPG.ST_MPS) + "/"
                + h.getStat(RPG.ST_MPSMAX);
        string[++xx] = "Base Speed    : " + h.getStat("Speed");
        string[++xx] = "Move Speed    : " + Movement.calcMoveSpeed(h);
        string[++xx] = "Attack Speed  : " + Combat.calcAttackSpeed(h);
        string[++xx] = "Current Level : " + h.getStat(RPG.ST_LEVEL);
        string[++xx] = "Current Exp.  : " + h.getStat(RPG.ST_EXP);
        string[++xx] = "Skill Points  : " + h.getStat(RPG.ST_SKILLPOINTS);
        string[++xx] = "Hunger level  : "
                + Text.properCase(Hero.hungerString(h));
        string[++xx] = "Game Turns    : " + Game.hero().getStat("TurnCount");
        string[++xx] = "Kill Count    : " + Game.hero().getStat("KillCount");
        string[++xx] = "Game Time     :  " + Game.hero().getStat("GameTime");

        ++xx;
        string[++xx] = "Worn:";
        ++xx;
        Thing wornItem = h.getWielded(RPG.WT_MAINHAND);
        if (wornItem == null) {
            string[xx] = "Right Hand    : ";
        } else {
            string[xx] = "Right Hand    : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_SECONDHAND);
        if (wornItem == null) {
            string[xx] = "Left Hand     : ";
        } else {
            string[xx] = "Left Hand     : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_TWOHANDS);
        if (wornItem == null) {
            string[xx] = "Both Hands    : ";
        } else {
            string[xx] = "Both Hands    : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_RANGEDWEAPON);
        if (wornItem == null) {
            string[xx] = "Ranged Weapon : ";
        } else {
            string[xx] = "Ranged Weapon : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_MISSILE);
        if (wornItem == null) {
            string[xx] = "Missile       : ";
        } else {
            string[xx] = "Missile       : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_TORSO);
        if (wornItem == null) {
            string[xx] = "Torso         : ";
        } else {
            string[xx] = "Torso         : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_LEGS);
        if (wornItem == null) {
            string[xx] = "Legs          : ";
        } else {
            string[xx] = "Legs          : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_HEAD);
        if (wornItem == null) {
            string[xx] = "Head          : ";
        } else {
            string[xx] = "Head          : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_BOOTS);
        if (wornItem == null) {
            string[xx] = "Boots         : ";
        } else {
            string[xx] = "Boots         : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_HANDS);
        if (wornItem == null) {
            string[xx] = "Hands         : ";
        } else {
            string[xx] = "Hands         : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_CLOAK);
        if (wornItem == null) {
            string[xx] = "Cloak         : ";
        } else {
            string[xx] = "Cloak         : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_FULLBODY);
        if (wornItem == null) {
            string[xx] = "Full Body     : ";
        } else {
            string[xx] = "Full Body     : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_BRACERS);
        if (wornItem == null) {
            string[xx] = "Bracers       : ";
        } else {
            string[xx] = "Bracers       : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_BELT);
        if (wornItem == null) {
            string[xx] = "Belt          : ";
        } else {
            string[xx] = "Belt          : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_RIGHTRING);
        if (wornItem == null) {
            string[xx] = "Right Ring    : ";
        } else {
            string[xx] = "Right Ring    : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_LEFTRING);
        if (wornItem == null) {
            string[xx] = "Left Ring     : ";
        } else {
            string[xx] = "Left Ring     : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        wornItem = h.getWielded(RPG.WT_NECK);
        if (wornItem == null) {
            string[xx] = "Neck          : ";
        } else {
            string[xx] = "Neck          : "
                    + Describer.describe(h, wornItem, Description.ARTICLE_NONE);
        }
        ++xx;
        string[++xx] = "Resistances:";
        string[++xx] = "Normal        : " + h.getStat("RES:normal");
        string[++xx] = "Impact        : " + h.getStat("RES:impact");
        string[++xx] = "Piercing      : " + h.getStat("RES:piercing");
        string[++xx] = "Water         : " + h.getStat("RES:water");
        string[++xx] = "Poison        : " + h.getStat("RES:poison");
        string[++xx] = "Fire          : " + h.getStat("RES:fire");
        string[++xx] = "Ice           : " + h.getStat("RES:ice");
        string[++xx] = "Chill         : " + h.getStat("RES:chill");
        string[++xx] = "Acid          : " + h.getStat("RES:acid");
        string[++xx] = "Shock         : " + h.getStat("RES:shock");
        string[++xx] = "Drain         : " + h.getStat("RES:drain");
        string[++xx] = "Disintegrate  : " + h.getStat("RES:disintegrate");
        ++xx;

        Thing w = h.getWielded(RPG.WT_MAINHAND);
        if ((w != null) && (w.getFlag("IsWeapon"))) {
            string[++xx] = "Wielding " + w.getName(Game.hero());
        } else {
            string[++xx] = "Unarmed";
            w = Combat.unarmedWeapon(h);
        }

        string[++xx] = "Attack Cost     : " + Combat.attackCost(h, w);
        string[++xx] = "Attack Skill    : " + Weapon.calcASK(w, h, null);
        string[++xx] = "Attack Strength : " + Weapon.calcAST(w, h, null);

        string[++xx] = "Defence Skill   : " + Weapon.calcDSK(h);

        string[++xx] = "Armour Class    : " + Armour.calcArmour(h, "normal");

        ++xx;
        string[++xx] = "Your skills:";
        ArrayList al = Skill.getList(h);
        ArrayList al_core = Skill.getUnmarkedList(h);

        int skillLines = 0;
        for (int i = 0; i < al.size(); i++) {
            String s = (String) al.get(i);
            String s_core = (String) al_core.get(i);
            if (Skill.isNotUsed(s_core)) {
                // s = s + " (Unused)";
            }
            if (!Skill.isAbility(s_core)) {
                string[++xx] = s;
                skillLines++;
            }
        }
        ++xx;
        string[++xx] = "Your abilities:";

        int abilityLines = 0;
        for (int i = 0; i < al.size(); i++) {
            String s = (String) al.get(i);
            String s_core = (String) al_core.get(i);
            if (Skill.isNotUsed(s_core)) {
                // s = s + " (Unused)";
            }
            if (Skill.isAbility(s_core)) {
                string[++xx] = s;
                abilityLines++;
            }
        }
        string[++xx] = "END";
        String[] gh = new String[xx];
        for (int r = 0; r < xx; r++) {
            if (string[r] != null)
                gh[r] = string[r];
            else
                gh[r] = "";
        }
        Renderer.instance.inv.type = Renderer.instance.inv.TYPE_STRINGS;
        Game.selectString("Hero Stats", gh, false);

    }

    private void doWield(Thing h) {
        Game.selectItem("Wield/wear which item?", Game.hero()
                .getWieldableContents());
        input = 'w';
    }

    private void doWield1(Thing h1) {
        Thing o = h1;
        Thing h = Game.hero();
        if (o != null) {
            Thing item = o;
            boolean wielded = false;
            final int wt = item.getStat("WieldType");

            if (item.y == wt) {
                if (h.clearUsage(wt)) {
                    Game.message("You are no longer using " + item.getTheName());
                }
                return;
            }

            if ((wt == RPG.WT_LEFTRING) || (wt == RPG.WT_RIGHTRING)) {
                // Game.message("Which finger? (r/l)");
                // char c = Game.getOption("lr");
                // char c = RPG.r(2) == 0 ? 'l' : 'r';
                char c = 'r';
                Thing wornItem = h.getWielded(RPG.WT_RIGHTRING);
                if (wornItem != null)
                    c = 'l';
                if (c == 'r') {
                    if (h.wield(item, RPG.WT_RIGHTRING)) {
                        Game.message("You put " + item.getTheName()
                                + " on your right finger");
                        wielded = true;
                    }
                } else if (c == 'l') {
                    if (h.wield(item, RPG.WT_LEFTRING)) {
                        Game.message("You put " + item.getTheName()
                                + " on your left finger");
                        wielded = true;
                    }
                }
            } else if ((wt == RPG.WT_MAINHAND) || (wt == RPG.WT_SECONDHAND)) {
                // Game.message("Which hand? (r/l) [" + h.inHandMessage() +
                // "]");
                // char c = Game.getOption("lr");
                // char c = RPG.r(2) == 0 ? 'l' : 'r';
                char c = 'r';
                Thing wornItem = h.getWielded(RPG.WT_MAINHAND);
                if (wornItem != null)
                    c = 'l';
                if (c == 'r') {
                    if (h.wield(item, RPG.WT_MAINHAND)) {
                        Game.message("You wield " + item.getTheName()
                                + " in your right hand");
                        wielded = true;
                    }
                } else if (c == 'l') {
                    if (h.wield(item, RPG.WT_SECONDHAND)) {
                        Game.message("You wield " + item.getTheName()
                                + " in your left hand");
                        wielded = true;
                    }
                }
            } else if ((wt == RPG.WT_TWOHANDS)) {
                if (h.wield(item, RPG.WT_TWOHANDS)) {
                    Game.message("You wield " + item.getTheName()
                            + " in both hands");
                    wielded = true;
                }
            } else if ((wt == RPG.WT_RANGEDWEAPON)) {
                if (h.wield(item, wt)) {
                    Game.message("You wield " + item.getTheName()
                            + " as your ranged weapon");
                    wielded = true;
                }
            } else if ((wt == RPG.WT_MISSILE)) {
                if (h.wield(item, wt)) {
                    Game.message("You prepare to fire " + item.getTheName());
                    wielded = true;
                }
            } else {
                if (h.wield(item, wt)) {
                    Game.message("You are now wearing " + item.getTheName());
                    wielded = true;
                }

            }

            if (wielded && item.getFlag("IsCursed")) {
                h.message(item.getTheName() + " glows black for a second");
                item.set("IsStatusKnown", 1);
            }

            h.incStat("APS", -Being.actionCost(h));
        }
    }

    private void doExit(final Thing h) {
        if (!exit)
            new Thread(new Runnable() {
                public void run() {
                    exit = true;
                    Game.message("Entering new area, please wait...");
                    h.incStat("APS", -100);
                    map.exitMap(h.x, h.y);
                    endTurn();
                    exit = false;
                    // Game.save();
                }
            }).start();
    }

    private void doPray(Thing h, Action action) {
        Gods.pray(h);
        input = ' ';
    }

    /**
     * Called when the player tries to cast a spell
     *
     * @param h The hero
     */

    private void doZap(Thing h, boolean select) {
        if ((h.getStat(Skill.CASTING) <= 0)
                && (h.getStat(Skill.HOLYMAGIC) <= 0)) {
            h.message("You are unable to cast magic spells");
            Renderer.instance.inv(false);
            return;
        }

        if (select || (lastUsed == null)) {
            Game.selectItem("Select spell to cast:",
                    h.getFlaggedContents("IsSpell"));
            input = 'z';
        } else
            doZap1(lastUsed);
    }

    private void doZap1(Thing s) {
        Thing h = Game.hero();
        if (s != null) {
            if (Spell.canCast(h, s)) {
                Spell.castCost(h, s);

                h.message("You cast " + s.getName(Game.hero()));

                castSpell(h, s);
                Spell.train(h, s);
            } else {
                Game.message("You have insufficient power to cast "
                        + s.getName(Game.hero()));
            }
            lastUsed = s;
        }
    }

    private void doShowQuests() {
        // TODO: fix this
        ArrayList quests = Quest.getQuests();
        String[] temp = new String[quests.size()];
        for (int i = 0; i < quests.size(); i++) {
            temp[i] = Quest.getQuestText((Thing) quests.get(i));

        }
        Game.selectString("Your Quests:", temp, false);
        return;
    }

    private void doHelp() {
        /*
         * Game.message("Key Commands:\n" +
		 * "  a = apply skill                       ? = help\n" +
		 * "  c = chat to somebody                  = = load renderer\n" +
		 * "  d = drop item                         - = save renderer\n" +
		 * "  e = eat item                          # = view quests\n" +
		 * "  f = fire ranged weapon                ( = zoom map in\n" +
		 * "  g = give item                         ) = zoom map out\n" // +
		 * "  h = help\n" + "  i = inspect inventory\n" + "  j = jump\n" +
		 * "  k = kick something\n" + "  l = look\n" + "  m = message log\n" +
		 * "  o = open / close door\n" + "  p = pick up item\n" +
		 * "  , = pick up item (extended)\n" + "  q = quaff potion\n" +
		 * "  r = read book or scroll\n" + "  s = search area\n" +
		 * "  t = throw / shoot missile\n" + "  u = use item\n" +
		 * "  v = view hero statistics\n" + "  w = wield weapon / wear armour\n"
		 * + "  x = exit area / climb staircase\n" +
		 * "  y = pray for divine aid\n" + "  z = cast spell\n" +
		 * "  n = toggle run" + "\n");
		 */
        // String[] string = { "Again", "Apply Skill", "Cast Spell",
        // "Chat With Somebody", "Drop Item", "Eat Item",
        // "Enter/Exit Area", "Fire Weapon", "Give Item", "Inventory",
        // "Jump", "Look", "Kick", "Message Log", "Pick Up",
        // "Open/Close Door", "Quaff Potion", "Read", "Run",
        // "Search Area", "Steal", "Use Item", "Throw", "View Quests",
        // "View Hero Statistics", "Wait", "Wield/Wear",
        // "Yell For Divine Aid", "Zap Wand", "Zoom In", "Zoom Out",
        // "Load Game", "Save Game" };
        String[] t = new String[menu.length + 5];
        for (int r = 0; r < menu.length; r++) {
            t[r] = menu[r];
        }
        t[menu.length] = "Load Game";
        t[menu.length + 1] = "Save Game";
        t[menu.length + 2] = "Restart";
        t[menu.length + 3] = "Zoom In";
        t[menu.length + 4] = "Zoom Out";
        Game.selectString("Commands:", t, true);
        input = 'h';

    }

    public static void cursor(boolean b) {
        if (b) {
            Engine.messages = Engine.messageLog.get(0);
            curx = Game.hero().x;
            cury = Game.hero().y;
            Thing f = Engine.instance.map.findNearestFoe(Game.hero());
            if (f != null) {
                curx = f.x;
                cury = f.y;
            }

            cursor = true;
        } else
            cursor = false;
    }
}
