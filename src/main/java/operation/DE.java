package operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tagphi.common.coor.Coordinate;
import com.test.dbscan.Apidata;
import com.test.dbscan.Cluster;
import com.test.dbscan.DBScanVO;

public class DE {

	public static void main(String[] args) throws IOException {

		BufferedReader brstat = new BufferedReader(new FileReader(new File("")));
		BufferedReader brip = new BufferedReader(new FileReader(new File("")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("")));

		String txt = "";
		List<String> rawlist = new ArrayList<String>();

		while ((txt = brstat.readLine()) != null) {
			String[] list = txt.split("\t");
			String lat = list[1];
			String lon = list[2];
			String count = list[3];
			String str = lat + "," + lon + "\t" + count;
			rawlist.add(str);
		}
		brstat.close();

		List<String> coor_ip = new ArrayList<String>();
		while ((txt = brip.readLine()) != null) {
			String[] list = txt.split("\t");
			String ip = list[0];
			String lat = list[2];
			String lon = list[3];
			String str = lat + "," + lon + "\t" + ip;
			coor_ip.add(str);
		}
		brip.close();

		Map<String,List<Coordinate>>map = new HashMap<String,List<Coordinate>>();
		for (String str : rawlist) {
			String latlon = str.split("\t")[0];
			List<Coordinate> list=new ArrayList<Coordinate>();
			map.put(latlon, list);
			for (String l : coor_ip) {
				String coor = l.split("\t")[0];
				if (latlon.equals(coor)) {
					String ip = l.split("\t")[1];
					String type = Apidata.type(ip);
					DBScanVO vo = Operation.opr(ip, "60", 0.2, 3, 0.1, 10);
					int size = vo.getNormal().size();
					if (size > 0) {
						for (Cluster c : vo.getNormal()) {
							int psize = c.getSize();
							if (psize > 0) {
								Coordinate center = vo.getNormal().get(0).getCenter();
								list.add(center);
								String lat_ = String.valueOf(center.getLatitude());
								String lon_ = String.valueOf(center.getLongitude());
								str = str + "\t" + lat_ + "," + lon_+","+type;
							}
						}
					}
				}
			}
			bw.write(str);
			bw.newLine();
			System.out.println(str);
		}
		bw.flush();
		bw.close();
	}

}
