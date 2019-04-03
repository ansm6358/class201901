package drawingPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import global.Constants.EToolBar;
import shape.Shape;

public class DrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private enum EActionState {eReady, e2PDrawing, eNPDrawing}; //cmc를 n포인트 드로일으로 pdr을 two포인트 드로일으로 바꿔진행 
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
		
		this.setBackground(Color.WHITE);
		
		this.mouseHandler = new MouseHandler();
		this.addMouseListener(this.mouseHandler);
		this.addMouseMotionListener(this.mouseHandler);
		
		this.shapeVector = new Vector<Shape>();
		this.currentTool = EToolBar.rectangle.getShape(); //버튼 순서를 바꾸면 설렉트를 부르는데 버튼이 다른것이 눌려 있다
	}
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		for(Shape shape: this.shapeVector) {		
			shape.draw(graphics);
		}
	}
	private void drawShape() {		
		Graphics graphics = this.getGraphics();
		graphics.setXORMode(getBackground());
		this.currentShape.draw(graphics);
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
				initDrawing(e.getX(),e.getY());
				eActionState = EActionState.e2PDrawing;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(eActionState == EActionState.e2PDrawing) {
				finishDrawing(e.getX(),e.getY());
				eActionState = EActionState.eReady;
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(eActionState == EActionState.e2PDrawing) {
				keepDrawing(e.getX(),e.getY()); 
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
