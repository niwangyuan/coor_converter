package com.test.dbscan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;

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
		        //按照职位降序排列
		        return -o1.getSize()+o2.getSize();
		    }
		}
		Collections.sort(clist,new SizeComparator());

	}
	
	
	/*
	 * 获取簇
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
	 * 簇转换
	 */
	public static List<ConvertCluster> convert_list(List<Cluster> clist,int index) {
		List<ConvertCluster> c_clist=new ArrayList<ConvertCluster>();
		for(int i =0;i<clist.size();i++) {
			if(i!=index&&clist.get(i).getSize()>0) {
				ConvertCluster cc=new ConvertCluster(clist.get(i),clist.get(index));
				cc.convert_test();
				cc.convertpoints();
				c_clist.add(cc);
			}
		}
		return c_clist;
	}
	
	public static void main(String[] args) {
		String ip="58.251.73.231";
		List<Cluster> clist=clustering(ip);
		arylist(clist);
		List<ConvertCluster> cclist=convert_list(clist,0);
		for(ConvertCluster cc:cclist) {
			double dis=cc.getCenter().distance(cc.getCrt().getCenter());
			double c_dis=	cc.get_convertcenter().distance(cc.getCrt().getCenter());
			System.out.println(dis+"======="+c_dis+"==="+cc.getInfo());
			
		}
		
	}

}
