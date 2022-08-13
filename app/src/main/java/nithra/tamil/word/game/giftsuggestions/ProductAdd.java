/*
package nithra.tamil.word.game.giftsuggestions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.AddSeller;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GiftFor;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Occasion;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdd extends AppCompatActivity {

    TextInputEditText productname, prod_prize, offer_prize, offer_percentage, prod_des;
    TextView save;
    Spinner spin_occaction, spin_gender;
    TextView myproduct;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    SharedPreference sharedPreference = new SharedPreference();
    String gift_name, gift_image, gift_category, gift_for, gift_amount, discount, total_amount, gift_description;
    ArrayList<String> spin;
    ArrayList<String> spin1;
    ArrayList<GiftFor> giftfor;
    ArrayList<Occasion> occasion;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_product);
        productname = findViewById(R.id.productname);
        spin_occaction = findViewById(R.id.spin_occaction);
        spin_gender = findViewById(R.id.spin_gender);
        prod_prize = findViewById(R.id.prod_prize);
        offer_prize = findViewById(R.id.offer_prize);
        offer_percentage = findViewById(R.id.offer_percentage);
        prod_des = findViewById(R.id.prod_des);
        save = findViewById(R.id.save);
        myproduct = findViewById(R.id.myproduct);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        spin = new ArrayList<>();
        spin1 = new ArrayList<>();
        giftfor = new ArrayList<GiftFor>();
        occasion = new ArrayList<Occasion>();


        gender_gift();
        gift_occasion();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_name = productname.getText().toString().trim();
                gift_amount = offer_prize.getText().toString().trim();
                discount = offer_percentage.getText().toString().trim();
                total_amount = prod_prize.getText().toString().trim();
                gift_description = prod_des.getText().toString().trim();

                if (gift_name.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Product Name...");
                } else if (spin_occaction.getSelectedItemPosition() == 0) {
                    Utils_Class.toast_center(getApplicationContext(), "Please select Occasion...");
                } else if (spin_gender.getSelectedItemPosition() == 0) {
                    Utils_Class.toast_center(getApplicationContext(), "Please select Gender...");
                } else if (gift_amount.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Product Prize...");
                } else if (discount.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Offer Percentage...");
                } else if (total_amount.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Offer Prize...");
                }  else if (gift_description.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Product Description...");
                } else {

                    submit_res();

                }


            }
        });

        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });
        myproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProduct.class);
                startActivity(intent);
            }
        });
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public void submit_res() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "add_gift");
        map.put("user_id", sharedPreference.getString(getApplicationContext(), "user_id"));
        map.put("gift_category", gift_category);
        map.put("gift_for", gift_for);
        map.put("gift_name", gift_name);
        // map.put("gift_image", sell_name);
        map.put("gift_description", gift_description);
        map.put("gift_amount", gift_amount);
        map.put("discount", discount);
        map.put("total_amount", total_amount);


        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<AddSeller>> call = retrofitAPI.add_seller(map);
        call.enqueue(new Callback<ArrayList<AddSeller>>() {
            @Override
            public void onResponse(Call<ArrayList<AddSeller>> call, Response<ArrayList<AddSeller>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {

                        spin_occaction.setSelection(0);
                        spin_gender.setSelection(0);
                        productname.getText().clear();
                        prod_prize.getText().clear();
                        offer_percentage.getText().clear();
                        offer_prize.getText().clear();
                        prod_des.getText().clear();

                        Toast.makeText(getApplicationContext(), "Your product added successfully, Thank you", Toast.LENGTH_SHORT).show();
                    }

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<AddSeller>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });

    }


    public void gender_gift() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "gift_for");
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GiftFor>> call = retrofitAPI.gift_giftfor(map);
        call.enqueue(new Callback<ArrayList<GiftFor>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftFor>> call, Response<ArrayList<GiftFor>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    giftfor.addAll(response.body());
                    spinner();
                    //adapter.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GiftFor>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void spinner() {
        spin.add(0, "All category");
        for (int i = 0; i < giftfor.size(); i++) {
            spin.add(giftfor.get(i).people);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(adapter);
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    gift_for = giftfor.get(i-1).people;
                }
               */
/* if (i == 0) {
                    spin_gender.setEnabled(false);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spin_1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_gender.setAdapter(adapter);
                } else {
                    spin_gender.setEnabled(true);
                    spin.clear();
                }*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();

    }

    public void gift_occasion() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "category");
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Occasion>> call = retrofitAPI.gift_occasion(map);
        call.enqueue(new Callback<ArrayList<Occasion>>() {
            @Override
            public void onResponse(Call<ArrayList<Occasion>> call, Response<ArrayList<Occasion>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    occasion.addAll(response.body());
                    spinner1();
                    //adapter.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Occasion>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void spinner1() {
        spin1.add(0, "All category");
        for (int i = 0; i < occasion.size(); i++) {
            spin1.add(occasion.get(i).category);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spin1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_occaction.setAdapter(adapter);
        spin_occaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    gift_category = occasion.get(i-1).category;
                }
                */
/*if (i == 0) {

                    spin_gender.setEnabled(false);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spin_1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_gender.setAdapter(adapter);
                } else {
                    spin_gender.setEnabled(true);
                    spin_1.clear();
                    spinner_1(i);
                }*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();

    }

}
*/
