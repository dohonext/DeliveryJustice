package doho.justicebd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyActivity extends Activity implements OnClickListener, LocationListener {

    // [GPS fields]
    private LocationManager locManager;
    private double longitude;
    private double latitude;
    private String longitudeStr;
    private String latitudeStr;
    private Location last;

    // [WebView fields]
    private WebView webView1;
    private String currentUrl;

    // [Layout fields]
    private LinearLayout layoutHome;
    private ImageView home;
    private ImageView about;
    private ImageView food1;
    private ImageView food2;
    private ImageView food3;
    private ImageView food4;
    private ImageView food5;
    private ImageView food6;
    private ImageView food7;
    private ImageView food8;
    private ImageView splash;
  //private ImageView gps;
    private AlphaAnimation alphaAnimation;

    // [Query fields]
    private String[] queryKeyword = {"중국집", "치킨배달", "피자배달", "한식배달", "도시락배달", "족발 보쌈", "야식배달"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        showLogo();
        gpsInit();
        webViewInit();
        layoutInit();
        setEventHandlers();
    }

    protected void showLogo() {
        splash = (ImageView)findViewById(R.id.splash);
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setStartOffset(1000);
        aa.setDuration(1000);
        aa.setFillAfter(true);
        splash.startAnimation(aa);
        splash.setVisibility(View.INVISIBLE);
    }

    protected void gpsInit() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        try {
            last = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (last == null) {
                last = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (last == null) {
                    Toast.makeText(this.getApplicationContext(), "휴대폰 GPS(위치정보)를 활성화 시켜주세요", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            latitude = last.getLatitude();
            longitude = last.getLongitude();
            longitudeStr = String.valueOf(longitude);
            latitudeStr = String.valueOf(latitude);
        } catch (Exception e) {
        }

    }

    protected void webViewInit(){
        webView1 = (WebView)findViewById(R.id.webView);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setDomStorageEnabled(true);
        webView1.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webView1.setWebViewClient(new MyWebViewClient());
        webView1.loadUrl("");
    }

    protected void layoutInit(){
        layoutHome = (LinearLayout)findViewById(R.id.layout_home);

        home = (ImageView)findViewById(R.id.home);
        about = (ImageView)findViewById(R.id.about);
        food1 = (ImageView)findViewById(R.id.food1);
        food2 = (ImageView)findViewById(R.id.food2);
        food3 = (ImageView)findViewById(R.id.food3);
        food4 = (ImageView)findViewById(R.id.food4);
        food5 = (ImageView)findViewById(R.id.food5);
        food6 = (ImageView)findViewById(R.id.food6);
        food7 = (ImageView)findViewById(R.id.food7);
        food8 = (ImageView)findViewById(R.id.food8);

        alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1);
        alphaAnimation.setStartOffset(700);
    }

    protected void setEventHandlers(){
        home.setOnClickListener(this);
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
                clickHome();
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
                clickFastFood();
                break;
            default:
                break;
        }
    }

    protected void clickHome(){
        sleep(200);
        layoutHome.setVisibility(View.VISIBLE);
    }

    protected void clickAbout() {
        webView1.loadUrl("file:///android_asset/about.html");
        layoutHome.setVisibility(View.INVISIBLE);
    }

    protected void clickFood(String queryKeyword) {
        webView1.clearHistory();
        webView1.loadUrl("http://m.map.naver.com/search.nhn?query=" + queryKeyword + "&sm=clk&centerCoord=" + latitudeStr + ":" + longitudeStr + "&type=SITE_1&siteSort=1");
        layoutHome.startAnimation(alphaAnimation);
        layoutHome.setVisibility(View.INVISIBLE);
    }

    protected void clickFastFood(){
        webView1.loadUrl("file:///android_asset/fastfood.html");
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

        @Override
        public void onPageFinished(WebView view, String url) {
            //Apply the custom CSS to m.naver.com (removing some unnecessary elements). footer, header, form#searchTop, .geolc, .sc_sort, .scf
            //From the Android Ver.KITKAT, it uses Chromium webview. Need to control it.
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.KITKAT){
                webView1.loadUrl("javascript:" +
                        "try{alert(document.getElementsByClassName('geolc')[0].style.display='none'); } catch(exception){};" +
                        "try{alert(document.getElementsByClassName('sc_sort')[0].style.display='none'); } catch(exception){};" +
                        "try{alert(document.getElementsByClassName('scf')[0].style.display='none'); } catch(exception){};" +
                        "try{alert(document.getElementsByClassName('rw')[0].style.display='none'); } catch(exception){};" +
                        "try{alert(document.getElementById('searchTop')[0].style.display='none'); } catch(exception){}" +
                        "finally{ alert(document.getElementById('header').style.display='none');" +
                        "alert(document.getElementById('footer').style.display='none');}");
            } else{
                webView1.loadUrl("javascript:" +
                "try{document.getElementsByClassName('geolc')[0].style.display='none'; } catch(exception){};" +
                "try{document.getElementsByClassName('sc_sort')[0].style.display='none'; } catch(exception){};" +
                "try{document.getElementsByClassName('scf')[0].style.display='none'; } catch(exception){};" +
                "try{document.getElementsByClassName('rw')[0].style.display='none'; } catch(exception){};" +
                "try{document.getElementById('searchTop')[0].style.display='none'; } catch(exception){}" +
                "finally{ document.getElementById('header').style.display='none';" +
                        "document.getElementById('footer').style.display='none';}");
            }
//                    "document.getElementById('footer').style.display='none';" +
//                    "document.getElementById('header').style.display='none';" +
//                    "document.getElementById('searchTop')[0].style.display='none';" +
//                    "document.getElementsByClassName('geolc')[0].style.display='none';" +
//                    "document.getElementsByClassName('sc_sort')[0].style.display='none';");

// TODO : Refactoring
//                    "var aElements = [ document.getElementById('header'), document.getElementById('footer'), document.getElementById('searchTop'), document.getElementsByClassName('geolc')[0], " +
//                                     "document.getElementsByClassName('sc_sort')[0], document.getElementsByClassName('scf')[0], document.getElementsByClassName('rw')[0] ];" +
//                    "var iArrayLength = aElements.length;" +
//                    "for(var i = 0; i < iArrayLength; i++){" +
//                        "if(aElements[i]){aElements[i].style.display='none';}" +
//                    "}");
        }
    }

    @Override
    public void onBackPressed() {
        if(layoutHome.getVisibility() == View.VISIBLE) {
            this.finish();
        }
        currentUrl = webView1.getUrl();
        if(currentUrl.substring(0, 29).matches("http://m.map.naver.com/search") || currentUrl.substring(0, 21).matches("file:///android_asset")) {
            clickHome();
        }else {
            webView1.goBack();
        }
    }

    @Override
    public void finish() {
        System.runFinalizersOnExit(true) ;
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
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
    public void onStop() {
        //Stop using Gps module when the App goes background.
        super.onStop();
        locManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        //Restart using Gps module when the App goes foreground again.
        super.onResume();
        gpsInit();
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
