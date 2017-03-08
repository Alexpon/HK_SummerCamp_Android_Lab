package mbp.alexpon.com.hkbike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ChangeUserData extends ActionBarActivity {

    private EditText name;
    private EditText age;
    private Button change;
    private Button back;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_data);
        initViews();
        setListener();
    }

    public void initViews(){
        name = (EditText) findViewById(R.id.change_name);
        age = (EditText) findViewById(R.id.change_age);
        change = (Button) findViewById(R.id.change_submit);
        back = (Button) findViewById(R.id.change_menu);
        userLocalStore = new UserLocalStore(this);
    }

    public void setListener(){
        change.setOnClickListener(myListener);
        back.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.change_submit:
                    User user = userLocalStore.getLoggedInUser();
                    String n = name.getText().toString();
                    String un = user.username;
                    String pw = user.password;
                    String dis = user.dis;
                    int ageG =  Integer.parseInt(age.getText().toString());
                    User user2 = new User(n, ageG, un, pw, dis);
                    refresh(user2);
                    startActivity(new Intent(ChangeUserData.this, Login.class));
                    break;

                case R.id.change_menu:
                    startActivity(new Intent(ChangeUserData.this, MainActivity.class));
                    finish();
            }
        }
    };

    private void refresh(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.refreshUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
            }
        });
    }


}
