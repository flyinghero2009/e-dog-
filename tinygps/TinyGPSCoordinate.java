package com.cwits.cadasyunjing.tinygps;


public class TinyGPSCoordinate {

	/**
	 * 前方500m开始提醒
	 */
	private final static double mDistance = 500;
	
	/**
	 * 地球一度的距离是111000m;
	 */
	private final static double mDistance_OneDgree = 111000;
	
	/**
	 * 前方30度检测
	 */
	private final static double mDegree = 30;
	
	/**
	 * 计算出右前方的偏15度的坐标
	 * @param current_lat
	 * @param current_lon
	 * @param degree
	 * @return
	 */
	public static TinyGPSLatLon calRightFront_Latlon(double current_lat,double current_lon,double degree)
	{
		TinyGPSLatLon latlon = new TinyGPSLatLon();
		//计算出斜边的距离
		double edge_length = mDistance/Math.sin(90 - (mDegree/2));
		//计算方向角的角度
		double edge_degree = degree + mDegree/2;
		
		//计算并设置目标的经纬度
		latlon.setLon(current_lon + edge_length*Math.sin(edge_degree)/mDistance_OneDgree);
		latlon.setLat(current_lat + edge_length*Math.cos(edge_degree)/mDistance_OneDgree);
		
		return latlon;
	}
	
	/**
	 * 计算出左前方偏15度坐标
	 * @param current_lat
	 * @param current_lon
	 * @param degree
	 * @return
	 */
	public static TinyGPSLatLon calLeftFront_Latlon(double current_lat,double current_lon,double degree)
	{
		TinyGPSLatLon latlon = new TinyGPSLatLon();
		double edge_length = mDistance/Math.sin(90 - (mDegree/2));
		double edge_degree = degree - mDegree/2;
		
		latlon.setLon(current_lon + edge_length*Math.sin(edge_degree)/mDistance_OneDgree);
		latlon.setLat(current_lat + edge_length*Math.cos(edge_degree)/mDistance_OneDgree);
		
		return latlon;
	}
	
	/**
	 * 计算地球两点间的距离
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static double latlonline (double lat1,double lon1,double lat2,double lon2)

	{
	   double x,y,line; 
	   double PI=3.1415926535897932; 
	   double Rlat=6356752; //  极半径  |  不同的投影系统使用不同的极半径和赤道半径
	   double Rlon=6378137; //赤道半径  |  WGS-84 系统: 长轴 6378137 , 短轴 6356752


	   x=(lon2-lon1) * PI * Rlon * Math.cos(((lat1+lat2)/2) *PI /180)/180; 
	   y=(lat2-lat1) * PI * Rlat /180; 
	   line=Math.sqrt( x*x + y*y); 
	   return line; 

	}
}
