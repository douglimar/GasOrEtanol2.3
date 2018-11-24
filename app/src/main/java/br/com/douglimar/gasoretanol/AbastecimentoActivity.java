package br.com.douglimar.gasoretanol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

import java.text.NumberFormat;
import java.util.Locale;

public class AbastecimentoActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final Locale ptBR = new Locale("pt", "BR");

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(ptBR);

    private double billAmount = 0.0;
    private TextView tvDisplayMoney;
    private TextView tvDisplayFuelPrice;
    private TextView tvMoneyLabel;
    private TextView tvGasPriceLabel;
    private TextView tvResult;
    private ImageView imgResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Create a AdView
        // Load Advertisement Banner
        AdView mAdView = findViewById(R.id.adViewAbastecimento);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        int maxLength = 3;

        tvResult = findViewById(R.id.tvResultAbastecimento);
        imgResult = findViewById(R.id.imgResultAbastecimento);

        // Etanol Value

        tvMoneyLabel = findViewById(R.id.tvMoneyLabel);
        tvDisplayMoney = findViewById(R.id.tvDisplayMoney);

        final EditText edtDisplayMoney = findViewById(R.id.edtDisplayMoney);

        // Define the Mask to TextView to get Currency Format
        edtDisplayMoney.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
        edtDisplayMoney.addTextChangedListener(editTextTextWatcher);

        edtDisplayMoney.requestFocus();

        // Gas Value
        tvGasPriceLabel = findViewById(R.id.tvFuelPriceLabel);
        tvDisplayFuelPrice = findViewById(R.id.tvDisplayFuelPrice);

        final EditText edtDisplayFuelPrice = findViewById(R.id.edtDisplayFuelPrice);

        // Define the Mask to TextView to get Currency Format
        edtDisplayFuelPrice.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        edtDisplayFuelPrice.addTextChangedListener(editTextTextWatcher2);

        final Button btnCalcular = findViewById(R.id.btnCalcular);


        edtDisplayMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMoneyLabel.setTextColor(getResources().getColor(R.color.colorOrange));
                tvGasPriceLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayMoney.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                tvDisplayFuelPrice.setBackgroundColor(getResources().getColor(R.color.lightGray));
            }
        });

        edtDisplayFuelPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGasPriceLabel.setTextColor(getResources().getColor(R.color.colorOrange));
                tvMoneyLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayMoney.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvDisplayFuelPrice.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
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

                tvGasPriceLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                tvMoneyLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayFuelPrice.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvDisplayMoney.setBackgroundColor(getResources().getColor(R.color.lightGray));

                btnCalcular.requestFocus();

                hideKeyboardFrom(getApplicationContext(), v);

                if ( (edtDisplayFuelPrice.getText().toString().isEmpty())||
                        (edtDisplayMoney.getText().toString().isEmpty()) ) {

                    Toast.makeText(getApplicationContext(), "Informe Quanto você quer abastecer e o Valor do Combustível antes de continuar.", Toast.LENGTH_SHORT).show();
                }
                else  {

                    Double dMoney = Double.parseDouble(edtDisplayMoney.getText().toString()) / 100;
                    Double dFuelPrice = Double.parseDouble(edtDisplayFuelPrice.getText().toString()) / 100;

                    tvResult.setText(calcAbastecimento(dMoney, dFuelPrice));




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

    private final TextWatcher editTextTextWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {

                billAmount = Double.parseDouble(s.toString()) / 100.0;

            } catch (NumberFormatException e ) {

                billAmount = 0.0;
            }

            tvDisplayFuelPrice.setText(currencyFormat.format(billAmount));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private final TextWatcher editTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {

                billAmount = Double.parseDouble(s.toString()) / 100.0;

            } catch (NumberFormatException e ) {

                billAmount = 0.0;
            }

            tvDisplayMoney.setText(currencyFormat.format(billAmount));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @SuppressLint("DefaultLocale")
    private String calcAbastecimento(Double vMoney, Double vFuelPrice) {

        Double result = vMoney/vFuelPrice;

        imgResult.setImageResource(R.drawable.ic_postos_24dp);
        tvResult.setBackground(getResources().getDrawable(R.drawable.bg_activity_km_por_litro));

        return String.format("Com esse valor você irá abastecer \n\n %s \n\nlitros de combustível.", String.format("%.2f", result));

    }


    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
