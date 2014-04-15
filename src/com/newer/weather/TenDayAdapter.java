package com.newer.weather;

import java.util.HashMap;
import java.util.List;

import com.newer.weather.Myweenkdayadapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TenDayAdapter extends BaseAdapter{
private List<HashMap<String, Object>> dataThree;
private Context context;
private LayoutInflater layoutInflater;
private HashMap<String, Integer> images;
	public TenDayAdapter(Context context,
			List<HashMap<String, Object>> dataThree, HashMap<String, Integer> images) {
		this.context=context;
		this.dataThree=dataThree;
		this.images = images;
		layoutInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dataThree.size();
	}

	@Override
	public Object getItem(int position) {
		return dataThree.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
  static class ViewHolder{
	 TextView textViewDayNight;
	 TextView textViewTemprature;
	 ImageView imageViewTempImage;
	 ImageView imageViewRainy;
	 TextView textViewRainRate;
  }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null==convertView) {
			convertView=layoutInflater.inflate(R.layout.list_48hourweather_item, null);
			holder=new ViewHolder();
			holder.textViewDayNight=(TextView) convertView.findViewById(R.id.textView_reday_night);
			holder.textViewTemprature=(TextView) convertView.findViewById(R.id.textView_retemprature);
			holder.imageViewTempImage=(ImageView) convertView.findViewById(R.id.imageView_retemp_image);
			holder.imageViewRainy = (ImageView) convertView.findViewById(R.id.imageView_rerainy);
			holder.textViewRainRate =(TextView) convertView.findViewById(R.id.textView_rerain_rate);
			convertView.setTag(holder);
			
			
		}else{
		holder=(ViewHolder) convertView.getTag();
		}
		holder.textViewDayNight.setText(dataThree.get(position).get("day").toString());
		holder.textViewTemprature.setText(dataThree.get(position).get("temprature").toString());
		String sta = dataThree.get(position).get("sta").toString();
		holder.imageViewTempImage.setImageResource(images.get(sta));
		holder.imageViewRainy.setImageResource(R.drawable.umbrella);
		holder.textViewRainRate.setText(dataThree.get(position).get("rainRate").toString());
		return convertView;
	}

}
