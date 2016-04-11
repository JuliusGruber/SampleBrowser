import java.awt.Canvas;
import java.awt.Window;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


import com.cycling74.max.MaxObject;


public class MouseWheel extends MaxObject{

	Canvas canvas = null;
	
	public MouseWheel() {
		canvas = new Canvas();
		canvas.setSize(1800, 600);
		canvas.setLocation(0, 0);
		canvas.addMouseWheelListener(new MousewheelListener () );
		
		canvas.setVisible(true);
	}


	
	
	
	private class MousewheelListener implements MouseWheelListener{
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			post("MouseWheelEvent: "+arg0.toString());
			
		}
	}
}
