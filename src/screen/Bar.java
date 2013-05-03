package screen;

import java.awt.Color;
import java.awt.Graphics2D;

import pvpmagic.Vector;
import pvpmagic.View;

public class Bar extends InterfaceElement {

	int angle = 0;

	public Color bgColor;

	public Color fullColor = new Color(0f, 0.4f, 0f);
	public Color midColor = new Color(0.6f, 0.6f, 0f);
	public Color lowColor = new Color(0.4f, 0.2f, 0f);
	public Color veryLowColor = new Color(0.3f, 0f, 0f);

	public double current;
	public double total;

	private double drawValue;

	public Bar(String _id, Vector _size, double _total){
		type = "bar";

		id = _id;
		w = _size.x;
		h = _size.y;

		total = _total;
		current = total;
	}

	@Override
	public void update(){

		animateBar();
		
		if (current < total/4){
			color = veryLowColor;
		} else if (current < total/3){
			color = lowColor;
		} else if (current < total/2){
			color = midColor;
		} else {
			color = fullColor;
		}
	}


	private void animateBar(){
		if (drawValue<current){
			drawValue++;
			//drawValue+=(drawValue-trueValue)/5;
			if (current-drawValue<1){
				drawValue = current;
			}
		}
		if (drawValue>current){
			//drawValue-=(drawValue-trueValue)/5;
			drawValue--;
			if (current-drawValue>-1){
				drawValue = current;
			}
		}
		if (drawValue < 0){
			drawValue = 0;
		}
	}
	public void setValue(double value){
		current = value;
	}

	public void setColorRange(Color full, Color mid, Color low, Color veryLow){
		if (full != null) fullColor = full;
		if (mid != null) midColor = mid;
		if (low != null) lowColor = low;
		if (veryLow != null) veryLowColor = veryLow;
	}


	@Override
	public void draw(Graphics2D g) {
		g.rotate(angle);
		if (bgColor != null){
			g.setColor(bgColor);
			g.fillRect((int)x, (int)y, (int)w, (int)h);
		}
		g.setColor(color);
		g.fillRect((int)x, (int)y, (int)(w*current/total), (int)h);
		g.setColor(Color.white);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
		g.rotate(angle);
	}

	public void draw(View v) {
		Vector pos = new Vector(x,y);
		Vector size = new Vector(w,h);
		if (bgColor != null){
			v.setColor(bgColor);
			v.fillRect(pos, size);
		}
		v.setColor(color);
		v.fillRect(pos, new Vector((float)(size.x*current/total), size.y));
		v.setColor(Color.white);
		v.drawRect(pos, size);
	}

}