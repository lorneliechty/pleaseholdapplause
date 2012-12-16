package liechty.android.pleaseholdapplause;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class PresentationListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mPresentationAdapter;
	
	public interface PresentationListItemClickListener {
		public void onPresentationListItemClick(View v, long presentationID);
	}
	PresentationListItemClickListener mListClickListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPresentationAdapter = new SimpleCursorAdapter (getActivity(), 
				R.layout.presentation_list_row, 
				null, 
				new String[] {PHAProviderContract.Presentation.CursorColumns.TITLE}, 
				new int[] {R.id.presentation_list_row_title},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		setListAdapter(mPresentationAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	} 
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListClickListener = (PresentationListItemClickListener)activity;
		} catch (Exception e) {
			// The activity does not want to listen for clicks?...
			// ... eat the exception... this is not fatal.
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		mListClickListener.onPresentationListItemClick(v, id);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), 
        		PHAProviderContract.Presentation.getPresentations(),
                null, 
                null,
                null, 
                null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mPresentationAdapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mPresentationAdapter.swapCursor(null);
	}
}
