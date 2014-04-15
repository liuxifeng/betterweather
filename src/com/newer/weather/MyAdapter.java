package com.newer.weather;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
  private static final String TAG = "MyAdapter";
  private Context context;
  private LayoutInflater layoutInflater;
  private List<HashMap<String, Object>> dataset;
   private HashMap<String, Integer> images;
  
	
	public MyAdapter(Context context,
			List<HashMap<String, Object>> dataset, HashMap<String, Integer> images) {
		this.context=context;
		this.dataset=dataset;
		this.images=images;
		layoutInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dataset.size();
	}

	@Override
	public Object getItem(int position) {
		return dataset.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
static class ViewHolder{
	TextView textViewData;
	ImageView imageViewFour;
	TextView textViewHighTemp;
	TextView textViewLowTemp;
	TextView textViewStatu;
	
}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
		if (null==convertView) {
			
         convertView=layoutInflater.inflate(R.layout.list_gallery, null);
         holder=new ViewHolder();
         holder.textViewData=(TextView) convertView.findViewById(R.id.textView_data);
         holder.imageViewFour = (ImageView) convertView.findViewById(R.id.imageView_four);
         holder.textViewHighTemp=(TextView) convertView.findViewById(R.id.textView_high_temp);
         holder.textViewLowTemp = (TextView) convertView.findViewById(R.id.textView_low_temp);
         holder.textViewStatu=(TextView) convertView.findViewById(R.id.textView_statu);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.textViewData.setText(dataset.get(position).get("data").toString());
//		String bit = dataset.get(position).get("imageUrl").toString();
		
//		new ImageTask().execute(bit);
//		Log.d(TAG, bit);
		
		//holder.imageViewFour.setImageBitmap((Bitmap) );
		holder.textViewHighTemp.setText(dataset.get(position).get("high").toString());
		holder.textViewLowTemp.setText(dataset.get(position).get("low").toString());
	    String stat=dataset.get(position).get("statu").toString();
		holder.textViewStatu.setText(stat);
		holder.imageViewFour.setImageResource(images.get(stat));
//		if (stat.equals("晴间多云")) {
//			holder.imageViewFour.setImageResource(images[1]);
//			
//		}
//		else if(stat.equals("可能有雨")){
//			holder.imageViewFour.setImageResource(images[0]);
//
//		}
		
		return convertView;
	}
	
	
	
	
	
	
	
	

}
