package com.newer.weather;

public class CurrentWeatherInfo {

	private String local_tz_offset;
	private String weather;
	private String temperature_string;
	private String temp_f;
	private String temp_c;
	private String relative_humidity;
	private String wind_string;
	private String wind_dir;
	private String wind_degrees;
	
	public CurrentWeatherInfo() {
		// TODO Auto-generated constructor stub
	}

	public CurrentWeatherInfo(String local_tz_offset, String weather,
			String temperature_string, String temp_f, String temp_c,
			String relative_humidity, String wind_string, String wind_dir,
			String wind_degrees) {
		super();
		this.local_tz_offset = local_tz_offset;
		this.weather = weather;
		this.temperature_string = temperature_string;
		this.temp_f = temp_f;
		this.temp_c = temp_c;
		this.relative_humidity = relative_humidity;
		this.wind_string = wind_string;
		this.wind_dir = wind_dir;
		this.wind_degrees = wind_degrees;
	}








	public String getLocal_tz_offset() {
		return local_tz_offset;
	}

	public void setLocal_tz_offset(String local_tz_offset) {
		this.local_tz_offset = local_tz_offset;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getTemperature_string() {
		return temperature_string;
	}



	public void setTemperature_string(String temperature_string) {
		this.temperature_string = temperature_string;
	}



	public String getTemp_f() {
		return temp_f;
	}

	public void setTemp_f(String temp_f) {
		this.temp_f = temp_f;
	}

	public String getTemp_c() {
		return temp_c;
	}

	public void setTemp_c(String temp_c) {
		this.temp_c = temp_c;
	}

	public String getRelative_humidity() {
		return relative_humidity;
	}

	public void setRelative_humidity(String relative_humidity) {
		this.relative_humidity = relative_humidity;
	}

	public String getWind_string() {
		return wind_string;
	}

	public void setWind_string(String wind_string) {
		this.wind_string = wind_string;
	}

	public String getWind_dir() {
		return wind_dir;
	}

	public void setWind_dir(String wind_dir) {
		this.wind_dir = wind_dir;
	}

	public String getWind_degrees() {
		return wind_degrees;
	}

	public void setWind_degrees(String wind_degrees) {
		this.wind_degrees = wind_degrees;
	}



	@Override
	public String toString() {
		return "CurrentWeatherInfo [local_tz_offset=" + local_tz_offset
				+ ", weather=" + weather + ", temperature_string="
				+ temperature_string + ", temp_f=" + temp_f + ", temp_c="
				+ temp_c + ", relative_humidity=" + relative_humidity
				+ ", wind_string=" + wind_string + ", wind_dir=" + wind_dir
				+ ", wind_degrees=" + wind_degrees + "]";
	}



	
	
	
}
