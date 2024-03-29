package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

/**
 * Push can be cast on an ally or enemy to move them a
 * direct distance away from the caster in the direction of 
 * the cursor
 * @author Miraj
 *
 */
public class PushSpell extends Spell {
	public static String TYPE = "PushSpell";

	public PushSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Push";

		Image sprite = Resource.get("PushSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(30), 15);
	
		_shape = new Circle(this, new Vector(-8, -8), 8);

		_cooldown = 1000;
		_manaCost = 10;
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target._type.equals(Player.TYPE) && !target.equals(_caster)) {
			Player p = (Player) target;
			Vector f = p._pos.minus(_caster._pos).normalize();
			p._destination = null;//p._pos.plus(f.mult(100));
			p.root(500, _caster)._display = false;

			//p.applyForce(f.mult(30));
			p._vel = f.mult(15);
			target.changeHealth(-5, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("PushSpell"), _pos.minus(12, 12), _size);
		v.unrotate();
	}
}
