package de.rainlessrouting.common.model;

import java.io.Serializable;


public class SimplePrecipitation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private double lat;
	private double lon;
	private float niederschlag;
	
	public SimplePrecipitation(double lat, double lon, float nieder) {
		this.setLat(lat);
		this.setLon(lon);
		this.setNiederschlag(nieder);
	}
		
	public float getNiederschlag() {
		return niederschlag;
	}
	public void setNiederschlag(float niederschlag) {
		this.niederschlag = niederschlag;
	}
	public double getLon() {
		return this.lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
}
