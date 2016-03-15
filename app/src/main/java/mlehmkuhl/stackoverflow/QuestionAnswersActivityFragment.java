package mlehmkuhl.stackoverflow;

import android.app.ProgressDialog;
import android.content.Context;
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
import mlehmkuhl.stackoverflow.adapter.QuestionAnswersActivityAdapter;
import mlehmkuhl.stackoverflow.db.StackOverflowDB;
import mlehmkuhl.stackoverflow.model.QuestionAnswersDTO;
import mlehmkuhl.stackoverflow.ui.DividerItemDecoration;

public class QuestionAnswersActivityFragment extends Fragment {

	@Bind(R.id.list) RecyclerView mRecyclerView;

	private QuestionAnswersActivityAdapter mAdapter;

	private ProgressDialog mProgressDialog;

	private int questionId;
	private String title;
	private String question;

	public QuestionAnswersActivityFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question_answers_activity, container, false);

		ButterKnife.bind(this, view);

		Bundle b = getActivity().getIntent().getExtras();
		questionId = b.getInt("id");
		title = b.getString("title");
		question = b.getString("question");
		new HttpRequestTask().execute();

		mAdapter = new QuestionAnswersActivityAdapter(getActivity());
		mAdapter.setOnItemClickListener(new QuestionAnswersActivityAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(final QuestionAnswersDTO item) {
			}
		});

		mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	private class HttpRequestTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected Object doInBackground(Void... params) {

			try {
				final String url = "https://api.stackexchange.com/2.2/questions/"+
						questionId+"/answers?pagesize=20&order=desc&sort=activity&site=stackoverflow&filter=!-*f(6t0WW)1e";
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

				List<QuestionAnswersDTO> data = new ArrayList<>();

				StackOverflowDB db = new StackOverflowDB(getContext());

				for(int i = 0 ; i < items.size(); i++){
					LinkedHashMap item = (LinkedHashMap) items.get(i);
					LinkedHashMap owner = (LinkedHashMap) item.get("owner");
					String profile = owner.get("profile_image").toString();
					String name = owner.get("display_name").toString();
					String answers = item.get("body").toString();
					int id = (int) item.get("answer_id");

					QuestionAnswersDTO dto = new QuestionAnswersDTO();
					dto.setId(id);
					dto.setName(name);
					dto.setProfile(profile);
					dto.setAnswers(answers);
					if(i == 0){
						dto.setQuestion(question);
						dto.setTitle(title);
					}

					db.insert(dto);
					data.add(dto);
				}

				if(items.size() == 0){
					QuestionAnswersDTO dto = new QuestionAnswersDTO();
					dto.setQuestion(question);
					dto.setTitle(title);
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
