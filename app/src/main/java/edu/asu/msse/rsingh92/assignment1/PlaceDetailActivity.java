package edu.asu.msse.rsingh92.assignment1;

/*
        * Copyright 2020 Rohit Kumar Singh,
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        * http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        *
        * @author Rohit Kumar Singh rsingh92@asu.edu
        *
        * @version January 2016
        */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;

import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {


    private EditText name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;
    private PlaceDescription place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        addressTitle = findViewById(R.id.addressTitle);
        addressStreet = findViewById(R.id.addressStreet);
        elevation = findViewById(R.id.elevation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        place = getPlace();

        name.setText(place.getName());
        description.setText(place.getDescription());
        category.setText(place.getCategory());
        addressTitle.setText(place.getAddressTitle());
        addressStreet.setText(place.getAddressStreet());
        elevation.setText(place.getElevation());
        latitude.setText(place.getLatitude());
        longitude.setText(place.getLongitude());

    }

    private PlaceDescription getPlace(){
        PlaceDescription place = (PlaceDescription)getIntent().getSerializableExtra("Place");
        return place;
    }

}