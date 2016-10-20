package com.cwits.cadasyunjing.tinygps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.cwits.cadasyunjing.db.DataBaseManager;
import com.cwits.cadasyunjing.quadtree.QuadTreeManager;
import com.cwits.cyx_drive_sdk.data.TimeUtil;

import android.content.Context;

/**
 * 电子狗信息管理类，基本上所有的入口都是从这个类接入的
 * @author Howard zhang
 *
 */
public class TinyGpsManager {
	
	private final static int AAA = 30;//夹角
	private final static int AAB = 25;//夹角 
	private final static int AAD = 25;

	private Context mContext;
	
	/**
	 * 电子狗数据库管理类
	 */
	private TinyGPSDataBaseUtil mTinyGPSDataBaseUtil;
	
	/**
	 * 构造
	 * @param context
	 */
	public TinyGpsManager(Context context)
	{
		this.mContext = context;
		//mTinyGPSDataBaseUtil = new TinyGPSDataBaseUtil(mContext);
		//initDataBase();
	}
	
	
	
	  public static boolean initTxtToDBBase(InputStream inputStream,Context context) {  
		    InputStreamReader inputStreamReader = null;
		    boolean flag = true;
		    try {  
		        inputStreamReader = new InputStreamReader(inputStream, "UTF-8");  
		    } catch (UnsupportedEncodingException e1) {  
		        e1.printStackTrace();
		        flag = false;
		    }
		    if(!flag)
		    	return flag;
		    BufferedReader reader = new BufferedReader(inputStreamReader);  
		    String line;  
		    try {
		    	System.out.print(TimeUtil.dateLongFormatString(System.currentTimeMillis(), TimeUtil.format7));
		    	DataBaseManager dbMgr = new DataBaseManager(context,2);
		    	dbMgr.delAllTinyGPSTab();
		        while ((line = reader.readLine()) != null) {  
		        	String spStr[] = line.split("\\\t");
		        	TinyGPSInfo info = new TinyGPSInfo();
		        	info.setTinyGPS_Degree(Integer.parseInt(spStr[3]));
		        	info.setTinyGPS_Lat(Double.parseDouble(spStr[1]));
		        	info.setTinyGPS_Lon(Double.parseDouble(spStr[0]));
		        	info.setTinyGPS_SpeedLimited(Integer.parseInt(spStr[4]));
		        	info.setTinyGPS_TypeName(spStr[2]);
		        	List<String> codes = QuadTreeManager.getQuadTreeExtendCodeByLatLon(Double.parseDouble(spStr[1]), Double.parseDouble(spStr[0]));
		        	for(int i = 0;i < codes.size();i++)
		        	{
		        		String code = codes.get(i);
		        		try{
		        			if(dbMgr.tabbleIsExist(code))
			        		{
			        			dbMgr.insertTinyGPSDataInfo(info, code);
			        		}else
			        		{
			        			dbMgr.createTinyGPSTable(code);
			        			dbMgr.insertTinyGPSDataInfo(info, code);
			        		}
		        		}catch(Exception e)
		        		{
		        			e.printStackTrace();
		        		}
		        		
		        	}
		        }
		        System.out.print(TimeUtil.dateLongFormatString(System.currentTimeMillis(), TimeUtil.format7));
		    } catch (IOException e) {  
		        e.printStackTrace();
		        flag = false;
		    }
		    return flag;
		}
	
	
	/**
	 * 算法，根据车的经纬度及方向角和电子狗的经纬度判断
	 * @param car_lat
	 * @param car_lon
	 * @param car_degree
	 * @param tinygps_lat
	 * @param tinygps_lon
	 * @return
	 */
	public static boolean judgeMatchDegree(double car_lat,double car_lon,int car_degree,double tinygps_lat,double tinygps_lon)
	{
		boolean flag = false;
		//第一象限
		if(tinygps_lat >= car_lat&&tinygps_lon > car_lon)
		{
			int cartotinygps = 90 - ((int)(Math.toDegrees(Math.atan(Math.abs((tinygps_lat - car_lat)/(tinygps_lon - car_lon)))) + 0.5f));			
			flag = compareDegree(cartotinygps,car_degree,AAA);
			
			
		}else if(tinygps_lat > car_lat&&tinygps_lon <= car_lon)
		{
			//第二象限
			if(tinygps_lon == car_lon)
			{
				flag = compareDegree(0,car_degree,AAA);
			}else
			{
				int cartotinygps = 270 + (int)(Math.toDegrees(Math.atan(Math.abs((tinygps_lat - car_lat)/(tinygps_lon - car_lon)))) + 0.5f);
				flag = compareDegree(cartotinygps,car_degree,AAA);
			}
			
						
			
		}else if(tinygps_lat <= car_lat&&tinygps_lon < car_lon)
		{
			//第三象限
			int cartotinygps = 270 - (int)(Math.toDegrees(Math.atan(Math.abs((tinygps_lat - car_lat)/(tinygps_lon - car_lon)))) + 0.5f);
			flag = compareDegree(cartotinygps,car_degree,AAA);
			
		}else if(tinygps_lat < car_lat&&tinygps_lon >= car_lon)
		{
			//第四象限
			if(tinygps_lon == car_lon)
			{
				flag = compareDegree(180,car_degree,AAA);
			}else
			{
				int cartotinygps = 90 + (int)(Math.toDegrees(Math.atan(Math.abs((tinygps_lat - car_lat)/(tinygps_lon - car_lon)))) + 0.5f);
				flag = compareDegree(cartotinygps,car_degree,AAA);
			}
			
		}
		return flag;
	}
	
	
	/**
	 * 查询电子狗信息
	 */
	public List<TinyGPSInfo> queryTinyGPS(double lat,double lon,double degree)
	{
		//计算出经纬度
		TinyGPSLatLon latlon_left = TinyGPSCoordinate.calLeftFront_Latlon(lat, lon, degree);
		TinyGPSLatLon latlon_right = TinyGPSCoordinate.calRightFront_Latlon(lat, lon, degree);
		//构建当前经纬度
		TinyGPSLatLon latlon_currnent = new TinyGPSLatLon();
		latlon_currnent.setLat(lat);
		latlon_currnent.setLon(lon);
		latlon_currnent.setDegree(degree);
		
		//排序
		TinyGPSLatLon[] latlons = sortMinToMax_Lon(latlon_left,latlon_currnent,latlon_right);
		//TinyGPSLatLon maxlatlon = getMaxLatLon(lat,lon,latlon_left,latlon_right);
		//TinyGPSLatLon minlatlon = getMinLatLon(lat,lon,latlon_left,latlon_right);
		//查询
		List<TinyGPSInfo> infos = getGPSInfos(latlons);
		return infos;
	}
	
