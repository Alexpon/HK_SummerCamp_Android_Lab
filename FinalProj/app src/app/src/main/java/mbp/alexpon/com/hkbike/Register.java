package mbp.alexpon.com.hkbike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Register extends Activity {

    private EditText name;
    private EditText age;
    private EditText username;
    private EditText password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        setListener();
    }

    public void initViews(){
        name = (EditText) findViewById(R.id.edRN);
        age = (EditText) findViewById(R.id.edAge);
        username = (EditText) findViewById(R.id.edRUN);
        password = (EditText) findViewById(R.id.edRPD);
        register = (Button) findViewById(R.id.register);
    }

    public void setListener(){
        register.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register:
                    String n = name.getText().toString();
                    String un = username.getText().toString();
                    String pw = password.getText().toString();
                    int ageG =  Integer.parseInt(age.getText().toString());
                    User user = new User(n, ageG, un, pw, "0");
                    registerUser(user);
                    break;
            }
        }
    };

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
