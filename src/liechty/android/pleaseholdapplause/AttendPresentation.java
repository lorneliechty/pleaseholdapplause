package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AttendPresentation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_presentation);
		
		int presentationCode = getIntent().getIntExtra(PHAIntent.Extra.PRESENTATION_ID, 0);
		if (presentationCode != 0) {
			((TextView)findViewById(R.id.attend_presentation_id_label)).setText(Integer.toString(presentationCode));
		}
	}
}
