package liechty.android.pleaseholdapplause;

import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * This class provides a BroadcastReceiver based timer Fragment with reset button.
 * Implementing this via a BroadcastReceiver is intentionally done and is pretty
 * much just to to illustrate the manner in which an Activity can be updated based
 * on processes running in the background (whether system or non-system).
 * 
 * ... it's not even really a great timer because it doesn't update based on the 
 * minutes since you started... it updates with the system time minute changes.
 * But given the fact that our app's use case is Presentations, which normally run
 * for many minutes, this shouldn't be too big of a problem.
 */
public class PresentationTimerFragment extends Fragment implements OnClickListener {

	TextView mClock = null;
	long mStartTime = (long) 0.0;
	
	BroadcastReceiver mTimeTickReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				updateTimerDisplay(System.currentTimeMillis() - mStartTime);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStartTime = System.currentTimeMillis();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.presentation_timer, null);
		mClock = (TextView)layout.findViewById(R.id.presentation_timer_clock);
		
		layout.findViewById(R.id.presentation_timer_reset_button).setOnClickListener(this);
		
		return layout;
	}
	
	private void updateTimerDisplay(long duration) {
		if (mClock != null) {
			mClock.setText(String.format("%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(duration),
					TimeUnit.MILLISECONDS.toMinutes(duration)));
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		getActivity().registerReceiver(mTimeTickReceiver, filter);
		
		updateTimerDisplay(System.currentTimeMillis() - mStartTime);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		getActivity().unregisterReceiver(mTimeTickReceiver);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.presentation_timer_reset_button) {
			mStartTime = System.currentTimeMillis();
			updateTimerDisplay(0);
		}
	}
}
