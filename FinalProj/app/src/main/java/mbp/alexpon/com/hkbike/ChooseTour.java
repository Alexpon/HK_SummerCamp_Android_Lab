package mbp.alexpon.com.hkbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChooseTour extends ActionBarActivity {

    private ImageView img;
    private Button btn_submit;
    private Button btn_back;
    private TextView tripName;
    private TextView length;
    private TextView time;
    private TextView detail;
    private String[] str_trip;
    private String[] str_rent;
    private String[] str_leng;
    private String[] str_time;
    private String[] str_intr;

    private Intent intent;
    private RatingBar ratingbar;
    private double longitude;
    private double latitude;
    private int path;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ListView lvLeftMenu;
    private ActionBarDrawerToggle mDrawerToggle;
    private SimpleAdapter adapter;
    static int[] pic={R.drawable.easy,R.drawable.easy,R.drawable.normal,
                R.drawable.easy,R.drawable.hard,R.drawable.normal,
                R.drawable.normal,R.drawable.hard,R.drawable.normal};

    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tour);
        initViews();
        setListener();
        initTrip();

        for(int i=0; i<9; i++){
            HashMap<String,Object> hashMap= new HashMap<String, Object>();
            hashMap.put("where",str_trip[i]);
            hashMap.put("diffi","Has a bike borrower "+str_rent[i]);
            hashMap.put("pic", pic[i]);
            list.add(hashMap);
        }
        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.list_layout,
                new String[] {"pic","where","diffi"},
                new int[] { R.id.imagev1, R.id.text1, R.id.text2} );
        setDrawer();

    }

    public void initViews(){
        img = (ImageView) findViewById(R.id.tourMap);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_back = (Button) findViewById(R.id.btn_back);
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        tripName = (TextView) findViewById(R.id.trip_name);
        detail = (TextView) findViewById(R.id.detail);
        length = (TextView) findViewById(R.id.tv_path);
        time = (TextView) findViewById(R.id.tv_time);
        ratingbar.setIsIndicator(true);

        str_trip = getResources().getStringArray(R.array.trip);
        str_rent = getResources().getStringArray(R.array.renting);
        str_leng = getResources().getStringArray(R.array.length);
        str_time = getResources().getStringArray(R.array.time);
        str_intr = getResources().getStringArray(R.array.intro);



        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    public void initTrip(){
        img.setImageResource(R.drawable.trip1);
        tripName.setText(str_trip[0]);
        length.setText(str_leng[0]);
        time.setText(str_time[0]);
        ratingbar.setRating((float)1.0);
        longitude = 114.034074;
        latitude = 22.443555;
        path = 1;
    }

    public void setListener(){
        btn_submit.setOnClickListener(myListener);
        btn_back.setOnClickListener(myListener);
        detail.setOnClickListener(myListener);
    }


    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    Bundle bundle = new Bundle();
                    bundle.putDouble("KEY_LONGITUDE", longitude);
                    bundle.putDouble("KEY_LATITUDE", latitude);
                    bundle.putInt("KEY_PATH", path);
                    Intent intentMap = new Intent(ChooseTour.this, MapsActivity.class);
                    intentMap.putExtras(bundle);
                    startActivity(intentMap);
                    break;
                case R.id.btn_back:
                    intent = new Intent(ChooseTour.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.detail:
                    ShowMsgDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void ShowMsgDialog(){
            AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
            MyAlertDialog.setTitle(str_trip[path-1]);
            MyAlertDialog.setMessage(str_intr[path-1]);
            DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {}
            };
            MyAlertDialog.setNeutralButton("離開",OkClick );
            MyAlertDialog.show();
    }

    private void setDrawer(){
        toolbar.setTitle("Choose a Trip");//设置Toolbar标题
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
        lvLeftMenu.setAdapter(adapter);
        lvLeftMenu.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {

                case 0:
                    img.setImageResource(R.drawable.trip1);
                    tripName.setText(str_trip[0]);
                    length.setText(str_leng[0]);
                    time.setText(str_time[0]);
                    ratingbar.setRating((float)1.0);
                    longitude = 114.034074;
                    latitude = 22.443555;
                    path = 1;
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    img.setImageResource(R.drawable.trip2);
                    tripName.setText(str_trip[1]);
                    length.setText(str_leng[1]);
                    time.setText(str_time[1]);
                    ratingbar.setRating((float)2.5);
                    longitude = 114.033992;
                    latitude = 22.445426;
                    path = 2;
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    img.setImageResource(R.drawable.trip3);
                    tripName.setText(str_trip[2]);
                    length.setText(str_leng[2]);
                    time.setText(str_time[2]);
                    ratingbar.setRating((float)4.0);
                    longitude = 113.994668;
                    latitude = 22.461746;
                    path = 3;
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    img.setImageResource(R.drawable.trip4);
                    tripName.setText(str_trip[3]);
                    length.setText(str_leng[3]);
                    time.setText(str_time[3]);
                    ratingbar.setRating((float)3.0);
                    longitude = 114.128333;
                    latitude = 22.501753;
                    path = 4;
                    mDrawerLayout.closeDrawers();
                    break;
                case 4:
                    img.setImageResource(R.drawable.trip5);
                    tripName.setText(str_trip[4]);
                    length.setText(str_leng[4]);
                    time.setText(str_time[4]);
                    ratingbar.setRating((float)2.0);
                    longitude = 22.501753;
                    latitude = 114.128333;
                    path = 5;
                    mDrawerLayout.closeDrawers();
                    break;
                case 5:
                    img.setImageResource(R.drawable.trip6);
                    tripName.setText(str_trip[5]);
                    length.setText(str_leng[5]);
                    time.setText(str_time[5]);
                    ratingbar.setRating((float)3.0);
                    longitude = 22.501753;
                    latitude = 114.128333;
                    path = 6;
                    mDrawerLayout.closeDrawers();
                    break;
                case 6:
                    img.setImageResource(R.drawable.trip7);
                    tripName.setText(str_trip[6]);
                    length.setText(str_leng[6]);
                    time.setText(str_time[6]);
                    ratingbar.setRating((float)3.5);
                    longitude = 22.501753;
                    latitude = 114.128333;
                    path = 7;
                    mDrawerLayout.closeDrawers();
                    break;
                case 7:
                    img.setImageResource(R.drawable.trip8);
                    tripName.setText(str_trip[7]);
                    length.setText(str_leng[7]);
                    time.setText(str_time[7]);
                    ratingbar.setRating((float)2.5);
                    longitude = 22.501753;
                    latitude = 114.128333;
                    path = 8;
                    mDrawerLayout.closeDrawers();
                    break;
                case 8:
                    img.setImageResource(R.drawable.trip9);
                    tripName.setText(str_trip[8]);
                    length.setText(str_leng[8]);
                    time.setText(str_time[8]);
                    ratingbar.setRating((float)4.0);
                    longitude = 22.501753;
                    latitude = 114.128333;
                    path = 9;
                    mDrawerLayout.closeDrawers();
                    break;
                default:

                    break;
            }
        }
    }

}
