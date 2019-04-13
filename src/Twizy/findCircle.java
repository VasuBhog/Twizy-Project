package Twizy;

import org.opencv.core.Point;

public class findCircle {
	
	
	public Point center;
	public int radius;
	
	public findCircle() {
		this.center = new Point();
		this.radius=0;
	}
	
	public findCircle(Point c, int r) {
		this.center=c;
		this.radius=r;
	}

	@Override
	public String toString() {
		return "findCircle [center=" + center + ", radius=" + radius + "]";
	}
	
	

}
