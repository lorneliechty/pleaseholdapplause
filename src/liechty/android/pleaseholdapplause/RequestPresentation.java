package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class RequestPresentation extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_presentation);
    }
    
    public void onClick(View v) {
    	if (v.getId() == R.id.start_presentation_button) {
    		String name = ((EditText)findViewById(R.id.presenter_name_edit)).getText().toString();
    		String title = ((EditText)findViewById(R.id.presentation_title_edit)).getText().toString();
    		String venue = ((EditText)findViewById(R.id.presentation_venue_edit)).getText().toString();
            Long now = Long.valueOf(System.currentTimeMillis());
    		
    		ContentValues values = new ContentValues();
    		values.put(PHAProviderContract.Presentation.CursorColumns.SPEAKER, name);
    		values.put(PHAProviderContract.Presentation.CursorColumns.TITLE, title);
    		values.put(PHAProviderContract.Presentation.CursorColumns.VENUE, venue);
    		values.put(PHAProviderContract.Presentation.CursorColumns.START_TIME_epoch_s, now);
    		values.put(PHAProviderContract.Presentation.CursorColumns.END_TIME_epoch_s, now + 60*60); // One hour later
    		Uri presentationUri = getContentResolver().insert(PHAProviderContract.Presentation.getPresentations(), values);
    		
    		if (presentationUri != null) {
				Intent intent = new Intent();
				intent.putExtra(PHAIntent.Extra.PRESENTATION_ID, Long.parseLong(presentationUri.getLastPathSegment()));
				setResult(RESULT_OK, intent);
				finish();
    		}
    		else {
    			// There was some problem with adding the presentation... notify the user
    			Toast.makeText(getApplicationContext(), "There was a problem adding the presentation", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	
    	// For us a back press is a cancel
    	setResult(RESULT_CANCELED);
    	finish();
    }
}
