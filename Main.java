import java.util.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("");
        Game game = new Game();
        
        
        Mouse mouse = new Mouse();
        frame.addMouseListener(mouse);
        Keyboard keyboard = new Keyboard();
        frame.addKeyListener(keyboard);
        MoveMouse mm = new MoveMouse();
        frame.addMouseMotionListener(mm);
        MouseWheel mw = new MouseWheel();
        frame.addMouseWheelListener(mw);
        Display screen = new Display(game, mouse, keyboard, mm, mw, frame);
        frame.add(screen);
        
        frame.setBounds(0,0,1400,840);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Thread(game).start();
        new Thread(new frameRateUpdater(30,screen)).start();
    }
}