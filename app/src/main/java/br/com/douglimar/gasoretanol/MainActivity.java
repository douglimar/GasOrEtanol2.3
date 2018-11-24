package br.com.douglimar.gasoretanol;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Button btnGasOrEtanol = findViewById(R.id.btnGasOrEtanol);
        Button btnFindAGasStation = findViewById(R.id.btnLocalizarPostos);
        Button btnKmPorLitro = findViewById(R.id.btnLitrosAAbastecer);
        Button btnConsumoMedio = findViewById(R.id.btnConsumoMedio);

        // Create a AdView
        // Load Advertisement Banner
        AdView adView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        btnGasOrEtanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Firebase Statistics
                //********************************************
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Gas or Etanol Button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //********************************************

                Intent intent = new Intent(getApplicationContext(), GasOrEtanolActivity.class);
                startActivity(intent);
            }
        });


        btnFindAGasStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Firebase Statistics
                //********************************************
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "find a Gas Station");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //********************************************

                findAGasStation();
            }
        });



        btnConsumoMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Firebase Statistics
                //********************************************
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Consumo Medio");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //********************************************

                Intent intent = new Intent(getApplicationContext(), ConsumoMedioActivity.class);
                startActivity(intent);
            }
        });



        btnKmPorLitro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Firebase Statistics
                //********************************************
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "KM por Litro");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //********************************************

                Intent intent = new Intent(getApplicationContext(), AbastecimentoActivity.class);
                startActivity(intent);
            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void findAGasStation(){

//        Uri gmmIntentUri = Uri.parse(String.valueOf(R.string.expression_gmaps_geolocation_gasstation));
        String s_Search_tag = getString(R.string.posto_gasolina);

        //Toast.makeText(MainActivity.this, R.string.expression_gmaps_geolocation_gas_station, Toast.LENGTH_SHORT).show();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + s_Search_tag);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }

}
