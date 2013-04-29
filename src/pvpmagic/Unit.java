package pvpmagic;


public abstract class Unit {
	protected double _health = 100;
	protected double _mana;
	
	protected GameData _data;
	
	public Vector _pos;
	protected Vector _size;
	protected Vector _vel = new Vector(0,0), _force = new Vector(0,0);
	boolean _movable = true;
	
	public void applyForce(Vector force){
		if (_movable) _force = _force.plus(force);
	}
	public void applyForce(float _x, float _y){
		if (_movable) _force = _force.plus(new Vector(_x, _y));
	}

	boolean _collidable = true;
	double _restitution = 0.5;
	double _mass = 1;
	
	private String _type;
	public Unit(GameData data, String type){ _data = data; _type = type; }
	public String type(){ return _type; }
	
	protected boolean _canBeStunned = false;
	protected double _stunnedTime = 0;
	public void stun(double time){ if (_canBeStunned) _stunnedTime = time; }
	
	boolean _canBeRooted = false;
	double _rootedTime = 0;
	public void root(double time){ if (_canBeRooted) _rootedTime = time; }

	boolean _canBeSilenced = false;
	double _silencedTime = 0;
	public void silence(double time){ if (_canBeSilenced) _silencedTime = time; }

	boolean _delete = false;
	
	protected Shape _shape;
	
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._movable){
			u._pos = u._pos.plus(c.mtv(u).div(2));
		}
	}
	
	public void update(){
	}
	
	public void draw(View v){}
}
