package liechty.android.pleaseholdapplause.provider;

import android.net.Uri;

public final class PHAProviderContract {

	public static final String AUTHORITY = "liechty.android.pleaseholdapplause";
	private static final String BASE_URI = "content://" + AUTHORITY; 
	
	public static final class Presentation {
		
		public static final String DIRECTORY = "presentations";
		
		public static final class CursorColumns {
			
			public static final String _ID = "_id";
			public static final String SPEAKER = "speaker";
			public static final String TITLE = "title";
			public static final String VENUE = "venue";
			public static final String START_TIME_epoch_s = "start";
			public static final String END_TIME_epoch_s = "end";
			public static final String WEB_ID = "web_id";
			
		}
		
		public static Uri getPresentations() {
			return Uri.parse(BASE_URI + "/" + DIRECTORY);
		}
		
		public static Uri getPresentation(long id) {
			return Uri.parse(BASE_URI + "/" + DIRECTORY + "/" + Long.toString(id));
		}
	}
	
}
