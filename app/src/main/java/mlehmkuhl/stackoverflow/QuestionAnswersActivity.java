package mlehmkuhl.stackoverflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class QuestionAnswersActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_answers);

		Bundle b = getIntent().getExtras();
		int questionId = b.getInt("id");

		Bundle bundle = new Bundle();
		bundle.putInt("id", questionId);
		QuestionAnswersActivityFragment questionAnswersActivityFragment = new QuestionAnswersActivityFragment();
		questionAnswersActivityFragment.setArguments(bundle);

		ButterKnife.bind(this);
	}

}
