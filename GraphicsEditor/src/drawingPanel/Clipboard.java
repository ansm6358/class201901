package drawingPanel;

import java.util.Vector;

import shape.GShape;

public class Clipboard {
	private Vector<GShape> shapes;
	
	public Clipboard() {
		this.shapes = new Vector<GShape>();
	}
	public void setContents(Vector<GShape> shapes) {
		this.shapes.clear();
		this.shapes.addAll(shapes);
		
	}
	
	public Vector<GShape> getContents() {
		Vector<GShape> clonedshapes = new Vector<GShape>();
		for(GShape shape: this.shapes) {
			GShape clonedshape = shape.clone();
			clonedshapes.add(clonedshape);
		}
		return clonedshapes;
	}
}
