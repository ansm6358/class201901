package shape;

import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Shape {
	protected java.awt.Shape shape;
	
	abstract public void setOrigin(int x, int y);
	abstract public void setPoint(int x, int y);
	abstract public void addPoint(int x, int y);
	
	public Shape clone() {
		try {
			return (Shape)this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();			
		}return null;
	}
	public void draw(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D)graphics;
		graphics2d.draw(this.shape);
	}
	public boolean contains(int x, int y) {
		return this.shape.contains(x,y);
	}
}
