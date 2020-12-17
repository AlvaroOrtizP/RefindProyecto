package com.example.refindproyecto;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refindproyecto.Modelo.Anuncio;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityAnuncio extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ImageView imgItemDetail;
    private TextView tvTituloDetail;
    private TextView tvDescripcionDetail;
    private Anuncio datosAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        datosAnuncio = (Anuncio) getIntent().getExtras().getSerializable("datosanuncio");
        initViews();
        initValues();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng localizacion = new LatLng(datosAnuncio.getLatitud(), datosAnuncio.getLongitud());
        mMap.addMarker(new MarkerOptions().position(localizacion).title(datosAnuncio.getTitulo()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 13));
    }
    private void initViews() {
        imgItemDetail = findViewById(R.id.imgItemDetail);
        tvTituloDetail = findViewById(R.id.tvTituloDetail);
        tvDescripcionDetail = findViewById(R.id.tvDescripcionDetail);
    }

    private void initValues(){
        imgItemDetail.setImageResource(datosAnuncio.getImgResource());
        tvTituloDetail.setText(datosAnuncio.getTitulo());
        tvDescripcionDetail.setText(datosAnuncio.getDescripcion());
    }
}