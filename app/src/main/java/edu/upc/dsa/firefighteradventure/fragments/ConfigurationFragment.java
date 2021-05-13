package edu.upc.dsa.firefighteradventure.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;

public class ConfigurationFragment extends PreferenceFragmentCompat {

    MainActivity mainActivity;
    ListPreference listPreference;
    Preference btnBackPreferences;
    SwitchPreferenceCompat switchPreference;
    PreferenceCategory preferenceCategory;

    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.fragment_preferences, rootKey);

    }

    public void refreshListPreferenceTitle() {

        System.out.println(mainActivity.getSavedLanguage());

        switch (mainActivity.getSavedLanguage()) {

            case "en":
                listPreference.setTitle(getString(R.string.selected_language_string) + " " + getResources().getStringArray(R.array.app_language)[0]);
                break;
            case "ca":
                listPreference.setTitle(getString(R.string.selected_language_string) + " " + getResources().getStringArray(R.array.app_language)[1]);
                break;
            case "es":
                listPreference.setTitle(getString(R.string.selected_language_string) + " " + getResources().getStringArray(R.array.app_language)[2]);
                break;

        }

        listPreference.setDialogTitle(R.string.choose_a_language_string);
        listPreference.setEntries(getResources().getStringArray(R.array.app_language));

        btnBackPreferences.setTitle(R.string.back_string);
        preferenceCategory.setTitle(R.string.language_string);
        switchPreference.setTitle(R.string.choose_app_language_string);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mainActivity = (MainActivity) getActivity();
        listPreference = findPreference("savedLanguage");
        preferenceCategory = findPreference("languageCategory");
        btnBackPreferences = findPreference("btnBackPreferences");
        switchPreference = findPreference("chooseAppLanguage");

        btnBackPreferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mainActivity.goBack();
                return false;
            }
        });

        mainActivity.setBackActivated(true);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (mainActivity.getChooseAppLanguage()) {

                    mainActivity.setLocale(new Locale(mainActivity.getSavedLanguage()));

                } else {

                    mainActivity.setLocale(new Locale(Locale.getDefault().getDisplayLanguage()));

                }

                refreshListPreferenceTitle();

            }

        };

        PreferenceManager.getDefaultSharedPreferences(mainActivity).registerOnSharedPreferenceChangeListener(listener);

        refreshListPreferenceTitle();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(mainActivity).unregisterOnSharedPreferenceChangeListener(listener);

    }
}