package com.test.dbscan;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class DBScan {
	private double radius;
	private int minPts;
	private List<Cluster> cluster_list = new ArrayList<Cluster>();
	private List<Point> total_ps = new ArrayList<Point>();

	/*
	 * entity
	 * rad 计算半径
	 * minpts 最小聚类点
	 */
	public DBScan(double radius, int minPts) {
		this.radius = radius;
		this.minPts = minPts;
	}

	/*
	 * clustering
	 */
	public void process(List<Point> points) {
		int size = points.size();
		int idx = 0;
		int cluster = 1;
		total_ps = points;

		cluster_list.add(new Cluster(cluster));
		// System.out.println(String.valueOf(c.size())+"..");
		while (idx < size) {
			Point p = points.get(idx++);
			// choose an unvisited point
			if (!p.getVisit()) {
				p.setVisit(true);// set visited
				List<Point> adjacentPoints = getAdjacentPoints(p, points);
				// set the point which adjacent points less than minPts noised
				if (adjacentPoints != null && adjacentPoints.size() < minPts) {
					p.setNoised(true);
				} else {
					p.setCluster(cluster);
					// System.out.println(String.valueOf(cluster));
					cluster_list.get(cluster - 1).addpoint(p);
					for (int i = 0; i < adjacentPoints.size(); i++) {
						Point adjacentPoint = adjacentPoints.get(i);
						// only check unvisited point, cause only unvisited have the chance to add new
						// adjacent points
						if (!adjacentPoint.getVisit()) {
							adjacentPoint.setVisit(true);
							List<Point> adjacentAdjacentPoints = getAdjacentPoints(adjacentPoint, points);
							// add point which adjacent points not less than minPts noised
							if (adjacentAdjacentPoints != null && adjacentAdjacentPoints.size() >= minPts) {
								adjacentPoints.addAll(adjacentAdjacentPoints);
							}
						}
						// add point which doest not belong to any cluster
						if (adjacentPoint.getCluster() == 0) {
							adjacentPoint.setCluster(cluster);
							// System.out.println(String.valueOf(cluster));
							cluster_list.get(cluster - 1).addpoint(adjacentPoint);
							// set point which marked noised before non-noised
							if (adjacentPoint.getNoised()) {
								adjacentPoint.setNoised(false);
							}
						}
					}
					cluster++;
					// System.out.println(String.valueOf(cluster));
					cluster_list.add(new Cluster(cluster));
					// System.out.println(String.valueOf(c.size())+"..");

				}
			}
		}
	}

	/*
	 * clustering process
	 * 
	 */
	private List<Point> getAdjacentPoints(Point centerPoint, List<Point> points) {
		List<Point> adjacentPoints = new ArrayList<Point>();
		for (Point p : points) {
			// include centerPoint itself
			// double distance = centerPoint.getDistance(p);
			double distance = centerPoint.getLDistance(p);
			if (distance <= radius) {
				adjacentPoints.add(p);
			}
		}
		return adjacentPoints;
	}

	/*
	 * result out
	 */
	public void print() {
		for (Cluster p : cluster_list) {
			System.out.println(p.toString()+"/n");
		}
	}


	/*
	 * 输出静态结果
	 */
	public DBScanVO out_static(double x,int lower) {
		
		DBScanVO vo = new DBScanVO();
		int l = total_ps.size();
		int[] csize = new int[cluster_list.size()];
		for (int i = 0; i < cluster_list.size(); i++) {
			csize[i] = cluster_list.get(i).getPoints().size();
		}
		int max = csize[0];

		for (int i = 0; i < cluster_list.size(); i++) {
			if (csize[i] > max) // 判断聚类点的个数最大值
				max = csize[i];
		}

		double k;
		k=Math.max(lower, l*x);
		k = Math.min(max,k);
		if(max>=l/2) {
			k=max;
		}
		System.out.println("聚类点数阈值："+k);

		int i = 0;
		for (Cluster p : cluster_list) {
			if (p.getPoints().size() >= k) {
				System.out.print("入选的聚类为:" + p.getName()); 
				System.out.println("   点的个数为:" +p.getPoints().size());
				vo.getNormal().add(p);
				int pPointLen = p.getPoints().size();
				i += pPointLen;
			} else {
				System.out.print("没有入选的聚类为:" + p.getName()+"   点的个数为:" +p.getPoints().size()); 
			}
		}
		for (Point p : total_ps) {
			if (p.getNoised()) {
				vo.getErrorPointList().add(p);
			}
		}
		vo.setBaseInfo("聚类选取点总数=" + i + "  ip共获得点数：" + total_ps.size());

		return   vo;
	}


	/*
	 * 输出C段结果
	 */
	public DBScanVO outJSONString_dynamic(double x) {
		
		DBScanVO vo = new DBScanVO();

		int l = total_ps.size();
		int[] csize = new int[cluster_list.size()];
		for (int i = 0; i < cluster_list.size(); i++) {
			csize[i] = cluster_list.get(i).getPoints().size();
		}
		int max = csize[0];

		for (int i = 0; i < cluster_list.size(); i++) {
			if (csize[i] > max) // 判断最大值
				max = csize[i];
		}
		double k;
		k=Math.max(l*x, 100);
		k = Math.min(max,k);
		if(max>=l/2) {
			k=max;
		}
		System.out.println("聚类点数阈值："+k);

		int i = 0;
		for (Cluster p : cluster_list) {
			if (p.getPoints().size() >= k) { 
				System.out.print("入选的聚类为:" + p.getName()); 
				System.out.println(" 点的个数为:" +p.getPoints().size());
				int pPointLen = p.getPoints().size();
				p.setInfo("点的个数为:" + pPointLen);
				i += pPointLen;
				vo.getNormal().add(p);
			} else {
				// error
				System.out.print("没有入选的聚类为:" + p.getName()); 
				System.out.println("   点的个数为:" +p.getPoints().size());
				vo.getErrorPointList().addAll(p.getPoints());
			}
		}
		for (Point p : total_ps) {
			if (p.getNoised()) {
				vo.getErrorPointList().add(p);
			}
		}
		vo.setBaseInfo("聚类选取点总数=" + i + "  ip共获得点数：" + total_ps.size());
		return   vo;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getMinPts() {
		return minPts;
	}

	public void setMinPts(int minPts) {
		this.minPts = minPts;
	}

	public List<Cluster> getCluster_list() {
		return cluster_list;
	}

	public void setCluster_list(List<Cluster> cluster_list) {
		this.cluster_list = cluster_list;
	}

	public List<Point> getPs() {
		return total_ps;
	}

	public void setPs(List<Point> ps) {
		this.total_ps = ps;
	}
}