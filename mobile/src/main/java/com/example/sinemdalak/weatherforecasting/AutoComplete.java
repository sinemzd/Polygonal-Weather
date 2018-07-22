package com.example.sinemdalak.weatherforecasting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinemdalak.weatherforecasting.model.AutoCompletePojo;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;


public class AutoComplete extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    EditText autoCompleteTextView;
    Context context;
    List<AutoCompletePojo> autoCompletePojoList;
    String city;
    ServiceInterface service;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    String ID, Sunrise, Sunset;
    InputMethodManager inputMethodManager;
    RecyclerViewAdapter.ItemClickListener itemClickListener;
    ImageView imgbtn3;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        autoCompleteTextView = findViewById(R.id.auto_city);
        imgbtn3 = findViewById(R.id.imgbtn3);
        text = findViewById(R.id.find_text);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "VAG_Rounded_Bold.ttf");
        autoCompleteTextView.setTypeface(typeface);
        text.setTypeface(typeface);

        clickImgButton();

        //klavye otomatik açılsın
        inputMethodManager = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        context = getApplicationContext();
        itemClickListener = this;
        autoCompletePojoList=new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(context, autoCompletePojoList);
        adapter.setClickListener(itemClickListener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //divider
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.style_divider);
        itemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(itemDecoration);


        //edittext e yazdığım şehir ismini takip ediyor butonsuz
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                city = autoCompleteTextView.getText().toString();
                if (city.length() >= 3) {
                    try {
                        getData(city);
                    } catch (CertificateException e) {

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void clickImgButton(){
        imgbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(AutoComplete.this, MainActivity.class);
                startActivity(activity);
            }
        });
    }


    protected void getData(String name) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        service = ServiceClient.getClient(context).create(ServiceInterface.class);
        Call<ArrayList<AutoCompletePojo>> call;
        call = service.getServiceResponse(name);
        call.enqueue(new Callback<ArrayList<AutoCompletePojo>>() {

            @Override
            public void onResponse(Call<ArrayList<AutoCompletePojo>> call, retrofit2.Response<ArrayList<AutoCompletePojo>> response) {

                autoCompletePojoList = response.body();
                Log.d("Example :", response.body().toString());
                adapter.notifyDataChange(autoCompletePojoList);

            }

            @Override
            public void onFailure(Call<ArrayList<AutoCompletePojo>> call, Throwable t) {
                Log.d("Error service: ", t.toString());
            }

        });


    }

    @Override
    public void onItemClick(View view, int position) {
        ID = adapter.getCity(position);
        Sunrise = adapter.getSunrise(position);
        Sunset = adapter.getSunset(position);
        //Toast.makeText(this, "You clicked " + adapter.getCity(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AutoComplete.this, MainActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("Sunrise", Sunrise);
        intent.putExtra("Sunset", Sunset);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

    }

    public Context getContext() {
        return context;
    }

}
