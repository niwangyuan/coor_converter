package com.test.dbscan;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;


/*
 * deprecated
 * 
 */
public class App {

	public static void main(String[] args) throws IOException {

		
		
		String ip="122.226.183.68";
		String days="30";
		String src="b";
		
		
		
		//区别动静态
		String type=Apidata.type(ip);
		type="1";
		if (type.equals("1")||type.equals("2")||type.equals("5"))
		{
			System.out.println("静态");
		
			List<Point> points = Apidata.get_ip_points(ip, days);//静态
			double r = Apidata.get_static_rad(points, 0.2);
			System.out.println("r==========="+r);
			Apidata.get_discrete(points,0.95);
			

	}
		else {
			System.out.println("动态");
			List<Point> points = Apidata.get_ipc_points(ip, days);//动态
			//double r = Apidata.r2(points, 0.2);
			System.out.println("共有点："+points.size());
			double r = Apidata.get_dynamic_rad(points, 0.2);
			
			
		}
	}
	

}
