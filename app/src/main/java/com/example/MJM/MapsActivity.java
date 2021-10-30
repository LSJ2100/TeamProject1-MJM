package com.example.MJM;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.MJM.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double[] latitude = {37.22475, 37.22146, 37.22246, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891,
                37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393};
        double[] longitude = {127.18768, 127.18684, 127.186712, 127.18684, 127.18850, 127.19080, 127.18851, 127.18534, 127.18296, 127.18375,
                127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186};
        String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관",
                "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)"};
        String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13",
                "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25"};

        for(int i = 0; i < 21; i++){
            MarkerOptions MJU = new MarkerOptions();
            MJU
                    .position(new LatLng(latitude[i], longitude[i]))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .alpha(0.5f)
                    .title(title[i])
                    .snippet(memo[i]);
            mMap.addMarker(MJU);
        }

        // Add a marker in Sydney and move the camera
        /*LatLng MJU = new LatLng(37.22475, 127.18768);
        mMap.addMarker(new MarkerOptions().position(MJU).title("명지대학교 정문"));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 18f));
    }
}