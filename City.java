import java.util.*;

public class City extends drawInterface {
    int population;
    int x, y;
    String name;
    int group = 0;
    int count = 0;
    
    double[][] hsegs = new double[1005][4];
    int size = 0;
    
    public City(Map mp) {
        String[] consonants = {"sh", "th", "ch", "r", "t", "p", "g", "b", "n", "m", "st", "sk"};
        String[] vowels = {"a", "e", "i", "o", "u"};
        String[] nonend = {"tr", "wh", "gr", "gh", "pr", "sl"}; 
        name = "";
        int lim = (int)(Math.random()*3+3);
        for (int i = 0 ; i < lim ; i++) {
            if (i == lim-1) {
                name += consonants[(int)(Math.random()*consonants.length)];
            }
            else {
                if (Math.random() < 0.5) {
                    name += consonants[(int)(Math.random()*consonants.length)];
                }
                else {
                    name += nonend[(int)(Math.random()*nonend.length)];
                }
            }
            if (i < lim-1) {
                name += vowels[(int)(Math.random()*vowels.length)];
            }
        }
        if (Math.random() < 0.2) {
            name += " City";
        }
        else if (Math.random() < 0.2) {
            name += "ville";
        }
        else if (Math.random() < 0.2) {
            name += "ton";
        }
        else if (Math.random() < 0.1) {
            name += "burg";
        }
        
        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        
        int[] off = {0, 0, 0, 0, 0};
        int mx = 0;
        int mxi = -1;
        for (int i = 0 ; i < 5 ; i++) {
            int val = mp.low[i] - mp.count[i];
            if (val > mx) {
                mx = val;
                mxi = i;
            }
        }
        if (mxi != -1) {
            population = (int)(Math.random()*(mp.highbound[mxi]-mp.lowbound[mxi]))+ mp.lowbound[mxi];
        
        }
        else {
            int choice = -1;
            while (true) {
                choice = (int)(Math.random()*5);
                System.out.println(choice);
                if (mp.count[choice] < mp.high[choice]) {
                    break;
                }
            }
            mxi = choice;
            population = (int)(Math.random()*(mp.highbound[mxi]-mp.lowbound[mxi]))+ mp.lowbound[mxi];
        }
        
        mp.count[mxi]++;
        
        group = mxi;
        
        System.out.println(name);
        System.out.println(population);
        
        x = (int)(Math.random()*1400);
        y = (int)(Math.random()*840);
        
        while (true) {
             x = (int)(Math.random()*1400);
             y = (int)(Math.random()*840);
             
             if (!mp.inside(x, y)) continue;
             boolean f = true;
             int sm = 0;
             for (int i = 0 ; i < mp.cities.size() ; i++) {
                 if (mp.cities.get(i) == this) continue;
                 double dist = Math.sqrt((mp.cities.get(i).x-x)*(mp.cities.get(i).x-x)+(mp.cities.get(i).y-y)*(mp.cities.get(i).y-y));
                 if (dist <= 150) {
                     sm++;
                     if (dist <= 50) {
                         f = false;
                     }
                 }
             }
             
             if (sm >= 5 || !f) {
                 continue;
             }
             
             break;
             
             
        }
    }
    
    public void makeRoads(Map mp) {
        //generateGrid(mp, new double[][]{{x - 1.5, y - 1.5}, {x, y - 2}, {x + 1.5, y - 1.5}, {x + 1.5, y + 1.5}, {x, y + 2}, {x - 1.5, y + 1.5}}, 0.25, false);
        
        double ang1 = Math.random()*20 - 10;
        double ang2 = Math.random()*20 - 10 + 90;
        int SZ = 9;
        
        int INC = 3;
        int s1 = mp.roads.size();
        /*for (int i = -SZ ; i <= SZ ; i += INC) {
            ang1 = Math.random()*12 - 6;
            mp.roads.add(new Road(new double[][]{{x - SZ, y + i - tan(ang1)*SZ}, {x + SZ, y + i + tan(ang1)*SZ}}, 1));
        }
        int s2 = mp.roads.size();
        for (int i = -SZ ; i <= SZ ; i += INC) {
            ang2 = Math.random()*12 - 6 + 90;
            mp.roads.add(new Road(new double[][]{{x - (1/tan(ang2))*SZ + i, y - SZ}, {x + (1/tan(ang2))*SZ + i, y + SZ}}, 1));
        }
        int s3 = mp.roads.size();
        for (int i = s1 ; i < s2-1 ; i++) {
            for (int j = s2 ; j < s3-1 ; j++) {
                double[][] p1 = mp.roads.get(i).points; //top
                double[][] p2 = mp.roads.get(i+1).points; //bottom
                double[][] p3 = mp.roads.get(j).points; //left
                double[][] p4 = mp.roads.get(j+1).points; //right
                double[] int1 = intersection(p1, p3);
                double[] int2 = intersection(p1, p4);
                double[] int3 = intersection(p2, p4);
                double[] int4 = intersection(p2, p3);
                generateGrid(mp, new double[][]{int1, int2, int3, int4}, 0.6, false, 0);

            }
        }*/
        
        System.out.println("Generating grid");
        generateGrid(mp, new double[][]{{x - 5, y - 30}, {x + 5, y - 30}, {x + 5, y + 30}, {x - 5, y + 30}}, 1, false, 0);
        
        for (int i = 0 ; i < mp.highways.size() ; i++) {
            Highway h = mp.highways.get(i);
            for (int j = 0 ; j < h.size-1 ; j++) {
                if (dist(x, y, h.points[j][0], h.points[j][1]) <= 60 || dist(x, y, h.points[j+1][0], h.points[j+1][1]) <= 60) {
                    hsegs[size][0] = h.points[j][0];
                    hsegs[size][1] = h.points[j][1];
                    hsegs[size][2] = h.points[j+1][0];
                    hsegs[size][3] = h.points[j+1][1];
                    size++;
                }
            }
        }
    }
    
