/*
 * Created on May 24, 2006 by tomdemuyt
 *
 */
package com.klemstinegroup.tyrant;

import com.klemstinegroup.tyrantandroid.Engine;
import com.klemstinegroup.tyrantandroid.Game;

/**
 * PortalStone by @author tomdemuyt
 */
public class PortalStone {
  
	private static class PortalStoneAction extends Script {
		public boolean handle(Thing t, Event e) {
		  Thing user=e.getThing("User");
		  PortalStone.use( user , t );
		  return true;
		}	  
	}
  
  static private int maxSlots = 10;
  
  public static void init() {
    Thing t = Lib.extend("portal stone", "base item");
    t.set("Image", 104);
    //t.set("Image", 190);
    t.set("ValueBase", 0);
    t.set("ItemWeight", 50);
    t.set("Frequency", 2); 		//This is a rare item
    t.set("LevelMin",1);
    t.set("ASCII","*");
    t.set("OnUse",new PortalStoneAction());
    t.set("Slots" , new Thing[maxSlots+1]);
    t.set("SlotCount" , 2);
    Lib.add(t);
  }
  
  static public void use( Thing user , Thing portalStone){
    //Default to first slot
    int slot=1;
    int slots = portalStone.getStat( "SlotCount" );
    Thing[] Slots = (Thing[])portalStone.get("Slots");
    
    //We can use it to mark location and to cast portal
		//Game.message("Do you want to Mark your location, or cast a Portal (M or P)");
		
		if ('p' == 'm'){
		  //Player wants to mark the current location
		  slot = Game.getNumber( "Which slot do you want to use ?\n" + PortalStone.toString(portalStone) , slots );
		  //Delete previous portals so that we do not unbalance the time/space continuum
		  if(Slots[slot]!=null){
		    Slots[slot].die();
		  }
		  Slots[slot] = Portal.create( "traveler portal" );
		  Portal.setDestination( Slots[slot] , user.getMap() , user.x , user.y );
		}else{
		  //Player wants to cast a portal
		  //slot = Game.getNumber( "Which slot do you want to use ?\n" + PortalStone.toString(portalStone) , slots );
		  //if( Slots[slot] != null ){
		    //Dont create portals on the same level, ca n'a pas d'allure
		    if (user.getMap()!=null&&Engine.portal.get("PortalTargetMap")!=null)if( user.getMap().equals( Engine.portal.get("PortalTargetMap") ) ){
		      Game.message( "You are already at " + ((Map)Engine.portal.get("PortalTargetMap" )).getDescription() );
		    }else{		    
          user.getMap().addThing(Engine.instance.portal, user.x, user.y);
          Thing port = Hero.worldMap().find("tutorial inn");
			Map tm = Portal.getTargetMap(port);
			Engine.portal1 = Portal.create("traveler portal");
			Portal.setDestination(Engine.portal1, Game.hero().getMap(), Game.hero().x,
					Game.hero().y);
		   tm.addThing(Engine.portal1,tm.getEntrance().x,tm.getEntrance().y);		    }
		  }//else{
		   // Game.message("You have not marked this slot.");
		  //}
		  
		//}
  }
  
  //One time in the future, the portal mage will allow to increase slots
	static public boolean addSlot( Thing portalStone ){
	int slots = portalStone.getStat( "SlotCount" );
	  if( slots != maxSlots ){
	    slots++;
	    portalStone.set("SlotCount",slots);
	    return true;
	  }
	  return false;
	}
	
	
	//List all slots, with a description of their map or a blank
	//I might restrict slots just for gui reasons, which is frankly pretty stupid
	static public String toString( Thing portalStone ){
	  String s = "";
    int slots = portalStone.getStat( "SlotCount" );
    Thing[] Slots = (Thing[])portalStone.get("Slots");		  
	  for( int i = 1 ; i <= slots ; i++){
	    s = s + String.valueOf( i ) + ". ";
	    if( Slots[i] != null ){
	     s = s + ((Map)Slots[i].get("PortalTargetMap" )).getDescription() + ".\n";
	    }else{
	      s = s + ".\n";
	    }
	  }
	  return s;
	}  

  public static void main(String[] args) {
    //TODO : test...
  }
}
