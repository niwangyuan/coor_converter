package com.test.dbscan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tagphi.common.utils.HttpRequestSender;

public class Apidata {
	
	private static final  double EARTH_RADIUS = 6378137;
	
	public static String type(String ip) {
		String api="https://api.rtbasia.com/ipscore/query?key=HIMABID&ip="+ip;
		String res =HttpRequestSender.sendRequest(api);
		
		res=res.split(",")[0];
		System.out.println(res);
		return res;
	}

	
	

	
	public static List<Point> get_ip_points(String ip,String d)
	{
		List<Point> points=new ArrayList<Point>();
		String ipapi = "https://ip.rtbasia.com/insideapi/latlngs?key=_rtb_lat_lng&l=1000&ips="+ip+"&d="+d;
	    String res = HttpRequestSender.sendRequest(ipapi);
	
		if(res != null){
			JSONArray arry = JSON.parseArray(res);
			
			
			JSONObject obj = arry.getJSONObject(0);
			JSONArray larry = obj.getJSONArray("latlngs");
			int size=larry.size();
			for(int i=0;i<size;i++) {
				String ll=larry.getString(i);
				String lat=ll.split(",")[0];
				String log=ll.split(",")[1];
				points.add(new Point(Double.parseDouble(lat),Double.parseDouble(log)));
				
				
			}
			System.out.println(points);
		}
		return points;
	}
	
	//src: 数据来源(b-baidu, az-avazu, t-tanx,g-gdt)
	public static List<Point> get_ip_points(String ip,String d,String src)
	{
		List<Point> points=new ArrayList<Point>();
		String ipapi = "https://ip.rtbasia.com/insideapi/latlngs?key=_rtb_lat_lng&l=1000&ips="+ip+"&d="+d+"&src="+src;
	    String res = HttpRequestSender.sendRequest(ipapi);
	
		if(res != null){
			JSONArray arry = JSON.parseArray(res);
			
			
			JSONObject obj = arry.getJSONObject(0);
			JSONArray larry = obj.getJSONArray("latlngs");
			int size=larry.size();
			for(int i=0;i<size;i++) {
				String ll=larry.getString(i);
				String lat=ll.split(",")[0];
				String log=ll.split(",")[1];
				points.add(new Point(Double.parseDouble(lat),Double.parseDouble(log)));
				
				
			}
			System.out.println(points);
	
		}
		return points;
	}
	public static List<Point> get_ipc_points(String ip,String d)
	{
		List<Point> points=new ArrayList<Point>();
		String[] l=ip.split("\\.");
		//String[] l=StringUtils.split(ip, "\\.");
		String ipstr=l[0]+"."+l[1]+"."+l[2];
		String ipapi = "https://ip.rtbasia.com/insideapi/latlngs_c?key=_rtb_lat_lng&l=1000&ipc="+ipstr+"&d="+d;
	    String res = HttpRequestSender.sendRequest(ipapi);
	
		if(res != null){
			JSONArray arry = JSON.parseArray(res);
			
			
			JSONObject obj = arry.getJSONObject(0);

			JSONArray larry = obj.getJSONArray("latlngs");
			int size=larry.size();
			for(int i=0;i<size;i++) {
				String ll=larry.getString(i);
				String lat=ll.split(",")[0];
				String log=ll.split(",")[1];
				points.add(new Point(Double.parseDouble(lat),Double.parseDouble(log)));
				
				
			}
			System.out.println(points);
		}
		return points;
	}
	
	//src: 数据来源(b-baidu, az-avazu, t-tanx,g-gdt)
	public static List<Point> get_ipc_points(String ip,String d,String src)//dongtai
	{
		List<Point> points=new ArrayList<Point>();
		String[] l=ip.split("\\.");
		//String[] l=StringUtils.split(ip, "\\.");
		String ipstr=l[0]+"."+l[1]+"."+l[2];
		String ipapi = "https://ip.rtbasia.com/insideapi/latlngs_c?key=_rtb_lat_lng&l=1000&ipc="+ipstr+"&d="+d+"&src="+src;
	    String res = HttpRequestSender.sendRequest(ipapi);
	
		if(res != null){
			JSONArray arry = JSON.parseArray(res);
			
			
			JSONObject obj = arry.getJSONObject(0);

			JSONArray larry = obj.getJSONArray("latlngs");
			int size=larry.size();
			for(int i=0;i<size;i++) {
				String ll=larry.getString(i);
				String lat=ll.split(",")[0];
				String log=ll.split(",")[1];
				points.add(new Point(Double.parseDouble(lat),Double.parseDouble(log)));
				
				
			}
			System.out.println(points);
	
		}
		return points;
	}
	
	public static double get_static_rad(List<Point> points,double x) //静态
	{
		List<Double> dislist=new ArrayList<Double>();
		int l=points.size();
		for(int i=0;i<l-1;i++) {
			for(int j=i+1;j<l;j++) {
				double dis=cal(points.get(i),points.get(j));
				dislist.add(dis);
			}
		}
		int index=(int) (dislist.size()*x);
		int index2;
		if(l>400) {
			index2=(int)(l*Math.sqrt(l));
		}
		else {
			index2=(int)(l*22);//总数极少的时候会超出范围
			if(index2>=dislist.size()) {
				index2=dislist.size()-1;
			}
		}
		Collections.sort(dislist);
		double r1=dislist.get(index);
		double r2=dislist.get(index2);
		System.out.println("计算半径1为："+r1);
		System.out.println("计算半径2为："+r2);
		
		double lower=Math.min(r1, r2);
		double upper=Math.max(r1, r2);
		double upbund=200;//
		double lowbund=10;
		double r=10;
		if (lower>lowbund) {
			if(lower>upbund) {
				r=upbund;
			}
			else {
				r=lower;
			}
		}
		if(lower<=10) {
			if(upper<lowbund) {
				r=Math.max(lowbund/2,upper);
			}
			else {
				if(upper>upbund) {
					r=upbund;
				}
				else {
					r=upper;
				}
			}
		}
		System.out.println("计算半径为："+r);
		
		return r;
		
		
	}
	public static double get_dynamic_rad(List<Point> points,double x) //动态
	{
		List<Double> dislist=new ArrayList<Double>();
		int l=points.size();
		for(int i=0;i<l-1;i++) {
			for(int j=i+1;j<l;j++) {
				double dis=cal(points.get(i),points.get(j));
				dislist.add(dis);
			}
		}
		int index=(int) (dislist.size()*x);
		int index2;
		if(l>400) {
			index2=(int)(l*Math.sqrt(l));
		}
		else {
			index2=(int)(l*22);//总数极少的时候会超出范围
			if(index2>=dislist.size()) {
				index2=dislist.size()-1;
			}
		}
		Collections.sort(dislist);
		double r1=dislist.get(index);
		double r2=dislist.get(index2);
		System.out.println("计算半径1为："+r1);
		System.out.println("计算半径2为："+r2);
		
		double lower=Math.min(r1, r2);
		double upper=7000;

		double r=Math.max(r1, r2);
		r=Math.min(upper, r);
		
		System.out.println("计算半径为："+r);
		
		return r;
		
		
	}
	public static double cal(Point p1,Point p2) { 
	
	       double radLat1 = Point.rad(p1.getLat());  
	       double radLat2 = Point.rad(p2.getLon());  
	       double a = radLat1 - radLat2;  
	       double b = Point.rad(p1.getLat()) - Point.rad(p2.getLon());  
	       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
	       s = s * EARTH_RADIUS;  
	       //s = Math.round(s * 10000) / 10000;  
	       return s; 
	}
	
	
}
