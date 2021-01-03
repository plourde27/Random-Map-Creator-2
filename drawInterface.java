import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.Math.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.lang.Math.*;

public class drawInterface extends JComponent {
    
    private int tx = 0;
    private int ty = 0;
    double scl = 1.0;
    int tsz = 20;
    int ang = 0;
    int[] coords = new int[]{0, 0, 1080, 720};
    ArrayList<Double> xs = new ArrayList<Double>();
    ArrayList<Double> ys = new ArrayList<Double>();
    
    
    public drawInterface() {
        /*tx = 0;
        ty = 0;
        scl = 1.0;*/
    }
    
    public void repaint() {
        super.repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    public void translate(int x, int y) {
        tx += x;
        ty += y;
    }
    
    public void scale(double s) {
        //scl = 1;
        scl = (scl * s);
    }
    
    public int[] transform(int x, int y, int tx, int ty) {
        int xx = (int) ((x - tx) * scl);
        int yy = (int) ((y - ty) * scl);
        return new int[]{xx, yy};
    }
    
    public int[] transform(int x, int y, int tx, int ty, double scl) {
        int xx = (int) ((x - tx) * scl);
        int yy = (int) ((y - ty) * scl);
        return new int[]{xx, yy};
    }
    
    public int[] transform(int x, int y, int lx, int rx, int ly, int ry) {
        int xx = (int) ((x - lx) * (((double)(1400)) / (rx - lx)));
        int yy = (int) ((y - ly) * (((double)(840)) / (ry - ly)));

        return new int[]{xx, yy};
    }
    
    public double[] transform(double x, double y, double lx, double rx, double ly, double ry) {
        double xx = ((x - lx) * (((double)(1400)) / (rx - lx)));
        double yy = ((y - ly) * (((double)(840)) / (ry - ly)));

        return new double[]{xx, yy};
    }
    
    public void rect(int x, int y, int w, int h, Graphics g, int lx, int rx, int ly, int ry) {
        int ox = x;
        int oy = y;
        x = transform(ox, oy, lx, rx, ly, ry)[0];
        y = transform(ox, oy, lx, rx, ly, ry)[1];
        g.fillRect(x - w / 2, y - h / 2, (int) (w * ((double)(rx-lx))/1400), (int) (h * ((double)(ry-ly))/840));
    }
    
    public void ellipse(double x, double y, int w, int h, Graphics g, int lx, int rx, int ly, int ry) {
        x = transform(x, y, lx, rx, ly, ry)[0];
        y = transform(x, y, lx, rx, ly, ry)[1];
        g.fillOval((int)x - (int) (w * (700.0/(rx-lx))), (int)y - (int) (h * (700.0/(rx-lx))), (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));
        //g.fillOval((int)x, (int)y, (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));

    }
    
    public void ellipse(int x, int y, int w, int h, Graphics g, int lx, int rx, int ly, int ry) {
        x = transform(x, y, lx, rx, ly, ry)[0];
        y = transform(x, y, lx, rx, ly, ry)[1];
        g.fillOval((int)x - (int) (w * (700.0/(rx-lx))), (int)y - (int) (h * (700.0/(rx-lx))), (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));
        //g.fillOval((int)x, (int)y, (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));

    }
    
    public void ellipseOutline(double x, double y, int w, int h, Graphics g, int lx, int rx, int ly, int ry) {
        x = transform(x, y, lx, rx, ly, ry)[0];
        y = transform(x, y, lx, rx, ly, ry)[1];
        g.drawOval((int)x - (int) (w * (700.0/(rx-lx))), (int)y - (int) (h * (700.0/(rx-lx))), (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));
        //g.fillOval((int)x, (int)y, (int) (w * (1400.0/(rx-lx))), (int) (h * (1400.0/(rx-lx))));

    }
    
    public void strokeWeight(int sw, Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(sw));
    }
    
    public void fill(Color c, Graphics gr) {
        gr.setColor(c);
    }
    
    public void fill(int r, int g, int b, Graphics gr) {
        gr.setColor(new Color(r, g, b));
    }
    
    public void fill(int r, int g, int b, int a, Graphics gr) {
        gr.setColor(new Color(r, g, b, a));
    }
    
    public void textSize(int sz, Graphics g) {
        g.setFont(new Font("Avenir", Font.PLAIN, sz));
    }
    
    public void textFont(String f, int sz, Graphics g) {
        tsz = sz;
        g.setFont(new Font(f, Font.PLAIN, sz));
    }
    
    public void textSize(int sz, Graphics g, String f) {
        tsz = sz;
        g.setFont(new Font(f, Font.PLAIN, sz));
    }
    
    public void text(String txt, int x, int y, Graphics g, int tx, int ty) {
        x = transform(x, y, tx, ty)[0];
        y = transform(x, y, tx, ty)[1];
        g.drawString(txt, x, y);
    }
    
