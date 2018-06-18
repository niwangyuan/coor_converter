package com.test.dbscan;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tagphi.common.coor.Coordinate;
import com.tagphi.common.utils.HttpRequestSender;

public class Apidata {
	
	private static final  double EARTH_RADIUS = 6378137;
	
	
	/*
	 * 获取ip类型
	 */
	public static String type(String ip) {
		String api="https://api.rtbasia.com/ipscore/query?key=HIMABID&ip="+ip;
		String res =HttpRequestSender.sendRequest(api);
		
		res=res.split(",")[0];
		return res;
	}

	/*
	 * 获取ip位置
	 */
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
		}
		return points;
	}
	
	/*
	 * src: 数据来源(b-baidu, az-avazu, t-tanx,g-gdt)
	 *获取ip位置
	 */
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
				//标记数据源
				points.add(new Point(Double.parseDouble(lat),Double.parseDouble(log),src));
			}
		}
		return points;
		
	}
	
	/*
	 * 获取c段位置
	 */
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
		}
		return points;
	}
	
	
	/*src: 数据来源(b-baidu, az-avazu, t-tanx,g-gdt)
	 * 获取c段位置
	 */
	public static List<Point> get_ipc_points(String ip,String d,String src)
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
		}
		return points;
	}
	
	
	/*
	 * 获取静态rad
	 * 计算半径
	 */
	public static double get_static_rad(List<Point> points, double threshold) {
        List<Double> dislist = new ArrayList<Double>();
        int l = points.size();
        double sum=0;
        for(Point p1:points) {
			double mindis=Double.MAX_VALUE;
			for(Point p2:points) {
				if(p2!=p1) {
					double dis = p1.getLDistance(p2);
					if(dis<mindis) {
						mindis=dis;
					}
				}
			}
			dislist.add(mindis);
			sum=sum+mindis;
		}
        int index = (int)(l*0.95);
        Collections.sort(dislist);
        if (CollectionUtils.isEmpty(dislist)) {
            return 1;
        }
        double r = dislist.get(index);

        return r;
	}
	
	
	/*
	 * 获取动态rad
	 */
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
	
	/*
	 * 获取离散度
	 */
	public static void get_discrete(List<Point> points,double x) 
	{
		List<Double> dislist=new ArrayList<Double>();
		double sum=0;
		double average = 0;
		double stv = 0;
		double discrete = 0;
		int l=points.size();
		for(Point p1:points) {
			double mindis=Double.MAX_VALUE;
			for(Point p2:points) {
				if(p2!=p1) {
					double dis = cal(p1,p2);
					if(dis<mindis) {
						mindis=dis;
					}
				}
			}
			dislist.add(mindis);
			sum=sum+mindis;
		}
		
		if(dislist.size()>0) {
			average = sum/dislist.size();
			for(double dis : dislist) {
				stv = stv+(dis-average)*(dis-average);
			}
			stv = Math.sqrt(stv/dislist.size());
			discrete = stv/average;
		}
		System.out.println("average====="+average);
		System.out.println("stv========="+stv);
		System.out.println("discrete========="+discrete);

		int index=(int) (dislist.size()*x);
		Collections.sort(dislist);
		double r=dislist.get(index);
		System.out.println("r========="+r);

		
	}
	
	/*
	 * 计算两点距离
	 * wgs84距离
	 */
	public static double cal(Point p1,Point p2) { 
	
	       double radLat1 = Point.rad(p1.getLat());  
	       double radLat2 = Point.rad(p2.getLat());  
	       double a = radLat1 - radLat2;  
	       double b = Point.rad(p1.getLon()) - Point.rad(p2.getLon());  
	       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
	       s = s * EARTH_RADIUS;  
	       //s = Math.round(s * 10000) / 10000;  
	       return s; 
	}
	
	/*
	 *  腾讯逆地址解析api
	 */
	public static String getTXPOI(Coordinate coor) {
		
		double lat=coor.getLatitude();
		double lon=coor.getLongitude();
		String request="http://apis.map.qq.com/ws/geocoder/v1/?location="+lat+","+lon+"&key=DEWBZ-BCGCO-HV4WL-SW7QD-QM62F-5PBDW&get_poi=1";
		String res = HttpRequestSender.sendRequest(request);
		if (res != null) {
			JSONObject obj = JSON.parseObject(res);
			System.out.println(res);
			JSONObject result = obj.getJSONObject("result");
			JSONObject address_component=result.getJSONObject("address_component");
			String nation = address_component.getString("nation");
			String province=address_component.getString("province");
			String city=address_component.getString("city");
			return nation+","+province+","+city;
		}
		else {
			return null;
		}
	}
	
	public static String oprTX(Coordinate coor) {
			String status_int = getTXPOI(coor);
			try {
				//腾讯接口频率每秒五次，不要删
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return status_int;
	}
	
}
