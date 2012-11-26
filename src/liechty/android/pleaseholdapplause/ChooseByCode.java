package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseByCode extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_by_code);
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.choose_by_code_submitButton) {
			
			EditText edit = ((EditText)findViewById(R.id.join_presentation_code_edit));
			if (edit.getText().length() > 0) {
				
				String code = edit.getText().toString();
				
				Intent intent = new Intent();
				intent.putExtra(PHAIntent.Extra.PRESENTATION_ID, Integer.parseInt(code));
				setResult(RESULT_OK, intent);
				
				finish();
			}
			else {
				// Let the user know what their mistake was
				Toast.makeText(this, "Please enter a valid presentation code", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		// Back is considered cancel in our situation
		setResult(RESULT_CANCELED);
		finish();
	}
}
