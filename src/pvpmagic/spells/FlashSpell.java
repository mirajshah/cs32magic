package pvpmagic.spells;

import pvpmagic.*;


public class FlashSpell extends Spell {
	public static String TYPE = "FlashSpell";

	public FlashSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Clone";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 30;
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c){
		//do nothing if it hits something, just disappear
		this._delete = true;
	}
	
	@Override
	public void draw(View v){		
		if (_caster._flag != null) {
			Vector flagPos = _pos.plus(40, 0);
			v.rotate(new Vector(1.5, -1), flagPos);
			v.drawImage(Resource._gameImages.get("flag"), flagPos, _caster._flag._size.mult(0.8));
			v.unrotate();
		}
		v.drawImage(Resource._gameImages.get("player1_back"), _pos, _caster._size);
	}
}