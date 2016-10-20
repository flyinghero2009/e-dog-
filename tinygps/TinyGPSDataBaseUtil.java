package com.cwits.cadasyunjing.tinygps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class TinyGPSDataBaseUtil {
	private Context context;  
    public static String dbName = "guangdong.db";// 数据库的名字  
    private static String DATABASE_PATH;// 数据库在手机里的路径 
    private SQLiteDatabase db = null;
    
    private String TABLE_NAME = "sample";
    private String FIELD_LAT = "lat";
    private String FIELD_LON = "lon";
  
    public TinyGPSDataBaseUtil(Context context) {  
        this.context = context;  
        String packageName = context.getPackageName();  
        DATABASE_PATH="/data/data/"+packageName+"/databases/";
        
        if(!checkDataBase())
        {
        	try {  
        		copyDataBase(); 
            } catch (IOException e) {  
                throw new Error("Error copying database");  
            }
        	
        }
    }  
  
    /** 
     * 判断数据库是否存在 
     *  
     * @return false or true 
     */  
    private boolean checkDataBase() {  
        SQLiteDatabase db = null;  
        try {  
            String databaseFilename = DATABASE_PATH + dbName;  
            db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);  
        } catch (SQLiteException e) {  
  
        }  
        if (db != null) {  
            db.close();  
        }  
        return db != null ? true : false;  
    }  
  
    public void initDataBase()
    {
    	if(db == null)
    	{
    		try {  
                String databaseFilename = DATABASE_PATH + dbName;  
                db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);  
            } catch (SQLiteException e) {  
      
            }
    	}
        
    }
    
    public void closeDataBase()
    {
    	if(db !=null)
    	{
    		db.close();
    	}
    }
    /** 
     * 复制数据库到手机指定文件夹下 
     *  
     * @throws IOException 
     */  
    private void copyDataBase() throws IOException {  
        String databaseFilenames = DATABASE_PATH + dbName;  
        File dir = new File(DATABASE_PATH);  
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个  
            dir.mkdir();  
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流  
        //InputStream is = context.getResources().openRawResource(R.raw.kao);// 得到数据库文件的数据流  
        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open("GuangdongDongguan.db");             
        byte[] buffer = new byte[8192];  
        int count = 0;  
        while ((count = is.read(buffer)) > 0) {  
            os.write(buffer, 0, count);  
            os.flush();  
        }  
        is.close();  
        os.close();  
    }
    
    /**
     * 通过电子狗的最大最小值查询
     * @param latlon_left
     * @param latlon_current
     * @param latlon_right
     * @return
     */
    public List<TinyGPSInfo> queryTinyGPS(TinyGPSLatLon latlon_min,TinyGPSLatLon latlon_middle,TinyGPSLatLon latlon_max)
    {
    	
    	//获取sql语句
    	String sql = "";
    	if(latlon_min.getLat() > latlon_middle.getLat())
    	{
    		sql = getSql(latlon_min,latlon_middle,latlon_max,true);
    	}else
    	{
    		sql = getSql(latlon_min,latlon_middle,latlon_max,false);
    	}
    	//查询
    	List<TinyGPSInfo> infos = query(sql);
    	return infos;
    }
    
    /**
     * 通过sql语句进行查询
     * @param sql
     * @return
     */
    private List<TinyGPSInfo> query(String sql)
    {
    	ArrayList<TinyGPSInfo> infos = new ArrayList<TinyGPSInfo>();
    	Cursor cursor = db.rawQuery(sql, null);
    	if (cursor != null) {

			while (cursor.moveToNext()) {
				TinyGPSInfo infoitem = new TinyGPSInfo();
				// int id = cursor.getInt(cursor.getColumnIndex("id"));
				infoitem.setTinyGPS_Lat(cursor.getDouble(cursor
						.getColumnIndex("lat")));
				infoitem.setTinyGPS_Lon(cursor.getDouble(cursor
						.getColumnIndex("lon")));
				infoitem.setTinyGPS_SpeedLimited(cursor.getInt(cursor
						.getColumnIndex("speed")));
				infoitem.setTinyGPS_Degree(cursor.getInt(cursor
						.getColumnIndex("degree")));
				String name = cursor.getString(cursor
						.getColumnIndex("name"));
				infoitem.setTinyGPS_TypeName(name);
				infoitem.setTinyGPS_Type(getType(name));
				infos.add(infoitem);

			}
			cursor.close();

		}
    	return infos;
    }
    
    /**
     * 获取类型
     * @param name
     * @return
     */
    private int getType(String name)
    {
    	int type = 0;
    	if(name == null)
    		return 0;
    	if(name.equals(TinyGpsType.TinyGPSName_BUS_LANE))
    	{
    		type = TinyGpsType.TinyGPSTY_BUS_LANE;
    	}else if(name.equals(TinyGpsType.TinyGPSName_EASY_ACCIDENT_SECTION))
    	{
    		type = TinyGpsType.TinyGPSTY_EASY_ACCIDENT_SECTION;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ELECTRONIC_MONITORING))
    	{
    		type = TinyGpsType.TinyGPSTY_ELECTRONIC_MONITORING;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ELEVATED_ON_MONITORING))
    	{
    		type = TinyGpsType.TinyGPSTY_ELEVATED_ON_MONITORING;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ELEVATED_ON_SPEED))
    	{
    		type = TinyGpsType.TinyGPSTY_ELEVATED_ON_SPEED;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ELEVATED_UNDER_RED_LIGHT))
    	{
    		type = TinyGpsType.TinyGPSTY_ELEVATED_UNDER_RED_LIGHT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ELEVATED_UNDER_SPEED))
    	{
    		type = TinyGpsType.TinyGPSTY_ELEVATED_UNDER_SPEED;
    	}else if(name.equals(TinyGpsType.TinyGPSName_FIXED_SPEED_POINT))
    	{
    		type = TinyGpsType.TinyGPSTY_FIXED_SPEED_POINT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_GAS_STATION))
    	{
    		type = TinyGpsType.TinyGPSTY_GAS_STATION;
    	}else if(name.equals(TinyGpsType.TinyGPSName_HIGH_SPEED_EXIT))
    	{
    		type = TinyGpsType.TinyGPSTY_HIGH_SPEED_EXIT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_INTERVAL_VELOCITY_ENDING_POIN))
    	{
    		type = TinyGpsType.TinyGPSTY_INTERVAL_VELOCITY_ENDING_POIN;
    	}else if(name.equals(TinyGpsType.TinyGPSName_INTERVAL_VELOCITY_MEASUREMENT))
    	{
    		type = TinyGpsType.TinyGPSTY_INTERVAL_VELOCITY_MEASUREMENT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_INTERVAL_VELOCITY_STARTING_POINT))
    	{
    		type = TinyGpsType.TinyGPSTY_INTERVAL_VELOCITY_STARTING_POINT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_MOBILE_SPEED_MEASUREMENT_AREA))
    	{
    		type = TinyGpsType.TinyGPSTY_MOBILE_SPEED_MEASUREMENT_AREA;
    	}else if(name.equals(TinyGpsType.TinyGPSName_ONE_WAY_STREET))
    	{
    		type = TinyGpsType.TinyGPSTY_ONE_WAY_STREET;
    	}else if(name.equals(TinyGpsType.TinyGPSName_RAILWAY_CROSSING))
    	{
    		type = TinyGpsType.TinyGPSTY_RAILWAY_CROSSING;
    	}else if(name.equals(TinyGpsType.TinyGPSName_RED_LIGHT_PHOTO))
    	{
    		type = TinyGpsType.TinyGPSTY_RED_LIGHT_PHOTO;
    	}else if(name.equals(TinyGpsType.TinyGPSName_SCHOOL))
    	{
    		type = TinyGpsType.TinyGPSTY_SCHOOL;
    	}else if(name.equals(TinyGpsType.TinyGPSName_SERVICE_AREA))
    	{
    		type = TinyGpsType.TinyGPSTY_SERVICE_AREA;
    	}else if(name.equals(TinyGpsType.TinyGPSName_THE_LINE_PROHIBITED))
    	{
    		type = TinyGpsType.TinyGPSTY_THE_LINE_PROHIBITED;
    	}else if(name.equals(TinyGpsType.TinyGPSTY_TOLL_GATE))
    	{
    		type = TinyGpsType.TinyGPSTY_TOLL_GATE;
    	}else if(name.equals(TinyGpsType.TinyGPSName_TRAFFIC_LIGHT))
    	{
    		type = TinyGpsType.TinyGPSTY_TRAFFIC_LIGHT;
    	}else if(name.equals(TinyGpsType.TinyGPSName_TUNNEL))
    	{
    		type = TinyGpsType.TinyGPSTY_TUNNEL;
    	}
    	return type;
    }
    
    /**
     * 获取查询sql语句
     * @param latlon_left
     * @param latlon_current
     * @param latlon_right
     * @param minormax
     * @return
     */
    private String getSql(TinyGPSLatLon latlon_left,TinyGPSLatLon latlon_current,TinyGPSLatLon latlon_right,boolean minormax)
    {
    	String sql = "";
    	if(minormax)
    	{
    		sql = "select * from " + TABLE_NAME + " where " + "(" + FIELD_LON + ">" + latlon_left.getLon()
    				+ " and " +  FIELD_LON + "<" + latlon_current.getLon() + " and " + 
    				FIELD_LAT + ">" + latlon_left.getLat() + " and " + FIELD_LAT + "<" + latlon_current.getLat() + ")"
    				
    				+ " or (" + FIELD_LON + ">" + latlon_current.getLon() + " and " + FIELD_LON + "<" + latlon_right.getLon()
    				+ " and " + FIELD_LAT + ">" + latlon_right.getLat() + " and " + FIELD_LAT + "<" + latlon_current.getLat() + ")";
    	}else
    	{
    		sql = "select * from " + TABLE_NAME + " where " + "(" + FIELD_LON + ">" + latlon_left.getLon()
    				+ " and " +  FIELD_LON + "<" + latlon_current.getLon() + " and " + 
    				FIELD_LAT + "<" + latlon_left.getLat() + " and " + FIELD_LAT + ">" + latlon_current.getLat() + ")"
    				
    				 + " or (" + FIELD_LON + ">" + latlon_current.getLon() + " and " + FIELD_LON + "<" + latlon_right.getLon()
    				+ " and " + FIELD_LAT + "<" + latlon_right.getLat() + " and " + FIELD_LAT + ">" + latlon_current.getLat() + ")";
    	}
    	return sql;
    	
    }
}
