package tests;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Test;

import pvpmagic.Door;
import pvpmagic.Flag;
import pvpmagic.Player;
import pvpmagic.Resource;
import pvpmagic.TimedEffect;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.spells.AbracadabraSpell;
import pvpmagic.spells.BurnSpell;
import pvpmagic.spells.CleanseSpell;
import pvpmagic.spells.CloneSpell;
import pvpmagic.spells.ConfuseSpell;
import pvpmagic.spells.DashSpell;
import pvpmagic.spells.DisarmSpell;
import pvpmagic.spells.FelifySpell;
import pvpmagic.spells.HideSpell;
import pvpmagic.spells.LockSpell;
import pvpmagic.spells.OpenSpell;
import pvpmagic.spells.PushSpell;
import pvpmagic.spells.RejuvenateSpell;
import pvpmagic.spells.RootSpell;
import pvpmagic.spells.ShieldSpell;
import pvpmagic.spells.Spell;
import pvpmagic.spells.StunSpell;
import pvpmagic.spells.SummonSpell;

public class Tests {

	Resource r = new Resource();
	Player p = new Player(null, "andrew", "Bob", new String[]{});

	@Test
	public void playerTests(){
		p.changeHealth(-20, null);
		assertTrue(p._health == p._maxHealth-20);

		p.changeMana(40, null);
		assertTrue(p._mana == p._maxMana);

		Vector force = new Vector(20, 10);
		p.applyForce(force);
		assertTrue(p._force.equals(force));

		p.silence(1000, null);
		assertTrue(p._isSilenced);
		
		Vector pos = new Vector(0, 0);
		Flag f = new Flag(null, pos, 10.0,"flag");
		p._flag = f;
		p.dropFlag();
		assertTrue(p._flag == null);
		
		p.kitty(1000, null);
		for (Entry<String,TimedEffect> e : p._timedEffects.entrySet()) {
			e.getValue().effect();
		}
		assertTrue(p._basicImage == "cat");
		p.cleanse();
		System.out.println(p._basicImage);
		assertTrue(p._basicImage != "cat");
	}

	@Test
	public void doorTests(){
		Door d = new Door(null, new Vector(100, 100), 100, "door_closed");
		d.open();
		assertTrue(d._basicImage.equals("door_open"));
		d.lock();
		assertTrue(d._basicImage.equals("door_closed"));
	}
	
	@Test
	public void spellCreationTests(){
		assertTrue(Spell.newSpell(null,"Stun", null, new Vector(1,1)) instanceof StunSpell);
		assertTrue(Spell.newSpell(null,"Disarm", null, new Vector(1,1)) instanceof DisarmSpell);
		assertTrue(Spell.newSpell(null,"Burn", null, new Vector(1,1)) instanceof BurnSpell);
		assertTrue(Spell.newSpell(null,"Root", null, new Vector(1,1)) instanceof RootSpell);
		assertTrue(Spell.newSpell(null,"Push", null, new Vector(1,1)) instanceof PushSpell);
		assertTrue(Spell.newSpell(null,"Abracadabra", null, new Vector(1,1)) instanceof AbracadabraSpell);
		assertTrue(Spell.newSpell(null,"Open", null, new Vector(1,1)) instanceof OpenSpell);
		assertTrue(Spell.newSpell(null,"Lock", null, new Vector(1,1)) instanceof LockSpell);
		assertTrue(Spell.newSpell(null,"Confuse", null, new Vector(1,1)) instanceof ConfuseSpell);
		assertTrue(Spell.newSpell(null,"Rejuvenate", null, new Vector(1,1)) instanceof RejuvenateSpell);
		assertTrue(Spell.newSpell(null,"Cleanse", null, new Vector(1,1)) instanceof CleanseSpell);
		assertTrue(Spell.newSpell(null,"Summon", null, new Vector(1,1)) instanceof SummonSpell);
		assertTrue(Spell.newSpell(null,"Clone", null, new Vector(1,1)) instanceof CloneSpell);
		assertTrue(Spell.newSpell(null,"Hide", null, new Vector(1,1)) instanceof HideSpell);
		assertTrue(Spell.newSpell(null,"Dash", null, new Vector(1,1)) instanceof DashSpell);
		assertTrue(Spell.newSpell(null,"Felify", null, new Vector(1,1)) instanceof FelifySpell);
		assertTrue(Spell.newSpell(null,"Shield", null, new Vector(1,1)) instanceof ShieldSpell);
	}
	
	@Test
	public void toNetfromNet() {
		p.changeHealth(-10, null);
		p.changeMana(-10, null);
		p.fromNet(p.toNet().split("\t"), new HashMap<Integer, Unit>());
		assertTrue(p._health == 90.0);
		assertTrue(p._mana == 90.0);
	}
}
