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

public class Map extends drawInterface {
    
    //Goals:
    //10M+ x1
    //2M - 10M x2-3
    //500K - 2M x4-6
    //200K - 500K x15-20
    //100K - 200K x25-30
    
    int oldChange = 0;
    boolean doneCities = false;
    boolean cityeval = false;
    boolean found = false;
    boolean[] dp = new boolean[1005];
    boolean[] bp = new boolean[1005];
    int highwayCount = 0;
    int ra = 0;
    int[] goalX = new int[]{250, 370, 490, 610, 730, 850, 970, 1090};
    int[] goalY = new int[]{300, 450, 600};
    boolean[] usedX = new boolean[goalX.length];
    boolean[] usedY = new boolean[goalX.length];
    int xcount = goalX.length;
    int ycount = goalY.length;

    int[] low = new int[]{1, 2, 4, 10, 15};
    int[] high = new int[]{1, 3, 6, 15, 20};
    int[] lowbound = new int[]{(int)1e7, (int)(2*1e6), (int)(5*1e5), (int)(2*1e5), (int)(1e5)};
    int[] highbound = new int[]{(int)(15*1e6), (int)(10*1e6), (int)(20*1e5), (int)(5*1e5), (int)(2*1e5)};
    int[] count = new int[]{0, 0, 0, 0, 0};
    
    ArrayList<City> cities = new ArrayList<City>();
    ArrayList<Highway> highways = new ArrayList<Highway>();
    ArrayList<Road> roads = new ArrayList<Road>();
    
    double points[][] = new double[500005][2];
    double temppoints[][] = new double[500005][2];
    int direct[] = new int[1005];
    int bypass[] = new int[1005];
    
    int size = 0;
    int frame = 0;
    
    public Map() {
        
        System.out.println("starting");
        
        generateMap();
        
        System.out.println(size);
        
        for (int i = 0 ; i < 38 ; i++) {
            cities.add(new City(this));
        }
        /*for (int i = 0 ; i < size ; i += 100) {
            if (i % (size/2) <= size/4) continue;
            System.out.println(i);
            double x1 = points[i][0];
            double y1 = points[i][1];
            double x2 = points[(i+1)%size][0];
            double y2 = points[(i+1)%size][1];
            double ang = atan(x1, y1, x2, y2);
            double inx1 = x1 + cos(ang+90) * 5;
            double iny1 = y1 + sin(ang+90) * 5;
            double inx2 = x1 + cos(ang-90) * 5;
            double iny2 = y1 + sin(ang-90) * 5;
            if (inside((int)inx1, (int)iny1)) {
                highways.add(new Highway(this, (int)inx1, (int)iny1));
            }
            else {
                highways.add(new Highway(this, (int)inx2, (int) iny2));
            }
            
            if (highways.get(highways.size() - 1).dud) {
                highways.remove(highways.size() - 1);
            }
            
        }*/
    }
    
    public void addHighway() {
        /*int ct = 0;
        while (ct < 100){
            Highway h = new Highway(this);
            if (!h.dud) {
                highways.add(h);
                break;
            }
            ct++;
        }
        if (ct == 100 ){
        }*/
    }
    
