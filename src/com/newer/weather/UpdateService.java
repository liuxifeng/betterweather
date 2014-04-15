package com.newer.weather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream.PutField;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends IntentService {

	public static final String ACTION = "com.newer.weather.ACTION_UPDATE_SERVICE";
	
	private static final String TAG = "UpdateService";
	public static final String ACTION_DATA = "SEND";
	public static final String ACTION_FOUR_DATA = "SEND_FOUR";

	private static final String KEY_PERIOD = "period";
	private static final String KEY_ICON = "icon";
	private static final String KEY_URL = "url";
	private static final String KEY_TITLE = "title";
	private static final String KEY_FCTTEXT = "fcttext";
	private static final String KEY_METRIC = "metric";
	private static final String KEY_POP = "pop";

	private ConnectivityManager connectivityManager;
	public static List<City> cities;
	public List<City> citiesTenDay;
	private List<Location> locations;
	private Location CityLocation;
	public static String forecastDay;
	public static  CurrentWeatherInfo currentWeatherInfo;
	public String name;
	
	private String data;
	public String fourWeatherInfo;
	public String updateFourWeatherInfo;
	private String tenWeatherInfo;
	public String updateTenWeatherInfo;
	public static String path;
	private String city;
	
	

	public UpdateService() {
		super("UpdateService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		
		
		city = GpsActivity.cityName;
		Log.d(TAG, "更新的地址为：" + city);
	
	}

	private void showToast(String string) {

		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	
	

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "onHandleIntent");

		// 获得意图，执行任务（在子线程中）
		String rate = intent.getStringExtra("RATE");
		Log.d(TAG, "RATE" + rate);

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		
		// 获得该城市四天的气象信息
		if (info != null && info.isConnected()) {
			// 网络可用
			showToast("网络可用");

			/**
			 *获得区号
			 *
			 */
			if (city != null) {
				
				try {
					String loc = getLocation(city);
					Log.d(TAG, "获得更新城市的区号：" + loc);
					
				CityLocation = parseLocation(loc);
				Log.d(TAG,"纬度;"+ CityLocation.getLat()+","+"经度："+CityLocation.getLon());
				String lat = CityLocation.getLat();
				String lon = CityLocation.getLon();

				/**
				 * 获得、存储更新城市当前天气
				 * 
				 */
				String  updateCurrent = "{" +getUpdateCurrentInfo(CityLocation)+"}";
				Log.d(TAG, "更新的城市的天气信息" + updateCurrent);
				
				writeUpdateCurrent(updateCurrent);

				/**
				 * 获得、写更新城市四天天气
				 */
				
					updateFourWeatherInfo = getUpdateFour(CityLocation);
					Log.d(TAG, "四天的信息" + updateFourWeatherInfo);
		
					writeUpdateFour(updateFourWeatherInfo);
					
					/**
					 * 获得、写更新城市十天的天气
					 */
					updateTenWeatherInfo = getUpdateTenDay(CityLocation);
					
					writeUpdateTen(updateTenWeatherInfo);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
			
			
				try {
					/**
					 * 获得、 写当天天气
					 */
				
				String currentInfo = getCurrentInfo();
				String jsonCurrentInfo = "{" + currentInfo + "}";
				Log.d(TAG, "当天天气信息" + jsonCurrentInfo);
			
				writeCurrent(jsonCurrentInfo);
			
			/**
			 * 获得、写四天天气
			 */
			
				fourWeatherInfo = getFourDayWeather();
				Log.d(TAG, "四天的信息" + fourWeatherInfo);
	
				writeFour(fourWeatherInfo);
				
				/**
				 * 获得、写十天的天气
				 */
				tenWeatherInfo = getTenDayWeather();
				
				writeTen(tenWeatherInfo);
							
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// 发个广播
			
			path = Environment.getDataDirectory().getAbsolutePath()+"/data/com.newer.weather/files";
			Log.d(TAG, path);
			Intent intent2 = new Intent();
			intent2.setAction(ACTION_DATA);
			intent2.putExtra("path", path);
			sendBroadcast(intent2);
			Log.d(TAG, "ACTION_DATA 广播已发送" + intent2.getStringExtra("path"));
		} else {
			// 网络不可用
			showToast("网络不可用");
		}

	}
	/**
	 * 存储更新城市的十天信息
	 * @param updateTenWeatherInfo
	 */
	private void writeUpdateTen(String updateTenWeatherInfo) {

		String fileTen = "tenDayForecast.txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(openFileOutput(fileTen,
							MODE_PRIVATE)));

			bufferedWriter.write(updateTenWeatherInfo);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得更新城市的十天天气信息
	 * @param cityLocation
	 * @return
	 * @throws IOException 
	 */
	
	private String getUpdateTenDay(Location cityLocation) throws IOException {
		
		String lat = cityLocation.getLat();
		String lon = cityLocation.getLon();
		
		StringBuilder tenDay = null;

		URL url = new URL(
				"http://api.wunderground.com/api/bcc935b839b5c918/forecast10day/lang:CN/q/"+lat+","+lon+".json");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 3);
		connection.setReadTimeout(1000 * 3);
		connection.setRequestMethod("GET");

		connection.connect();
		int code = connection.getResponseCode();

		if (code == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));

			char[] buffer = new char[1024 * 8];

			int size;
			tenDay = new StringBuilder();

			while (-1 != (size = reader.read(buffer))) {
				tenDay.append(buffer, 0, size);
			}
		}
		int start = tenDay.toString().indexOf("[");
		int end = tenDay.toString().indexOf("]") + 1;
		String updateTen = (String) tenDay.toString().subSequence(start, end);

		return updateTen;
	}

	/**
	 * 存储更新城市的四天天气信息
	 * @param updateFourWeatherInfo
	 */
	
	private void writeUpdateFour(String updateFourWeatherInfo) {
		String fileName = "fourDayForecast.txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(openFileOutput(fileName,
							MODE_PRIVATE)));

			bufferedWriter.write(updateFourWeatherInfo);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 获得更新城市的四天的天气信息
	 * @param cityLocation2
	 * @return
	 * @throws IOException 
	 */
	private String getUpdateFour(Location cityLocation) throws IOException {
		String lat = cityLocation.getLat();
		String lon = cityLocation.getLon();
		StringBuilder fourDay = null;

		URL url = new URL(
				"http://api.wunderground.com/api/bcc935b839b5c918/forecast/lang:CN/q/"+lat+","+lon+".json");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 3);
		connection.setReadTimeout(1000 * 3);
		connection.setRequestMethod("GET");

		connection.connect();
		int code = connection.getResponseCode();

		if (code == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));

			char[] buffer = new char[1024 * 8];

			int size;
			fourDay = new StringBuilder();

			while (-1 != (size = reader.read(buffer))) {
				fourDay.append(buffer, 0, size);
			}
		}
		int start = fourDay.toString().indexOf("[");
		int end = fourDay.toString().indexOf("]") + 1;
		String updateFour = (String) fourDay.toString().subSequence(start, end);

		return updateFour;
	}

	/**
	 * 存储更新城市的当天信息
	 * @param updateCurrent
	 */
