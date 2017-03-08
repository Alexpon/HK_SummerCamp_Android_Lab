package mbp.alexpon.com.hkbike;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class RankActivity extends Activity {

    private TextView username1, username2, username3, username4, username5;
    private TextView age1, age2, age3, age4, age5;
    private TextView distance1, distance2, distance3, distance4, distance5;
    private StoreRank storeRank;
    private User[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initViews();
        setViewText();
    }

    public void initViews(){
        username1 = (TextView) findViewById(R.id.username1);
        username2 = (TextView) findViewById(R.id.username2);
        username3 = (TextView) findViewById(R.id.username3);
        username4 = (TextView) findViewById(R.id.username4);
        username5 = (TextView) findViewById(R.id.username5);
        age1 = (TextView) findViewById(R.id.age1);
        age2 = (TextView) findViewById(R.id.age2);
        age3 = (TextView) findViewById(R.id.age3);
        age4 = (TextView) findViewById(R.id.age4);
        age5 = (TextView) findViewById(R.id.age5);
        distance1 = (TextView) findViewById(R.id.distance1);
        distance2 = (TextView) findViewById(R.id.distance2);
        distance3 = (TextView) findViewById(R.id.distance3);
        distance4 = (TextView) findViewById(R.id.distance4);
        distance5 = (TextView) findViewById(R.id.distance5);

        storeRank = new StoreRank(this);
        users = storeRank.getRank();
    }

    public void setViewText(){
        username1.setText(users[0].username);
        username2.setText(users[1].username);
        username3.setText(users[2].username);
        username4.setText(users[3].username);
        username5.setText(users[4].username);
        age1.setText(String.valueOf(users[0].age));
        age2.setText(String.valueOf(users[1].age));
        age3.setText(String.valueOf(users[2].age));
        age4.setText(String.valueOf(users[3].age));
        age5.setText(String.valueOf(users[4].age));
        distance1.setText(users[0].dis);
        distance2.setText(users[1].dis);
        distance3.setText(users[2].dis);
        distance4.setText(users[3].dis);
        distance5.setText(users[4].dis);
    }

}
