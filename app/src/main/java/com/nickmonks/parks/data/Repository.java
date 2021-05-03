package com.nickmonks.parks.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nickmonks.parks.controller.AppController;
import com.nickmonks.parks.model.Activities;
import com.nickmonks.parks.model.EntranceFees;
import com.nickmonks.parks.model.Images;
import com.nickmonks.parks.model.OperatingHours;
import com.nickmonks.parks.model.Park;
import com.nickmonks.parks.model.StandardHours;
import com.nickmonks.parks.model.Topics;
import com.nickmonks.parks.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    // Repository will fetch info from the API - JSON format

    static List<Park> parkList = new ArrayList<>();

    public static void getParks(final AsyncResponse callback, String stateCode){

        String url = Util.getParksUrl(stateCode);
        Log.d("url", "onClick: " + url);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                // Response contains the payload of the parks fetched
                    try {
                        // we will get the JSON array of data
                        JSONArray jsonArray = response.getJSONArray("data");

                        // We loop over every park field and populate it
                        for (int i = 0; i < jsonArray.length() ; i++) {

                            Park park = new Park();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Populate fields
                            park.setId(jsonObject.getString("id"));
                            park.setFullName(jsonObject.getString("fullName"));
                            park.setLatitude(jsonObject.getString("latitude"));
                            park.setLongitude(jsonObject.getString("longitude"));
                            park.setParkCode(jsonObject.getString("parkCode"));
                            park.setStates(jsonObject.getString("states"));
                            park.setWeatherInfo(jsonObject.getString("weatherInfo"));
                            park.setName(jsonObject.getString("name"));
                            park.setDesignation(jsonObject.getString("designation"));

                            // set up activities

                            JSONArray activityArray = jsonObject.getJSONArray("activities");
                            List<Activities> activitiesList = new ArrayList<>();
                            for (int j = 0; j < activityArray.length(); j++) {
                                Activities activities = new Activities();
                                activities.setId(activityArray.getJSONObject(j).getString("id"));
                                activities.setName(activityArray.getJSONObject(j).getString("name"));

                                activitiesList.add(activities);
                            }

                            park.setActivities(activitiesList);

                            // Topics
                            JSONArray topicsArray = jsonObject.getJSONArray("topics");
                            List<Topics> topicsList = new ArrayList<>();
                            for (int j = 0; j < topicsArray.length(); j++) {
                                Topics topics = new Topics();
                                topics.setId(topicsArray.getJSONObject(j).getString("id"));
                                topics.setName(topicsArray.getJSONObject(j).getString("name"));

                                topicsList.add(topics);
                            }

                            park.setTopics(topicsList);

                            //Operating Hours
                            JSONArray opHours = jsonObject.getJSONArray("operatingHours");
                            List<OperatingHours> operatingHours = new ArrayList<>();
                            for (int j = 0; j < opHours.length(); j++) {
                                OperatingHours op = new OperatingHours();

                                op.setDescription(opHours.getJSONObject(j).getString("description"));

                                StandardHours standardHours = new StandardHours();
                                JSONObject hours = opHours.getJSONObject(j).getJSONObject("standardHours");

                                    standardHours.setMonday(hours.getString("monday"));
                                    standardHours.setTuesday(hours.getString("tuesday"));
                                    standardHours.setWednesday(hours.getString("wednesday"));
                                    standardHours.setThursday(hours.getString("thursday"));
                                    standardHours.setFriday(hours.getString("friday"));
                                    standardHours.setSaturday(hours.getString("saturday"));
                                    standardHours.setSunday(hours.getString("sunday"));

                                op.setName(opHours.getJSONObject(j).getString("name"));
                                op.setStandardHours(standardHours);

                                operatingHours.add(op);
                            }

                            park.setOperatingHours(operatingHours);

                            park.setDirectionsInfo(jsonObject.getString("directionsInfo"));


                            // Entrance fee

                            JSONArray entranceFeesArray = jsonObject.getJSONArray("entranceFees");
                            List<EntranceFees> entranceFeeList = new ArrayList<>();
                            for (int j = 0; j < entranceFeesArray.length(); j++) {
                                EntranceFees entranceFees = new EntranceFees();

                                entranceFees.setCost(entranceFeesArray.getJSONObject(j).getString("cost"));
                                entranceFees.setDescription(entranceFeesArray.getJSONObject(j).getString("description"));
                                entranceFees.setTitle(entranceFeesArray.getJSONObject(j).getString("title"));

                                entranceFeeList.add(entranceFees);
                            }

                            park.setEntranceFees(entranceFeeList);

                            park.setWeatherInfo(jsonObject.getString("weatherInfo"));

                            park.setDescription(jsonObject.getString("description"));



                            // Images - SInce image is a JSON array, we need to manually map it
                            // on our POJO Image class and add itin the list.

                            JSONArray imageList = jsonObject.getJSONArray("images");
                            List<Images> list = new ArrayList<>();


                            for (int j = 0; j < imageList.length(); j++) {
                                Images images = new Images();
                                images.setCredit(imageList.getJSONObject(j).getString("credit"));
                                images.setTitle(imageList.getJSONObject(j).getString("title"));
                                images.setUrl(imageList.getJSONObject(j).getString("url"));

                                list.add(images);
                            }

                            park.setImages(list);



                            // add to our park list
                            parkList.add(park);

                            Log.d("Response", "onResponse: "+ parkList.toString());

                        }

                        // WHen the park is populated, we will pass this parkList to the callback
                        // so any class that implements this interface will have this list!
                        if (callback != null){ callback.processPark(parkList); }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.d("Response", "onResponse: ERROR ");
                    error.printStackTrace();
                });


        // Add to the queue in the app controller:
        AppController appController = AppController.getInstance();
        Log.d("Response", "getParks: "+appController);
        appController.addToRequestQueue(jsonObjectRequest);
    }
}
