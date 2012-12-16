package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.service.GetAllPresentationsService;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		// Immediately update the presentation list when the app is first started
		Intent intent = new Intent(getApplicationContext(),GetAllPresentationsService.class);
		startService(intent);
		
		// Schedule the presentation list to update every once in a while...
		PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
		((AlarmManager)getSystemService(ALARM_SERVICE)).cancel(pIntent);
		((AlarmManager)getSystemService(ALARM_SERVICE)).setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				AlarmManager.INTERVAL_FIFTEEN_MINUTES, 
				AlarmManager.INTERVAL_HALF_HOUR, 
				pIntent);
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
