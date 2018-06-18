package com.test.dbscan;

import com.tagphi.common.coor.Coordinate;

public class Point {
    private double lat;//wgs84
    private double lon;//wgs84
    
    private String src;//取点的数据源


	private boolean isVisit;
    private int cluster;
    private boolean isNoised;
    
    private static final  double EARTH_RADIUS = 6378137;
    static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }

    public Point(double x,double y) {
        this.lat = x;
        this.lon = y;
        this.isVisit = false;
        this.cluster = 0;
        this.isNoised = false;
    }
    
    public Point(double x,double y,String src) {
    		this.lat = x;
        this.lon = y;
        this.isVisit = false;
        this.cluster = 0;
        this.isNoised = false;
        this.src=src;
    	
    }
    
    public Point(Coordinate c) {
	    	this.lat=c.getLatitude();
	    	this.lon=c.getLongitude();
        this.isVisit = false;
        this.cluster = 0;
        this.isNoised = false;
    }

//    public double getDistance(Point point) {
//        return Math.sqrt((x-point.x)*(x-point.x)+(y-point.y)*(y-point.y));
//    }
    public double getLDistance(Point point) {
    		double radLat1 = rad(lat);  
        double radLat2 = rad(point.lat);  
        double a = radLat1 - radLat2;  
        double b = rad(lon) - rad(point.lon);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
        s = s * EARTH_RADIUS;   
        return s;
    	
    }

  

    public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setVisit(boolean isVisit) {
        this.isVisit = isVisit;
    }

    public boolean getVisit() {
        return isVisit;
    }

    public int getCluster() {
        return cluster;
    }

    public void setNoised(boolean isNoised) {
        this.isNoised = isNoised;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public boolean getNoised() {
        return this.isNoised;
    }
    
	public String getSrc() {
			return src;
		}
	
		public void setSrc(String src) {
			this.src = src;
		}
    @Override
    public String toString() {
        return lat+" "+lon+" "+cluster+" "+(isNoised?1:0);
    }

	

}