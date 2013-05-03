package pvpmagic;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

import pvpmagic.spells.Spell;

public class Player extends Unit {
	public static String TYPE = "player";
	
	String _characterName;
	String _playerName;

	public Vector _destination;

	double _spellCastingTime = 0;
	Spell _spellToCast = null;

	String[] _spells;
	//ArrayList<Carryable> inventory = new ArrayList<Carryable>();
	Flag _flag = null;
	Vector _flagSize = new Vector(40,40);
	
	HashMap<String, Long> _spellCastingTimes;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 6;


	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, TYPE);
		_canBeRooted = true;
		_canBeSilenced = true;

		_mass = 3;
		
		_health = 100;
		_mana = 100;

		_pos = new Vector(-50, -20);
		Image sprite = Resource._gameImages.get("player1_back");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(80);
		
		double hitBoxScale = 0.9;
		_shape = new Box(this, _size.mult(1-hitBoxScale).div(2), _size.mult(hitBoxScale));

		_spells = spellNames;
		_spellCastingTimes = new HashMap<String, Long>();
		
		_characterName = characterName;
		_playerName = playerName;
		
		this._restitution = 0;
		
		_appliesFriction = true;

	}

	@Override
	public void draw(View v){
		if (_flag != null) {
			Vector flagPos = _pos.plus(40, 0);
			v.rotate(new Vector(1.5, -1), flagPos);
			v.drawImage(Resource._gameImages.get("flag"), flagPos, _flag._size.mult(0.8));
			v.unrotate();
		}
		v.drawImage(Resource._gameImages.get("player1_back"), _pos, _size);
	}

	public void stop(){
		_destination = null;
		_vel = new Vector(0,0);
	}

	@Override
	public void update(){
		super.update();
		
		//flag-stun checking
		if(_isRooted && _isSilenced)
			dropFlag();
		
		//health and mana regeneration
		changeHealth(0.125);
		changeMana(0.125);
		
		Vector center = _pos.plus(_size.div(2.0));
		if (_spellCastingTime > 0) _spellCastingTime--;
		else if (_spellToCast != null){
			_data.finishCastingSpell(_spellToCast);
			_spellToCast = null;
		}
		if (_destination != null){
			if (_spellCastingTime > 0) _vel = new Vector(0, 0);
			else {
				_vel = (_destination.minus(center)).normalize().mult(_velocity);
				if (_destination.minus(center).mag() < 0.5){
					stop();
				} else if (_destination.minus(center).mag() < _velocity){
					_vel = _destination.minus(center);
				}
			}
		}
	}


	private void decrementMana(Spell spell) {
		//Change the mechanics of this to make it decrease
		//exponentially with quick successions of spells
		Long specificTimeLastCast = _spellCastingTimes.get(spell._name);
		if (specificTimeLastCast == null) specificTimeLastCast = (long) 0;
		
		double multiplier = 1;
		if (System.currentTimeMillis() - _timeLastCast < 1000) {
			multiplier += 0.25;
		}
		
		long score = (long) (System.currentTimeMillis() - specificTimeLastCast - spell._cooldown);
		if (score > 0 && score < 1000) {
			multiplier += 2;
		}
		
		changeMana((-1 * multiplier) * spell._manaCost);
	}

	public void castSpell(Spell spell) {
		_spellToCast = spell;
		_spellCastingTime = spell._castingTime;
		decrementMana(spell);
		_timeLastCast = System.currentTimeMillis();
		//Need some way of finding out if a spell and unit have crossed paths
		//Spell.newSpell(_spells[spellIndex], this, pos, dir).hit(target);
	}

	
	public void dropFlag() {
		if (_flag == null) {
			return; //nothing happens
		} else {
			double newx = 50 - Math.random()*101;
			double newy = 50 - Math.random()*101;
			Vector newpos = new Vector(this._pos.x + newx,this._pos.y + newy);
			_flag._pos = newpos;
			_flag._delete = false;
			_data._units.add(_flag);
			_flag = null;
		}
	}

	public void fear(long time) {
			timedEffects.add(new FearEffect(numberOfIntervals(time), this));		
	}

	@Override
	public void fromNet(String networkString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toNet() {
		// TODO Auto-generated method stub
		return null;
	}
}