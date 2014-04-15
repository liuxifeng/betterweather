package com.newer.weather;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

	private ListPreference listPreferenceUpdateRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		listPreferenceUpdateRate = (ListPreference) findPreference("data_update_rate");

		listPreferenceUpdateRate.setOnPreferenceChangeListener(this);

		listPreferenceUpdateRate
				.setSummary(listPreferenceUpdateRate.getEntry());
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {

		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue((String) newValue);
			listPreference.setSummary(listPreference.getEntries()[index]);
			return true;
		}

		preference.setSummary((CharSequence) newValue);
		return true;
	}

}

