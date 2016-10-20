package com.cwits.cadasyunjing.tinygps;

public class TinyGPSInfo {	    
	    
	    /**
	     * 类型
	     */
	    private int mTinyGPS_Type = 1;
	    
	    /**
	     * 类型名称
	     */
	    private String mTinyGPS_TypeName = "";
	    
	    private String mXZQH = "";
	    
	    /**
	     * 限速值
	     */
	    private int mSpeed_Limited = 0;
	    
	    /**
	     * 纬度
	     */
	    private double mLat = 0L;
	    
	    /**
	     * 经度
	     */
	    private double mLon = 0L;
	    
	    /**
	     * 方向角
	     */
	    private int mDegree = 0;
	    
	    
	    
	    /**
	     * 设置电子狗的类型
	     * @param type
	     */
	    public void setTinyGPS_Type(int type)
	    {
	    	this.mTinyGPS_Type = type;
	    }
	    
	    /**
	     * 获取电子狗的类型
	     * @return
	     */
	    public int getTinyGPS_type()
	    {
	    	return this.mTinyGPS_Type;
	    }
	    
	    /**
	     * 设置电子狗类型的名称
	     * @param type_name
	     */
	    public void setTinyGPS_TypeName(String type_name)
	    {
	    	this.mTinyGPS_TypeName = type_name;
	    }
	    
	    /**
	     * 获取电子狗类型名称
	     * @return
	     */
	    public String getTinyGPS_TypeName()
	    {
	    	return this.mTinyGPS_TypeName;
	    }
	    
	    /**
	     * 设置电子狗限速值
	     * @param speed
	     */
	    public void setTinyGPS_SpeedLimited(int speed)
	    {
	    	this.mSpeed_Limited = speed;
	    }
	    
	    
	    /**
	     * 获取电子狗限速值
	     * @return
	     */
	    public int getTinyGPS_SpeedLimited()
	    {
	    	return this.mSpeed_Limited;
	    }
	
	    /**
	     * 设置纬度
	     * @param lat
	     */
	    public void setTinyGPS_Lat(double lat)
	    {
	    	this.mLat = lat;
	    }
	    
	    /**
	     * 获取纬度
	     * @return
	     */
	    public double getTinyGPS_Lat()
	    {
	    	return this.mLat;
	    }
	    
	    /**
	     * 设置经度
	     * @param lon
	     */
	    public void setTinyGPS_Lon(double lon)
	    {
	    	this.mLon = lon;
	    }
	    
	    /**
	     * 获取经度
	     * @return
	     */
	    public double getTinyGPS_Lon()
	    {
	    	return this.mLon;
	    }
	    
	    /**
	     * 设置方向角
	     * @param degree
	     */
	    public void setTinyGPS_Degree(int degree)
	    {
	    	this.mDegree = degree;
	    }
	    
	    /**
	     * 获取方向角
	     * @return
	     */
	    public int getTinyGPS_Degree()
	    {
	    	return this.mDegree;
	    }
	    
	    public void setXZQH(String xzqh)
	    {
	    	this.mXZQH = xzqh;
	    }
	    
	    public String getXZQH()
	    {
	    	return this.mXZQH;
	    }
}
