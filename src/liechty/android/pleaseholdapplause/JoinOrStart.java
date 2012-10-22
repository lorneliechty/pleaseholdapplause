package liechty.android.pleaseholdapplause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class JoinOrStart extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_or_start);
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.join_button) {
			Intent intent = new Intent(getApplicationContext(), JoinPresentation.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.start_button) {
			Intent intent = new Intent(getApplicationContext(), StartPresentation.class);
			startActivity(intent);
		}
	}
}
