package mlehmkuhl.stackoverflow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mlehmkuhl.stackoverflow.R;
import mlehmkuhl.stackoverflow.model.ItemDTO;
import mlehmkuhl.stackoverflow.model.QuestionDTO;
import mlehmkuhl.stackoverflow.ui.CircleTransform;

public class QuestionActivityAdapter extends RecyclerView.Adapter<QuestionActivityAdapter.ViewHolder> {

	public List<QuestionDTO> questions;
	public Context context;

	private OnItemClickListener listener;

	public interface OnItemClickListener {

		void onItemClick(QuestionDTO dto);

	}

	public QuestionActivityAdapter(Context context) {
		this.context = context;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_question_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		QuestionDTO dto = questions.get(position);
		holder.title.setText(Html.fromHtml(dto.getTitle()));
		holder.userName.setText(dto.getUserName());
		holder.score.setText(String.valueOf(dto.getScore()));

		Glide.with(context)
				.load(dto.getUserProfile())
				.placeholder(R.drawable.person_image_empty)
				.error(R.drawable.person_image_empty)
				.transform(new CircleTransform(context))
				.into(holder.userProfile);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return questions == null ? 0 : questions.size();
	}

	public void swapData(List<QuestionDTO> questions) {
		this.questions = questions;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@Bind(R.id.score) TextView score;
		@Bind(R.id.title) TextView title;
		@Bind(R.id.userName) TextView userName;
		@Bind(R.id.userProfile) ImageView userProfile;

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
