package mlehmkuhl.stackoverflow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mlehmkuhl.stackoverflow.model.QuestionAnswersDTO;
import mlehmkuhl.stackoverflow.model.QuestionDTO;

public class StackOverflowDB extends SQLiteOpenHelper {

	private static String DBNAME = "stackoverflow";
	private static int VERSION = 1;

	public static final String QUESTION_ID = "_id";
	public static final String QUESTION_TITLE = "title";
	public static final String QUESTION_USER_NAME = "user_name";
	public static final String QUESTION_USER_PROFILE = "user_profile";
	public static final String QUESTION_SCORE = "score";
	public static final String QUESTION_BODY = "text";

	public static final String TABLE_QUESTION = "question";

	public static final String ANSWER_ID = "_id";
	public static final String ANSWER_USER_NAME = "user_name";
	public static final String ANSWER_USER_PROFILE = "user_profile";
	public static final String ANSWER_BODY = "text";

	public static final String TABLE_ANSWER = "answer";

	private SQLiteDatabase mDB;

	public StackOverflowDB(Context context) {
		super(context, DBNAME, null, VERSION);
		this.mDB = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql =     "create table "+ TABLE_QUESTION + " ( "
				+ QUESTION_ID + " integer primary key , "
				+ QUESTION_TITLE + " text , "
				+ QUESTION_USER_NAME + " text , "
				+ QUESTION_USER_PROFILE + " text , "
				+ QUESTION_SCORE + " int , "
				+ QUESTION_BODY + " text ) " ;

		db.execSQL(sql);

		sql =     "create table "+ TABLE_ANSWER + " ( "
				+ ANSWER_ID + " integer primary key , "
				+ ANSWER_USER_NAME + " text , "
				+ ANSWER_USER_PROFILE + " text , "
				+ ANSWER_BODY + " text ) " ;

		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public void insert(QuestionDTO questionDTO) {

		mDB = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(QUESTION_ID, questionDTO.getId());
		values.put(QUESTION_TITLE, questionDTO.getTitle());
		values.put(QUESTION_USER_NAME, questionDTO.getUserName());
		values.put(QUESTION_USER_PROFILE, questionDTO.getUserProfile());
		values.put(QUESTION_SCORE, questionDTO.getScore());
		values.put(QUESTION_BODY, questionDTO.getQuestion());

		try{
			mDB.insertOrThrow(TABLE_QUESTION, null, values);
		}catch (SQLiteConstraintException e){
			Log.d("Question Ignored","Duplicate Id");
		}finally {
			mDB.close();
		}
	}

	public void insert(QuestionAnswersDTO answersDTO) {

		mDB = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ANSWER_ID, answersDTO.getId());
		values.put(ANSWER_USER_NAME, answersDTO.getName());
		values.put(ANSWER_USER_PROFILE, answersDTO.getProfile());
		values.put(ANSWER_BODY, answersDTO.getAnswers());

		try{
			mDB.insertOrThrow(TABLE_ANSWER, null, values);
		}catch (SQLiteConstraintException e){
			Log.d("Answer Ignored","Duplicate Id");
		}finally {
			mDB.close();
		}
	}

	public Cursor getQuestions() {
		return mDB.query(TABLE_QUESTION, new String[] { QUESTION_ID,  QUESTION_TITLE , QUESTION_USER_NAME, QUESTION_USER_PROFILE, QUESTION_SCORE, QUESTION_BODY } ,
				null ,
				null, null, null,
				QUESTION_TITLE + " asc ");
	}

	public Cursor getAnswer() {
		return mDB.query(TABLE_ANSWER, new String[] { ANSWER_ID,  ANSWER_USER_NAME , ANSWER_USER_PROFILE, ANSWER_BODY } ,
				null ,
				null, null, null,
				ANSWER_ID + " asc ");
	}

}
