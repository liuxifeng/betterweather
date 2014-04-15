package com.newer.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.newer.weather.MainActivity.DumSectionFragment;
import com.newer.weather.MainActivity.ListSectionFragment;
import com.newer.weather.MainActivity.ThreeFrament;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailWeatherActivity extends FragmentActivity{


	private DetailSectionFragment detailSectionFragment;
	private DetailSectionTwoFragment detailSectionTwoFragment;
	private DetailSectionThreeFragment detailSectionThreeFragment;
	SectionsPagerAdapter mSectionsPagerAdapter;	
	ViewPager mViewPager;
	
	private List<HashMap<String, Object>> data = MainActivity.data;
	private int position;

	private HashMap<String, Integer> map = MainActivity.images;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		public Fragment getItem(int position) {
			
			Fragment fragment = null;
			switch (position) {
			case 0:

				
				detailSectionFragment = new DetailSectionFragment();
				fragment = detailSectionFragment;	
				mSectionsPagerAdapter.notifyDataSetChanged();
				
				break;

			case 1:


				detailSectionTwoFragment = new DetailSectionTwoFragment();
				fragment = detailSectionTwoFragment;
			
				break;
			case 2:
				detailSectionThreeFragment = new DetailSectionThreeFragment();
				fragment = detailSectionThreeFragment;
				break;
			}

			Bundle args = new Bundle();
			args.putInt(DetailSectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			mSectionsPagerAdapter.notifyDataSetChanged();
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int p) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				//return getString(R.string.title_section4).toUpperCase(l);
			return data.get(position).get("day").toString();
			case 1:
				return "星期六";
			case 2:
				return "星期七";
			}
			return null;
		}
	}

	/**
	 * 1
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class DetailSectionFragment extends Fragment {
		private TextView textViewDetailUpdate;
		private ImageView imageViewDetail;
		private TextView textViewDetailLow;
		private TextView textViewDetailHigh;
		private TextView textViewDetailStatu;
		private ListView listViewDetail;
		
	
		public static final String ARG_SECTION_NUMBER = "detail_section_number";

		public DetailSectionFragment() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_detail,
					container, false);
			imageViewDetail = (ImageView) rootView.findViewById(R.id.imageView_detail_img);
			textViewDetailLow = (TextView) rootView.findViewById(R.id.textView_detail_low);
			textViewDetailHigh = (TextView) rootView.findViewById(R.id.textView_detail_high);
			textViewDetailStatu = (TextView) rootView.findViewById(R.id.textView_detail_statu);
			listViewDetail = (ListView) rootView.findViewById(R.id.listView_detail);
			
			Intent intent = new Intent();
			position = intent.getIntExtra("name", 0);
			String statu = data.get(position).get("stat").toString();
			imageViewDetail.setImageResource(map.get(statu));
			textViewDetailLow.setText(data.get(position).get("temprature").toString());
			textViewDetailStatu.setText(statu);
			
			return rootView;
		}
	}
	
	/**
	 * 2
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class DetailSectionTwoFragment extends Fragment {
		private TextView textViewDetailUpdate;
		private ImageView imageViewDetail;
		private TextView textViewDetailLow;
		private TextView textViewDetailHigh;
		private TextView textViewDetailStatu;
		private ListView listViewDetail;
		
	
		public static final String ARG_SECTION_NUMBER_TWO = "detail_section_number_two";

		public DetailSectionTwoFragment() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_detail,
					container, false);
			imageViewDetail = (ImageView) rootView.findViewById(R.id.imageView_detail_img);
			textViewDetailLow = (TextView) rootView.findViewById(R.id.textView_detail_low);
			textViewDetailHigh = (TextView) rootView.findViewById(R.id.textView_detail_high);
			textViewDetailStatu = (TextView) rootView.findViewById(R.id.textView_detail_statu);
			listViewDetail = (ListView) rootView.findViewById(R.id.listView_detail);
			
			
			String statu = data.get(position+2).get("stat").toString();
			imageViewDetail.setImageResource(map.get(statu));
			textViewDetailLow.setText(data.get(position+2).get("temprature").toString());
			textViewDetailStatu.setText(statu);
			
			return rootView;
		}
	}
	
	/**
	 * 3
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class DetailSectionThreeFragment extends Fragment {
		private TextView textViewDetailUpdate;
		private ImageView imageViewDetail;
		private TextView textViewDetailLow;
		private TextView textViewDetailHigh;
		private TextView textViewDetailStatu;
		private ListView listViewDetail;
		
	
		public static final String ARG_SECTION_NUMBER_THREE = "detail_section_number_three";

		public DetailSectionThreeFragment() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_detail,
					container, false);
			imageViewDetail = (ImageView) rootView.findViewById(R.id.imageView_detail_img);
			textViewDetailLow = (TextView) rootView.findViewById(R.id.textView_detail_low);
			textViewDetailHigh = (TextView) rootView.findViewById(R.id.textView_detail_high);
			textViewDetailStatu = (TextView) rootView.findViewById(R.id.textView_detail_statu);
			listViewDetail = (ListView) rootView.findViewById(R.id.listView_detail);
			
			
			String statu = data.get(position+4).get("stat").toString();
			imageViewDetail.setImageResource(map.get(statu));
			textViewDetailLow.setText(data.get(position+4).get("temprature").toString());
			textViewDetailStatu.setText(statu);
			
			return rootView;
		}
	}
	
}
