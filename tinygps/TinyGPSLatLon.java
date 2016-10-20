package com.cwits.cadasyunjing.tinygps;

/**
 * 坐标信息
 * @author Howard zhang
 *
 */
public class TinyGPSLatLon {
	
	/**
	 * 纬度
	 */
	private double mLat = 0.0;
	
	/**
	 * 经度
	 */
	private double mLon = 0.0;
	
	/**
	 * 方向角
	 */
	private double mDegree = 0.0;
	
	/**
	 * 设置纬度
	 * @param lat
	 */
	public void setLat(double lat)
	{
		this.mLat = lat;
	}
	
	/**
	 * 获取纬度
	 * @return
	 */
	public double getLat()
	{
		return this.mLat;
	}
	
	/**
	 * 设置经度
	 * @param lon
	 */
	public void setLon(double lon)
	{
		this.mLon = lon;
	}
	
	/**
	 * 获取经度
	 * @return
	 */
	public double getLon()
	{
		return this.mLon;
	}
	
	/**
	 * 设置方向角
	 * @param degree
	 */
	public void setDegree(double degree)
	{
		this.mDegree = degree;
	}
	
	/**
	 * 获取方向角
	 * @return
	 */
	public double getDegree()
	{
		return this.mDegree;
	}
}
