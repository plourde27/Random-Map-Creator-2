import java.awt.event.*;
public class MouseWheel implements MouseWheelListener{
    int level;
    
    public MouseWheel(){
        level = 0;
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
        int nc = e.getWheelRotation();
        level += nc;
    }
}