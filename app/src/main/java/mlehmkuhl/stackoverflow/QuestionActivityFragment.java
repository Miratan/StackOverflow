package mlehmkuhl.stackoverflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mlehmkuhl.stackoverflow.adapter.QuestionActivityAdapter;
import mlehmkuhl.stackoverflow.db.StackOverflowDB;
import mlehmkuhl.stackoverflow.model.QuestionDTO;
import mlehmkuhl.stackoverflow.ui.DividerItemDecoration;

public class QuestionActivityFragment extends Fragment {

	@Bind(R.id.list) RecyclerView mRecyclerView;

	private QuestionActivityAdapter mAdapter;

	private ProgressDialog mProgressDialog;

	private int tag;

	public QuestionActivityFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question_activity, container, false);

		ButterKnife.bind(this, view);

		Bundle b = getActivity().getIntent().getExtras();
		tag = b.getInt("tag");
		new HttpRequestTask().execute();

		mAdapter = new QuestionActivityAdapter(getActivity());
		mAdapter.setOnItemClickListener(new QuestionActivityAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(final QuestionDTO item) {
				openQuestionAnswersActivity(item);
			}
		});

		mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	private void openQuestionAnswersActivity(QuestionDTO item) {

		Intent intent = new Intent(getContext(), QuestionAnswersActivity.class);
		Bundle b = new Bundle();
		b.putInt("id", item.getId());
		b.putString("title", item.getTitle());
		b.putString("question", item.getQuestion());
		intent.putExtras(b);
		startActivity(intent);

	}

	private class HttpRequestTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected Object doInBackground(Void... params) {

			String search = "";
			switch (tag){
				case 0:
					search = "android";
					break;
				case 1:
					search = "java";
					break;
				case 2:
					search = "android-studio";
					break;
				case 3:
					search = "marshmallow";
					break;
				case 4:
					search = "nexus";
					break;
			}

			try {
				final String url = "https://api.stackexchange.com/2.2/questions?pagesize=20&order=desc&sort=activity&tagged="+search+"&site=stackoverflow&filter=!9YdnSIN18";
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				Object object = restTemplate.getForObject(url, Object.class);
				return object;
			} catch (Exception e) {
				Log.e("MainActivity", e.getMessage(), e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object object) {

			if(object != null){

				LinkedHashMap map = (LinkedHashMap) object;
				ArrayList items = (ArrayList) map.get("items");

				List<QuestionDTO> data = new ArrayList<>();

				StackOverflowDB db = new StackOverflowDB(getContext());

				for(int i = 0 ; i < items.size(); i++){
					LinkedHashMap item = (LinkedHashMap) items.get(i);
					LinkedHashMap owner = (LinkedHashMap) item.get("owner");
					int score = (int) item.get("score");
					String title = item.get("title").toString();
					String question = item.get("body").toString();
					int questionId = (int) item.get("question_id");
					String profile = owner.get("profile_image").toString();
					String name = owner.get("display_name").toString();

					QuestionDTO dto = new QuestionDTO();
					dto.setTitle(title);
					dto.setScore(score);
					dto.setUserName(name);
					dto.setUserProfile(profile);
					dto.setId(questionId);
					dto.setQuestion(question);

					db.insert(dto);
					data.add(dto);
				}

				mAdapter.swapData(data);

			}else{

				NetworkInfo info = ((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
				if (info == null){
					Toast.makeText(getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
				}

			}
			dismissProgressDialog();

		}

	}

	protected void showProgressDialog(@StringRes int resId) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setMessage(getString(resId));
		mProgressDialog.show();
	}

	public void showProgressDialog() {
		showProgressDialog(R.string.please_wait);
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

}
