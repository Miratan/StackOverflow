package mlehmkuhl.stackoverflow.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class StackOverflowContentProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "mlehmkuhl.stackoverflow.db";

	public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/question");
	public static final Uri CONTENT_URI_ANSWER = Uri.parse("content://" + PROVIDER_NAME + "/answer");

	private static final int QUESTION = 1;
	private static final int ANSWER = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "question", QUESTION);
		uriMatcher.addURI(PROVIDER_NAME, "answer", ANSWER);
	}

	StackOverflowDB stackOverflowDB;

	@Override
	public boolean onCreate() {
		stackOverflowDB = new StackOverflowDB(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		if(uriMatcher.match(uri)== QUESTION){
			return stackOverflowDB.getQuestions();
		}else if(uriMatcher.match(uri)== ANSWER){
			return stackOverflowDB.getAnswer();
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