	/**
	 * 对三组TinyGPSLatLon数据进行从小到大排序
	 * @param latlon_left
	 * @param latlon_current
	 * @param latlon_right
	 * @return
	 */
	private TinyGPSLatLon[] sortMinToMax_Lon(TinyGPSLatLon latlon_left,TinyGPSLatLon latlon_current,TinyGPSLatLon latlon_right)
	{
		TinyGPSLatLon[] latlons = new TinyGPSLatLon[]{latlon_left,latlon_current,latlon_right};
		if(latlon_left.getLon() >= latlon_current.getLon())
		{
			if(latlon_right.getLon() >= latlon_current.getLon())
			{
				latlons[0] = latlon_current;
				if(latlon_left.getLon() >= latlon_right.getLon())
				{
					latlons[1] = latlon_right;
					latlons[2] = latlon_left;
				}else
				{
					latlons[1] = latlon_left;
					latlons[2] = latlon_right;
				}
			}else
			{
				latlons[0] = latlon_right;
				latlons[1] = latlon_current;
				latlons[2] = latlon_left;
			}
		}else
		{
			if(latlon_right.getLon() >= latlon_left.getLon())
			{
				latlons[0] = latlon_left;
				if(latlon_right.getLon() >= latlon_current.getLon())
				{
					latlons[1] = latlon_current;
					latlons[2] = latlon_right;
				}else
				{
					latlons[1] = latlon_right;
					latlons[2] = latlon_current;
				}
			}else
			{
				latlons[0] = latlon_right;
				latlons[1] = latlon_left;
				latlons[2] = latlon_current;
			}
		}
		
		return latlons;
	}
	
