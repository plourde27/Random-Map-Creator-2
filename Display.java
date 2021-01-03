import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.Math.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.lang.Math.*;
import java.applet.Applet;
import java.net.URL;
import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.net.URL;

public class Display extends drawInterface {
    
    
    
    Game game;
    Mouse mouse;
    MoveMouse mm;
    MouseWheel mw;
    Keyboard kb;
    Frame frame;
    Map mp;
    
    boolean start = false;
    double lx = 0;
    double rx = 1400;
    double ly = 0;
    double ry = 840;
    int time = 0;
    double scl = 1.0;
    double ol = 0;
    double cy = 0;
    double cx = 0;
    
    boolean first = true;
    
    int ox = 0;
    int oy = 0;
    
    int[][][] cols;
    
    public Display(Game g, Mouse m, Keyboard k, MoveMouse mmw, MouseWheel mww, Frame frm) {
        super();
        
        frame = frm;
        game = g;
        mouse = m;
        kb = k;
        mm = mmw;
        mw = mww;
        mp = new Map();
        
        
        
        
    }
    
    
    public void draw() {
        
        super.repaint();
        
    }
    
    public void zoom(int x, int y, double rat) {
        lx += ((x - lx) * (1.0 - rat));
        rx -= ((rx - x) * (1.0 - rat));
        ly += ((y - ly) * (1.0 - rat));
        ry -= ((ry - y) * (1.0 - rat));
    }
    
    
    public void paintComponent(Graphics g){
        
        int dx = mm.x - ox;
        int dy = mm.y - oy;
        
        if (mm.ct < 2) {
            dx = 0;
            dy = 0;
            first = false;
        }
        
        double rat = ((double)(rx - lx)) / 1400;
        //rat = 1.0 / rat;
        double olx = lx;
        
        if (mm.dragging) {
            lx -= dx * rat;
            rx -= dx * rat;
            ly -= dy * rat;
            ry -= dy * rat;
        }
        
        
        cx = (lx + (mm.x * ((double)(rx - lx)) / 1400));
        cy = (ly + (mm.y * ((double)(ry - ly)) / 840));
        
        
        
        zoom((int)cx, (int)cy, Math.pow(1.003, mw.level - ol));
        

        
        super.paintComponent(g);
        
        
        fill(0, 180, 250, g);
        rect(700, 420, 1400, 840, g, 0, 1400, 0, 840);
        
        mp.draw(this, g, (int)lx, (int)rx, (int)ly, (int)ry);
        
        time++;
        
        ol = mw.level;
        
        fill(0, 0, 0, g);
        
        /*for (int i = 0 ; i < 2000 ; i += 20) {
            line(i, 0, i, 2000, g, (int)lx, (int)rx, (int)ly, (int)ry);
            line(0, i, 2000, i, g, (int)lx, (int)rx, (int)ly, (int)ry);
        }*/
        
        //ellipse(mm.x, mm.y, 3, 3, g, 0, 1400, 0, 840);

        mm.dx = 0;
        mm.dy = 0;
        
        ox = mm.x;
        oy = mm.y;
        
        mm.dragging = false;
        mouse.clicked = false;
    }
}