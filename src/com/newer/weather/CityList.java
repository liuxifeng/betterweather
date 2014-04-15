package com.newer.weather;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;



@Root(name="china")
public class CityList {

	@Element
	private City city;
	@Attribute
	private String dn;
	private List<City> cities;
	public CityList() {
		cities = new ArrayList<City>();
	}

	public String getDn() {
		return dn;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}
	
	public void addCity(City city) {
		cities.add(city);
	}

	@Override
	public String toString() {
		return "CityList [city=" + city + ", dn=" + dn + "]";
	}

	
//	public City get(int index){
//		return cityList.get(index);
//	}
//	
//	public int size() {
//		return cityList.size();
//	}
//	
//	public void set(int index, City element) {
//		cityList.set(index, element);
//	}

	
	
	
	
	
}
