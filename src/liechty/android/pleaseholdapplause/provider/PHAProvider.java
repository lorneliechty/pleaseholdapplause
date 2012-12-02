package liechty.android.pleaseholdapplause.provider;

import java.util.HashMap;

import liechty.android.pleaseholdapplause.provider.db.PHADatabaseHelper;
import liechty.android.pleaseholdapplause.provider.db.PresentationsTable;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class PHAProvider extends ContentProvider {
	
	private static final int PRESENTATIONS			= 1000;
	private static final int PRESENTATION_SPECIFIC	= 1001;
	
	private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static HashMap<String, String> sPresentationMap;
	
	static {
		sMatcher.addURI(PHAProviderContract.AUTHORITY, PHAProviderContract.Presentation.DIRECTORY, PRESENTATIONS);
		sMatcher.addURI(PHAProviderContract.AUTHORITY, PHAProviderContract.Presentation.DIRECTORY + "/#", PRESENTATION_SPECIFIC);
		
		// Add Presentation object projection map... not specifically useful now, but
		// provides us with healthy abstraction between cursor columns and db table columns.
		// More useful in situations where you either have cursors that are formed from
		// queries that include joins OR if you are exporting your provider and don't want
		// to show the world your internal database table names / columns
		sPresentationMap = new HashMap<String, String>();
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns._ID,
				PresentationsTable.Columns._ID);
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns.SPEAKER,
				PresentationsTable.Columns.SPEAKER);
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns.TITLE,
				PresentationsTable.Columns.TITLE);
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns.VENUE,
				PresentationsTable.Columns.VENUE);
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns.START_TIME_epoch_s,
				PresentationsTable.Columns.START_TIME_epoch_s);
		sPresentationMap.put(
				PHAProviderContract.Presentation.CursorColumns.END_TIME_epoch_s,
				PresentationsTable.Columns.END_TIME_epoch_s);
	}
	
	private PHADatabaseHelper mDBHelper = null;
	
	public PHAProvider() {
	}

	@Override
	public boolean onCreate() {
		// Creates a new helper object. Note that the database itself isn't opened until
		// something tries to access it, and it's only created if it doesn't already exist.
		mDBHelper = new PHADatabaseHelper(this.getContext());
		
		// Assumes that any failures will be reported by a thrown exception.
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		int match = sMatcher.match(uri);
		switch (match) {
			case PRESENTATIONS:
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		
		ContentValues insertValues = values;
		
		int match = sMatcher.match(uri);
		switch (match) {
			case PRESENTATIONS:
				if (insertValues == null) {
					// This is not OK... can not insert a null / default Presentation object
					return null;
				}
				break;
				
			default:
				throw new IllegalArgumentException("Cannot insert to Uri: " + uri);
		}
		
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long rowId = db.insert(PresentationsTable.TABLE_NAME, PresentationsTable.Columns.SPEAKER, insertValues);
        
        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
        	result = ContentUris.withAppendedId(PHAProviderContract.Presentation.getPresentations(), rowId);

            // Notifies observers registered against this provider that the data changed.
        	getContext().getContentResolver().notifyChange(uri, null);
            getContext().getContentResolver().notifyChange(result, null);
        }
		
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		
		int match = sMatcher.match(uri);
		switch (match) {
			case PRESENTATIONS:
				builder.setTables(PresentationsTable.TABLE_NAME);
				builder.setProjectionMap(sPresentationMap);
				break;
				
			case PRESENTATION_SPECIFIC:
				builder.setTables(PresentationsTable.TABLE_NAME);
				builder.setProjectionMap(sPresentationMap);
				builder.appendWhere(
            		   PresentationsTable.Columns._ID + "=" + 
    				   uri.getPathSegments().get(uri.getPathSegments().indexOf(PHAProviderContract.Presentation.DIRECTORY) + 1));
				break;
		}
		
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
