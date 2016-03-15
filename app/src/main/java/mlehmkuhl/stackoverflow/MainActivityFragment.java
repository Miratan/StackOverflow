package mlehmkuhl.stackoverflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mlehmkuhl.stackoverflow.adapter.MainActivityAdapter;
import mlehmkuhl.stackoverflow.model.ItemDTO;
import mlehmkuhl.stackoverflow.ui.DividerItemDecoration;

public class MainActivityFragment extends Fragment {

	@Bind(R.id.list) RecyclerView mRecyclerView;

	private MainActivityAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_activity, container, false);

		ButterKnife.bind(this, view);


		mAdapter = new MainActivityAdapter(getActivity());
		mAdapter.setOnItemClickListener(new MainActivityAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(final ItemDTO item) {
				openQuestionActivity(item);
			}
		});

		List<ItemDTO> data = new ArrayList<>();
		createList(data);
		mAdapter.swapData(data);

		mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}


	private void openQuestionActivity(ItemDTO item) {

		Intent intent = new Intent(getContext(), QuestionActivity.class);
		Bundle b = new Bundle();

		switch (item.getId()) {
			case 0:
				b.putInt("tag", 0);
				break;
			case 1:
				b.putInt("tag", 1);
				break;
			case 2:
				b.putInt("tag", 2);
				break;
			case 3:
				b.putInt("tag", 3);
				break;
			case 4:
				b.putInt("tag", 4);
				break;
		}
		intent.putExtras(b);
		startActivity(intent);
	}

	private void createList(List<ItemDTO> data) {

		ItemDTO dto = new ItemDTO();
		dto.setDesc("Android");
		dto.setId(0);
		data.add(dto);

		dto = new ItemDTO();
		dto.setDesc("Java");
		dto.setId(1);
		data.add(dto);

		dto = new ItemDTO();
		dto.setDesc("Android Studio");
		dto.setId(2);
		data.add(dto);

		dto = new ItemDTO();
		dto.setDesc("Marshmallow");
		dto.setId(3);
		data.add(dto);

		dto = new ItemDTO();
		dto.setDesc("Nexus");
		dto.setId(4);
		data.add(dto);

	}

}
