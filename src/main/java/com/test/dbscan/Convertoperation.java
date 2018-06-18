package com.test.dbscan;

import java.io.BufferedReader;
import com.tagphi.common.coor.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Convertoperation {
	
	/*
	 * 按大小给簇排序
	 * 倒叙
	 */
	public static void arylist(List<Cluster> clist){
		for(Cluster c:clist) {
			c.operation();
		}
		class SizeComparator implements Comparator<Cluster>{
		    public int compare(Cluster o1, Cluster o2) {
		        //按照规模降序排列
		        return -o1.getSize()+o2.getSize();
		    }
		}
		Collections.sort(clist,new SizeComparator());

	}
	
	
	/*
	 * 获取最原始的簇列表
	 */
	public static List<Cluster> clustering(String ip){
		List<Point> points=Apidata.get_ip_points(ip, "50");
		double r = Apidata.get_static_rad(points, 0.2);
		DBScan dbscan = new DBScan(r, 5);
		dbscan.process(points);
		List<Cluster> clist=dbscan.getCluster_list();
		for(Cluster c:clist) {
			c.operation();
		}
		return clist;
	}
	
	/*
	 * 统计用
	 * 获取带上数据平台标签的数据
	 */
	public static List<Cluster> clustering(String ip,String s)
	{	
		List<Point> points1=Apidata.get_ip_points(ip, "200", "b");
		List<Point> points2=Apidata.get_ip_points(ip, "50", "t");
		List<Point> points3=Apidata.get_ip_points(ip, "200", "g");
		List<Point> points4=Apidata.get_ip_points(ip, "50", "az");
		List<Point> points=new ArrayList<Point>();
		points.addAll(points1);
		points.addAll(points2);
		points.addAll(points3);
		points.addAll(points4);
		double r=0;
		try {
			r = Apidata.get_static_rad(points, 0.2);
		}
		catch(Exception e) {
			
		}
		DBScan dbscan = new DBScan(r, 5);
		dbscan.process(points);
		//获得不做处理的列表
		List<Cluster> clist=dbscan.getCluster_list();
		//处理
		//还没做
		for(Cluster c:clist) {
			c.operation();
		}
		return clist;
	}
	/*
	 * 簇结果按照index转换
	 * 输出转换结果
	 */
	public static List<ConvertCluster> convert_list(List<Cluster> clist,int index) {
		List<ConvertCluster> c_clist=new ArrayList<ConvertCluster>();
		for(int i =0;i<clist.size();i++) {

			if(i!=index&&clist.get(i).getSize()>15) {
				System.out.println("No."+i+"start: \r");
				ConvertCluster cc=new ConvertCluster(clist.get(i),clist.get(index));
				cc.convert_test();
				cc.convertpoints();
				c_clist.add(cc);
			}
		}
		return c_clist;
	}
	
	/*
	 * 簇结果按照index和方式
	 * 输出转换结果
	 */
	public static List<ConvertCluster> convert_list(List<Cluster> clist,int index,String request) {
		List<ConvertCluster> c_clist=new ArrayList<ConvertCluster>();
		for(int i =0;i<clist.size();i++) {
			if(i!=index&&clist.get(i).getSize()>0) {
				ConvertCluster cc=new ConvertCluster(clist.get(i),clist.get(index));
				cc.convertpoints(request);
				c_clist.add(cc);
			}
		}
		return c_clist;
	}
	
	public static List<JSONObject> stat(String ip) {
		List<JSONObject> objstat=new ArrayList<JSONObject>();
		
		List<Cluster> clist=clustering(ip,"");
		arylist(clist);
		List<ConvertCluster> cclist=convert_list(clist,0);
		for(ConvertCluster cc:cclist) {
			Stat stat=cc.countsrc();
			JSONObject obj=JSONObject.fromObject(stat);
			System.out.println(obj);
			objstat.add(obj);
		}
		return objstat;
	}
	
	public static void main(String[] args) {
		String mer="";
		Mercator mercator=new Mercator(Double.parseDouble(mer.split(",")[1]),Double.parseDouble(mer.split(",")[0]));
		Coordinate coor = CoordinateConverter.mercator_to_bd09(mercator);
		
		Coordinate bd=CoordinateConverter.bd09_to_gcj02(coor);
		Coordinate wgs=CoordinateConverter.wgs84_to_gcj02(coor);
		System.out.println(coor.getLatitude()+","+coor.getLongitude());
		System.out.println(bd.getLatitude()+","+bd.getLongitude());
		System.out.println(wgs.getLatitude()+","+wgs.getLongitude());
		
	}
}
