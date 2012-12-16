package liechty.android.pleaseholdapplause.provider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PHADatabaseHelper extends SQLiteOpenHelper{
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "pleaseholdapplause.db";

	public PHADatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PresentationsTable.getCreateStatement());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
