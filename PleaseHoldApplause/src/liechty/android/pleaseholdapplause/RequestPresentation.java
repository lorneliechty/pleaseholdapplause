package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class RequestPresentation extends SherlockActivity {

	private static final int REQUEST_MENU_ITEM_ID = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_presentation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, REQUEST_MENU_ITEM_ID, Menu.NONE, getResources().getString(R.string.start_presentation_button))
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    	
    	if (item.getItemId() == REQUEST_MENU_ITEM_ID) {
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
				intent.setData(presentationUri);
				setResult(RESULT_OK, intent);
				finish();
    		}
    		else {
    			// There was some problem with adding the presentation... notify the user
    			Toast.makeText(getApplicationContext(), "There was a problem adding the presentation", Toast.LENGTH_SHORT).show();
    		}
    	}
    	
    	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	
    	// For us a back press is a cancel
    	setResult(RESULT_CANCELED);
    	finish();
    }
}