private void writeUpdateCurrent(String updateCurrent) {

	name = "currentWeather.txt";
	try {
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(openFileOutput(name,
						MODE_PRIVATE)));

		bufferedWriter.write(updateCurrent);
		bufferedWriter.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}

/**
 * 	获得city的当前天气
 * @param cityLocation
 * @return
 * @throws IOException 
 */

private String getUpdateCurrentInfo(Location cityLocation) throws IOException {

	String lat = cityLocation.getLat();
	String lon = cityLocation.getLon();
	StringBuilder curr = null;

	URL url = new URL(
			"http://api.wunderground.com/api/bcc935b839b5c918/conditions/lang:CN/q/"+lat+","+lon+".json");

	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setReadTimeout(1000 * 3);
	connection.setReadTimeout(1000 * 3);
	connection.setRequestMethod("GET");

	connection.connect();
	int code = connection.getResponseCode();

	if (code == HttpURLConnection.HTTP_OK) {
		InputStream inputStream = connection.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "utf-8"));

		char[] buffer = new char[1024 * 8];

		int size;
		curr = new StringBuilder();

		while (-1 != (size = reader.read(buffer))) {
			curr.append(buffer, 0, size);
		}
	}
	int start = curr.toString().indexOf("local_tz_offset") - 1;
	int end = curr.toString().indexOf("wind_mph") - 5;
	String currentUpdateWeather = curr.substring(start, end);

	return currentUpdateWeather;
	
}

