package nithra.tamil.word.game.giftsuggestions.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.AddSeller;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SellerRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Add extends Fragment {
    TextInputEditText sellername, shopname, shopaddress, mobilenumber, city, state,country;
    TextView save;
    String sell_name, shop_name, shop_add, mob_num, shop_city, shop_country, shop_state;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    FragMove fragMove;

    public Add() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        fragMove = (FragMove) getContext();
        sellername = view.findViewById(R.id.sellername);
        shopname = view.findViewById(R.id.shopname);
        shopaddress = view.findViewById(R.id.shopaddress);
        mobilenumber = view.findViewById(R.id.mobilenumber);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state);
        country = view.findViewById(R.id.country);
        save = view.findViewById(R.id.save);
        IVPreviewImage = view.findViewById(R.id.IVPreviewImage);
        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sell_name = sellername.getText().toString().trim();
                shop_name = shopname.getText().toString().trim();
                shop_add = shopaddress.getText().toString().trim();
                mob_num = mobilenumber.getText().toString().trim();
                shop_city = city.getText().toString().trim();
                shop_state = state.getText().toString().trim();
                shop_country = country.getText().toString().trim();
                submit_res();
                fragMove.product();
            }
        });
        return view;
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
        map.put("action", "add_seller");
        map.put("user_id", "");
        map.put("shop_name", shop_name);
        map.put("seller_mobile", mob_num);
        map.put("name", sell_name);
        map.put("state", shop_state);
        map.put("address", shop_add);
        map.put("pincode", mob_num);
        //map.put("latitude", mob_num);
        //map.put("district", mob_num);
        map.put("city", shop_city);
        //map.put("company_logo[]", mob_num);


        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<AddSeller>> call = retrofitAPI.add_seller(map);
        call.enqueue(new Callback<ArrayList<AddSeller>>() {
            @Override
            public void onResponse(Call<ArrayList<AddSeller>> call, Response<ArrayList<AddSeller>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                  /* if (response.body().get(0).getStatus().equals("Success")) {

                        Toast.makeText(getContext(), "Your shop added successfully, Thank you", Toast.LENGTH_SHORT).show();
                    }*/

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<AddSeller>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });


    }


}