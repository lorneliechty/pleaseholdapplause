package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class JoinOrStart extends Activity implements OnClickListener {

	private static int REQUEST_CODE_CHOOSE_PRESENTATION = 0;
	private static int REQUEST_CODE_REQUEST_PRESENTATION = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_or_start);
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.join_button) {
			Intent intent = new Intent();
			intent.setAction(PHAIntent.CHOOSE_PRESENTATION);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			startActivityForResult(intent, REQUEST_CODE_CHOOSE_PRESENTATION);
		}
		else if (v.getId() == R.id.start_button) {
			Intent intent = new Intent(getApplicationContext(), RequestPresentation.class);
			startActivityForResult(intent, REQUEST_CODE_REQUEST_PRESENTATION);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CODE_CHOOSE_PRESENTATION && resultCode == RESULT_OK ) {
			joinPresentation(data.getLongExtra(PHAIntent.Extra.PRESENTATION_ID, 0));
		}
		else if (requestCode == REQUEST_CODE_REQUEST_PRESENTATION && resultCode == RESULT_OK) {
			startPresentation(data.getData());
		}
	}
	
	private void joinPresentation(long presentationID) {
		Intent intent = new Intent(getApplicationContext(), AttendPresentation.class);
		intent.putExtra(PHAIntent.Extra.PRESENTATION_ID, presentationID);
		startActivity(intent);
	}
	
	private void startPresentation(Uri presentationUri) {
		Intent intent = new Intent(getApplicationContext(), DeliverPresentation.class);
		intent.setData(presentationUri);
		startActivity(intent);
	}
}
