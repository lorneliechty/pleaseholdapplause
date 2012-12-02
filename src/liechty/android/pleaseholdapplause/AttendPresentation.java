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
		
		long presentationCode = getIntent().getLongExtra(PHAIntent.Extra.PRESENTATION_ID, 0);
		
		Cursor c = getContentResolver().query(PHAProviderContract.Presentation.getPresentation(presentationCode), 
				null, null, null, null);
		
		String title = null;
		if (c != null && c.moveToFirst()) {
			title = c.getString(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.TITLE));
		}
		
		if (presentationCode != 0) {
			((TextView)findViewById(R.id.attend_presentation_id_label)).setText(
					Long.toString(presentationCode) + " : " + title);
		}
	}
}
