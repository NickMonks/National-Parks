package com.nickmonks.parks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickmonks.parks.adapter.OnParkClickListener;
import com.nickmonks.parks.adapter.ParkRecyclerViewAdapter;
import com.nickmonks.parks.data.AsyncResponse;
import com.nickmonks.parks.data.Repository;
import com.nickmonks.parks.model.Park;
import com.nickmonks.parks.model.ParkViewModel;

import java.util.ArrayList;
import java.util.List;


public class ParksFragment extends Fragment implements OnParkClickListener {

    private RecyclerView recyclerView;
    private ParkRecyclerViewAdapter parkRecyclerViewAdapter;
    private List<Park> parkList;
    private ParkViewModel parkViewModel;

    public ParksFragment() {
        // Required empty public constructor
    }

    public static ParksFragment newInstance() {
        ParksFragment fragment = new ParksFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkList = new ArrayList<>();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate Park ViewModel:
        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        // Retrieve the park lists from the ViewModel
        if (parkViewModel.getParks().getValue() != null ){
            parkList = parkViewModel.getParks().getValue();

            // Retrieve park Lists from the ViewModel LiveData - With this pattern we ensure
            // we won't fetch again and again the data; we store it in the LiveData

            parkRecyclerViewAdapter = new ParkRecyclerViewAdapter(parkList, ParksFragment.this);
            recyclerView.setAdapter(parkRecyclerViewAdapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parks, container, false);

        recyclerView = view.findViewById(R.id.park_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onParkClicked(Park park) {
        // pass data from the activity to the fragment
        // we will use viewModel, with livedata
        Log.d("Park", "onParkClicked: " + park.getName());
        parkViewModel.setSelectedPark(park);

        getFragmentManager().beginTransaction()
                .replace(R.id.park_fragment,DetailsFragment.newInstance())
                .commit();
    }
}