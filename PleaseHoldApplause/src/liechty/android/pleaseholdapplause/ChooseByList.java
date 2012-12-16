package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.PresentationListFragment.PresentationListItemClickListener;
import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ChooseByList extends FragmentActivity implements PresentationListItemClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_by_list);
	}

	public void onPresentationListItemClick(View v, long presentationID) {
		Intent intent = new Intent();
		intent.setData(PHAProviderContract.Presentation.getPresentation(presentationID));
		setResult(RESULT_OK, intent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		// Back is considered cancel in our situation
		setResult(RESULT_CANCELED);
		finish();
	}
}