/**
 * 获得经度、纬度
 * @param name 
 * @return
 * @throws IOException 
 */
	private String getLocation(String name) throws IOException {

		StringBuilder loc = null;

		URL url = new URL(
				"http://api.wunderground.com/api/bcc935b839b5c918/geolookup/lang:CN/q/CN/"+name+".json");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 3);
		connection.setReadTimeout(1000 * 3);
		connection.setRequestMethod("GET");

		connection.connect();
		int code = connection.getResponseCode();

		if (code == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));

			char[] buffer = new char[1024 * 8];

			int size;
			loc = new StringBuilder();

			while (-1 != (size = reader.read(buffer))) {
				loc.append(buffer, 0, size);
			}
		}
		
		
		int start = loc.toString().indexOf("[");
		int end = loc.toString().indexOf("]") + 1;
		String location = (String) loc.toString().subSequence(start, end);
		return location;
	}

	/**
	 * 写十天天气信息
	 * @param tenWeatherInfo2
	 */
	private void writeTen(String tenWeatherInfo2) {

		String fileTen = "tenDayForecast.txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(openFileOutput(fileTen,
							MODE_PRIVATE)));

			bufferedWriter.write(tenWeatherInfo2);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 获得十天天气
	 * @return
	 * @throws IOException 
	 */
	private String getTenDayWeather() throws IOException {
		StringBuilder tenDay = null;

		URL url = new URL(
				"http://api.wunderground.com/api/bcc935b839b5c918/forecast10day/lang:CN/q/zmw:00000.1.57687.json");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 3);
		connection.setReadTimeout(1000 * 3);
		connection.setRequestMethod("GET");

		connection.connect();
		int code = connection.getResponseCode();

		if (code == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));

			char[] buffer = new char[1024 * 8];

			int size;
			tenDay = new StringBuilder();

			while (-1 != (size = reader.read(buffer))) {
				tenDay.append(buffer, 0, size);
			}
		}
		int start = tenDay.toString().indexOf("[");
		int end = tenDay.toString().indexOf("]") + 1;
		String ten = (String) tenDay.toString().subSequence(start, end);

		return ten;

		}

/**
 * 写
 * @param fourWeatherInfo
 */
	private void writeFour(String fourWeatherInfo) {
		String fileName = "fourDayForecast.txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(openFileOutput(fileName,
							MODE_PRIVATE)));

			bufferedWriter.write(fourWeatherInfo);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d(TAG, "保存完毕4");
	}
/**
 * write
 * @param jsonCurrentInfo
 */
	private void writeCurrent(String jsonCurrentInfo) {
		name = "currentWeather.txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(openFileOutput(name,
							MODE_PRIVATE)));

			bufferedWriter.write(jsonCurrentInfo);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//-----------获得四天的天气信息
private String getFourDayWeather() throws IOException {
	StringBuilder fourDay = null;

	URL url = new URL(
			"http://api.wunderground.com/api/bcc935b839b5c918/forecast/lang:CN/q/zmw:00000.1.57687.json");

	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setReadTimeout(1000 * 3);
	connection.setReadTimeout(1000 * 3);
	connection.setRequestMethod("GET");

	connection.connect();
	int code = connection.getResponseCode();

	if (code == HttpURLConnection.HTTP_OK) {
		InputStream inputStream = connection.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "utf-8"));

		char[] buffer = new char[1024 * 8];

		int size;
		fourDay = new StringBuilder();

		while (-1 != (size = reader.read(buffer))) {
			fourDay.append(buffer, 0, size);
		}
	}
	int start = fourDay.toString().indexOf("[");
	int end = fourDay.toString().indexOf("]") + 1;
	String four = (String) fourDay.toString().subSequence(start, end);

	return four;

	}

	


	// ----------获得当天天气
	private  String getCurrentInfo() throws IOException {
		StringBuilder curr = null;

		URL url = new URL(
				"http://api.wunderground.com/api/bcc935b839b5c918/conditions/lang:CN/q/zmw:00000.1.57679.json");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 3);
		connection.setReadTimeout(1000 * 3);
		connection.setRequestMethod("GET");

		connection.connect();
		int code = connection.getResponseCode();

		if (code == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));

			char[] buffer = new char[1024 * 8];

			int size;
			curr = new StringBuilder();

			while (-1 != (size = reader.read(buffer))) {
				curr.append(buffer, 0, size);
			}
		}
		int start = curr.toString().indexOf("local_tz_offset") - 1;
		int end = curr.toString().indexOf("wind_mph") - 5;
		String currentWeather = curr.substring(start, end);

		return currentWeather;

	}

/**
 * 解析更新城市的经纬度
 * @param locationInfo
 * @return
 * @throws JSONException
 */
	private Location parseLocation(String locationInfo)
			throws JSONException {
		Location location = null;
		JSONArray array = new JSONArray(locationInfo);
		int size = array.length();
		for (int i = 0; i < size; i++) {

			JSONObject object = array.getJSONObject(i);
			String lat = object.getString("lat");
			String lon = object.getString("lon");
			location = new Location(lat, lon);
			
		}

		return location;
	}

	

	

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}



}
