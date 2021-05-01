package com.nickmonks.parks.data;

import com.nickmonks.parks.model.Park;

import java.util.List;

// Process the list containing the JSON Park
// we prefix it with async since we will need to fetch
// asynchronously the dats

public interface AsyncResponse {
    void processPark(List<Park> Park);
}
