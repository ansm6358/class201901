package main;

public class GMain {

	static private GMainFrame mainframe;
	
	public static void main(String[] args) {
		GMainFrame mainframe = new GMainFrame();
		mainframe.initialize();
		mainframe.setVisible(true);
		
	}

}