    public void ntext(String txt, int x, int y, Graphics g, int tx, int ty) {
        int lns = 0;
        String ntxt = "";
        for (int i = 0 ; i < txt.length() ; i++) {
            
            if (txt.substring(i, i+1).equals("\\")) {
                i++;
                lns ++;
                ntxt = "";
                
            }
            else {
                ntxt += txt.charAt(i);
            }
        }
        
        double TFAC = 0.314;
        double YFAC = 1.1;
        
        //y -= 120;
        y -= (int)((lns / 2.0) * tsz * (YFAC));
        y += (int)(tsz * (0.32));
        
        
        ntxt = "";
        for (int i = 0 ; i < txt.length() ; i++) {
            
            if (txt.substring(i, i+1).equals("\\")) {
                i++;
                text(ntxt, (int)(x - (ntxt.length() - 2) * (tsz * TFAC)), y, g, tx, ty);
                ntxt = "";
                y += (int) (tsz * YFAC);
            }
            else {
                ntxt += txt.charAt(i);
            }
        }
        text(ntxt, (int)(x - ntxt.length() * (tsz * TFAC)), y, g, tx, ty);
        /*fill(0, 0, 0, 50, g);
        int cs = ntxt.length();
        rect(x, y,(int)( cs * (tsz * TFAC) * 2), 20, g, tx, ty);
        int st = (int)(x - cs * (tsz * TFAC));
        for (int i = 0 ; i < cs ; i++) {
             st += (tsz * TFAC * 2);
            fill(255, 0, 0, g);
            line(st, y - 10, st, y + 10, g, tx, ty);
           
        }*/
    }
    
    public void line(int x1, int y1, int x2, int y2, Graphics g, int lx, int rx, int ly, int ry) {
        int ox1 = x1;
        int oy1 = y1;
        int ox2 = x2;
        int oy2 = y2;
        x1 = transform(ox1, oy1, lx, rx, ly, ry)[0];
        y1 = transform(ox1, oy1, lx, rx, ly, ry)[1];
        x2 = transform(ox2, oy2, lx, rx, ly, ry)[0];
        y2 = transform(ox2, oy2, lx, rx, ly, ry)[1];
        g.drawLine(x1, y1, x2, y2);
    }
    
    public void line(double x1, double y1, double x2, double y2, Graphics g, int lx, int rx, int ly, int ry) {
        double ox1 = x1;
        double oy1 = y1;
        double ox2 = x2;
        double oy2 = y2;
        x1 = transform(ox1, oy1, lx, rx, ly, ry)[0];
        y1 = transform(ox1, oy1, lx, rx, ly, ry)[1];
        x2 = transform(ox2, oy2, lx, rx, ly, ry)[0];
        y2 = transform(ox2, oy2, lx, rx, ly, ry)[1];
        g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }
    
    public void vertex(double x, double y) {
        xs.add(x);
        ys.add(y);
    }
    
    public void endShape(Graphics g, int lx, int rx, int ly, int ry) {
        int[] x = new int[xs.size()];
        int[] y = new int[ys.size()];
        for (int i = 0 ; i < xs.size() ; i++) {
            x[i] = (int)transform(xs.get(i), ys.get(i), lx, rx, ly, ry)[0];
            y[i] = (int)transform(xs.get(i), ys.get(i), lx, rx, ly, ry)[1];
        }
        g.fillPolygon(new Polygon(x, y, xs.size()));
        xs = new ArrayList<Double>();
        ys = new ArrayList<Double>();
    }
    
    public void endShapeOutline(Graphics g, int lx, int rx, int ly, int ry) {
        int[] x = new int[xs.size()];
        int[] y = new int[ys.size()];
        for (int i = 0 ; i < xs.size() ; i++) {
            x[i] = (int)transform(xs.get(i), ys.get(i), lx, rx, ly, ry)[0];
            y[i] = (int)transform(xs.get(i), ys.get(i), lx, rx, ly, ry)[1];
        }
        g.drawPolygon(new Polygon(x, y, xs.size()));
        xs = new ArrayList<Double>();
        ys = new ArrayList<Double>();
    }
    
    public void resetMatrix() {
        tx = 0;
        ty = 0;
        scl = 1.0;
    }
    
    public int[] circleIntersectsLine(int x, int y, int r, int x1, int y1, int x2, int y2) {
        
        double m1 = (y2 - y1) / ((double)(x2 - x1));
        double b1 = y1 - m1 * x1;
        double m2 = -1.0 / m1;
        double b2 = y - m2 * x;
        double ix = (b2 - b1) / (m1 - m2);
        double iy = ix * m2 + b2;
        if (!(Math.sqrt((ix-x)*(ix-x)+(iy-y)*(iy-y)) <= r / 2 && ix >= Math.min(x1, x2) && ix <= Math.max(x1, x2) && iy >= Math.min(y1, y2) && iy <= Math.max(y1, y2))) {
            return new int[]{-1, -1};
        }
        else {
            int ang = (int) (Math.atan(((double)(iy - y)) / (ix - x)) * (180.0 / Math.PI));
            if (x < ix) {
                ang += 180;
            }
            int ds = r / 2;
            return new int[]{(int)ix + (int) (Math.cos(ang * (Math.PI / 180.0)) * (ds)), (int)iy + (int) (Math.sin(ang * (Math.PI / 180.0)) * (ds))};
        }
    }
    
    public double cos(int ang) {
        return Math.cos(ang * (Math.PI / 180.0));
    }
    
    public double sin(int ang) {
        return Math.sin(ang * (Math.PI / 180.0));
    }
    
    public double cos(double ang) {
        return Math.cos(ang * (Math.PI / 180.0));
    }
    
    public double sin(double ang) {
        return Math.sin(ang * (Math.PI / 180.0));
    }
    
    public double atan(double x1, double y1, double x2, double y2) {
        double val = Math.atan((y2 - y1) / (x2 - x1 + 0.00001)) * (180 / Math.PI);
        if (x2 < x1) {
            val += 180;
        }
        return val%360;
    }
    
    public double atan(int x1, int y1, int x2, int y2) {
        double val = Math.atan((y2 - y1) / (x2 - x1 + 0.00001)) * (180 / Math.PI);
        if (x2 < x1) {
            val += 180;
        }
        return val%360;
    }
    
    public double tan(double ang) {
        return sin(ang) / cos(ang);
    }
    
    public double dist(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    
    public double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
}