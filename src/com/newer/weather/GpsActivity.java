package com.newer.weather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GpsActivity extends Activity implements OnItemClickListener  {
	public static final String ACTION_ADD = "ADD_CITY";
	protected static final String KEY_IMAGE = "image";
	protected static final String KEY_CITYNAME = "cityname";
	private static final String TAG = "GpsActivity";
	private static String name;
	private ListView listViewCity;
	private List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter adapter;
	
	protected String[] from = {KEY_IMAGE,KEY_CITYNAME};
	protected int[] to = {R.id.image,R.id.textView};
	private ImageView image;
	public String fileCity;
	StringBuilder sbuilder = new StringBuilder();
	private int p;
	public static String cityName;
	public String path=UpdateService.path;
	private String infomation;
	private String[] infos;
	private TextView textViewCurrent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gps);
//		image = (ImageView) findViewById(R.id.image);
		textViewCurrent = (TextView) findViewById(R.id.textView_changsha);
		
        listViewCity=(ListView) findViewById(R.id.listView_city);
		Log.d(TAG, path);
		try {

			list = readCityFile(path);
			Log.d(TAG, "liebia"+list.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
//		
//		HashMap<String, Object> item = new HashMap<String, Object>();
//		item.put(KEY_IMAGE, R.drawable.gpsnew);
//		item.put(KEY_CITYNAME, "changsha");
//		list.add(item);
		
		adapter = new SimpleAdapter(getApplicationContext(), list,
				R.layout.list_city, from, to);
		
		listViewCity.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		listViewCity.setOnItemClickListener(this);
		
		
		
		
		
//		listViewCity.setOnItemLongClickListener(this);
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		registerForContextMenu(listViewCity);
//		
//		
//	}
//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add_city:
			createCityDialog();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createCityDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("输入城市名");
		final EditText editTextCity = new EditText(this);
		builder.setView(editTextCity);
		builder.setNegativeButton("取消", null);
		
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				
				name = editTextCity.getText().toString();

				fileCity = "cities.txt";
				try {
					BufferedWriter bufferedWriter = new BufferedWriter(
							new OutputStreamWriter(openFileOutput("cities.txt",
									MODE_APPEND)));
					StringBuilder message = sbuilder.append(name+",");
					Log.d(TAG, "message"+message.toString());
					bufferedWriter.write(message.toString());
					bufferedWriter.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			
				
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put(KEY_IMAGE, R.drawable.gpsnew);
				item.put(KEY_CITYNAME, name);
				list.add(item);
				
				adapter.notifyDataSetChanged();
				
				
			}

		});
		builder.show();
		adapter.notifyDataSetChanged();
	}

//	public static String getName() {
//		return name;
//
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "页面跳转");
		cityName = (String) list.get(position).get(KEY_CITYNAME);
		textViewCurrent.setText(" ");
		textViewCurrent.setText(cityName);
		adapter.notifyDataSetChanged();
		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("city", cityName);
//		intent.setAction(ACTION_ADD);
//		sendBroadcast(intent);
//		Log.d(TAG, "发送广播2");
		startActivity(intent);
		
	}
	
	
	public List<HashMap<String, Object>> readCityFile(String path) throws IOException{
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String,Object>>();
		
		InputStream inputStream = new FileInputStream(new File(path,
				"cities.txt"));
		int size = inputStream.available();

		byte[] buffer = new byte[size];
		inputStream.read(buffer);
		inputStream.close();
		String cityInfo = new String(buffer);
		Log.d(TAG, cityInfo);
		
		String[] info = cityInfo.split(",");
		for (int i = 0; i < info.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(KEY_IMAGE, R.drawable.ic_action_place);
			map.put(KEY_CITYNAME, info[i]);
			list.add(map);
			
		}
		
		return list;
	}

//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view,
//			int position, long id) {
//
//		p = position;
//		return false;
//	}
//	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		getMenuInflater().inflate(R.menu.delete, menu);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//
//		switch (item.getItemId()) {
//		case R.id.delete:
//			String cityName = list.get(p).get(KEY_CITYNAME).toString();
//			Log.d(TAG, "被点击的城市"+cityName);
////			try {
////			InputStream inputStream = new FileInputStream(new File(path,
////						"cities.txt"));
////				int size = inputStream.available();
////
////				byte[] buffer = new byte[size];
////				inputStream.read(buffer);
////				inputStream.close();
////
////				infomation = new String(buffer);
////
////				Log.d(TAG, "文件里的内容" + infomation);
////				list.clear();
////				infos = infomation.split(",");
////				for (int i = 0; i < infos.length; i++) {
////				
////					if (!infos[i].equals(cityName)) {
////						HashMap<String, Object> map = new HashMap<String, Object>();
////						map.put(KEY_CITYNAME, infos[i]);
////						map.put(KEY_IMAGE, R.drawable.gpsnew);
////					
////						list.add(map);
////					}
////				}
////						Log.d(TAG, "删除后的列表里的内容" + list.toString());
////						fileCity = "cities.txt";
////						try {
////							BufferedWriter bufferedWriter = new BufferedWriter(
////									new OutputStreamWriter(openFileOutput(fileCity,
////											MODE_PRIVATE)));
////							StringBuilder b = new StringBuilder();
////						    StringBuilder newMsg = b.append(list.toString()+",");
////							bufferedWriter.write(newMsg.toString());
////							bufferedWriter.close();
////						} catch (FileNotFoundException e) {
////							e.printStackTrace();
////						} catch (IOException e) {
////							e.printStackTrace();
////						}
////						
//////				list = readCityFile(path);	
//////				Log.d(TAG, "伤处"+list.toString());
////			
////			} catch (FileNotFoundException e) {
////				e.printStackTrace();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//
//			break;
//
//		default:
//			break;
//		}
//		return super.onContextItemSelected(item);
//	}
}
