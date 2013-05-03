package pvpmagic.spells;

import java.util.Arrays;
import java.util.HashMap;

import pvpmagic.*;

public abstract class Spell extends Unit {
	
	int time = 0;
	
	protected Player _caster;
	protected double _damage;
	public double _cooldown = 0;
	public double _manaCost = 0;
	
	public double _castingTime = 5;
	
	public String _name;
	
	double _velocity = 1;

	Vector _dir;
	

	public Spell(GameData data, String type, Player caster, Vector dir) {
		super(data, type);
		_caster = caster;
		_pos = caster._pos;
		setup(dir);
	}
	public Spell(GameData data, String type, Vector pos, Vector dir){
		super(data, type);
		_pos = pos;
		setup(dir);
	}
	public void setup(Vector dir){
		_dir = dir;
		setVelocity();
		_size = new Vector(10, 10);
		double radius = _size.mag()/2;
		_shape = new Circle(this, new Vector(0,0), radius);
	}
	
	public void setVelocity(){
		_vel = _dir.normalize().mult(_velocity);
	}
	public void setVelocity(double velocity){
		_velocity = velocity;
		_vel = _dir.normalize().mult(_velocity);
	}
	
	public static Spell newSpell(GameData data, String name, Player caster, Vector dir){
		if (name == null){
			System.out.println("Given a null name!");
			return null;
		}
		
		if (name.equals("Stun")){ return new StunSpell(data, caster, dir); }
		else if (name.equals("Disarm")) { return new DisarmSpell(data, caster, dir); }
		else if (name.equals("Burn")) { return new BurnSpell(data, caster, dir); }
		else if (name.equals("Root")) { return new RootSpell(data, caster, dir); }
		else if (name.equals("Blind")) { return new BlindSpell(data, caster, dir); }
		else if (name.equals("Push")) { return new PushSpell(data, caster, dir); }
		else if (name.equals("Abracadabra")) { return new AbracadabraSpell(data, caster, dir); }
		else if (name.equals("Open")) { return new OpenSpell(data, caster, dir); }
		else if (name.equals("Lock")) { return new LockSpell(data, caster, dir); }
		else if (name.equals("Fear")) { return new FearSpell(data, caster, dir); }
		System.out.println("Spell name \""+name+"\" not found!");
		return null;
	}
	
	@Override
	public void update(){
		super.update();
		time++;
	}
	
	@Override
	public boolean canCollideWith(Unit u){
		if (u == _caster && time < 10) return false;
		return true;
	}
	
	public static void fromNet(Integer netID, String networkString, HashMap<Integer, Unit> objectMap, GameData data) {
		String[] netSplit = networkString.split("\t");
		Player caster; Spell spell;
		if ((caster = (Player) objectMap.get(Integer.parseInt(netSplit[1]))) == null) {
			throw new RuntimeException("ERROR: Player " + netSplit[1] + 
					" does not exist in objectMap. This should not happen.");
		} else {
			Vector dir = Vector.fromNet(netSplit[5]);
			Vector pos = Vector.fromNet(netSplit[7]);
			if ((spell = (Spell) objectMap.get(netID)) == null) {
				spell = newSpell(data, netSplit[1], caster, dir);
				spell._pos = pos;
			} else {
				spell._caster = caster;
				spell._dir = dir;
				spell._pos = pos;
			}
		}
	}
	
	public String toNet() {
		return "n\t" + _name + "\tc\t" + _caster._netID + "\td\t" + _dir.toNet() + "\tp\t" + _pos.toNet();
	}
	
	public Boolean validNetworkString(String[] networkData) {
		if (networkData.length != 8 || !(networkData[0].equals("n") 
				&& networkData[2].equals("c") && networkData[4].equals("d")
				&& networkData[6].equals("p"))) {
			System.err.println("ERROR: Invalid String from network - " + Arrays.toString(networkData));
			return false;
		} else {
			return true;
		}
	}
}
