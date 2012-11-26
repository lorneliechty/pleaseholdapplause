package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RequestPresentation extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_presentation);
    }
    
    public void onClick(View v) {
    	if (v.getId() == R.id.start_presentation_button) {
    		Intent intent = new Intent();
    		intent.putExtra(PHAIntent.Extra.PRESENTATION_ID, 0);
    		setResult(RESULT_OK, intent);
    		finish();
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
