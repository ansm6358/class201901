package shape;

public class Rectangle extends Shape {
	private java.awt.Rectangle rectangle; //������ ������ ���� ������ ���ٱ��� �ּҸ� ������� �Ѵ�.
	
	public Rectangle() {
		super();
		this.shape = new java.awt.Rectangle();
		this.rectangle = (java.awt.Rectangle)this.shape;
	}
		
	public void setOrigin(int x, int y) {
		this.rectangle.setBounds(x,y,0,0);
		
	}
	public void setPoint(int x, int y) {
		this.rectangle.setSize(x-this.rectangle.x, y-this.rectangle.y);

	}
	public void addPoint(int x, int y) {
	}
	
}