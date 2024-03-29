package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;


public class FlagPedestal extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "FlagPedestal";
	Flag _flag = null;
	
	public FlagPedestal(GameData data, Vector pos, double size, String basicImage){
		super(data, TYPE, STATICOBJ, basicImage);
		_pos = pos;
		Image sprite = Resource.get("flagPedestal");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y*3/4));
		points.add(new Vector(_size.x/2, 0));
		points.add(new Vector(_size.x, _size.y*3/4));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		v.drawImage(Resource.get(_basicImage), _pos, _size);
	}

	@Override
	public void changeHealth(double health, Player caster){}
	
	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._type.equals(Player.TYPE)) {
			Player p = (Player) u;
			if (p._flag != null){
				_flag = p._flag;
				p._flag = null;
			}
		}
	}	
	
	@Override
	public String toNet() {
		String flagID = (_flag == null) ? null : Integer.toString(_flag._netID);
		return _netID +
				"\t" + (_staticObj ? "static" : _type) + 
				"\t" + flagID +
				"\t" + _basicImage;
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type) 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 4) {
			if (!networkString[2].equals("null")){
				_flag = (Flag) objectMap.get(Integer.parseInt(networkString[2]));
			} else {
				_flag = null;
			}
			_basicImage = networkString[3];
		}
	}
}