	/**
	 * 获取数据库
	 * @return
	 */
	private void initDataBase()
	{
		mTinyGPSDataBaseUtil.initDataBase();
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDataBase()
	{
		mTinyGPSDataBaseUtil.closeDataBase();
	}
	
	/**
	 * 获取满足条件的全部电子狗信息
	 * @param maxlatlon 最大经纬度
	 * @param minlatlon 最小经纬度
	 * @param currnentDegr 当前方向角
	 * @return 信息集合
	 */
	private List<TinyGPSInfo> getGPSInfos(TinyGPSLatLon[] latlons)
	{
		List<TinyGPSInfo> infos = mTinyGPSDataBaseUtil.queryTinyGPS(latlons[0], latlons[1], latlons[2]);
		return infos;
	}
	
	
	/**
	 * 获取最大经纬度
	 * @param currnent_lat
	 * @param current_lon
	 * @param latlon_left
	 * @param latlon_right
	 * @return
	 */
	public TinyGPSLatLon getMaxLatLon(double currnent_lat,double current_lon,TinyGPSLatLon latlon_left,TinyGPSLatLon latlon_right)
	{
		TinyGPSLatLon latlon = new TinyGPSLatLon();
		double lat = currnent_lat;
		double lon = current_lon;
		if(lat < latlon_left.getLat())
			lat = latlon_left.getLat();
		if(lat < latlon_right.getLat())
			lat = latlon_right.getLat();
		if(lon < latlon_left.getLon())
			lon = latlon_left.getLon();
		if(lon < latlon_right.getLon())
			lon = latlon_right.getLon();
		latlon.setLat(lat);
		latlon.setLon(lon);
		return latlon;
	}
	
	/**
	 * 获取最小经纬度
	 * @param currnent_lat
	 * @param current_lon
	 * @param latlon_left
	 * @param latlon_right
	 * @return
	 */
	public TinyGPSLatLon getMinLatLon(double currnent_lat,double current_lon,TinyGPSLatLon latlon_left,TinyGPSLatLon latlon_right)
	{
		TinyGPSLatLon latlon = new TinyGPSLatLon();
		double lat = currnent_lat;
		double lon = current_lon;
		if(lat > latlon_left.getLat())
			lat = latlon_left.getLat();
		if(lat > latlon_right.getLat())
			lat = latlon_right.getLat();
		if(lon > latlon_left.getLon())
			lon = latlon_left.getLon();
		if(lon > latlon_right.getLon())
			lon = latlon_right.getLon();
		latlon.setLat(lat);
		latlon.setLon(lon);
		return latlon;
	}
	
	//同向比较
	public static boolean compareSynth(int tinyGPS_degree,int car_degree)
	{
		boolean flag = false;
		/*
		if((car_degree >= AAB)&&(car_degree <= (360 - AAC)))
		{
			if((tinyGPS_degree <= car_degree)&&((car_degree - tinyGPS_degree) <= AAB))
			{
				flag = true;
			}else if((tinyGPS_degree > car_degree)&&((tinyGPS_degree - car_degree) <= AAC))
			{
				flag = true;
			}
		}else if((car_degree > (360 - AAC))&&(car_degree < 360))
		{
			if(tinyGPS_degree <= car_degree)
			{
				if((car_degree - tinyGPS_degree) <= AAB)
				{
					flag = true;
				}else if((360 + tinyGPS_degree  - car_degree) <= AAC)
				{
					flag = true;
				}
				
			}else if((tinyGPS_degree > car_degree)&&((tinyGPS_degree - car_degree) <= AAC))
			{
				flag = true;
			}
		}else if((car_degree < AAB)&&(car_degree >= 0))
		{
			if(tinyGPS_degree <= car_degree)
			{
				flag = true;
			}else
			{
				if(((tinyGPS_degree - car_degree) <= AAC))
				{
					flag = true;
				}else if((car_degree + 360 - tinyGPS_degree) <= AAB)
				{
					flag = true;
				}
				
			}
		}
		*/
		if((Math.abs(tinyGPS_degree - car_degree) <= AAD)||(Math.abs(tinyGPS_degree - car_degree) >= (360 - AAD)))
		{
			flag = true;
		}
		return flag;
	}
	
	
	//反向比较
	public static boolean compareReverse(int tinyGPS_degree,int car_degree)
	{
		if(Math.abs(tinyGPS_degree - car_degree) >= 180 - AAB)
		{
			return true;
		}
		return false;
	}
	private static boolean compareDegree(int tinyGPS_degree,int car_degree,int angle_off)
	{
		if((Math.abs(tinyGPS_degree - car_degree) <= angle_off)||(Math.abs(tinyGPS_degree - car_degree) >= (360 - angle_off)))
		{
			return true;
		}
		return false;
	}
}
