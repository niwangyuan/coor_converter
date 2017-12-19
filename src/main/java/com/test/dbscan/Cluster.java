package com.test.dbscan;

import java.util.ArrayList;
import java.util.List;

import com.tagphi.common.coor.Coordinate;

public class Cluster {

	private String name;
	private String info;
	private List<Point> points = new ArrayList<Point>();
	private Coordinate center;
	private double r;
	private int size;
	
	
	public void operation() {
		size=points.size();
		get_center();
		get_r();

	}
	
	/*
	 * 简易的寻找中心
	 */
	public void get_center() {
		double x=0;
		double y=0;
		double z=0;
		for (Point p:points) {
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
		center=new Coordinate(la,lo);
		
	}
	
	/*
	 * 寻找最大半径
	 */
	public void get_r() {
		double max_r=0;
		for(Point p:points) {
			Coordinate c=new Coordinate(p.getLat(),p.getLon());
			double dis=center.distance(c);
			if(dis>max_r) {
				max_r=dis;
			}
			
		}
		r=max_r;
		
		
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Cluster(String name) {
		this.name = name;
	}

	public Cluster(int name) {
		this.name = String.valueOf(name);
	}

	public void addpoint(Point point) {
		points.add(point);
	}

	public String getName() {
		return name;
	}

	public List<Point> getPoints() {
		return points;
	}

	public Coordinate getCenter() {
		return center;
	}

	public void setCenter(Coordinate center) {
		this.center = center;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
}
