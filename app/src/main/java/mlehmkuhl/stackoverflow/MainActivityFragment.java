package mlehmkuhl.stackoverflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

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
				Toast.makeText(getContext(), item.getDesc(), Toast.LENGTH_SHORT).show();
				//FIXME - carregar as 20 questões mais recentes que possuem a tag associada ao tópico
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
