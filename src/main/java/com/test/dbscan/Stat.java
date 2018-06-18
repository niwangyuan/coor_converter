package com.test.dbscan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stat {
	
	private Map<String,Integer> baidu=new HashMap<String, Integer>();
	private Map<String,Integer> gdt=new HashMap<String,Integer>();
	private Map<String,Integer> tanx=new HashMap<String,Integer>();
	private Map<String,Integer> avazn=new HashMap<String,Integer>();
	
	public Stat() {
		
	}
	
	public void operation(String ip) {
		
	}

	public Map<String, Integer> getBaidu() {
		return baidu;
	}

	public void setBaidu(Map<String, Integer> baidu) {
		this.baidu = baidu;
	}

	public Map<String, Integer> getGdt() {
		return gdt;
	}

	public void setGdt(Map<String, Integer> gdt) {
		this.gdt = gdt;
	}

	public Map<String, Integer> getTanx() {
		return tanx;
	}

	public void setTanx(Map<String, Integer> tanx) {
		this.tanx = tanx;
	}

	public Map<String, Integer> getAvazn() {
		return avazn;
	}

	public void setAvazn(Map<String, Integer> avazn) {
		this.avazn = avazn;
	}


}
