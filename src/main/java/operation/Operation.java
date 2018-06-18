package operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.tagphi.common.coor.Coordinate;
import com.test.dbscan.Apidata;
import com.test.dbscan.Cluster;
import com.test.dbscan.DBScan;
import com.test.dbscan.DBScanVO;
import com.test.dbscan.Point;

public class Operation{

	/*
	 * 静态ip输出位置
	 * 数据源
	 * 
	 */
	public static DBScanVO opr(String ip, String days,String src, double rad_x, int minpts, double x , int lower) {

		List<Point> points = Apidata.get_ip_points(ip, days,src);//静态
		//rad_x=0.2;
		double r = Apidata.get_static_rad(points, rad_x);

		//minpts=5;
		DBScan dbscan = new DBScan(r, minpts);

		dbscan.process(points);

		//x=0.1  ,   lower=10
		DBScanVO vo = dbscan.out_static(x, lower);
		for(Cluster c: vo.getNormal()) {
			c.operation();
		}

		return vo;
	}

	/*
	 * 静态ip输出位置
	 * 
	 * 
	 */
	public static DBScanVO opr(String ip, String days, double rad_x, int minpts, double x , int lower) {

		List<Point> points = Apidata.get_ip_points(ip, days);//静态
		//rad_x=0.2;
		double r = Apidata.get_static_rad(points, rad_x);

		//minpts=5;
		DBScan dbscan = new DBScan(r, minpts);

		dbscan.process(points);

		//x=0.1  ,   lower=10;
		DBScanVO vo = dbscan.out_static(x, lower);
		for(Cluster c: vo.getNormal()) {
			c.operation();
		}
		return vo;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		String pathin="/Users/wangyuanni/Desktop/res.txt";
		String pathout="/Users/wangyuanni/Desktop/cor_test";
		try {
			BufferedReader rd = new BufferedReader(new FileReader(new File(pathin)));
			BufferedWriter wt = new BufferedWriter(new FileWriter(new File(pathout)));
			
			String txt="";
			txt=rd.readLine();
			while((txt=rd.readLine())!=null) {
				String ip = txt.split("\t")[0];
				String city = txt.split("\t")[3];
				String lat = txt.split("\t")[7];
				String lon = txt.split("\t")[8];
				String str = ip+"\t"+city+"\t"+lat+"\t"+lon;
				Coordinate rigthcoor= new Coordinate(Double.parseDouble(lat),Double.parseDouble(lon));
				
				//全数据源
				DBScanVO vo= opr(ip, "120", 0.2, 3, 0.1, 10);
				int size= vo.getNormal().size();
				if(size==1) {
					int psize=vo.getNormal().get(0).getSize();
					if(psize>0) {
						Coordinate center = vo.getNormal().get(0).getCenter();
						double distance = center.distance(rigthcoor);
						double maxr=vo.getNormal().get(0).getR();
						String res = str +"\t"+distance+"\t"+maxr+"\t"+psize;
						wt.write(res);
						System.out.println(res);
						wt.newLine();
					}
					else {
						String res=str;
						wt.write(res);
						System.out.println(res);
						wt.newLine();
					}
				}
				else if(size==0) {
					String res=str;
					wt.write(res);
					System.out.println(res);
					wt.newLine();
				}
				else if(size>1) {
					String res=str;
					for(Cluster c:vo.getNormal()) {
						int psize=c.getSize();
						if(psize>0) {
							Coordinate center = vo.getNormal().get(0).getCenter();
							double distance = center.distance(rigthcoor);
							double maxr=vo.getNormal().get(0).getR();
							res = res +"\t"+distance+"\t"+maxr+"\t"+psize;
						}
						else {
						}
					}
					wt.write(res);
					System.out.println(res);
					wt.newLine();
				}
			}
			rd.close();
			wt.flush();
			wt.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
