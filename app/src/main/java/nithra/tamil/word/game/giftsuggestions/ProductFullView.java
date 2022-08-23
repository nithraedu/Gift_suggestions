package nithra.tamil.word.game.giftsuggestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.AddGift;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GetGift;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GiftList;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFullView extends AppCompatActivity {
    ArrayList<GetGift> gift;
    ImageView IVPreviewImage;
    TextView giftname,giftcategory,giftgender,giftprize,offerprize,offerpercen,description;
    SharedPreference sharedPreference = new SharedPreference();
    Intent intent;
    Bundle extra;
    String id_gift;
    ImageView back,profile_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_full_view);
        gift = new ArrayList<GetGift>();
        IVPreviewImage=findViewById(R.id.IVPreviewImage);
        giftname=findViewById(R.id.giftname);
        giftcategory=findViewById(R.id.giftcategory);
        giftgender=findViewById(R.id.giftgender);
        giftprize=findViewById(R.id.giftprize);
        offerprize=findViewById(R.id.offerprize);
        offerpercen=findViewById(R.id.offerpercen);
        description=findViewById(R.id.description);
        intent = getIntent();
        extra = intent.getExtras();
        id_gift = extra.getString("id");
        back = findViewById(R.id.back);
        profile_edit = findViewById(R.id.profile_edit);

        giftprize.setPaintFlags(giftprize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Utils_Class.mProgress(this, "Loading please wait...", false).show();
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProductEdit.class);
                i.putExtra("id", id_gift);
                startActivity(i);
            }
        });

        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img_view;
                Dialog dialog = new Dialog(ProductFullView.this, android.R.style.Theme_DeviceDefault);
                dialog.setContentView(R.layout.image_view);

                //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(true);
                img_view = dialog.findViewById(R.id.img_view);
                Glide.with(getApplicationContext()).load(gift.get(0).getGiftImage())
                        //.error(R.drawable.gift_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_view);
                dialog.show();

            }
        });

        Utils_Class.mProgress(ProductFullView.this, "Loading please wait...", false).show();

        category();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getInt(getApplicationContext(), "finish_product") == 1) {
            category();
            sharedPreference.putInt(ProductFullView.this, "finish_product", 0);
        }
    }

    public void category() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_gift");
        map.put("id", id_gift);

        System.out.println("print map : " + map);

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GetGift>> call = retrofitAPI.getgift(map);
        call.enqueue(new Callback<ArrayList<GetGift>>() {
            @Override
            public void onResponse(Call<ArrayList<GetGift>> call, Response<ArrayList<GetGift>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    gift.clear();
                    gift.addAll(response.body());
                    Glide.with(getApplicationContext()).load(gift.get(0).getGiftImage())
                            //.error(R.drawable.gift_1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage);
                    giftname.setText(gift.get(0).getGiftName());
                    giftcategory.setText(gift.get(0).getGiftCat());
                    giftgender.setText(gift.get(0).getGiftForPeople());
                    giftprize.setText("\u20B9 " +gift.get(0).getGiftAmount());
                    offerpercen.setText(gift.get(0).getDiscount());
                    offerprize.setText("\u20B9 " +gift.get(0).getTotalAmount());
                    description.setText(gift.get(0).getGiftDescription());
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GetGift>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

}