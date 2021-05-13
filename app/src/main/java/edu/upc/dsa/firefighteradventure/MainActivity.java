package edu.upc.dsa.firefighteradventure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Locale;

import androidx.preference.PreferenceManager;
import edu.upc.dsa.firefighteradventure.models.GameSettings;
import edu.upc.dsa.firefighteradventure.models.Item;
import edu.upc.dsa.firefighteradventure.models.Map;
import edu.upc.dsa.firefighteradventure.models.User;
import edu.upc.dsa.firefighteradventure.models.UserSettings;
import edu.upc.dsa.firefighteradventure.services.GameService;
import edu.upc.dsa.firefighteradventure.services.ShopService;
import edu.upc.dsa.firefighteradventure.services.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final boolean remote_machine = true;

    private static final String remote_ip = "147.83.7.207";
    private static final int remote_port = 8080;

    private static final String local_ip = "10.0.2.2";
    private static final int local_port = 8080;

    private String base_url;

    private static final String api_name = "api";

    private UserSettings userSettings;
    private GameSettings gameSettings;

    private UserService userService;
    private GameService gameService;
    private ShopService shopService;

    private ProgressBar progressBar;
    private ConstraintLayout clLoading;

    private boolean backActivated;
    private boolean loadingData;

    private List<Map> gameMaps;
    private List<Item> shopItems;

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getChooseAppLanguage()) {

            Locale locale = new Locale(getSavedLanguage());
            setLocale(locale);

        } else {

            setLocale(new Locale(Locale.getDefault().getDisplayLanguage()));

        }

        progressBar = findViewById(R.id.progressBar);
        clLoading = findViewById(R.id.clLoading);

        setBackActivated(false);
        startServices();

    }

    public void setLocale(Locale locale) {

        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

    }

    private void startServices() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Retrofit retrofit;

        if (remote_machine) {

            this.base_url = "http://" + remote_ip + ":" + remote_port;

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + remote_ip + ":" + remote_port + "/" + api_name + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        } else {

            this.base_url = "http://" + local_ip + ":" + local_port;

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + local_ip + ":" + local_port + "/" + api_name + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }

        userService = retrofit.create(UserService.class);
        gameService = retrofit.create(GameService.class);
        shopService = retrofit.create(ShopService.class);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {

            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {

                hideKeyboard(this);

            }

        }

        return super.dispatchTouchEvent(ev);

    }

    private void hideKeyboard(Activity activity) {

        if (activity != null && activity.getWindow() != null) {

            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {

                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);

            }

        }

    }

    public String getSavedLanguage() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("savedLanguage", Locale.getDefault().getDisplayLanguage());

    }

    public void setSavedLanguage(String language) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedLanguage", language);
        editor.apply();

    }

    public boolean getChooseAppLanguage() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("chooseAppLanguage", true);

    }

    public void setChooseAppLanguage(boolean useAndroidSystemLanguage) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("chooseAppLanguage", useAndroidSystemLanguage);
        editor.apply();

    }



    public String getSavedPassword() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("password", "");

    }


    public void setSavedUsername(String username) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.apply();

    }

    public String getSavedUsername() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("username", "");

    }

    public void setSavedPassword(String password) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("password", password);
        editor.apply();

    }

    public UserService getUserService() {

        return userService;

    }

    public GameService getGameService() {

        return gameService;

    }

    public void setLoadingData(boolean loadingData) {

        if (loadingData) {

            this.loadingData = true;

            progressBar.setProgress(0);
            clLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        } else {

            progressBar.setVisibility(View.GONE);
            clLoading.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            this.loadingData = false;

        }

    }



    @Override
    public void onBackPressed() {

        if (isBackActivated() && !isLoadingData()) {

            super.onBackPressed();

        }

    }

    public void goBack() {

        super.onBackPressed();

    }

    public boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

    public void restart(){

        Intent intent = getIntent();
        this.finish();
        startActivity(intent);

    }

    public InputFilter spaceFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    public ShopService getShopService() {
        return shopService;
    }

    public boolean isBackActivated() {
        return backActivated;
    }

    public void setBackActivated(boolean backActivated) {
        this.backActivated = backActivated;
    }

    public boolean isLoadingData() {
        return loadingData;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public int getMaxHealth(int defense) {

        int step = (this.gameSettings.getMax_health() - this.gameSettings.getMin_health())
                / this.gameSettings.getMax_defense();

        return (this.gameSettings.getMin_health() + step * defense);


    }

    public List<Map> getGameMaps() {
        return gameMaps;
    }

    public void setGameMaps(List<Map> gameMaps) {
        this.gameMaps = gameMaps;
    }

    public List<Item> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<Item> gameItems) {
        this.shopItems = gameItems;
    }

    public Map getMapById(int id) {

        for (Map map : this.gameMaps) {

            if (map.getId() == id) {

                return map;

            }

        }

        return null;

    }


    public Item getItemById(int id) {

        for (Item item : this.shopItems) {

            if (item.getId() == id) {

                return item;

            }

        }

        return null;

    }

    public String getBase_url() {
        return base_url;
    }
}

