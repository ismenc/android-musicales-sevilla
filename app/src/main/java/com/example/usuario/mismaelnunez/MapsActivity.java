package com.example.usuario.mismaelnunez;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng locTeatro;
    private GPSLocation gps;
    private String nombreTeatro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;

        locTeatro = new LatLng(extras.getDouble("LATITUD"), extras.getDouble("LONGITUD"));
        nombreTeatro = extras.getString("NOMBRETEATRO");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        solicitarPermisoLocalizacion();

        // Calculamos nuetra ubicación
        gps = new GPSLocation(this);
        LatLng misUbicacion = new LatLng(gps.getLatitud(), gps.getLongitud());

        // Añadimos los marcadores del teatro y usuario
        mMap.addMarker(new MarkerOptions().position(locTeatro).title(nombreTeatro)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.musical));
        mMap.addMarker(new MarkerOptions().position(misUbicacion).title("Yo")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.persona));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( locTeatro, 12));

        // Click listener largo
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Medir la distancia se hace con array por fuerza
                float distancia[] = {0};
                Location.distanceBetween(latLng.latitude, latLng.longitude, locTeatro.latitude, locTeatro.longitude, distancia);

                Toast.makeText(MapsActivity.this, "Distancia: " + distancia[0], Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Método que solicita el permiso de localización (a menudo no funciona)
     */
    private void solicitarPermisoLocalizacion(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Entramos aquí si habíamos rechazado previamente este permiso
                Toast.makeText(this, "Esta app necesita permiso de ubicación para funcionar.", Toast.LENGTH_LONG).show();
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }
}
