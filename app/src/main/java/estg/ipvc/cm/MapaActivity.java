package estg.ipvc.cm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //prepara o mapa e retorna para o método onMapReady

        mLocationRequest = new LocationRequest();
        //Iniciar o serviço GooglePlay
        buildGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;
        LatLng statue = new LatLng(41.69323, -8.83287);
        map.addMarker(new MarkerOptions().position(statue).title("Viana do Castelo"));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

       // map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(statue) //centra o mapa
                .zoom(18) // set ao zoom
                .bearing(45) // orientação
               // .tilt(60) // inclinação
                .build(); // cria a posição da camera
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


   /* @Override
    public void onMapClick(LatLng latLng){
        Intent i = new Intent(this, streetView.class);
        i.putExtra(Utils.LAT, latLng.latitude);
        i.putExtra(Utils.LONG, latLng.longitude);
        startActivity(i);
    } */

    @Override
    public void onMapLongClick(LatLng latLng){
        Toast.makeText(this, "Lat: " + String.valueOf(latLng.latitude)
                + "Long: " + String.valueOf(latLng.longitude), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    protected void createLocationRequest(){
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected synchronized void buildGoogleApiClient(){
        //constroi o pedido de API da google no OnStart
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addApi(LocationServices.API) .build();
        //Constroi o pedido de sinal
        createLocationRequest();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Iniciar o serviço do GooglePlay
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint){
        startLocationUpdates();
    }

    protected void startLocationUpdates(){
        //Pedido de Sinal
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Confirma as Permissoes
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location){
        Toast.makeText(this, "gps recebido -> Lat: " + location.getLatitude() + "long: " +location.getLongitude(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == 0){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (requestCode == 0){
                    startLocationUpdates();
                }
            }else {
                //Permission denied or request cancelled
            }
        }
    }


}
