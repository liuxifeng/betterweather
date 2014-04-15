package com.newer.weather;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";

	private ListSectionFragment listSectionFragment;
	private ThreeFrament threeFrament;
	private DumSectionFragment dumSectionFragment;
	public static String name;
	private String currentCondition;
	private String paserCurrentCondition;
	private String paserFourCondition;
	private String fourCondition;
	private String forecastDay;
	private String paserTenCondition;
	private String tenCondition;
	private Bitmap img;
	public static HashMap<String, Integer> images;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	MyAdapter adapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private static CurrentWeatherInfo currentWeatherInfo;
	private List<City> fourWeatherInfo;
	private List<City> tenWeatherInfo;
	private List<HashMap<String, Object>> dataset = new ArrayList<HashMap<String, Object>>();
	public static List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private List<HashMap<String, Object>> dataThree = new ArrayList<HashMap<String, Object>>();
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
   
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UpdateService.ACTION_DATA)) {
				String path = intent.getStringExtra("path");

				Log.d(TAG, "在主活动" + path);
				try {
					
					paserCurrentCondition = readFile(path);

					currentWeatherInfo = paserCurrentWeather(paserCurrentCondition);
					//4day
					paserFourCondition = readFileFour(path);
					//Log.d(TAG, "获得四天的信息" + paserFourCondition);
					
					fourWeatherInfo = paserFourWeather(paserFourCondition);
					
					Log.d(TAG, "main" + currentWeatherInfo.toString());
					Log.d(TAG, "主活动中四天解析后的信息" + fourWeatherInfo.toString());
					
					dataset.clear();
					/**
					 * 第一界面的数据
					 */
					int num = 0;
					Log.d(TAG, "jiexi"+fourWeatherInfo.get(0).getTitle());
					for (int i = 0; i < 4; i++) {
						String highTemp = fourWeatherInfo.get(num).getFcttext_metric();
						String[] hightTemps = highTemp.split("\\. ");
						Log.d(TAG, "high" + hightTemps.length);
						String statu = hightTemps[0];
						String high = hightTemps[1];
						//Log.d(TAG, "high :" + high);
						
						
						String lowTemp = fourWeatherInfo.get(num + 1).getFcttext_metric();
						String[] lowTemps = lowTemp.split("\\. ");
						String low = lowTemps[1];
						Log.d(TAG, "low :" + low);
						
						String imgUrl = fourWeatherInfo.get(num).getIcon_url();
						Log.d(TAG, imgUrl);
					
						
						Log.d(TAG, "bitmap : " + img);
						
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("data",fourWeatherInfo.get(num).getTitle());
						item.put("high", high);
						item.put("low", low);
						item.put("imageUrl", imgUrl);
						item.put("statu", statu);
						dataset.add(item);
						num = num + 2;
					}
					dumSectionFragment = new DumSectionFragment();
					mSectionsPagerAdapter.notifyDataSetChanged();
					
				/**
				 * 第二界面的数据
				 */
					for (int i = 0; i < 8; i++) {
						
						String fourTemps = fourWeatherInfo.get(i).getFcttext_metric();
						String[] fourhightTemps = fourTemps.split("\\. ");
						Log.d(TAG, "ten" + fourhightTemps.length);
						String stat = fourhightTemps[0];
						
						String fourRainRate;
						if (fourhightTemps.length == 5) {
							fourRainRate = fourhightTemps[4];
						} else {

							fourRainRate = "0%";
						}
						
						String fourhigh = fourhightTemps[1];
						
						Log.d(TAG, fourhigh + "," + fourRainRate);
						
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("day", fourWeatherInfo.get(i).getTitle());
						item.put("temprature", fourhigh);
						item.put("tempImage", "tempImage" + i);
						item.put("rainImage", "rainImage" + i);
						item.put("rainRate", fourRainRate);
						item.put("stat", stat);
						data.add(item);
					}
					listSectionFragment = new ListSectionFragment();
				    mSectionsPagerAdapter.notifyDataSetChanged();
				    
				    /**
				     * 第三界面
				     */
					//10day
					paserTenCondition = readFileTen(path);
					tenWeatherInfo = paserTenWeather(paserTenCondition);
					Log.d(TAG, "解析10 ：" + tenWeatherInfo.toString());
					
					for (int i = 0; i < 20; i++) {
						
						String tenTemps = tenWeatherInfo.get(i).getFcttext_metric();
						String[] tenhightTemps = tenTemps.split("\\. ");
						Log.d(TAG, "ten" + tenhightTemps.length);
						String sta = tenhightTemps[0];
						String tenRainRate;
						if (tenhightTemps.length == 5) {
							tenRainRate = tenhightTemps[4];
						} else {

							tenRainRate = "0%";
						}
						
						String tenhigh = tenhightTemps[1];
						
						Log.d(TAG, tenhigh + "," + tenRainRate);
						
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("day", tenWeatherInfo.get(i).getTitle());
						item.put("temprature", tenhigh);
						item.put("tempImage", "tempImage" + i);
						item.put("rainImage", "rainImage" + i);
						item.put("sta", sta);
						item.put("rainRate", tenRainRate);
						dataThree.add(item);
					}
					
					threeFrament = new ThreeFrament();
				    mSectionsPagerAdapter.notifyDataSetChanged();
					
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			
		}
		
		/**
		 * 解析10天天气信息
		 * @param paserTenCondition
		 * @return
		 * @throws JSONException 
		 */
		
		private List<City> paserTenWeather(String paserTenCondition) throws JSONException {
			List<City> list = new ArrayList<City>();
			
			JSONArray array = new JSONArray(paserTenCondition);
			int size = array.length();
			for (int i = 0; i < size; i++) {

				JSONObject object = array.getJSONObject(i);
				String period = object.getString("period");
				String icon = object.getString("icon");
				String icon_url = object.getString("icon_url");
				String title = object.getString("title");
				String fcttext = object.getString("fcttext");
				String fcttext_metric = object.getString("fcttext_metric");
				String pop = object.getString("pop");

				City city = new City(period, icon, icon_url, title, fcttext,
						fcttext_metric, pop);
				list.add(city);
			}
			return list;

		}

		/**
		 * 读取十天天气信息
		 * @param path
		 * @return
		 * @throws IOException 
		 */

		private String readFileTen(String path) throws IOException {
			InputStream inputStream = new FileInputStream(new File(path,
					"tenDayForecast.txt"));
			int size = inputStream.available();

			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();

			tenCondition = new String(buffer);

			return tenCondition;
		}

		/**
		 * 解析四天天气信息
		 * 
		 * @param paserFourCondition
		 * @return
		 * @throws JSONException 
		 */
		private List<City> paserFourWeather(String paserFourCondition) throws JSONException {

			List<City> list = new ArrayList<City>();
			
			JSONArray array = new JSONArray(paserFourCondition);
			int size = array.length();
			for (int i = 0; i < size; i++) {

				JSONObject object = array.getJSONObject(i);
				String period = object.getString("period");
				String icon = object.getString("icon");
				String icon_url = object.getString("icon_url");
				String title = object.getString("title");
				String fcttext = object.getString("fcttext");
				String fcttext_metric = object.getString("fcttext_metric");
				String pop = object.getString("pop");

				City city = new City(period, icon, icon_url, title, fcttext,
						fcttext_metric, pop);
				list.add(city);
			}
			return list;

		}

		/**
		 * 读取四天天气信息
		 * 
		 * @param pathFour
		 * @return
		 * @throws IOException
		 */

		private String readFileFour(String path) throws IOException {
			InputStream inputStream = new FileInputStream(new File(path,
					"fourDayForecast.txt"));
			int size = inputStream.available();

			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();

			fourCondition = new String(buffer);

			return fourCondition;

			
		}

		/**
		 * 解析当天天气信息
		 * 
		 * @param paserCurrentCondition
		 * @return
		 * @throws JSONException
		 */
		private CurrentWeatherInfo paserCurrentWeather(
				String paserCurrentCondition) throws JSONException {

			JSONObject object = new JSONObject(paserCurrentCondition);
			String local_tz_offset = object.getString("local_tz_offset");
			String weather = object.getString("weather");
			String temperature_string = object.getString("temperature_string");
			String temp_f = object.getString("temp_f");
			String temp_c = object.getString("temp_c");
			String relative_humidity = object.getString("relative_humidity");
			String wind_string = object.getString("wind_string");
			String wind_dir = object.getString("wind_dir");
			String wind_degrees = object.getString("wind_degrees");
			CurrentWeatherInfo info = new CurrentWeatherInfo(local_tz_offset,
					weather, temperature_string, temp_f, temp_c,
					relative_humidity, wind_string, wind_dir, wind_degrees);
			return info;
		}

		/**
		 * 从文件中读取当天的天气信息
		 * 
		 * @param path
		 * @return
		 * @throws IOException
		 */
		private String readFile(String path) throws IOException {
			InputStream inputStream = new FileInputStream(new File(path,
					"currentWeather.txt"));
			int size = inputStream.available();

			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();

			currentCondition = new String(buffer);

			Log.d(TAG, "在主活动currentCondition" + currentCondition);
			return currentCondition;

		}

	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		images = new HashMap<String, Integer>();
        images.put("晴间多云", R.drawable.partlycloudy);
        images.put("多云", R.drawable.mostlycloudy);
        images.put("晴", R.drawable.clear);
        images.put("可能有雷暴", R.drawable.chancetstorms);
        images.put("雷暴", R.drawable.chancetstorms);
        images.put("可能有雨", R.drawable.chancerain);
        images.put("汽雾", R.drawable.nt_hazy);
        images.put("雾", R.drawable.fog);
        images.put("有云", R.drawable.partlycloudy);
        images.put("中雨", R.drawable.chancerain);
        images.put("小雨", R.drawable.chancerain);
        images.put("阴", R.drawable.nt_cloudy);
        
//        ActionBar actionBar = getActionBar();
//        actionBar.show();
		Log.d(TAG, "onCreate");
		// 发送广播
		sendBroadcast(new Intent(UpdateService.ACTION));
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
       
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;

		case R.id.add:

			createDialog();
			break;
		case R.id.gps:

			startActivity(new Intent(this, GpsActivity.class));
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void createDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("输入城市名");
		final EditText editTextCity = new EditText(this);
		builder.setView(editTextCity);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				name = editTextCity.getText().toString();

			}
		});
		builder.show();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		/**
		 * 
		 * 片段切换
		 * 
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.d(TAG, "getItem" + position);
			Fragment fragment = null;

			Bundle args = new Bundle();
			switch (position) {
			case 0:

				currentWeatherInfo = new CurrentWeatherInfo("", "晴", "24",
						"27", "30", "78%", "From the Variable at 2 MPH", "ds", "wd");
				dataset=new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < 4; i++) {
					HashMap<String, Object> item=new HashMap<String, Object>();
					String n=getNumber(i);
					item.put("data","星期"+n);
					item.put("high", "最高"+2+i );
					item.put("low", "最低"+(23-2*i));
					item.put("imageUrl", images.get("晴"));
					item.put("statu", "晴");
					dataset.add(item);
				}
				dumSectionFragment = new DumSectionFragment();
				fragment = dumSectionFragment;	
				mSectionsPagerAdapter.notifyDataSetChanged();
				
				// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				// position+1 );
				// fragment.setArguments(args);
				Log.d(TAG, "dummySectionFragment");
				
				break;

			case 1:

				listSectionFragment = new ListSectionFragment();
				fragment = listSectionFragment;
				//mSectionsPagerAdapter.notifyDataSetChanged();
				// args.putInt(ListSectionFragment.ARG_SECTION_NUMBER,
				// position+1);
				// fragment.setArguments(args);
				Log.d(TAG, "listSectionFragment");
				break;
			case 2:
				threeFrament = new ThreeFrament();
				fragment = threeFrament;
				//mSectionsPagerAdapter.notifyDataSetChanged();
				break;
			}

			Log.d(TAG, "ok");
			return fragment;
		}

		private String getNumber(int i) {
			String[] n={"一","二","三","四"};
			
			return n[i];
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 * 
	 * 第一部分界面
	 */
	@SuppressLint("ValidFragment")
	public class DumSectionFragment extends Fragment {

		private GridView gallery;
		private ImageView imageView_condition;
		private ImageView imageView_wind;
		private ImageView imageView_huminy;
		private TextView textViewTemp;
		private TextView textViewWind;
		private TextView textViewHumidity;
		private TextView textViewDescrible;

		public DumSectionFragment() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// 动态接收广播
			registerReceiver(broadcastReceiver, new IntentFilter("SEND"));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.d(TAG, "onCreateView");
				
			
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			imageView_wind = (ImageView) rootView
					.findViewById(R.id.imageView_wind);
			imageView_huminy=(ImageView) rootView.findViewById(R.id.imageView_humidity);
			imageView_condition = (ImageView) rootView
					.findViewById(R.id.imageView_condition);
			
			
			imageView_condition.setImageResource(images.get(currentWeatherInfo.getWeather()));
//			if (currentWeatherInfo.getWeather().endsWith("晴间多云")) {
//				imageView_condition.setImageResource(images[1]);
//			} else if (currentWeatherInfo.getWeather().endsWith("可能有雨")) {
//				imageView_condition.setImageResource(images[0]);
//			}
//			else {
//			imageView_condition.setImageResource(images[1]);
//			}
			textViewTemp = (TextView) rootView
					.findViewById(R.id.textView_condition);
			textViewTemp.setText(currentWeatherInfo.getTemp_c()+"摄氏度");

			textViewWind = (TextView) rootView.findViewById(R.id.textView_wind);
			textViewWind.setText(currentWeatherInfo.getWind_string());

			textViewHumidity = (TextView) rootView
					.findViewById(R.id.textView_humidity);
			textViewHumidity.setText(currentWeatherInfo.getRelative_humidity());

			textViewDescrible = (TextView) rootView
					.findViewById(R.id.textView_contetnt);
			textViewDescrible.setText(currentWeatherInfo.getWeather());

			gallery = (GridView) rootView
					.findViewById(R.id.gridView_weather_list);
			adapter = new MyAdapter(getActivity(), dataset,images);
			gallery.setAdapter(adapter);
			
			
			Log.d(TAG, "onCreateView DummySectionFragment");
			return rootView;
		}

	}

	@SuppressLint("ValidFragment")
	public class ListSectionFragment extends Fragment implements OnItemClickListener {
		private static final String NAME = "name";
		/**
		 * The fragment argument representing the section number for this
		 * fragment. 第二部分界面
		 * 
		 */

		private Myweenkdayadapter myweenkdayadapter;
		private ListView listView_two;

		public ListSectionFragment() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			View rootView = inflater.inflate(R.layout.list_layout_one,
					container, false);
			listView_two = (ListView) rootView
					.findViewById(R.id.listView_48hourweather_list);
			myweenkdayadapter = new Myweenkdayadapter(getActivity(), data,images);
			listView_two.setAdapter(myweenkdayadapter);
			
			listView_two.setOnItemClickListener(this);
			Log.d(TAG, "onCreateView ListSectionFragment");
			return rootView;
		}


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			Intent intent = new Intent(getActivity(), DetailWeatherActivity.class);
			intent.putExtra(NAME, position);
			startActivity(intent);
		}


		

	}
/***
 * 第三界面
 * @author Administrator
 *
 */
	@SuppressLint("ValidFragment")
	public class ThreeFrament extends Fragment {
		
		
		private ListView listView_two;
        private TenDayAdapter adapter;
		
	public ThreeFrament() {
		// TODO Auto-generated constructor stub
	}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.list_layout_one,
					container, false);
			listView_two = (ListView) rootView
					.findViewById(R.id.listView_48hourweather_list);
			 
			adapter = new TenDayAdapter(getActivity(),dataThree,images);
			listView_two.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.d(TAG, "onCreateView ListSectionFragment");
			return rootView;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		unregisterReceiver(broadcastReceiver);
	}
}
