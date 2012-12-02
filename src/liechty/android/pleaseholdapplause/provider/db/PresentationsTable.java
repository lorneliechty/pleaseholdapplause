package liechty.android.pleaseholdapplause.provider.db;

public final class PresentationsTable {
	
	public static final String TABLE_NAME = "presentations";

	public static final class Columns {
		public static final String _ID = "_id";
		public static final String SPEAKER = "speaker";
		public static final String TITLE = "title";
		public static final String VENUE = "venue";
		public static final String START_TIME_epoch_s = "start";
		public static final String END_TIME_epoch_s = "end";
	}
	
	public static String getCreateStatement() {
		return "CREATE TABLE " + TABLE_NAME + " ("
				+ Columns._ID + " INTEGER PRIMARY KEY, "
				+ Columns.SPEAKER + " TEXT, "
				+ Columns.TITLE + " TEXT, "
				+ Columns.VENUE + " TEXT, "
				+ Columns.START_TIME_epoch_s + " INTEGER, "
				+ Columns.END_TIME_epoch_s + " INTEGER"
				+ ");";
	}
}
