package com.test.dbscan;

import java.util.ArrayList;
import java.util.List;

public class DBScanVO {
	private String baseInfo = "";
	private List<Cluster> normal = new ArrayList<Cluster>();
	private List<Point> errorPointList = new ArrayList<Point>();

	public String getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}

	public List<Cluster> getNomo() {
		return normal;
	}

	public void setNomo(List<Cluster> nomo) {
		this.normal = nomo;
	}

	public List<Point> getErrorPointList() {
		return errorPointList;
	}

	public void setErrorPointList(List<Point> errorPointList) {
		this.errorPointList = errorPointList;
	}

}
