package com.test.dbscan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tagphi.common.coor.Coordinate;
import com.tagphi.common.coor.CoordinateConverter;

public class ConvertCluster {
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinate getCenter() {
		return center;
	}

	public void setCenter(Coordinate center) {
		this.center = center;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public Cluster getCrt() {
		return crt;
	}

	public void setCrt(Cluster crt) {
		this.crt = crt;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public List<Point> getConvertpoints() {
		return convertpoints;
	}

	public void setConvertpoints(List<Point> convertpoints) {
		this.convertpoints = convertpoints;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private String name;//编号
	private Coordinate center;//原有中心
	private List<Point> points;//原有点
	private Cluster crt;//参照簇
	private double size;//大小
	private List<Point> convertpoints=new ArrayList<Point>();//转换后的点
	private String info="wgs84";//转换方式
	
	public ConvertCluster(Cluster cluster,Cluster crt_c) {
		points=cluster.getPoints();
		size=points.size();
		center=cluster.getCenter();
		crt=crt_c;
		this.name=cluster.getName();
	}
	
	public void convert_test() {
		double min=center.distance(crt.getCenter());
		min=Math.min(min, 1000);
		//min=Math.min(min, 400);
		Coordinate test;
		double dis;
		/*
		 *  find the optimal converter
		 */
		//wgs84/bd09;
		test=CoordinateConverter.wgs84_to_bd09(center);
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===wgs/bd");
		if(dis<min) {
			min=dis;
			info="wgs/bd";
		}
		//wgs/gcj
		test=CoordinateConverter.wgs84_to_gcj02(center);
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===wgs/gcj");
		if(dis<min){
			min=dis;
			info="wgs/gcj";
		}
		//bd/wgs
		test=CoordinateConverter.bd09_to_wgs84(center);
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===bd/wgs");
		if(dis<min) {
			min=dis;
			info="bd/wgs";
		}
		//gcj/wgs
		test=CoordinateConverter.gcj02_to_wgs84(center);
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===gcj/wgs");
		if(dis<min){
			min=dis;
			info="gcj/wgs";
		}
		//bd/gcj
		test=CoordinateConverter.bd09_to_wgs84(CoordinateConverter.wgs84_to_gcj02(center));
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===bd/gcj");
		if(dis<min){
			min=dis;
			info="bd/gcj";
		}
		//gcj/bd
		test=CoordinateConverter.gcj02_to_wgs84(CoordinateConverter.wgs84_to_bd09(center));
		dis=test.distance(crt.getCenter());
		//System.out.println(dis+"===gcj/bd");
		if(dis<min){
			min=dis;
			info="gcj/bd";
		}

		System.out.println(info+"   "+min);
	}
	
	public void convertpoints() {
		if(info.equals("wgs/bd")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.wgs84_to_bd09(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("wgs/gcj")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.wgs84_to_gcj02(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("bd/wgs")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.bd09_to_wgs84(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("gcj/wgs")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.gcj02_to_wgs84(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("gcj/bd")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.gcj02_to_wgs84(CoordinateConverter.wgs84_to_bd09(coor)));
				convertpoints.add(a);
			}
		}
		else if(info.equals("bd/gcj")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.bd09_to_wgs84(CoordinateConverter.wgs84_to_gcj02(coor)));
				convertpoints.add(a);
			}
		}
		else if(info.equals("wgs")) {
			
		}
	}
	
	/*
	 * 选择转换方式
	 */
	public void convertpoints(String request) {
		info=request;
		if(info.equals("wgs/bd")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.wgs84_to_bd09(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("wgs/gcj")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.wgs84_to_gcj02(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("bd/wgs")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.bd09_to_wgs84(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("gcj/wgs")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.gcj02_to_wgs84(coor));
				convertpoints.add(a);
			}
		}
		else if(info.equals("gcj/bd")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.gcj02_to_wgs84(CoordinateConverter.wgs84_to_bd09(coor)));
				convertpoints.add(a);
			}
		}
		else if(info.equals("bd/gcj")) {
			for(Point p:points) {
				Coordinate coor=new Coordinate(p.getLat(),p.getLon());
				Point a=new Point(CoordinateConverter.bd09_to_wgs84(CoordinateConverter.wgs84_to_gcj02(coor)));
				convertpoints.add(a);
			}
		}
	}
	
	/*
	 * 计算类中点的主要资源平台
	 */
	public Stat countsrc(){
		Stat stat=new Stat();
		
		for(Point p:points) {
			if (p.getSrc().equals("b")) {
				if (stat.getBaidu().containsKey(info)) {
					stat.getBaidu().put(info, stat.getBaidu().get(info) + 1);
				} else {
					stat.getBaidu().put(info, 1);
				}
			}
			else if(p.getSrc().equals("g")) {
					if (stat.getGdt().containsKey(info)) {
						stat.getGdt().put(info, stat.getGdt().get(info) + 1);
					} else {
						stat.getGdt().put(info, 1);
					}

			}
			else if(p.getSrc().equals("t")) {
					if (stat.getTanx().containsKey(info)) {
						stat.getTanx().put(info, stat.getTanx().get(info) + 1);
					} else {
						stat.getTanx().put(info, 1);
					}

			}
			else if(p.getSrc().equals("az")) {
					if (stat.getAvazn().containsKey(info)) {
						stat.getAvazn().put(info, stat.getAvazn().get(info) + 1);
					} else {
						stat.getAvazn().put(info, 1);
					}

			}
		}
//		String statres="baidu:"+baidu+":"+info;
//		statres=statres+"\r"+"gdt:"+gdt+":"+info;
//		statres=statres+"\r"+"tanx:"+tanx+":"+info;
//		statres=statres+"\r"+"avazn:"+avazn+":"+info;
//		
//		return statres;
		return stat;
	}
	
	public Coordinate get_convertcenter() {
		double x=0;
		double y=0;
		double z=0;
		for (Point p:convertpoints) {
			double lat = p.getLat()* Math.PI / 180;
			double lon = p.getLon() * Math.PI / 180;
			x = Math.cos(lat) * Math.cos(lon);
			y = Math.cos(lat) * Math.sin(lon);
			z = Math.sin(lat);
		}
		double lo = Math.atan2(y, x);
		double hyp = Math.sqrt(x * x + y * y);
		double la = Math.atan2(z, hyp);
		la = la * 180 / Math.PI;
		lo = lo * 180 / Math.PI;
		Coordinate convertcenter=new Coordinate(la,lo);
		return convertcenter;
		
	}
	

}
