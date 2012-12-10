package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class AttendPresentation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_presentation);
		
		Cursor c = getContentResolver().query(getIntent().getData(), 
				null, null, null, null);
		
		long web_id = 0;
		if (c != null && c.moveToFirst()) {
			web_id = c.getLong((c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.WEB_ID)));
		}
		
		((TextView)findViewById(R.id.attend_presentation_id_label)).setText(
				"web ID : " + String.valueOf(web_id));
	}
}
