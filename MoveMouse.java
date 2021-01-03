import java.awt.event.*;
public class MoveMouse implements MouseMotionListener {
    int x, y;
    int dx = 0;
    int dy = 0;
    int ct = 0;
    boolean dragging = false;
    boolean first = true;
    
    public MoveMouse() {
        x = 0;
        y = 0;
    }
    
    public void mouseMoved(MouseEvent e) {
        
        ct++;
        
        x = e.getX();
        y = e.getY() - 24;
        
    }
    
    public void mouseDragged(MouseEvent e) {
        int ox = x;
        int oy = y;
        dragging = true;
        x = e.getX();
        y = e.getY() - 24;
    }
}