    public void makeLine(double x1, double y1, double x2, double y2) {
        double ang = Math.atan((y2-y1)/(x2-x1+0.0001))*(180/Math.PI);
        if (x2 < x1) {
            ang += 180;
        }
        double jump = 19;
        /*if (ang == -1000) {
            ang = goalAng;
        }*/
        double dist = 1.2;
        double td = (Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)));
        
        if (td < dist) {
            points[size][0] = x2;
            points[size][1] = y2;
            size++;
            return;
        }
        int count = (int)(td / dist);
        
        for (int i = 0 ; i <= count/2 ; i++) {
            points[size][0] = x1;
            points[size][1] = y1;
            x1 += Math.cos(ang*(Math.PI/180))*dist;
            y1 += Math.sin(ang*(Math.PI/180))*dist;
            ang += Math.random()*(jump*dist/10.0) - (jump*dist/20.0);
            size++;
        }
        makeLine(x1, y1, x2, y2);
    }
    
    public void generateQuarter(int x1, int y1, int x2, int y2) {
        if (x1 >= x2) {
            makeLine(x1, y1, x2, y2);
            return;
        }
        double ang = Math.atan(((double)(y2 - y1)) / (x2 - x1)) * (180 / Math.PI);
        if (x2 < x1) {
            ang += 180;
        }
        
        ang = (ang - 45) * 2;
        //ang = 0;
        //ang = 36;
        int nx = (int)(x1+Math.cos(ang*(Math.PI/180))*180);
        int ny = (int)(y1+Math.sin(ang*(Math.PI/180))*180);
        makeLine(x1, y1, nx, ny);
        generateQuarter(nx, ny, x2, y2);
    }
    
    public void generateQuarter2(int x1, int y1, int x2, int y2) {
        int osize = size;
        generateQuarter(x1, y1, x2, y1 + (x2 - x1));
        for (int i = osize ; i < size ; i++) {
            points[i][1] = (y1 + (points[i][1] - y1) * (((double)y2 - y1) / (x2 - x1)));
        }
    }
    
    public void generateHalfCircle(int mx, int rx, int ly, int my, int ry) {
        
        
        
        generateQuarter2(mx, ly, rx, my);
        int osize = size;
        generateQuarter2(mx, ly, rx, ly + (ry - my));
        for (int i = osize ; i < size ; i++) {
            points[i][1] = ry - (points[i][1] - ly);
        }
        for (int i = osize ; i < size ; i++) {
            temppoints[osize + (size - osize) - (i - osize) - 1][0] = points[i][0];
            temppoints[osize + (size - osize) - (i - osize) - 1][1] = points[i][1];
        }
        for (int i = osize ; i < size ; i++) {
            points[i][0] = temppoints[i][0];
            points[i][1] = temppoints[i][1];
        }
        int osize2 = size;

    }
    
    public void makeBay(int l, int r, int sz) {
        int mx = 700;
        int my = 420;
        
        for (int i = l ; i < r ; i++) {
            double ang = Math.atan((points[i][1] - my) / ((double)points[i][0] - mx))*(180/Math.PI);
            if (points[i][0] < mx) {
                ang += 180;
            }
            double dist = Math.sqrt((points[i][1]-my)*(points[i][1]-my)+(points[i][0]-mx)*(points[i][0]-mx));
            double nd = 0;
            if (i < l + (r-l)/3) {
                nd = dist - (i-l) * (((double)sz) / ((l + (r-l)/3) - l));
            }
            else if (i > r - (r-l)/3) {
                nd = dist - (r-i) * (((double)sz) / ((l + (r-l)/3) - l));
            }
            else {
                nd = dist - sz;
            }
            //double nd = 100;
            points[i][0] = (mx + Math.cos(ang*(Math.PI/180))*nd);
            points[i][1] = (my + Math.sin(ang*(Math.PI/180))*nd);

        }
    }
    
    
    public void drawCircle() {
        int lx = 50 + (int)(Math.random()*100);
        int mx = 700;
        int rx = 1350 - (int)(Math.random()*100);
        
        int ly = 50 + (int)(Math.random()*70);
        int my = 420;
        int ry = 790 - (int)(Math.random()*70);
        
        generateHalfCircle(mx, rx, ly, my, ry);
        
        int osize = size;
        
        generateHalfCircle(mx, mx + (mx - lx), ly, my, ry);
        
        for (int i = osize ; i < size ; i++) {
            points[i][0] = mx - (points[i][0] - mx);
        }
        
        for (int i = osize ; i < size ; i++) {
            temppoints[osize + (size - osize) - (i - osize) - 1][0] = points[i][0];
            temppoints[osize + (size - osize) - (i - osize) - 1][1] = points[i][1];
        }
        for (int i = osize ; i < size ; i++) {
            points[i][0] = temppoints[i][0];
            points[i][1] = temppoints[i][1];
        }
        
        int i = 0;
        while (i < size) {
            if (Math.random() < 0.004) {
                int jump = (int)(Math.random()*400 + 200);
                int sz = (int)(Math.random()*60+20);
                if (i + jump >= size)  {
                    i++;
                    continue;
                }
                makeBay(i, i+jump, sz);
                i += jump;
            }
            i++;
        }
    }
    
    public void generateMap() {
        drawCircle();
    }
    
    public void draw(Display d, Graphics g, int lx, int rx, int ly, int ry) {
        
        ra++;
        int FC = 3;
        /*int FC = 12;
        if (highways.size() > 0 && ra % FC == 0 && highways.get(highways.size() - 1).dud) {
            highways.remove(highways.size() - 1);
        }
        if (ra % FC == 0) {
            highways.add(new Highway(this));
        }*/
        
        for (int i = 0 ; i < 1005 ; i++) {
            direct[i] = 0;
            bypass[i] = 0;
        }
        
        int oldSize = highways.size();
        if (ra % FC == 0) {
            int gX = -1;
            int gY = -1;
            boolean valid = true;
            if ((Math.random() <= 0.5 && xcount > 0) || (xcount > 0 && ycount == 0)) {
                int ind = 0;
                while (true) {
                    ind = (int)(Math.random() * goalX.length);
                    if (!usedX[ind]) {
                        usedX[ind] = true;
                        break;
                    }
                }
                gX = goalX[ind];
                xcount--;
                System.out.println("X " + ind);
            }
            else if (ycount > 0) {
                int ind = 0;
                while (true) {
                    ind = (int)(Math.random() * goalY.length);
                    if (!usedY[ind]) {
                        usedY[ind] = true;
                        break;
                    }
                }
                gY = goalY[ind];
                ycount--;
                System.out.println("Y " + ind);
            }
            else {
                valid = false;
            }
            
            if (valid) {
                int ct = 0;
                while (ct < 10000) {
                    ct++;
                    int i = (int)(Math.random()*size);
                    double x1 = points[i][0];
                    double y1 = points[i][1];
                    double x2 = points[(i+1)%size][0];
                    double y2 = points[(i+1)%size][1];
                    if (!((gX == -1 || Math.abs(x1 - gX) <= 20) && (gY == -1 || Math.abs(y1 - gY) <= 20)))  {
                        continue;
                    }
                    double ang = atan(x1, y1, x2, y2);
                    double inx1 = x1 + cos(ang+90) * 5;
                    double iny1 = y1 + sin(ang+90) * 5;
                    double inx2 = x1 + cos(ang-90) * 5;
                    double iny2 = y1 + sin(ang-90) * 5;
                    if (inside((int)inx1, (int)iny1)) {
                        highways.add(new Highway(this, (int)inx1, (int)iny1, false));
                    }
                    else {
                        highways.add(new Highway(this, (int)inx2, (int) iny2, false));
                    }
                    
                    if (highways.get(highways.size() - 1).dud) {
                        highways.remove(highways.size() - 1);
                    }
                    
                    break;
                }
            }
            else if (highways.size() < 19) {
                if (!cityeval) {
                    for (int i = 1 ; i <= 5 ; i++) {
                        int count = 0;
                        int dc = 0;
                        int bc = 0;
                        for (int j = 0 ; j < cities.size() ; j++) {
                            if (cities.get(j).group != i - 1) continue;
                            boolean ff = false;
                            boolean gg = false;
                            count++;
                            for (int k = 0 ; k < highways.size() ; k++) {
                                Highway hwy = highways.get(k);
                                for (int l = 0 ; l < hwy.size ; l++) {
                                    if (dist(hwy.points[l][0], hwy.points[l][1], cities.get(j).x, cities.get(j).y) <= 5) {
                                        ff = true;
                                    }
                                    if (dist(hwy.points[l][0], hwy.points[l][1], cities.get(j).x, cities.get(j).y) <= 30) {
                                        gg = true;
                                    }
                                }
                            }
                            
                            if (ff) { dc++; dp[j] = true; }
                            if (gg) { bc++; bp[j] = true; }
                        }
                        int pc1 = (int)((dc / ((double)count)) * 100);
                        int pc2 = (int)((bc / ((double)count)) * 100);
            
                        System.out.println("GROUP " + i + " CITIES:");
                        System.out.println("Direct Rate: " + pc1 + "% (" + dc + " / " + count + ")");
                        System.out.println("Bypass Rate: " + pc2 + "% (" + bc + " / " + count + ")");
                    }
                    cityeval = true;
                }
                boolean found = false;
                for (int i = 1 ; i <= 5 ; i++) {
                    for (int j = 0 ; j < cities.size() ; j++) {
                        if (cities.get(j).group != i - 1) continue;
                        if (!bp[j] || (i <= 1 && !dp[j])) {
                            
                            highways.add(new Highway(this, cities.get(j).x, cities.get(j).y, true));
                            if (highways.get(highways.size() - 1).dud) {
                                highways.remove(highways.size() - 1);
                            }
                            else {
                                dp[j] = true;
                                bp[j] = true;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
                    
            }
            

        }
        
        if (highways.size() != oldSize) {
            oldChange = ra;
        }
        
        if (!doneCities && ra - oldChange >= 30) {
            //System.out.println("CITIES!!!");
            for (int i = 0 ; i < cities.size() ; i++) {
                cities.get(i).makeRoads(this);
            }
            doneCities = true;
        }
        
        /*if (d.mouse.clicked) {
            if (highways.size() > 0 && highways.get(highways.size() - 1).dud) {
                highways.remove(highways.size() - 1);
            }
            highways.add(new Highway(this, d.mouse.x, d.mouse.y - 24));
        }*/

        
        /*frame++;
        if (frame%15 == 0) {
            size = 0;
            generateMap();
        }*/
        
        
        fill(0, 0, 0, g);
        strokeWeight(2, g);
        for (int i = 0 ; i < size ; i++) {
            vertex(points[i][0], points[i][1]);
        }
        endShapeOutline(g, lx, rx, ly, ry);
        fill(0, 0, 0, g);
        //ellipse(points[ra%size][0], points[ra%size][1], 25, 25, g, lx, rx, ly, ry);
        
        /*for (int i = 0 ; i < 1400 ; i += 100) {
            for (int j = 0 ; j < 840 ; j += 100) {
                if (inside(i, j)) {
                    fill(255, 0, 0, g);
                }
                else {
                    fill(0, 0, 0, g);
                }
                ellipse(i, j, 5, 5, g, lx, rx, ly, ry);
            }
        }*/
        fill(250, 240, 220, g);
        strokeWeight(0, g);
        for (int i = 0 ; i < size ; i++) {
            vertex(points[i][0], points[i][1]);
        }
        endShape(g, lx, rx, ly, ry);
        
        for (int i = 0 ; i < cities.size() ; i++) {
            
            fill(0, (4 - cities.get(i).group) * 30, 0, 20, g);
             if (direct[i] > 0 || bypass[i] > 0) {
                //fill(255, 125, 0, 20, g);
            }
            strokeWeight(0, g);
            ellipse(cities.get(i).x, cities.get(i).y, 5 * (5 - cities.get(i).group), 5 * (5 - cities.get(i).group), g, lx, rx, ly, ry);
            //fill
            fill(0, 255, 0, g);
           
            strokeWeight(1, g);
            //ellipseOutline(cities.get(i).x, cities.get(i).y, 8 * (5 - cities.get(i).group), 8 * (5 - cities.get(i).group), g, lx, rx, ly, ry);
        }
        
        int sz = cities.size();
        
        int[][] demand = new int[cities.size()][cities.size()];
        
        int[][] dist = new int[sz][sz];
        
        for (int i = 0 ; i < cities.size() ; i++) {
            for (int j = 0 ; j < cities.size() ; j++) {
                if (i == j) dist[i][j] = 0;
                else dist[i][j] = 10000;
            }
        }
        
        for (int i = 0 ; i < cities.size() ; i++) {
            for (int j = 0 ; j < cities.size() ; j++) {
                if (i==j) continue;
                int low = Math.min(cities.get(i).population, cities.get(j).population);
                int high = Math.max(cities.get(i).population, cities.get(j).population);

                int totPop = (int) (low + (high - low) / 2.0);
                
                double distance = Math.sqrt(Math.pow(cities.get(i).x-cities.get(j).x,2)+Math.pow(cities.get(i).y-cities.get(j).y,2));
                
                double dem = totPop / (distance * distance);
                
                demand[i][j] = (int)dem;
                dist[i][j] = (int)distance;
            }
        }
        
        for (int i = 0 ; i < cities.size() ; i++) {
            int mx = 0;
            int mxi = -1;
            for (int j = 0 ; j < cities.size() ; j++) {
                if (demand[i][j] >= mx && dist[i][j] <= 250) {
                    mx = demand[i][j];
                    mxi = j;
                }
            }
        
            fill(255, 0, 0, 100, g);
        
            //strokeWeight(10, g);
        
            //line(cities.get(i).x, cities.get(i).y, cities.get(mxi).x, cities.get(mxi).y, g, lx, rx, ly, ry);
        }
        
        fill(255, 0, 0, 150, g);
        
        strokeWeight(Math.max(2, (int)(0.1 * (1400.0 / (rx - lx)))), g);
        
        for (int i = 0 ; i < highways.size() ; i++) {
            Highway h = highways.get(i);
            
            for (int j = 0 ; j < h.size - 1 ; j++) {
                //fill(0, 0, 255, 10, g);
                //ellipseOutline(h.points[j][0], h.points[j][1], 120, 120, g, lx, rx, ly, ry);
                fill(150, 95, 0, g);
                
                double ang = atan(h.points[j][0], h.points[j][1], h.points[j+1][0], h.points[j+1][1]);
                
                //int rad = (int)(5 * (1400.0 / (rx - lx)));
                double rad = 0.06;
                line(h.points[j][0] + cos(ang + 90)*rad, h.points[j][1] + sin(ang + 90)*rad, h.points[j+1][0] + cos(ang + 90)*rad, h.points[j+1][1] + sin(ang + 90)*rad, g, lx, rx, ly, ry);
                line(h.points[j][0] + cos(ang - 90)*rad, h.points[j][1] + sin(ang - 90)*rad, h.points[j+1][0] + cos(ang - 90)*rad, h.points[j+1][1] + sin(ang - 90)*rad, g, lx, rx, ly, ry);

                //ellipse(h.points[j][0], h.points[j][1], 8, 8, g, lx, rx, ly, ry);
                //ellipse(h.points[j+1][0], h.points[j+1][1], 8, 8, g, lx, rx, ly, ry);
            }
        }
        
        for (int i = 0 ; i < roads.size() ; i++) {
            Road r = roads.get(i);
            for (int j = 0 ; j < r.size - 1 ; j++) {
                fill(255, 255, 255, g);
                strokeWeight((int)Math.max(1, (int)(0.04 * (1400.0 / (rx - lx)))), g);
                if (r.type == 1) {
                    strokeWeight((int)Math.max(1, (int)(0.08 * (1400.0 / (rx - lx)))), g);
                }
                line(r.points[j][0], r.points[j][1], r.points[j+1][0], r.points[j+1][1], g, lx, rx, ly, ry);
            }
        }
        
        /*for (int i = 0 ; i < 360 ; i += 45) {
            fill(0, 0, 0, g);
            strokeWeight(2, g);
            line(700, 420, 700 + 100*cos(i), 420 + 100*sin(i), g, lx, rx, ly, ry);
            text(Integer.toString(i), (int)(700 + 100*cos(i)), (int)(420 + 100*sin(i)), g, 0, 0);
        }*/
        
        
        
        
        
        
    }
    
    public boolean inside(int x, int y) {
        // ray-casting algorithm based on
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
    
        boolean inside = false;
        for (int i = 0, j = size - 1; i < size; j = i ++) {
            double xi = points[i][0], yi = points[i][1];
            double xj = points[j][0], yj = points[j][1];
    
            boolean intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) {inside = !inside;}
        }
    
        return inside;
    }
}