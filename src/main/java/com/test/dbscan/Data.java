package com.test.dbscan;

import java.io.BufferedWriter;
import com.tagphi.common.coor.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Data {
    private static DecimalFormat df=(DecimalFormat) NumberFormat.getInstance();


    protected static Point createRandomPoint(int min, int max) {
    	Random r = new Random();
    	double x = min + (max - min) * r.nextDouble();
    	double y = min + (max - min) * r.nextDouble();
    	return new Point(x,y);
    }
    
    protected static List createRandomPoints(int min, int max, int number) {
    	List points = new ArrayList(number);
    	for(int i = 0; i<number; i++) {
    		points.add(createRandomPoint(min,max));
    	}
    	return points;
    }


    public static void writeData(String points,String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(points);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double format(double x) {
        return Double.valueOf(df.format(x));
    }
    
    

}