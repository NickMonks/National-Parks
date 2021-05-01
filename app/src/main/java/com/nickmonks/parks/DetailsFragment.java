package com.nickmonks.parks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nickmonks.parks.adapter.ViewPagerAdapter;
import com.nickmonks.parks.model.Park;
import com.nickmonks.parks.model.ParkViewModel;


public class DetailsFragment extends Fragment {

    private ParkViewModel parkViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager2; // we use "2" since its the lastest one - review in future

    private TextView description;
    private TextView activities;
    private TextView entranceFee;
    private TextView opHours;
    private TextView detailsTopics;
    private TextView directions;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance() {

        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        TextView parkName = view.findViewById(R.id.details_park_name);
        TextView parkDes = view.findViewById(R.id.details_park_designation_textview);

        //set up viewpager - we include the row layout using the adapter

        viewPager2 = view.findViewById(R.id.details_viewpager);

        // When you update the value stored in the LiveData object (by getting data),
        // it triggers all registered observers subscribed to the LiveData object,
        // as long as the attached LifecycleOwner is in active state

        // when we select, from the previous screen, a park, the livedata will notify the event handlers
        // (or subscribers) about this change in data.

        parkViewModel.getSelectedPark().observe(this, new Observer<Park>() {
            @Override
            public void onChanged(Park park) {

                parkName.setText(park.getName());
                parkDes.setText(park.getDesignation());

                // populate all fields
                description = view.getRootView().findViewById(R.id.description_description);
                activities = view.getRootView().findViewById(R.id.activities_description);
                entranceFee = view.getRootView().findViewById(R.id.entrance_fee_description);
                opHours = view.getRootView().findViewById(R.id.operating_hours_description);
                detailsTopics = view.getRootView().findViewById(R.id.topics_description);
                directions = view.getRootView().findViewById(R.id.direction_description);


                // Once the park is selected


                viewPagerAdapter = new ViewPagerAdapter(park.getImages());
                viewPager2.setAdapter(viewPagerAdapter);
                description.setText(park.getDescription());

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < park.getActivities().size(); i++) {
                    stringBuilder.append(park.getActivities().get(i).getName())
                            .append(" | ");
                }

                activities.setText(stringBuilder);

                if (park.getEntranceFees().size() > 0) {
                    entranceFee.setText(String.format("Cost: $%s", park.getEntranceFees().get(0).getCost()));
                }else{
                    entranceFee.setText(R.string.info_unavailable);
                }

                StringBuilder opsString = new StringBuilder();
                opsString
                        .append("Monday: ").append(park.getOperatingHours().get(0).getStandardHours().getMonday()).append("\n")
                        .append("Tuesday: ").append(park.getOperatingHours().get(0).getStandardHours().getTuesday()).append("\n")
                        .append("Wednesday: ").append(park.getOperatingHours().get(0).getStandardHours().getWednesday()).append("\n")
                        .append("Thursday: ").append(park.getOperatingHours().get(0).getStandardHours().getThursday()).append("\n")
                        .append("Friday: ").append(park.getOperatingHours().get(0).getStandardHours().getFriday()).append("\n")
                        .append("Saturday: ").append(park.getOperatingHours().get(0).getStandardHours().getSaturday()).append("\n")
                        .append("Sunday: ").append(park.getOperatingHours().get(0).getStandardHours().getSunday()).append("\n");

                opHours.setText(opsString);

                StringBuilder topicBuilder = new StringBuilder();
                for (int i = 0; i < park.getTopics().size(); i++) {
                    topicBuilder.append(park.getTopics().get(i).getName()).append(" | ");
                }

                detailsTopics.setText(topicBuilder);

                if (!TextUtils.isEmpty(park.getDirectionsInfo())){
                    directions.setText(park.getDirectionsInfo());
                }else{
                    directions.setText(R.string.no_directions);
                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
}