    public double[] intersection(double[][] l1, double[][] l2) {
        double m1 = (l1[1][1] - l1[0][1]) / (l1[1][0] - l1[0][0]);
        double m2 = (l2[1][1] - l2[0][1]) / (l2[1][0] - l2[0][0]);
        double b1 = l1[0][1] - l1[0][0] * m1;
        double b2 = l2[0][1] - l2[0][0] * m2;
        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;
        return new double[]{x, y};
    }
    
    public void generateGrid(Map mp, double[][] poly, double dist, boolean done, int tp) {
        count++;
        System.out.println("Start");
        if (!done) {
            if (Math.random() <= 0.1) {
                int len = poly.length;
                double cx = 0;
                double cy = 0;
                for (int i = 0 ; i < len ; i++) {
                    cx += poly[i][0];
                    cy += poly[i][1];
                }
                cx /= len;
                cy /= len;
                double sx = (poly[0][0] + poly[1][0]) / 2;
                double sy = (poly[0][1] + poly[1][1]) / 2;
                mp.roads.add(new Road(new double[][]{{cx, cy}, {sx, sy}}, 0));
                return;
            }
            generateGrid(mp, poly, dist, true, tp);
            for (int i = 0 ; i < poly.length ; i++) {
                double temp = poly[i][0];
                poly[i][0] = poly[i][1];
                poly[i][1] = temp;
            }
            int oldSize = mp.roads.size();
            //generateGrid(mp, poly, dist, true, tp);
            for (int i = oldSize ; i < mp.roads.size() ; i++) {
                Road r = mp.roads.get(i);
                for (int j = 0 ; j < r.size ; j++) {
                    double temp = r.points[j][0];
                    r.points[j][0] = r.points[j][1];
                    r.points[j][1] = temp;
                }
            }
            return;
        }
        
        
        double minX = 100000000;
        double maxX = 0;
        int len = poly.length;
        
        for (int i = 0 ; i < len ; i++) {
            minX = Math.min(minX, poly[i][0]);
            maxX = Math.max(maxX, poly[i][0]);
        }
        
        double x = minX - minX % dist + dist;
        int iter = 0;
        while (x <= maxX) {
            double minY = 1000000000;
            double maxY = 0;
            for (int i = 0 ; i < len ; i++) {
                double x1 = poly[i][0];
                double y1 = poly[i][1];
                double x2 = poly[(i+1)%len][0];
                double y2 = poly[(i+1)%len][1];
                if (x1 == x2) continue;
                if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) continue;

                double thisY = 0;
                if (x1 < x2) {
                    thisY = y1 + ((y2 - y1) / (x2 - x1)) * (x - x1);
                }
                else {
                    thisY = y2 + ((y1 - y2) / (x1 - x2)) * (x - x2);
                }
                maxY = Math.max(maxY, thisY);
                minY = Math.min(minY, thisY);
            }
            //System.out.println(x + " " + minY + " " + maxY);
            double botX = x + Math.random()*dist/5 - dist/10;
            double botY = minY;
            double topX = x + Math.random()*dist/5 - dist/10;
            double topY = maxY;
            
            ArrayList<Double> ls = new ArrayList<Double>();
            
            
            
            for (int i = 0 ; i < size ; i++) {
                double[] intp = intersection(new double[][]{{botX, botY}, {topX, topY}}, new double[][]{{hsegs[i][0], hsegs[i][1]}, {hsegs[i][2], hsegs[i][3]}});
                double along = (intp[0] - botX) / (topX - botX);
                System.out.println(along);
                if (along >= 0 && along <= 1) {
                    ls.add(along);
                }
            }
            
            ls.add(0.0);
            ls.add(1.0);
            
            System.out.println("Size: " + ls.size());
            
            Collections.sort(ls);
            
            for (int i = 0 ; i < ls.size() - 1 ; i++) {
                double l1 = ls.get(i);
                double l2 = ls.get(i+1);
                double x1 = botX + (topX - botX) * l1;
                double y1 = botY + (topY - botY) * l1;
                double x2 = botX + (topX - botX) * l2;
                double y2 = botY + (topY - botY) * l2;
                double DIST = 0.5;
                double ang = atan(x1, y1, x2, y2);
                if (l1 > 0) {
                    x1 += DIST * cos(ang);
                    y1 += DIST * sin(ang);
                }
                if (l2 < 1) {
                    x2 -= DIST * cos(ang);
                    y2 -= DIST * sin(ang);
                }
                mp.roads.add(new Road(new double[][]{{x1, y1}, {x2, y2}}, tp));

            }
            
            
            
            x += dist;
            iter++;
        }
        
        
        
    }
}