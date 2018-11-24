package br.com.douglimar.gasoretanol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ConsumoMedioActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edtKmPercorrido;
    private EditText edtLitrosCombustivel;
    private ImageView imgConsumoMedio;
    private TextView tvConsumoMedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_medio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        edtKmPercorrido = findViewById(R.id.edtKmPercorrido);
        edtLitrosCombustivel = findViewById(R.id.edtLitrosGastos);
        Button btnCalcularConsumo = findViewById(R.id.btnConsumoMedio);
        imgConsumoMedio = findViewById(R.id.imgConsumoMedio);
        tvConsumoMedio = findViewById(R.id.tvConsumoMedio);

        // Create a AdView
        // Load Advertisement Banner
        AdView mAdView = findViewById(R.id.adViewonsumoMedio);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        btnCalcularConsumo.setOnClickListener(new View.OnClickListener() {
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


                hideKeyboardFrom(getApplicationContext(), v);


                if ( (edtKmPercorrido.getText().toString().isEmpty())||
                        (edtLitrosCombustivel.getText().toString().isEmpty()) ) {

                    Toast.makeText(getApplicationContext(), getString(R.string.consumo_medio_toast), Toast.LENGTH_SHORT).show();
                }
                else {
                    Double iKM = Double.parseDouble(edtKmPercorrido.getText().toString());
                    Double iLitros = Double.parseDouble(edtLitrosCombustivel.getText().toString());

                    calcularConsumoMedio(iKM, iLitros);
                }

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


    @SuppressLint("DefaultLocale")
    private void calcularConsumoMedio(Double pKmPercorrido, Double pLitrosCombustivel) {

        Double result = pKmPercorrido / pLitrosCombustivel;

        tvConsumoMedio.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        tvConsumoMedio.setText(String.format(getString(R.string.consumo_medio_result), String.format("%.2f", result)));
        imgConsumoMedio.setImageResource(R.drawable.ic_battery_90_green_24dp);


    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
