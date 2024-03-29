package pvpmagic;

public class ConfuseEffect extends TimedEffect {
	public static String TYPE = "ConfuseEffect";
	
	public ConfuseEffect(double numberOfIntervals, Player caster, Player target) {
		_numberOfIntervals = numberOfIntervals;
		_target = target;
		_type = TYPE;
		_toBeCleansed = true;
	}

	@Override
	public void effect() {
		_effectCompleted = false;
		if (_numberOfIntervals > 0) {
			_target._isSilenced = true;
			_target._isRooted = true;
			if (_numberOfIntervals % 20 == 0) {
				double xOffset = -200 + Math.random()*400 + 1;
				double yOffset = -200 + Math.random()*400 + 1;
				((Player) _target)._destination = 
						new Vector(_target._pos.x + xOffset, _target._pos.y + yOffset);
			}
			_numberOfIntervals -= 1;
		} else {
			_target._isSilenced = false;
			_target._isRooted = false;
			_effectCompleted = true;
			((Player) _target)._destination = null;
		}
	}

}
