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

public class Road { 
    /*Types:
     * 0 = City Street
     * 1 = City Boulevard
     * 2 = Small Rural Road
     * 3 = Large Rural Road
     * 4 = Official Route (but not highway)
     * 5 = Rural Boulevard (also an official route)
     */
    double[][] points = new double[15][2];
    int size = 0;
    int type = 0;
    
    public Road(double[][] p, int t) {
        size = p.length;
        for (int i = 0 ; i < size ; i++) {
            points[i][0] = p[i][0];
            points[i][1] = p[i][1];
        }
        type = t;
    }
}