package com.test.dbscan;

import java.util.ArrayList;
import java.util.List;

public class DBScanVO {
	private String baseInfo = "";
	public String getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}
	public List<Cluster> getNormal() {
		return normal;
	}
	public void setNormal(List<Cluster> normal) {
		this.normal = normal;
	}
	public List<Point> getErrorPointList() {
		return errorPointList;
	}
	public void setErrorPointList(List<Point> errorPointList) {
		this.errorPointList = errorPointList;
	}
	private List<Cluster> normal = new ArrayList<Cluster>();
	private List<Point> errorPointList = new ArrayList<Point>();
	
	

}
