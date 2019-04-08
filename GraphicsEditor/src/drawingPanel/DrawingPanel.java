package drawingPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import global.Constants.EToolBar;
import shape.Shape;
import shape.Polygon;;

public class DrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private enum EActionState {eReady, e2PDrawing, eNPDrawing, eMoving, eResizing,eRotating}; //cmc를 n포인트 드로일으로 pdr을 two포인트 드로일으로 바꿔진행 
	private EActionState eActionState; //그리는 방식이 같은 것끼리 도형을 분리 ***이 때 무브와 드로잉은 구분해야한다.	
	private MouseHandler mouseHandler;
	
	private Vector<Shape> shapeVector;
	private Shape currentShape;
	
	private Shape currentTool;
	public void setCurrentTool(EToolBar currentTool) {
		this.currentTool = currentTool.getShape();
	}
	public DrawingPanel() {
		this.eActionState = EActionState.eReady;
		
		this.setForeground(Color.black);
		this.setBackground(Color.WHITE);
		
		this.mouseHandler = new MouseHandler();
		this.addMouseListener(this.mouseHandler);
		this.addMouseMotionListener(this.mouseHandler);
		
		this.shapeVector = new Vector<Shape>();

	}
	public void initialize() {
		
	}
	public void paint(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D)graphics;
		super.paint(graphics2d);
		
		for(Shape shape: this.shapeVector) {		
			shape.draw(graphics2d);
		}
	}
	private void drawShape() {		
		Graphics2D graphics2d = (Graphics2D)this.getGraphics();
		graphics2d.setXORMode(getBackground());
		this.currentShape.draw(graphics2d);
	}
	
	private boolean onShape(int x, int y) {
		this.currentShape = null;
		for(Shape shape: this.shapeVector) {
			if(shape.contains(x, y)) {
				this.currentShape = shape;
				return true;
			}
		}
		return false;
	}
	private void initDrawing(int x, int y) {
		this.currentShape = this.currentTool.clone();
		this.currentShape.setOrigin(x, y);
		this.drawShape();
	}
	
	private void keepDrawing(int x, int y) { 
		this.drawShape();
		this.currentShape.setPoint(x, y);
		this.drawShape();
	}
	private void continueDrawing(int x, int y) {
		this.currentShape.addPoint(x,y);

	}
	
	private void finishDrawing(int x, int y) {
		this.shapeVector.add(this.currentShape);
	}
	private void initMoving(int x, int y) {
		this.currentShape.initMoving(x, y);
	}
	
	private void keepMoving(int x, int y) { 
		this.drawShape();
		this.currentShape.keepMoving(x, y);
		this.drawShape();
	}
	
	private void finishMoving(int x, int y) {
		this.currentShape.finishMoving(x, y);
		
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 1) {
				mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				mouse2Clicked(e);
			}
		}
		private void mouse2Clicked(MouseEvent e) {	
			if(eActionState == EActionState.eNPDrawing) {
				finishDrawing(e.getX(),e.getY());
				eActionState = EActionState.eReady;
			}
		}
		private void mouse1Clicked(MouseEvent e) {
			if(eActionState == EActionState.eReady) {
				initDrawing(e.getX(),e.getY());
				eActionState = EActionState.eNPDrawing;
			} else if(eActionState == EActionState.eNPDrawing) {
				continueDrawing(e.getX(),e.getY());
			}
		} 
		
		@Override
		public void mouseMoved(MouseEvent e) {
			if (eActionState == EActionState.eNPDrawing) {
				keepDrawing(e.getX(),e.getY());
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if(eActionState == EActionState.eReady) {
				if(onShape(e.getX(), e.getY())) {
					initMoving(e.getX(), e.getY());
					eActionState = EActionState.eMoving;
				} else {
					if(! (currentTool instanceof Polygon)) {
					initDrawing(e.getX(),e.getY());
					eActionState = EActionState.e2PDrawing;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(eActionState == EActionState.e2PDrawing) {
				finishDrawing(e.getX(),e.getY());
				eActionState = EActionState.eReady;
			} else if(eActionState == EActionState.eMoving) {
				finishMoving(e.getX(),e.getY()); 
				eActionState = EActionState.eReady;
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(eActionState == EActionState.e2PDrawing) {
				keepDrawing(e.getX(),e.getY()); 
			} else if(eActionState == EActionState.eMoving) {
				keepMoving(e.getX(),e.getY()); 		
			}
		}
		
		
		
		@Override
		public void mouseEntered(MouseEvent e) {	
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}

}
