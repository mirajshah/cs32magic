package pvpmagic;

import java.awt.Color;

import pvpmagic.spells.Spell;

public class Player extends Unit {
	double _health;
	double _mana;

	String _characterName;
	String _playerName;

	Vector _destination;

	double _spellCastingTime = 0;
	Spell _spellToCast = null;

	String[] _spells;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 3;


	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, "player");
		_canBeStunned = true;
		_canBeRooted = true;
		_canBeSilenced = true;

		_health = 100;
		_mana = 100;

		_pos = new Vector(-50, -20);
		_size = new Vector(20, 20);

		_spells = spellNames;
		_characterName = characterName;
		_playerName = playerName;

	}

	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.blue);
		v.fillRect(_pos, _size);
	}

	public void stop(){
		_destination = null;
		_vel = new Vector(0,0);
	}

	@Override
	public void update(){
		super.update();
		if (_spellCastingTime > 0) _spellCastingTime--;
		else if (_spellToCast != null){
			_data.finishCastingSpell(_spellToCast);
			_spellToCast = null;
		}
		if (_destination != null){
			if (_spellCastingTime > 0) _vel = new Vector(0, 0);
			else {
				_vel = (_destination.minus(_pos)).normalize().mult(_velocity);
				if (_destination.minus(_pos).mag() < 0.5){
					stop();
				} else if (_destination.minus(_pos).mag() < _velocity){
					_vel = _destination.minus(_pos);
				}
			}
		}
	}


	private void decrementMana() {
		//Change the mechanics of this to make it decrease
		//exponentially with quick successions of spells
		_mana = _mana - 10;
		if (_mana < 0) _mana = 0;
	}

	public void castSpell(Spell spell) {
		_spellToCast = spell;
		_spellCastingTime = spell._castingTime;
		decrementMana();
		_timeLastCast = System.currentTimeMillis();
		//Need some way of finding out if a spell and unit have crossed paths
		//Spell.newSpell(_spells[spellIndex], this, pos, dir).hit(target);
	}
}