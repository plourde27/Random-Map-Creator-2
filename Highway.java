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

public class Highway extends drawInterface {
    int number = 0;
    double[][] points = new double[50005][2];
    double[][] bestpoints = new double[50005][2];

    int startX, startY;
    int endX, endY;
    int size = 0;
    int bestsize = 0;
    int bestcount = 0;
    boolean[] seen = new boolean[1005];
    boolean[] seenh = new boolean[1005];
    boolean[] oldseen = new boolean[1005];
    int[] intx = new int[1005];
    int[] inty = new int[1005];
    boolean[] goFor = new boolean[1005];
    boolean dud = true;
    
    public Highway(Map mp, int xx, int yy, boolean rand) {
        
        int num;
        while (true) {
            num = (int)(Math.random()*100);
            boolean f = true;
            for (int i = 0 ; i < mp.highways.size() ; i++) {
                f &= (mp.highways.get(i).number != num);
            }
            if (f) {
                break;
            }
        }
        number = num;
        
        int count = 0;
        int LIM = 150;
        
        while (count < LIM) {
            
            int cityCount = 0;
            
            startX = (int)(Math.random()*1400);
            startY = (int)(Math.random()*840);
            
            if (xx > 0) {
                startX = xx;
                startY = yy;
            }
            
            //int goalX = 200 + mp.highways.size() * 100;
            if (!mp.inside(startX, startY)) { count++; continue; }
            
            boolean g = false;
            /*int ind = -1;
            for (int i = 0 ; i < mp.size ; i++) {
                double dist = Math.sqrt((startX-mp.points[i][0])*(startX-mp.points[i][0])+(startY-mp.points[i][1])*(startY-mp.points[i][1]));
                g |= (dist <= 10);
                if (dist <= 10) {
                    ind = i;
                }
            }
            
            
            
            
            boolean f = true;
            for (int i = 0 ; i < mp.highways.size() ; i++) {
                double dist = Math.sqrt((startX-mp.highways.get(i).startX)*(startX-mp.highways.get(i).startX)+(startY-mp.highways.get(i).startY)*(startY-mp.highways.get(i).startY));
                f &= (dist >= 60);
            }
            
            if (!(f && g)) {count++; continue;}*/
            
            
            
            //double ang = atan(mp.points[ind][0], mp.points[ind][1], mp.points[(ind+1)%mp.size][0], mp.points[(ind+1)%mp.size][1]);
            double ang = choose(200, 1200, 200, 640);
            if (ang == -1000) {
                ang = choose(700, 700, 420, 420);
            }
            
            
            
            
            ang += Math.random()*10 - 5;
            
            if (rand) ang = Math.random()*360;
            
            if (!mp.inside((int)(startX + cos(ang)*100), (int)(startY + sin(ang)*100))) {
                break;
            }
            
            /*double mapang = ang;
            
            while (true) {
                if (Math.random() <= 0.5) {
                    ang = mapang + 90;
                }
                else {
                    ang = mapang - 90;
                }
                
                
                break;
            }*/
            
            /*if (mp.highwayCount >= 0) {
                int ct = 0;
                for (int i = 0 ; i < mp.cities.size() ; i++) {
                    if (mp.direct[i] == 0) {
                        ct++;
                    }
                }
                int cind = (int)(Math.random()*ct);
                ct = 0;
                for (int i = 0 ; i < mp.cities.size() ; i++) {
                    if (mp.direct[i] == 0) {
                        if (ct == cind) {
                            mp.direct[i] ++;
                            startX = mp.cities.get(i).x;
                            startY = mp.cities.get(i).y;
                        }
                        ct++;
                    }
                }
            }*/
            
            double x = startX;
            double y = startY;
            
            
            
            
            
            double startAng = ang;
            
            int JUMP = 5;
            int ANGJUMP = 7;
            int lock = 0;
            
            size = 0;
            
            points[0][0] = x;
            points[0][1] = y;
            
            size++;
            
            int lsize = 0;
            boolean found4 = false;
            boolean fulf = false;
            
            count++;
            
            double total = 0;
            
            /*if (mp.highways.size() >= 6) {
                x = 400;
                y = 420;
            }*/
            
            int[] rollback = new int[1005];
            int[] rollback2 = new int[1005];
            
            for (int i = 0 ; i < 1005 ; i++) {
                rollback[i] = mp.direct[i];
                rollback2[i] = mp.bypass[i];
                oldseen[i] = seenh[i];
            }
            
            for (int i = 0 ; i < mp.cities.size() ; i++) {
                if (mp.cities.get(i).group <= 1) {
                    goFor[i] = true;
                }
                else if (mp.bypass[i] == 0 && mp.direct[i] == 0 && Math.random() <= 0.8) {
                    goFor[i] = true;
                }
                else if (mp.bypass[i] > 0 && mp.direct[i] == 0 && Math.random() <= 0.6) {
                    goFor[i] = true;
                }
                else if (mp.direct[i] > 0 && mp.bypass[i] > 0 && Math.random() <= 0.6) {
                    goFor[i] = true;
                }
            }
            
            String s = "";
            for (int i = 0 ; i < mp.cities.size() ; i++) {
                if (goFor[i]) {
                    s += "1";
                }
                else {
                    s += "0";
                }
            }
            
            
            while (size < 20000 && true) {

                boolean found2 = false;
                boolean found3 = false;
                double ansx = 0;
                double ansy = 0;
                double oang = ang;
                
                for (int i = 0 ; i < mp.highways.size() ; i++) {
                    Highway hw = mp.highways.get(i);
                    double mn = 100000;
                    for (int j = 0 ; j < hw.size-1 ; j++) {
                        double x1 = hw.points[j][0];
                        double y1 = hw.points[j][1];
                        double x2 = hw.points[j+1][0];
                        double y2 = hw.points[j+1][1];
                        mn = Math.min(mn, dist((int)x, (int)y, (int)x1, (int)y1));
                        if (dist((int)x, (int)y, (int)x1, (int)y1) <= 6 && !seenh[i]) {
                            seenh[i] = true;
                            intx[i] = (int)x1;
                            inty[i] = (int)y1;
                            if (rand || Math.random() <= 0.40 || Math.abs(findDelt(ang, startAng)) >= 70) {
                                found3 = true;
                                //points[size][0] = x1;
                                //points[size][1] = y1;
                                //size++;
                                ansx = x1;
                                ansy = y1;
                                break;
                            }
                            
                        }
                        /*if (dist((int)x, (int)y, (int)x1, (int)y1) <= 60 && !seenh[i]) {
                            double hwang = atan(x1, y1, x2, y2);
                            double delt = findDelt(ang, hwang);
                            if (Math.abs(Math.abs(delt) % 180 - 90) >= 60) {
                                double delt1 = findDelt(ang, hwang + 90);
                                double delt2 = findDelt(ang, hwang - 90);
                                if (Math.abs(delt1) <= Math.abs(delt2)) {
                                    if (delt1 > 0) {
                                        ang = (ang + Math.min(delt1, ANGJUMP) + 360)%360;
                                    }
                                    else {
                                        ang = (ang - Math.min(-delt1, ANGJUMP) + 360)%360;
                                    }
                                }
                                else {
                                    if (delt2 > 0) {
                                        ang = (ang + Math.min(delt2, ANGJUMP) + 360)%360;
                                    }
                                    else {
                                        ang = (ang - Math.min(-delt2, ANGJUMP) + 360)%360;
                                    }
                                }
                                found2 = true;
                                lock = 8;
                                break;
                            }
                        }*/
                        //else if (dist((int)x, (int)y, (int)x1, (int)y1) <= 30 && seenh[i]) {
                            /*size = lsize;
                            found4 = true;
                            break;*/
                        //}
                    }
                    if (mn <= 100) {
                    }
                    if (found3) break;
                }
                
                /*if (found4) {

                    break;
                }*/
                
                /*if (lock > 0) {
                    x += cos(ang) * JUMP;
                    y += sin(ang) * JUMP;
                
                    points[size][0] = x;
                    points[size][1] = y;
                    size++;
                    lock--;
                    continue;
                }*/
                
                if (found3) {
                    fulf = true;
                    points[size][0] = ansx;
                    points[size][1] = ansy;
                    size++;
                    break;
                }
                
                /*if (found2) {
                    x += cos(ang) * JUMP;
                    y += sin(ang) * JUMP;
                
                    points[size][0] = x;
                    points[size][1] = y;
                    size++;
                    continue;
                }*/
                
                
                double newAng = -1000;
                int mni = -1;
                double mn = 1000000000;
                double mn2 = 1000000000;
                boolean found = false;
                
                boolean h = false;
                
                for (int i = 0 ; i < mp.cities.size() ; i++) {
                    double ds = dist(mp.cities.get(i).x, mp.cities.get(i).y, (int)x, (int)y);
                    
                    if (ds <= 5 && mp.direct[i] == rollback[i]) {
                        mp.direct[i]++;
                        cityCount += 2 * (5 - mp.cities.get(i).group);
                        
                        fulf = true;
                        seen[i] = true;
                        mni = i;
                    }
                    if (ds <= 30 && mp.bypass[i] == rollback2[i]) {
                        mp.bypass[i]++;
                        cityCount += (5 - mp.cities.get(i).group);
                    }
                    if (!goFor[i]) continue;
                    if (ds > 200 || seen[i]) {
                        continue;
                    }
                    
                    newAng = atan((int)x, (int)y, mp.cities.get(i).x, mp.cities.get(i).y);
                    //mn = ds;
                    mni = i;
                    
                    
                    
                    double delt = 0;
    
                    delt = findDelt(ang, newAng);
                    
                    double SIDE = 30;
                    double rightX = mp.cities.get(i).x + cos(newAng + 90) * SIDE * (5 - mp.cities.get(i).group);
                    double rightY = mp.cities.get(i).x + sin(newAng + 90) * SIDE * (5 - mp.cities.get(i).group);
                    double leftX = mp.cities.get(i).x + cos(newAng - 90) * SIDE * (5 - mp.cities.get(i).group);
                    double leftY = mp.cities.get(i).x + sin(newAng - 90) * SIDE * (5 - mp.cities.get(i).group);

                    double delt2 = findDelt(ang, atan((int)x, (int)y, (int)leftX, (int)leftY));
                    double delt3 = findDelt(ang, atan((int)x, (int)y, (int)rightX, (int)rightY));
                    double newDelt = Math.min(delt2, delt3);
                    double hypoth = ang;
                    if (delt > 0) {
                        hypoth = (ang + Math.min(delt, ANGJUMP))%360;
                    }
                    else if (delt < 0) {
                        hypoth = ((ang - Math.min(Math.abs(delt), ANGJUMP) + 360)%360);
                    }
                    if (Math.abs(findDelt(startAng, hypoth)) <= 30 && Math.abs(delt) <= Math.abs(mn) && Math.abs(delt) <= 30) {
                        mn = delt;
                        found = true;
                    }
                    
                    hypoth = ang;
                    if (newDelt > 0) {
                        hypoth = (ang + Math.min(newDelt, ANGJUMP))%360;
                    }
                    else if (newDelt < 0) {
                        hypoth = ((ang - Math.min(Math.abs(newDelt), ANGJUMP) + 360)%360);
                    }
                    
                    if (Math.abs(findDelt(startAng, hypoth)) <= 30 && Math.abs(newDelt) <= Math.abs(mn2) && Math.abs(newDelt) <= 30) {
                        mn2 = newDelt;
                        found = true;
                    }
                }
                
                
                double delt = mn;
                if (mn2 * 1.2 <= mn) {
                    delt = mn2;
                }
                
                
                if (found && delt > 0) {
                    ang = (ang + Math.min(delt, ANGJUMP))%360;
                }
                else if (found && delt < 0) {
                    ang = ((ang - Math.min(Math.abs(delt), ANGJUMP) + 360)%360);
                }
                
                ang += Math.random() - 0.5;
                
                
                if (!h) {
                    x += cos(ang) * JUMP;
                    y += sin(ang) * JUMP;
                }
                else {
                    x = mp.cities.get(mni).x;
                    y = mp.cities.get(mni).y;
                    h = false;
                }


                if (!mp.inside((int)x, (int)y)) {
                    break;
                }
                
                if (found4) {
                    break;
                }
                
                points[size][0] = x;
                points[size][1] = y;
                size++;
                
                total += (ang - oang);
                
            }
            
            
            
            
            
            
            
            if (size >= 110 * (3.0 / JUMP) || (rand && size >= 20 * (3.0 / JUMP))) {
                double ang1 = atan(points[0][0], points[0][1], points[1][0], points[1][1]);
                double ang2 = atan(points[0][0], points[0][1], points[size-1][0], points[size-1][1]);
                if (Math.abs(findDelt(ang1, ang2)) <= 22) {
                    boolean ff = true;
                    for (int i = 0 ; i < mp.highways.size() ; i++) {
                        
                        Highway h = mp.highways.get(i);
                        for (int j = 0 ; j < h.size ; j++) {
                            for (int k = 0 ; k < size ; k++) {
                                if (j == k) continue;
                                if (dist(h.points[j][0], h.points[j][1], points[k][0], points[k][1]) <= 40) {
                                    if (dist(points[k][0], points[k][1], intx[i], inty[i]) > 90) {
                                        ff = false;
                                    }
                                }
                            }
                        }
                        
                    }
                    if (ff) {
                        dud = false;
                        if (cityCount >= bestcount) {
                            bestcount = cityCount;
                            bestsize = size;
                            for (int i = 0 ; i < size ; i++){ 
                                bestpoints[i][0] = points[i][0];
                                bestpoints[i][1] = points[i][1];
                            }
                        }
                    }
                }
                else {
                    //System.out.println("BAD ANGLE");
                }
            }
            else {
                //System.out.println("TOO SMALL");
            }
            
            if (count < LIM || dud) {
                for (int i = 0 ; i < mp.cities.size() ; i++) {
                    goFor[i] = false;
                }
                
                
                for (int i = 0 ; i < 1005 ; i++) {
                    mp.direct[i] = rollback[i];
                    mp.bypass[i] = rollback2[i];
                    seenh[i] = oldseen[i];
                }
            }
            
            String st = "";
            for (int i = 0 ; i < mp.cities.size() ; i++) {
                if (mp.bypass[i] > 0 || mp.direct[i] > 0) {
                    st += "1";
                }
                else {
                    st += "0";
                }
            }
            
            
            
            
        }
        
         String st = "";
            for (int i = 0 ; i < mp.cities.size() ; i++) {
                if (mp.bypass[i] > 0 || mp.direct[i] > 0) {
                    st += "1";
                }
                else {
                    st += "0";
                }
            }
        
        if (!dud && bestcount > 0) {
            size = bestsize;
            for (int i = 0 ; i < size ; i++) {
                points[i][0] = bestpoints[i][0];
                points[i][1] = bestpoints[i][1];
            }
            
            mp.highwayCount += 1;
        }
        
        
        
        
    }
    
    double choose(int lb, int rb, int tb, int bb) {
        boolean left = startX <= lb;
        boolean right = startX >= rb;
        boolean bot = startY >= bb;
        boolean top = startY <= tb;
        
        double ang = -1000;
        
        
        if (left) {
            if (bot && Math.random() <= 0.5) {
                ang = 270;
            }
            else if (top && Math.random() <= 0.5) {
                ang = 90;
            }
            else {
                ang = 0;
            }
        }
        else if (bot) {
            if (right && Math.random() <= 0.5) {
                ang = 180;
            }
            else {
                ang = 270;
            }
        }
        else if (top) {
            if (right && Math.random() <= 0.5) {
                ang = 180;
            }
            else {
                ang = 90;
            }
        }
        else if (right) {
            ang = 180;
        }
        return ang;
    }
    
    double findDelt(double ang, double newAng) {
        double delt = 0;
        if (Math.abs(newAng - ang) <= 180) {
            delt = ang - newAng;
        }
        else {
            delt = 360 - (ang - newAng);
        }
        return delt;
    }
}