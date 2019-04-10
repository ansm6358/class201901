package shape;

public class GPolygon extends GShape {
	private java.awt.Polygon polygon;
	
	public GPolygon() {
		super();
		this.shape = new java.awt.Polygon();
		this.polygon = (java.awt.Polygon)this.shape;
	}
	public void setOrigin(int x, int y) {
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x, y);
	}
	public void setPoint(int x, int y) {
		this.polygon.xpoints[this.polygon.npoints-1] = x;
		this.polygon.ypoints[this.polygon.npoints-1] = y;
	}
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
	}

	public void keepMoving(int x, int y) {
		int dw = x - this.px;
		int dh = y - this.py;
		
		for(int i = 0; i<this.polygon.npoints; i++) {
			this.polygon.xpoints[i] += dw;
			this.polygon.ypoints[i] += dh;
		}
		
		this.px = x;
		this.py = y;
	}
	public void finishMoving(int x, int y) {
		java.awt.Polygon newPolygon = new java.awt.Polygon();
		for(int i = 0; i<this.polygon.npoints; i++) {
			newPolygon.addPoint(this.polygon.xpoints[i],this.polygon.ypoints[i]);
		}
		this.shape = newPolygon;
		this.polygon = (java.awt.Polygon)this.shape;

	}
}