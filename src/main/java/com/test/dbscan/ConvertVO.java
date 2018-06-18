package com.test.dbscan;

import java.util.List;

public class ConvertVO {
	
	private String ip;//ip
	private String days;//天数
	private String src;//来源
	private int index;//标准簇编号
	//private List<Point> points;//原始点,暂时不显示
	private List<Cluster> clusterlist;//原始簇
	private List<ConvertCluster> convertlist;//转换簇结果
	

	/*
	 * 全手工选择转换
	 */
	public void operaiton(String ip,String day,String src,int index,String request) {
		this.ip=ip;
		this.days=day;
		this.src=src;
		this.index=index;
		clusterlist=Convertoperation.clustering(ip);
		Convertoperation.arylist(clusterlist);
		//按选择方式转换
		convertlist=Convertoperation.convert_list(clusterlist,index,request);
		
	}
	
	/*
	 * 全自动转换结果
	 * 默认选择标准
	 */
	public void operation(String ip,String day,String src) {
		this.ip=ip;
		this.days=day;
		this.src=src;
		index=0;
		clusterlist=Convertoperation.clustering(ip);
		Convertoperation.arylist(clusterlist);
		convertlist=Convertoperation.convert_list(clusterlist,index);
		
	}
	
	/*
	 * 选择标准簇转换
	 */
	public void operation(String ip,String day,String src,int index) {
		this.ip=ip;
		this.days=day;
		this.src=src;
		this.index=index;
		clusterlist=Convertoperation.clustering(ip);
		Convertoperation.arylist(clusterlist);
		convertlist=Convertoperation.convert_list(clusterlist,index);
		
	}
	

}
