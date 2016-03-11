package mlehmkuhl.stackoverflow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mlehmkuhl.stackoverflow.R;
import mlehmkuhl.stackoverflow.model.QuestionAnswersDTO;
import mlehmkuhl.stackoverflow.model.QuestionDTO;
import mlehmkuhl.stackoverflow.ui.CircleTransform;

public class QuestionAnswersActivityAdapter extends RecyclerView.Adapter<QuestionAnswersActivityAdapter.ViewHolder> {

	public List<QuestionAnswersDTO> questions;
	public Context context;

	private OnItemClickListener listener;

	public interface OnItemClickListener {

		void onItemClick(QuestionAnswersDTO dto);

	}

	public QuestionAnswersActivityAdapter(Context context) {
		this.context = context;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_question_answers_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		QuestionAnswersDTO dto = questions.get(position);

		if(dto.getTitle() != null && !dto.getTitle().isEmpty()){
			holder.questionDetails.setVisibility(View.VISIBLE);
			holder.question.setText(dto.getQuestion());
			holder.title.setText(dto.getTitle());
		}

		if(dto.getName()!= null){
			holder.answersDetails.setVisibility(View.VISIBLE);
			holder.name.setText(dto.getName());
		}

		if(dto.getProfile() != null){
			Glide.with(context)
					.load(dto.getProfile())
					.placeholder(R.drawable.person_image_empty)
					.error(R.drawable.person_image_empty)
					.transform(new CircleTransform(context))
					.into(holder.profile);
		}

		if(dto.getAnswers() != null){
			holder.answers.setText(dto.getAnswers());
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return questions == null ? 0 : questions.size();
	}

	public void swapData(List<QuestionAnswersDTO> questions) {
		this.questions = questions;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@Bind(R.id.answers) TextView answers;
		@Bind(R.id.name) TextView name;
		@Bind(R.id.profile) ImageView profile;
		@Bind(R.id.questionDetails)	LinearLayout questionDetails;
		@Bind(R.id.answersDetails)	LinearLayout answersDetails;
		@Bind(R.id.title) TextView title;
		@Bind(R.id.question) TextView question;


		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (listener != null) {
				int position = getAdapterPosition();
				if (position != RecyclerView.NO_POSITION) {
					listener.onItemClick(questions.get(position));
				}
			}
		}
	}

}
