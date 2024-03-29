package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;
/**
 * Root anchors an enemy into the ground, preventing
 * them from moving for a set period of time.
 * @author Miraj
 *
 */
public class RootSpell extends Spell {
	public static String TYPE = "RootSpell";
	double scale = .3;

	public RootSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Root";
		Image sprite = Resource.get("RootSpell");
		setProperties(new Vector(sprite.getWidth(null)*scale, sprite.getHeight(null)*scale), 15);
		
		_cooldown = 3000;
		_manaCost = 10;
		_shape = new Circle(this, new Vector(-12, -12), 12);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-10, _caster);
		if (target._type.equals(Player.TYPE)){
			target.root(3000, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("RootSpell"), _pos.minus(12, 12), _size);
		v.unrotate();
	}
}
