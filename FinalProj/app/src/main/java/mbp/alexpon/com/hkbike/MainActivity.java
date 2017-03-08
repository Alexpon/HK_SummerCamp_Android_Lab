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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private ImageButton btn_go;
    private ImageButton btn_login;
    private Intent intent;
    private UserLocalStore userLocalStore;
    private StoreRank userRankStore;


    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
        setDrawer();

    }

    public void initViews(){
        btn_go = (ImageButton) findViewById(R.id.btn_go);
        btn_login = (ImageButton) findViewById(R.id.btn_login);
        userLocalStore = new UserLocalStore(this);
        userRankStore = new StoreRank(this);

        drawer = getResources().getStringArray(R.array.main_drawer);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    public void setListener(){
        btn_go.setOnClickListener(myListener);
        btn_login.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_go:
                    intent = new Intent(MainActivity.this, ChooseTour.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_login:
                    if(authenticate()==false){
                        intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }
                    else {
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);
                        logout_success();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    protected void onStart(){
        super.onStart();

    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        if(authenticate()==true) {
            User user = userLocalStore.getLoggedInUser();
            AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(MainActivity.this);
            MyAlertDialog.setTitle("個人資料");
            MyAlertDialog.setMessage("Name: " + user.name + "\nAge: " + user.age + "\nUsername: " +
                    user.username + "\nPassword: " + user.password + "\nDistance: "+user.dis+" m");
            DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            };
            MyAlertDialog.setNeutralButton("確定", OkClick);
            MyAlertDialog.show();
        }
        else{
            intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    private void logout_success(){
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("Logout");
        MyAlertDialog.setMessage("Logout success");

        DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        };

        MyAlertDialog.setNegativeButton("ok", ok);
        MyAlertDialog.create().show();
    }

    private void about_msg(){
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("About");
        MyAlertDialog.setMessage(R.string.about);

        DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        };

        MyAlertDialog.setNegativeButton("ok", ok);
        MyAlertDialog.create().show();
    }

    private void setRank(){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchRankDataInBackground(new GetUserCallBacks() {
            @Override
            public void done(User[] returnedUser) {
                Log.i("DONE!!!!!!", returnedUser[0].username);
                Log.i("DONE!!!!!!", returnedUser[1].username);
                Log.i("DONE!!!!!!", returnedUser[2].username);
                Log.i("DONE!!!!!!", returnedUser[3].username);
                Log.i("DONE!!!!!!", returnedUser[4].username);

                userRankStore.storeRank(returnedUser);
                startActivity(new Intent(MainActivity.this, RankActivity.class));
                finish();
            }
        });
    }

    private void setDrawer(){
        toolbar.setTitle("HK Bike");//设置Toolbar标题
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
                    displayUserDetails();
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, ChangeUserData.class));
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    setRank();
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    about_msg();
                    mDrawerLayout.closeDrawers();
                    break;
                default:
                    break;
            }
        }
    }
}
