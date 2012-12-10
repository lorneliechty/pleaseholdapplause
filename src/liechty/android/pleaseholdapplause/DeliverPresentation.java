package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class DeliverPresentation extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deliver_presentation);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Cursor c = getContentResolver().query(getIntent().getData(), null, null, null, null);
		if (c != null && c.moveToFirst()) {
			((TextView)findViewById(R.id.textView1)).setText("web_id: " +
					String.valueOf(c.getInt(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.WEB_ID))));
		}
	}
}
