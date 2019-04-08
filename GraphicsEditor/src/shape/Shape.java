package shape;

import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Shape implements Cloneable{
	
	protected java.awt.Shape shape;
	protected int px;
	protected int py;
	
	abstract public void setOrigin(int x, int y);
	abstract public void setPoint(int x, int y);
	abstract public void addPoint(int x, int y);
	
	public void initMoving(int x, int y) {
		this.px = x;
		this.py = y;
	}
	public abstract void keepMoving(int x, int y);
	public abstract void finishMoving(int x, int y);
	
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
