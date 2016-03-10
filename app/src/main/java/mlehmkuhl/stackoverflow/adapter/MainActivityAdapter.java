package mlehmkuhl.stackoverflow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mlehmkuhl.stackoverflow.R;
import mlehmkuhl.stackoverflow.model.ItemDTO;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

	public List<ItemDTO> items;
	public Context context;

	private OnItemClickListener listener;

	public interface OnItemClickListener {

		void onItemClick(ItemDTO dto);

	}

	public MainActivityAdapter(Context context) {
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ItemDTO dto = items.get(position);
		holder.desc.setText(dto.getDesc());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return items == null ? 0 : items.size();
	}

	public void swapData(List<ItemDTO> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@Bind(R.id.desc) TextView desc;

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
					listener.onItemClick(items.get(position));
				}
			}
		}
	}

}
