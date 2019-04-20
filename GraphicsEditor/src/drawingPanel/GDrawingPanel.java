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
import shape.GShape;
import shape.GShape.EOnState;
import shape.GPolygon;;

public class GDrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private enum EActionState {
		eReady, eDrawing, eNPDrawing2, eMoving, eResizing, eRotating
	}; // cmc를 n포인트 드로일으로 pdr을 two포인트 드로일으로 바꿔진행

	private EActionState eActionState; // 그리는 방식이 같은 것끼리 도형을 분리 ***이 때 무브와 드로잉은 구분해야한다.
	private MouseHandler mouseHandler;

	private Vector<GShape> shapeVector;
	private GShape currentShape;

	private GShape currentTool;

	public void setCurrentTool(EToolBar currentTool) {
		this.currentTool = currentTool.getShape();
	}

	public GDrawingPanel() {
		this.eActionState = EActionState.eReady;

		this.setForeground(Color.black);
		this.setBackground(Color.WHITE);

		this.mouseHandler = new MouseHandler();
		this.addMouseListener(this.mouseHandler);
		this.addMouseMotionListener(this.mouseHandler);

		this.shapeVector = new Vector<GShape>();

	}

	public void initialize() {

	}

	public void paint(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D) graphics;
		super.paint(graphics2d);

		for (GShape shape : this.shapeVector) {
			shape.draw(graphics2d);
		}
	}

	private void drawShape() {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(getBackground());
		this.currentShape.draw(graphics2d);
	}

	private EOnState onShape(int x, int y) {
		this.currentShape = null;
		for (GShape shape : this.shapeVector) {
			EOnState eOnState = shape.onShape(x, y);
			if (eOnState != null) {
				this.currentShape = shape;
				return eOnState;
			}
		}
		return null;
	}

	private void defineActionState(int x, int y) {
		EOnState eOnState = onShape(x, y);
		if (eOnState == null) {
			eActionState = EActionState.eDrawing; // this: 사이드 이펙트로 통신해서 이 지역 변수와 통신 return은 그 자리에서만 사용하라는 것
		} else {
			switch (eOnState) {
			case eOnShape:
				this.eActionState = EActionState.eMoving;
				break;
			case eOnResize:
				this.eActionState = EActionState.eResizing;
				break;
			case eOnRotate:
				this.eActionState = EActionState.eRotating;
				break;
			default:
				// exception
				this.eActionState = null;
				break;
			}
		}
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
		this.currentShape.addPoint(x, y);

	}

	private void finishDrawing(int x, int y) {
		this.shapeVector.add(this.currentShape);
	}

	private void initMoving(int x, int y) {
		// this.currentShape.setSelected(true);
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(getBackground());
		this.currentShape.initMoving(graphics2d, x, y);

	}

	private void keepMoving(int x, int y) {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(getBackground());
		this.currentShape.draw(graphics2d);
		this.currentShape.keepMoving(x, y);
		this.currentShape.draw(graphics2d);
	}

	private void finishMoving(int x, int y) {
		this.currentShape.finishMoving(x, y);

	}

	private void initResizing(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void initRotating(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void keepResizing(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void keepRotating(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void finishResizing(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void finishRotating(int x, int y) {
		// TODO Auto-generated method stub

	}

	private class MouseHandler implements MouseListener, MouseMotionListener { //state transition mapping
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				mouse2Clicked(e);
			}
		}

		private void mouse2Clicked(MouseEvent e) {
			if (eActionState == EActionState.eDrawing) {
				if (currentTool instanceof GPolygon) {
					finishDrawing(e.getX(), e.getY());
					eActionState = EActionState.eReady;
				}
			}
		}

		private void mouse1Clicked(MouseEvent e) {
			if (eActionState == EActionState.eReady) {
				if (currentTool instanceof GPolygon) {
					initDrawing(e.getX(), e.getY());
					eActionState = EActionState.eDrawing;
				}
			} else if (eActionState == EActionState.eDrawing) {
				if (currentTool instanceof GPolygon) {
					continueDrawing(e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eActionState == EActionState.eDrawing) {
				if (currentTool instanceof GPolygon) {
					keepDrawing(e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (eActionState == EActionState.eReady) {
				defineActionState(e.getX(), e.getY());
				if (eActionState == EActionState.eDrawing) {
					if (!(currentTool instanceof GPolygon)) {
						initDrawing(e.getX(), e.getY());
					} else {
						eActionState = EActionState.eReady;
					}
				} else if (eActionState == EActionState.eMoving) { // 앵커는 없는데 밑에 그림이 있는 경우
					initMoving(e.getX(), e.getY());
				} else if (eActionState == EActionState.eResizing) {
					initResizing(e.getX(), e.getY());

				} else if (eActionState == EActionState.eRotating) {
					initRotating(e.getX(), e.getY());

				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (eActionState == EActionState.eDrawing) {
				if (!(currentTool instanceof GPolygon)) {
					finishDrawing(e.getX(), e.getY());
					eActionState = EActionState.eReady;
				}
			} else if (eActionState == EActionState.eMoving) {
				finishMoving(e.getX(), e.getY());
				eActionState = EActionState.eReady;
			} else if (eActionState == EActionState.eResizing) {
				finishResizing(e.getX(), e.getY());

				eActionState = EActionState.eReady;

			} else if (eActionState == EActionState.eRotating) {
				finishRotating(e.getX(), e.getY());

				eActionState = EActionState.eReady;

			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (eActionState == EActionState.eDrawing) {
				if (!(currentTool instanceof GPolygon)) {
					keepDrawing(e.getX(), e.getY());
				}
			} else if (eActionState == EActionState.eMoving) {
				keepMoving(e.getX(), e.getY());
			} else if (eActionState == EActionState.eResizing) {
				keepResizing(e.getX(), e.getY());
			} else if (eActionState == EActionState.eRotating) {
				keepRotating(e.getX(), e.getY());
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
