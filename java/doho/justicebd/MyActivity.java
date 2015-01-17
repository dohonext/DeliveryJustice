package doho.justicebd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyActivity extends Activity implements OnClickListener, LocationListener {

    // GPS fields
    private LocationManager locManager;
    private double longitude;
    private double latitude;
    private String longitudeStr;
    private String latitudeStr;
    private Location last;
    private Location loc;

    // WebView fields
    private WebView webView1;

    // Layout fields
    private LinearLayout layoutHome;
    private Button home;
    private Button menu;
    private Button gps;
    private Button about;
    private ImageView food1;
    private ImageView food2;
    private ImageView food3;
    private ImageView food4;
    private ImageView food5;
    private ImageView food6;
    private ImageView food7;
    private ImageView food8;

    // Query fields
    private String[] queryKeyword = {"치킨배달", "중국집", "피자배달", "족발 보쌈", "도시락배달", "야식배달", "햄버거배달", "한식배달"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        gpsInit();
        webViewInit();
        layoutInit();
        setEventHandlers();
    }

    protected void gpsInit() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        last = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (last == null) {
            last = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        latitude = last.getLatitude();
        longitude = last.getLongitude();
        longitudeStr = String.valueOf(longitude);
        latitudeStr = String.valueOf(latitude);
    }

    protected void webViewInit(){
        webView1 = (WebView)findViewById(R.id.webView);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webView1.setWebViewClient(new MyWebViewClient());
        webView1.loadUrl("");
    }

    protected void layoutInit(){
        layoutHome = (LinearLayout)findViewById(R.id.layout_home);

        home = (Button)findViewById(R.id.home);
        menu = (Button)findViewById(R.id.menu);
        gps = (Button)findViewById(R.id.gps);
        about = (Button)findViewById(R.id.about);

        food1 = (ImageView)findViewById(R.id.food1);
        food2 = (ImageView)findViewById(R.id.food2);
        food3 = (ImageView)findViewById(R.id.food3);
        food4 = (ImageView)findViewById(R.id.food4);
        food5 = (ImageView)findViewById(R.id.food5);
        food6 = (ImageView)findViewById(R.id.food6);
        food7 = (ImageView)findViewById(R.id.food7);
        food8 = (ImageView)findViewById(R.id.food8);
    }

    protected void setEventHandlers(){
        home.setOnClickListener(this);
        menu.setOnClickListener(this);
        gps.setOnClickListener(this);
        about.setOnClickListener(this);

        food1.setOnClickListener(this);
        food2.setOnClickListener(this);
        food3.setOnClickListener(this);
        food4.setOnClickListener(this);
        food5.setOnClickListener(this);
        food6.setOnClickListener(this);
        food7.setOnClickListener(this);
        food8.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                clickHomeOrMenu();
                break;
            case R.id.menu:
                clickHomeOrMenu();
                break;
            case R.id.gps:
                clickGps();
                break;
            case R.id.about:
                clickAbout();
                break;
            case R.id.food1:
                clickFood(queryKeyword[0]);
                break;
            case R.id.food2:
                clickFood(queryKeyword[1]);
                break;
            case R.id.food3:
                clickFood(queryKeyword[2]);
                break;
            case R.id.food4:
                clickFood(queryKeyword[3]);
                break;
            case R.id.food5:
                clickFood(queryKeyword[4]);
                break;
            case R.id.food6:
                clickFood(queryKeyword[5]);
                break;
            case R.id.food7:
                clickFood(queryKeyword[6]);
                break;
            case R.id.food8:
                clickFood(queryKeyword[7]);
                break;
            default:
                break;
        }
    }

    protected void clickHomeOrMenu(){
        sleep(300);
        layoutHome.setVisibility(View.VISIBLE);
    }

    protected void clickGps() {
        loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if ( loc == null) {
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        Toast.makeText(this.getApplicationContext(),"위치재탐색\n위도:"+loc.getLatitude()+" "+"경도:"+loc.getLongitude(), Toast.LENGTH_LONG).show();
        longitude = loc.getLongitude();
        latitude = loc.getLatitude();
        longitudeStr = String.valueOf(longitude);
        latitudeStr = String.valueOf(latitude);
    }

    protected void clickAbout() {
        // TODO
    }

    protected void clickFood(String queryKeyword) {
        webView1.loadUrl("http://m.map.naver.com/search.nhn?query="+ queryKeyword +"&sm=clk&centerCoord="+latitudeStr+":"+longitudeStr+"&type=SITE_1&siteSort=1");
        layoutHome.setVisibility(View.INVISIBLE);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //Enable phone calls when Phone numbers on webview are clicked.
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse(url));
                startActivity(intent);
            }else if(url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        longitudeStr = String.valueOf(longitude);
        latitudeStr = String.valueOf(latitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        last = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void sleep(int i){
        try{
            Thread.sleep(i);
        }
        catch(InterruptedException e) {}
    }

}
