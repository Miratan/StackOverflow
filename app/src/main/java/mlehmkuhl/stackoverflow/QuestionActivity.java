package mlehmkuhl.stackoverflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		Bundle b = getIntent().getExtras();
		int type = b.getInt("tag");

		Bundle bundle = new Bundle();
		bundle.putInt("tag", type);
		QuestionActivityFragment questionActivityFragment = new QuestionActivityFragment();
		questionActivityFragment.setArguments(bundle);

		ButterKnife.bind(this);
	}

}
