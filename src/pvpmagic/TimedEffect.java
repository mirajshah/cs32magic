package pvpmagic;

import java.util.HashMap;

public abstract class TimedEffect {
	Boolean _effectCompleted = false;
	Boolean _toBeCleansed = false;
	
	Double _numberOfIntervals;
	Double _changePerInterval = null;
	
	Player _caster;
	Unit _target;

	public String _type;
	public boolean _display = true;
	
	public abstract void effect();
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Unit caster, Unit target) {
		return newTimedEffect(type, numberOfIntervals, target, null);
	}
	
	public static TimedEffect newTimedEffect(String type, Double numberOfIntervals,
			Player caster, Unit target, Double changePerInterval){
		if (type == null || target == null){
			System.out.println("ERROR: newTimedEffect given a null argument!");
			return null;
		}
		if (caster == null) System.err.println("NULL CASTER!");

		if (type.equals(ConfuseEffect.TYPE)){ 
			return new ConfuseEffect(numberOfIntervals, caster, (Player) target); 
		} else if (type.equals(HealthBurnEffect.TYPE)) {
			return new HealthBurnEffect(numberOfIntervals, changePerInterval, caster, target); 
		} else if (type.equals(HealthBoostEffect.TYPE)) {
			return new HealthBoostEffect(numberOfIntervals, changePerInterval, caster, target); 
		} else if (type.equals(HideEffect.TYPE)) { 
			return new HideEffect(numberOfIntervals, caster, (Player) target); 
		} else if (type.equals(ManaBurnEffect.TYPE)) {
			return new ManaBurnEffect(numberOfIntervals, changePerInterval, caster, target); 
		} else if (type.equals(ManaBoostEffect.TYPE)) {
			return new ManaBoostEffect(numberOfIntervals, changePerInterval, caster, target); 
		} else if (type.equals(RootEffect.TYPE)) { 
			return new RootEffect(numberOfIntervals, caster, target); 
		} else if (type.equals(SilenceEffect.TYPE)) {
			return new SilenceEffect(numberOfIntervals, caster, target); 
		}

		System.out.println("Effect name \""+ type +"\" not found!");
		return null;
	}
	
	public String toNet() {
		String casterID = (_caster == null) ? "null" : Integer.toString(_caster._netID);
		return _type + 
				"," + _effectCompleted +
				"," + _numberOfIntervals +
				"," + _changePerInterval +
				"," +  casterID +
				"," + _target._netID;
	}
	
	public static TimedEffect fromNet(String effectNetString, HashMap<Integer, Unit> objectMap) {
		String[] args = effectNetString.split(",");
		Player caster;
		if (Boolean.parseBoolean(args[1])) {
			if (!args[4].equals("null")) {
				caster = null;
			} else {
				caster = (Player) objectMap.get(Integer.parseInt(args[4]));
			}
			Unit target = objectMap.get(Integer.parseInt(args[5]));
			if (args[3].equals("null")) {
				return newTimedEffect(args[0], Double.parseDouble(args[2]), caster, target);
			} else {
				return newTimedEffect(args[0], Double.parseDouble(args[2]), caster, target, 
						Double.parseDouble(args[4]));
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return toNet();
	}
}
