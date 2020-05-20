package com.example.ee461l_p1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // end of Wenran driving, Yinghong driving now

    private GoogleMap mMap;
    private double lat;
    private double lon;
    private JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("latitude1", 0.00);
        lon = intent.getDoubleExtra("longitude1", 0.00);

        // end of Yinghong driving, Wenran driving now

        response = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        String urlBase = "https://api.darksky.net/forecast/a2e05739a5bc30b2503c7ecf36c793fa/";
        final String location = Double.toString(lat) + "," + Double.toString(lon);
        final String url = urlBase + location + "?exclude=[minutely,hourly,daily,alerts,flags]";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                response = res;
                try{
                    JSONObject jsonObject = response.getJSONObject("currently");
                    String temperature = jsonObject.getString("temperature");
                    String humidity = jsonObject.getString("humidity");
                    String windSpeed = jsonObject.getString("windSpeed");
                    String precipProbability = jsonObject.getString("precipIntensity");
                    //Double precipProbability = jsonObject.getJSONObject("current").getDouble("precipIntensity");
                    String precipType;
                    if (jsonObject.has("precipType")){
                        precipType = jsonObject.getString("precipType");
                    }else{
                        precipType = "null";
                    }

                    // end of Wenran driving, Yinghong driving now

                    TextView showResponse = (TextView) findViewById(R.id.textView);
                    showResponse.setText( " Weather at " + location +
                            "\n Temperature: " +temperature +
                            "\n Humidity: " + humidity +
                            "\n Windspeed: " + windSpeed +
                            "\n Precipitation Probability: " + precipProbability +
                            "\n Precipitation Type: " + precipType);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });

        queue.add(objectRequest);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    // end of Yinghong driving, Wenran driving now

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng newLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(newLocation).title("Marker 1"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newLocation, 15);
        mMap.moveCamera(update);

    }
}
