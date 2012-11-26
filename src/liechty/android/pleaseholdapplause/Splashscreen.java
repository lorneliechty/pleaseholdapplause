package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(getApplicationContext(), JoinOrStart.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	}

}
