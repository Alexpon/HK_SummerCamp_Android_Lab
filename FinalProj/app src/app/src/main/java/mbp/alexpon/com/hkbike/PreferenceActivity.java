package mbp.alexpon.com.hkbike;

import android.app.Activity;
import android.os.Bundle;

public class PreferenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PreferencesFragment()).commit();
    }

}