package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class DisarmSpell extends Spell {
	public static String TYPE = "DisarmSpell";

	public DisarmSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Disarm";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5);
		if(target instanceof Player) {
			target.silence(5000);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}
