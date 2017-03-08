package mbp.alexpon.com.hkbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MapsActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;
    private double tripLongitude;
    private double tripLatitude;
    private int path;
    private Intent intent;
    private Bundle bundle;
    private Button button;
    private TextView distance;
    private double dis_sum;
    private final String TAG = "MyAwesomeApp";

    private SharedPreferences sharedPrefs;
    private boolean is_food_on;
    private boolean is_place_on;
    private boolean is_bike_on;
    private boolean is_path_on;
    private boolean stop = false;

    private String[] property;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<Double> Pathnewlat;//緯度
    private ArrayList<Double> Pathnewlog;//經度

    private Marker[] marker = new Marker[30];
    private UserLocalStore userLocalStore;

    Double pre_lat;
    Double pre_log;
    double  lat[];
    double  log[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_maps);
        initViews();
        setListener();
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setUpMapIfNeeded();
        setDrawer();
    }

    public void initViews() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        tripLongitude = bundle.getDouble("KEY_LONGITUDE");
        tripLatitude = bundle.getDouble("KEY_LATITUDE");
        path = bundle.getInt("KEY_PATH");
        button = (Button) findViewById(R.id.button);
        distance = (TextView) findViewById(R.id.distance);
        dis_sum = 0;

        property = getResources().getStringArray(R.array.property);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, property);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.map_drawer);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

        userLocalStore = new UserLocalStore(this);
        Pathnewlat = new ArrayList<Double>();
        Pathnewlog = new ArrayList<Double>();
    }

    public void setListener() {
        button.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CaptureMapScreen();
        }
    };

    private void setDrawer() {
        toolbar.setTitle("Show on Screen");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(new DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    SharedPreferences.Editor spEditor = sharedPrefs.edit();
                    spEditor.putBoolean("path", true);
                    spEditor.commit();
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    SharedPreferences.Editor spEditor2 = sharedPrefs.edit();
                    spEditor2.putBoolean("path", false);
                    spEditor2.commit();
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    Intent preIntent = new Intent();
                    preIntent.setClass(MapsActivity.this, PreferenceActivity.class);
                    startActivity(preIntent);
                    mDrawerLayout.closeDrawers();
                    break;
                default:

                    break;
            }
        }
    }

    public void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();
                Intent intent1 = new Intent(MapsActivity.this, PictureShare.class);
                intent1.putExtra("image", bitmapByte);
                startActivity(intent1);
            }
        };

        mMap.snapshot(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_food_on = sharedPrefs.getBoolean("food", true);
        is_place_on = sharedPrefs.getBoolean("place", true);
        is_bike_on = sharedPrefs.getBoolean("bicycle", true);
        is_path_on = sharedPrefs.getBoolean("path", false);
        setUpMapIfNeeded();
        setMarkerVisible();
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        LatLng place = new LatLng(tripLatitude, tripLongitude);
        moveMap(place);
        setPath();
        setPlace();
    }


    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();
        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setPath() {
        switch (path) {
            case 1:
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(22.448013, 114.049322))
                        .add(new LatLng(22.447988, 114.049489))
                        .add(new LatLng(22.447295, 114.049312))
                        .add(new LatLng(22.446862, 114.049184))
                        .add(new LatLng(22.446606, 114.049077))
                        .add(new LatLng(22.446351, 114.048951))
                        .add(new LatLng(22.44604, 114.048807))
                        .add(new LatLng(22.445653, 114.04853))
                        .add(new LatLng(22.445097, 114.048026))
                        .add(new LatLng(22.444648, 114.047559))
                        .add(new LatLng(22.444382, 114.047181))
                        .add(new LatLng(22.444277, 114.046688))
                        .add(new LatLng(22.444181, 114.046188))
                        .add(new LatLng(22.444144, 114.045507))
                        .add(new LatLng(22.444167, 114.045063))
                        .add(new LatLng(22.444301, 114.044289))
                        .add(new LatLng(22.444397, 114.043694))
                        .add(new LatLng(22.444441, 114.042999))
                        .add(new LatLng(22.444443, 114.04292))
                        .add(new LatLng(22.444431, 114.042852))
                        .add(new LatLng(22.444543, 114.040862))
                        .add(new LatLng(22.444625, 114.040762))
                        .add(new LatLng(22.444537, 114.040685))
                        .add(new LatLng(22.444568, 114.039968))
                        .add(new LatLng(22.44458, 114.039821))
                        .add(new LatLng(22.444561, 114.039615))
                        .add(new LatLng(22.444523, 114.039472))
                        .add(new LatLng(22.444505, 114.039248))
                        .add(new LatLng(22.444663, 114.038655))
                        .add(new LatLng(22.444633, 114.038419))
                        .add(new LatLng(22.44456, 114.037845))
                        .add(new LatLng(22.444478, 114.037709))
                        .add(new LatLng(22.4445, 114.037314))
                        .add(new LatLng(22.444548, 114.036137))
                        .add(new LatLng(22.444605, 114.036116))
                        .add(new LatLng(22.444613, 114.036022))
                        .add(new LatLng(22.444515, 114.035772))
                        .add(new LatLng(22.444535, 114.034293))
                        .add(new LatLng(22.444691, 114.033791))
                        .add(new LatLng(22.44487, 114.033375))
                        .add(new LatLng(22.445096, 114.033058))
                        .add(new LatLng(22.445291, 114.032993))
                        .add(new LatLng(22.445527, 114.032902))
                        .add(new LatLng(22.445927, 114.033658))
                        .add(new LatLng(22.446354, 114.03376))
                        .add(new LatLng(22.446526, 114.033831))
                        .add(new LatLng(22.44667, 114.033802))
                        .add(new LatLng(22.446794, 114.033916))
                        .add(new LatLng(22.447483, 114.034117))
                        .add(new LatLng(22.447959, 114.034084))
                        .add(new LatLng(22.448512, 114.033577))
                        .add(new LatLng(22.449219, 114.033395))
                        .add(new LatLng(22.449556, 114.033285))
                        .add(new LatLng(22.450456, 114.033478))
                        .add(new LatLng(22.45146, 114.033768))
                        .add(new LatLng(22.451502, 114.034607))
                        .add(new LatLng(22.451737, 114.034838))
                        .add(new LatLng(22.452427, 114.035262))
                        .add(new LatLng(22.45251, 114.03553))
                        .add(new LatLng(22.452607, 114.035407))
                        .add(new LatLng(22.45328, 114.035101))
                        .add(new LatLng(22.453332, 114.034868))
                        .add(new LatLng(22.453939, 114.034753))
                        .add(new LatLng(22.454642, 114.034967))
                        .add(new LatLng(22.455373, 114.035452))
                        .add(new LatLng(22.455433, 114.035322))
                        .add(new LatLng(22.455546, 114.03499))
                        .add(new LatLng(22.455645, 114.034811))
                        .add(new LatLng(22.455743, 114.034714))
                        .add(new LatLng(22.458513, 114.034037))
                        .add(new LatLng(22.458496, 114.033526))
                        .add(new LatLng(22.459393, 114.033407))
                        .add(new LatLng(22.460731, 114.032719))
                        .add(new LatLng(22.462084, 114.031632))
                        .add(new LatLng(22.464587, 114.02949))
                        .add(new LatLng(22.465262, 114.029066))
                        .add(new LatLng(22.466671, 114.028985))
                        .add(new LatLng(22.468838, 114.029698))
                        .add(new LatLng(22.469443, 114.030736))
                        .add(new LatLng(22.469448, 114.031165))
                        .add(new LatLng(22.466352, 114.038728))
                        .add(new LatLng(22.465916, 114.039683))
                        .add(new LatLng(22.463169, 114.043502))
                        .add(new LatLng(22.461533, 114.044618))
                        .add(new LatLng(22.456804, 114.046345))
                        .add(new LatLng(22.453799, 114.047729))
                        .add(new LatLng(22.45257, 114.047472))
                        .add(new LatLng(22.448013, 114.049322))
                        .color(R.color.wallet_holo_blue_light));


                break;
            case 2:
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(22.467864, 114.003604))
                        .add(new LatLng(22.468649, 114.003162))
                        .add(new LatLng(22.469628, 114.002819))
                        .add(new LatLng(22.470233, 114.002593))
                        .add(new LatLng(22.471075, 114.002291))
                        .add(new LatLng(22.471242, 114.002196))
                        .add(new LatLng(22.471519, 114.002016))
                        .add(new LatLng(22.472206, 114.001142))
                        .add(new LatLng(22.472483, 114.000353))
                        .add(new LatLng(22.472121, 113.999768))
                        .add(new LatLng(22.471422, 113.998953))
                        .add(new LatLng(22.470877, 113.998588))
                        .add(new LatLng(22.469578, 113.99788))
                        .add(new LatLng(22.468607, 113.997424))
                        .add(new LatLng(22.467501, 113.996839))
                        .add(new LatLng(22.466118, 113.996255))
                        .add(new LatLng(22.464676, 113.995547))
                        .add(new LatLng(22.463689, 113.995074))
                        .add(new LatLng(22.463263, 113.99493))
                        .add(new LatLng(22.461691, 113.994613))
                        .add(new LatLng(22.460759, 113.994447))
                        .add(new LatLng(22.460407, 113.994399))
                        .add(new LatLng(22.459999, 113.994388))
                        .add(new LatLng(22.45914, 113.994481))
                        .add(new LatLng(22.458782, 113.994553))
                        .add(new LatLng(22.457556, 113.995153))
                        .add(new LatLng(22.456897, 113.995424))
                        .add(new LatLng(22.455184, 113.996453))
                        .add(new LatLng(22.454271, 113.997098))
                        .add(new LatLng(22.454076, 113.997272))
                        .add(new LatLng(22.453995, 113.997371))
                        .add(new LatLng(22.453896, 113.997375))
                        .add(new LatLng(22.45388, 113.997355))
                        .add(new LatLng(22.452089, 113.998425))
                        .add(new LatLng(22.451042, 113.998964))
                        .add(new LatLng(22.450878, 113.998996))
                        .add(new LatLng(22.450526, 113.998972))
                        .add(new LatLng(22.450006, 113.99885))
                        .add(new LatLng(22.449635, 113.999029))
                        .add(new LatLng(22.44944, 113.99909))
                        .add(new LatLng(22.449327, 113.999133))
                        .add(new LatLng(22.448999, 113.999307))
                        .add(new LatLng(22.448827, 113.999454))
                        .add(new LatLng(22.448286, 113.99973))
                        .add(new LatLng(22.447816, 113.999974))
                        .add(new LatLng(22.447729, 113.99999))
                        .add(new LatLng(22.447526, 113.999958))
                        .add(new LatLng(22.446916, 113.999971))
                        .add(new LatLng(22.44644, 113.999843))
                        .add(new LatLng(22.446294, 114.000406))
                        .add(new LatLng(22.4462, 114.001213))
                        .add(new LatLng(22.44624, 114.001892))
                        .add(new LatLng(22.446321, 114.002544))
                        .add(new LatLng(22.446968, 114.003268))
                        .add(new LatLng(22.447143, 114.003102))
                        .add(new LatLng(22.447191, 114.003054))
                        .add(new LatLng(22.447695, 114.003384))
                        .add(new LatLng(22.448523, 114.003357))
                        .add(new LatLng(22.44981, 114.002614))
                        .add(new LatLng(22.450611, 114.002172))
                        .add(new LatLng(22.450695, 114.0024))
                        .add(new LatLng(22.449354, 114.003121))
                        .add(new LatLng(22.448459, 114.003596))
                        .add(new LatLng(22.448159, 114.003891))
                        .add(new LatLng(22.448223, 114.004087))
                        .add(new LatLng(22.448367, 114.004191))
                        .add(new LatLng(22.449552, 114.00532))
                        .add(new LatLng(22.450685, 114.006332))
                        .add(new LatLng(22.451082, 114.006624))
                        .add(new LatLng(22.451097, 114.006793))
                        .add(new LatLng(22.451401, 114.007139))
                        .add(new LatLng(22.451414, 114.007337))
                        .add(new LatLng(22.451669, 114.008089))
                        .add(new LatLng(22.451818, 114.009202))
                        .add(new LatLng(22.451729, 114.00929))
                        .add(new LatLng(22.450945, 114.009454))
                        .add(new LatLng(22.450286, 114.00969))
                        .add(new LatLng(22.44985, 114.009939))
                        .add(new LatLng(22.44927, 114.010374))
                        .add(new LatLng(22.448372, 114.011296))
                        .add(new LatLng(22.447956, 114.011758))
                        .add(new LatLng(22.447626, 114.012254))
                        .add(new LatLng(22.447448, 114.012632))
                        .add(new LatLng(22.447277, 114.013212))
                        .add(new LatLng(22.4472, 114.014051))
                        .add(new LatLng(22.447195, 114.014909))
                        .add(new LatLng(22.447125, 114.015679))
                        .add(new LatLng(22.44715, 114.016098))
                        .add(new LatLng(22.447338, 114.016706))
                        .add(new LatLng(22.44804, 114.017889))
                        .add(new LatLng(22.448309, 114.018327))
                        .add(new LatLng(22.448336, 114.018353))
                        .add(new LatLng(22.448462, 114.018553))
                        .add(new LatLng(22.448475, 114.018607))
                        .add(new LatLng(22.448781, 114.019116))
                        .add(new LatLng(22.449095, 114.019867))
                        .add(new LatLng(22.449507, 114.020867))
                        .add(new LatLng(22.449707, 114.021276))
                        .add(new LatLng(22.449596, 114.021487))
                        .add(new LatLng(22.449382, 114.02139))
                        .add(new LatLng(22.448715, 114.021384))
                        .add(new LatLng(22.448256, 114.021573))
                        .add(new LatLng(22.447896, 114.021772))
                        .add(new LatLng(22.447829, 114.021938))
                        .add(new LatLng(22.447877, 114.022264))
                        .add(new LatLng(22.447862, 114.022417))
                        .add(new LatLng(22.447896, 114.023478))
                        .add(new LatLng(22.447899, 114.023901))
                        .add(new LatLng(22.448156, 114.024856))
                        .add(new LatLng(22.448202, 114.024969))
                        .add(new LatLng(22.448146, 114.024958))
                        .add(new LatLng(22.448232, 114.0251))
                        .add(new LatLng(22.448313, 114.025158))
                        .add(new LatLng(22.448386, 114.025178))
                        .add(new LatLng(22.448924, 114.02517))
                        .add(new LatLng(22.449333, 114.025314))
                        .add(new LatLng(22.450241, 114.025908))
                        .add(new LatLng(22.450625, 114.026159))
                        .add(new LatLng(22.450684, 114.026112))
                        .add(new LatLng(22.450836, 114.026211))
                        .add(new LatLng(22.450829, 114.026321))
                        .add(new LatLng(22.451635, 114.02697))
                        .add(new LatLng(22.451767, 114.027101))
                        .add(new LatLng(22.451624, 114.027331))
                        .add(new LatLng(22.451624, 114.027386))
                        .add(new LatLng(22.450875, 114.028482))
                        .add(new LatLng(22.450775, 114.028511))
                        .add(new LatLng(22.450684, 114.028583))
                        .add(new LatLng(22.450642, 114.028651))
                        .add(new LatLng(22.450634, 114.028768))
                        .add(new LatLng(22.450106, 114.029569))
                        .add(new LatLng(22.450792, 114.030047))
                        .add(new LatLng(22.450811, 114.030176))
                        .add(new LatLng(22.450926, 114.030248))
                        .add(new LatLng(22.450967, 114.030244))
                        .add(new LatLng(22.451366, 114.030564))
                        .add(new LatLng(22.451394, 114.03062))
                        .add(new LatLng(22.451579, 114.030742))
                        .add(new LatLng(22.451617, 114.030739))
                        .add(new LatLng(22.451934, 114.030966))
                        .add(new LatLng(22.45196, 114.031021))
                        .add(new LatLng(22.452047, 114.031087))
                        .add(new LatLng(22.452058, 114.03113))
                        .add(new LatLng(22.451757, 114.031662))
                        .add(new LatLng(22.45153, 114.031741))
                        .add(new LatLng(22.451317, 114.031696))
                        .add(new LatLng(22.451022, 114.032048))
                        .add(new LatLng(22.450678, 114.031769))
                        .add(new LatLng(22.450056, 114.031648))
                        .add(new LatLng(22.449446, 114.031187))
                        .add(new LatLng(22.448096, 114.030144))
                        .add(new LatLng(22.447915, 114.030015))
                        .add(new LatLng(22.44769, 114.029996))
                        .add(new LatLng(22.447578, 114.030034))
                        .add(new LatLng(22.447499, 114.030095))
                        .add(new LatLng(22.446948, 114.030782))
                        .add(new LatLng(22.446146, 114.031825))
                        .add(new LatLng(22.445732, 114.032413))
                        .add(new LatLng(22.445635, 114.032804))
                        .color(R.color.wallet_holo_blue_light));

                break;
            case 3:
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                                .add(new LatLng(22.461746, 113.994668)).add(new LatLng(22.461697, 113.993377))
                                .add(new LatLng(22.460898, 113.989810)).add(new LatLng(22.461390, 113.989185))
                                .add(new LatLng(22.462139, 113.988058)).add(new LatLng(22.467324, 113.984046))
                                .add(new LatLng(22.463874, 113.983724)).add(new LatLng(22.463328, 113.983209))
                                .add(new LatLng(22.462634, 113.981428)).add(new LatLng(22.461082, 113.980108))
                                .add(new LatLng(22.459149, 113.979400)).add(new LatLng(22.455282, 113.973843))
                                .add(new LatLng(22.454142, 113.972781)).add(new LatLng(22.450984, 113.964595))
                                .add(new LatLng(22.447970, 113.960443)).add(new LatLng(22.447632, 113.959070))
                                .add(new LatLng(22.446040, 113.957632)).add(new LatLng(22.445882, 113.956978))
                                .add(new LatLng(22.444900, 113.956495)).add(new LatLng(22.443512, 113.954703))
                                .add(new LatLng(22.443680, 113.954296)).add(new LatLng(22.443085, 113.953019))
                                .add(new LatLng(22.442808, 113.953051)).add(new LatLng(22.442213, 113.951989))
                                .add(new LatLng(22.441712, 113.950283)).add(new LatLng(22.441018, 113.949468))
                                .add(new LatLng(22.440086, 113.949360)).add(new LatLng(22.439531, 113.949993))
                                .add(new LatLng(22.438460, 113.948824)).add(new LatLng(22.437161, 113.949393))
                                .add(new LatLng(22.437032, 113.948609)).add(new LatLng(22.435584, 113.948395))
                                .add(new LatLng(22.434691, 113.949114)).add(new LatLng(22.433779, 113.948116))
                                .add(new LatLng(22.433303, 113.947022)).add(new LatLng(22.432857, 113.947032))
                                .add(new LatLng(22.432460, 113.947612)).add(new LatLng(22.432143, 113.946517))
                                .add(new LatLng(22.431423, 113.946271)).add(new LatLng(22.431136, 113.945788))
                                .add(new LatLng(22.429956, 113.944640)).color(R.color.wallet_holo_blue_light)
                );
                break;
            case 4:
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(22.500689, 114.128277)).add(new LatLng(22.500504, 114.128076))
                        .add(new LatLng(22.500349, 114.127928)).add(new LatLng(22.500019, 114.127649))
                        .add(new LatLng(22.499766, 114.127436)).add(new LatLng(22.499686, 114.127369))
                        .add(new LatLng(22.499393, 114.126857)).add(new LatLng(22.499466, 114.126543))
                        .add(new LatLng(22.499592, 114.126093)).add(new LatLng(22.500094, 114.124583))
                        .add(new LatLng(22.500408, 114.124180)).add(new LatLng(22.500627, 114.124269))
                        .add(new LatLng(22.500779, 114.124028)).add(new LatLng(22.500817, 114.123605))
                        .add(new LatLng(22.500728, 114.123445)).add(new LatLng(22.501009, 114.123311))
                        .add(new LatLng(22.501638, 114.123431)).add(new LatLng(22.501874, 114.123293))
                        .add(new LatLng(22.502355, 114.123029)).add(new LatLng(22.502713, 114.122950))
                        .add(new LatLng(22.502747, 114.122865)).add(new LatLng(22.503022, 114.122100))
                        .add(new LatLng(22.504921, 114.119675)).add(new LatLng(22.505292, 114.119168))
                        .add(new LatLng(22.505688, 114.118716)).add(new LatLng(22.506361, 114.118280))
                        .add(new LatLng(22.506525, 114.118124)).add(new LatLng(22.508143, 114.117617))
                        .add(new LatLng(22.510294, 114.116319)).add(new LatLng(22.510420, 114.116122))
                        .add(new LatLng(22.511173, 114.115769)).add(new LatLng(22.511348, 114.115746))
                        .add(new LatLng(22.512442, 114.115325)).add(new LatLng(22.513014, 114.114461))
                        .add(new LatLng(22.513673, 114.114810)).add(new LatLng(22.514997, 114.114536))
                        .add(new LatLng(22.516012, 114.114477)).add(new LatLng(22.517744, 114.114525))
                        .add(new LatLng(22.517764, 114.114139)).add(new LatLng(22.517570, 114.113699))
                        .add(new LatLng(22.517878, 114.113399)).add(new LatLng(22.518570, 114.113477))
                        .color(R.color.wallet_holo_blue_light));
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(22.504921, 114.119675))
                        .add(new LatLng(22.505312, 114.120104))
                        .add(new LatLng(22.505910, 114.119342))
                        .add(new LatLng(22.506604, 114.118744))
                        .add(new LatLng(22.507120, 114.118507))
                        .add(new LatLng(22.507471, 114.118701))
                        .add(new LatLng(22.507894, 114.118938))
                        .add(new LatLng(22.508154, 114.118933))
                        .add(new LatLng(22.508291, 114.118854))
                        .add(new LatLng(22.508994, 114.117995))
                        .add(new LatLng(22.510123, 114.117239))
                        .add(new LatLng(22.511223, 114.116563))
                        .add(new LatLng(22.512323, 114.116128))
                        .add(new LatLng(22.513451, 114.115864))
                        .add(new LatLng(22.514678, 114.115681))
                        .add(new LatLng(22.515880, 114.115510))
                        .add(new LatLng(22.516567, 114.115489))
                        .add(new LatLng(22.517563, 114.115661))
                        .add(new LatLng(22.517744, 114.114525))
                        .color(R.color.wallet_holo_blue_light));
                break;
            default:
                break;
        }
    }

    private void setPlace() {
        BitmapDescriptor bike_icon =
                BitmapDescriptorFactory.fromResource(R.drawable.bike);
        BitmapDescriptor food_icon =
                BitmapDescriptorFactory.fromResource(R.drawable.food);
        BitmapDescriptor place_icon =
                BitmapDescriptorFactory.fromResource(R.drawable.place);


        marker[0] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.445313, 114.031825)).icon(bike_icon).title("Bicycle1"));
        marker[1] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.448721, 114.030565)).icon(bike_icon).title("Bicycle2"));
        marker[2] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.452065, 114.000136)).icon(bike_icon).title("Bicycle3"));
        marker[3] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.445837, 114.018468)).icon(bike_icon).title("占美單車"));
        marker[4] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.445168, 114.031907)).icon(bike_icon).title("Bicycle4"));
        marker[5] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.444295, 114.033784)).icon(bike_icon).title("Bicycle5"));
        marker[6] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.443215, 114.033333)).icon(bike_icon).title("Bicycle6"));
        marker[7] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.442710, 114.033481)).icon(bike_icon).title("Bicycle7"));
        marker[8] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.441795, 114.033534)).icon(bike_icon).title("Bicycle8"));

        marker[9] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.442690, 114.026052)).icon(food_icon).title("Ｂ仔涼粉"));
        marker[10] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.442235, 114.030941)).icon(food_icon).title("佳記甜品"));
        marker[11] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.443992, 114.028273)).icon(food_icon).title("勝利牛丸"));
        marker[12] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.443775, 114.029429)).icon(food_icon).title("好到底麵家"));
        marker[13] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.446151, 114.029896)).icon(food_icon).title("大榮華酒樓"));
        marker[14] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.443287, 114.024799)).icon(food_icon).title("永年士多"));
        marker[15] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.442412, 114.031565)).icon(food_icon).title("天鴻燒鵝飯店"));
        marker[16] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.442138, 114.031563)).icon(food_icon).title("發記腸粉粥品"));

        marker[17] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.441134, 114.025159)).icon(place_icon).title("基督教福音信義會互愛堂"));
        marker[18] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.446191, 114.031198)).icon(place_icon).title("神召會仁愛福音教會元朗堂"));
        marker[19] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.445398, 114.025652)).icon(place_icon).title("擊壤路五人足球場"));
        marker[20] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.441534, 114.023103)).icon(place_icon).title("元朗劇院"));
        marker[21] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.468049, 114.004141)).icon(place_icon).title("香港濕地公園"));
        marker[22] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.445100, 114.009214)).icon(place_icon).title("屏山文物徑"));
        marker[23] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.454635, 114.006210)).icon(place_icon).title("天水圍運動場"));
        marker[24] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.467059, 113.995983)).icon(place_icon).title("天水圍高爾夫球場"));
        marker[25] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.454227, 113.972913)).icon(place_icon).title("深圳灣公路大橋"));
        marker[26] = mMap.addMarker(new MarkerOptions().position(new LatLng(22.430756, 113.945464)).icon(place_icon).title("下白泥風景區"));

    }

    private void setMarkerVisible() {
        if (is_bike_on) {
            for (int i = 0; i < 9; i++) {
                marker[i].setVisible(true);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                marker[i].setVisible(false);
            }
        }
        if (is_food_on) {
            for (int i = 9; i < 17; i++) {
                marker[i].setVisible(true);
            }
        } else {
            for (int i = 9; i < 17; i++) {
                marker[i].setVisible(false);
            }
        }
        if (is_place_on) {
            for (int i = 17; i < 27; i++) {
                marker[i].setVisible(true);
            }
        } else {
            for (int i = 17; i < 27; i++) {
                marker[i].setVisible(false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (is_path_on) {
            if (Pathnewlat.isEmpty()|| stop) {
                Pathnewlat.add(latitude);
                Pathnewlog.add(longitude);
                pre_lat = latitude;
                pre_log = longitude;
                stop = false;
            } else {
                Pathnewlat.add(latitude);
                Pathnewlog.add(longitude);
                mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(new LatLng(pre_lat, pre_log))
                        .add(new LatLng(latitude, longitude))
                        .color(R.color.wallet_holo_blue_light));

                dis_sum = dis_sum + Distance(pre_log, pre_lat, longitude, latitude);
                distance.setText("Distance " + dis_sum + " m");

                pre_lat = latitude;
                pre_log = longitude;
            }
        }
        else {
            stop = true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            myAlertDialog();
        }
        return false;
    }

    public double Distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance;
    }

    private void myAlertDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("Your Path");
        MyAlertDialog.setMessage("add new path in MyPath");

        DialogInterface.OnClickListener leave = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        };
        DialogInterface.OnClickListener OnReturn = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener save= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                lat= new double[Pathnewlat.size()];
                log= new double[Pathnewlog.size()];
                for(int i=0;i<Pathnewlat.size();i++){
                    lat[i]=Pathnewlat.get(i);
                    log[i]=Pathnewlog.get(i);
                }
                User user = userLocalStore.getLoggedInUser();
                String n = user.name;
                String un = user.username;
                String pw = user.password;
                String dis = (Double.parseDouble(user.dis) + dis_sum) + "";
                int ageG =  user.age;
                User user2 = new User(n, ageG, un, pw, dis);
                refresh(user2);

            }
        };
        MyAlertDialog.setNeutralButton("leave", leave);
        MyAlertDialog.setPositiveButton("save", save);
        MyAlertDialog.setNegativeButton("back", OnReturn);
        MyAlertDialog.create().show();
    }

    private void refresh(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.refreshUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                finish();
            }
        });
    }
}
