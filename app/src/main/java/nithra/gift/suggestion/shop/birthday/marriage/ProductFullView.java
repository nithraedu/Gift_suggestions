package nithra.gift.suggestion.shop.birthday.marriage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.DeleteGift;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GetGift;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFullView extends AppCompatActivity {
    ArrayList<GetGift> gift;
    ImageView IVPreviewImage;
    TextView giftname, giftcategory, giftgender, giftprize, offerprize, offerpercen, description, head, detail_shop_name, detail_add;
    SharedPreference sharedPreference = new SharedPreference();
    Intent intent;
    Bundle extra;
    String id_gift;
    ImageView profile_edit, profile_delete;
    LinearLayout back;
    TextView btShowmore,btShowmore1;
    CardView card_mail, card_web;
    LinearLayout phone;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.seller_view);
        gift = new ArrayList<GetGift>();
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        giftname = findViewById(R.id.giftname);
        giftcategory = findViewById(R.id.giftcategory);
        giftgender = findViewById(R.id.giftgender);
        giftprize = findViewById(R.id.giftprize);
        offerprize = findViewById(R.id.offerprize);
        offerpercen = findViewById(R.id.offerpercen);
        description = findViewById(R.id.description);
        head = findViewById(R.id.head);
        detail_shop_name = findViewById(R.id.detail_shop_name);
        detail_add = findViewById(R.id.detail_add);
        intent = getIntent();
        extra = intent.getExtras();
        id_gift = extra.getString("id");
        back = findViewById(R.id.back);
        profile_edit = findViewById(R.id.profile_edit);
        btShowmore = findViewById(R.id.btShowmore);
        btShowmore1 = findViewById(R.id.btShowmore1);
        card_mail = findViewById(R.id.card_mail);
        card_web = findViewById(R.id.card_web);
        phone = findViewById(R.id.phone);
        profile_delete = findViewById(R.id.profile_delete);
        builder = new AlertDialog.Builder(ProductFullView.this);

        giftprize.setPaintFlags(giftprize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btShowmore.getText().toString().equalsIgnoreCase("Show more...")) {
                    description.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btShowmore.setText("Show less");
                } else {
                    description.setMaxLines(3);//your TextView
                    btShowmore.setText("Show more...");
                }
            }
        });

        btShowmore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btShowmore1.getText().toString().equalsIgnoreCase("Show more...")) {
                    detail_add.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btShowmore1.setText("Show less");
                } else {
                    detail_add.setMaxLines(3);//your TextView
                    btShowmore1.setText("Show more...");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        profile_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want delete your gift?").setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete_gift();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

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

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog;
                dialog = new Dialog(ProductFullView.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.call_dialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                TextView call1,call2;
                LinearLayout lay1,lay2;
                call1 = dialog.findViewById(R.id.call1);
                call2 = dialog.findViewById(R.id.call2);
                lay1=dialog.findViewById(R.id.lay1);
                lay2=dialog.findViewById(R.id.lay2);
                dialog.show();
                call1.setText(gift.get(0).getSellerMobile().trim());
                call2.setText(gift.get(0).getSellerMobile2().trim());
                String phone = gift.get(0).getSellerMobile().trim();
                String phone1 = gift.get(0).getSellerMobile2().trim();
                if (gift.get(0).getSellerMobile2().isEmpty()){
                    dialog.dismiss();
//                    lay2.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }


                lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone1, null));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });


              /*  if (phone.equals("")) {
                    Utils_Class.toast_center(Category_Full_View.this, "Mobile number not available");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }*/
            }
        });


        card_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gift.get(0).getShopEmail() != null && !gift.get(0).getShopEmail().trim().isEmpty()) {
                    if (Utils_Class.isNetworkAvailable(ProductFullView.this)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{gift.get(0).getShopEmail().trim()});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions");
                        intent.putExtra(Intent.EXTRA_TEXT, "Body Here");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    } else {
                        Utils_Class.toast_center(ProductFullView.this, "Check Your Internet Connection...");
                    }
                } else {
                    Utils_Class.toast_center(ProductFullView.this, "Email not available...");
                }
            }
        });

        card_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gift.get(0).getShopWebsite() != null && !gift.get(0).getShopWebsite().trim().isEmpty()) {
                    if (Utils_Class.isNetworkAvailable(ProductFullView.this)) {
                        String url = gift.get(0).getShopWebsite().trim();
                        System.out.println("urlprint" + url);
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(ProductFullView.this, Uri.parse(url));
                    } else {
                        Utils_Class.toast_center(ProductFullView.this, "Check Your Internet Connection...");
                    }
                } else {
                    Utils_Class.toast_center(ProductFullView.this, "Website not available...");
                }
            }
        });

        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentString = gift.get(0).getGiftImage();
                String[] separated = currentString.split(",");
                System.out.println("print_url1== " + currentString);
                Intent i = new Intent(getApplicationContext(), ImageSlide.class);
                i.putExtra("pos", currentString);
                startActivity(i);

                /*ImageView img_view;
                Dialog dialog = new Dialog(ProductFullView.this, android.R.style.Theme_DeviceDefault);
                dialog.setContentView(R.layout.image_view);

                //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(true);
                img_view = dialog.findViewById(R.id.img_view);
                Glide.with(getApplicationContext()).load(separated[0])
                        //.error(R.drawable.gift_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_view);
                dialog.show();*/
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
                    String currentString = gift.get(0).getGiftImage();
                    String[] separated = currentString.split(",");
                    System.out.println("print_comma== " + separated);
                    Glide.with(getApplicationContext()).load(separated[0])
                            .error(R.drawable.ic_gift_default_img)
                            .placeholder(R.drawable.ic_gift_default_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage);
                    giftname.setText(gift.get(0).getGiftName());
                    giftcategory.setText(gift.get(0).getGiftCat().replace(",",", "));
                    giftgender.setText(gift.get(0).getGiftForPeople().replace(",",", "));
                    giftprize.setText("\u20B9 " + gift.get(0).getTotalAmount());
//                    offerpercen.setText(gift.get(0).getDiscount());
                    offerprize.setText("\u20B9 " + gift.get(0).getGiftAmount());
                    description.setText(gift.get(0).getGiftDescription());
                    head.setText(gift.get(0).getDiscount() + "% offer");
                    detail_shop_name.setText(gift.get(0).getShopName());
                    detail_add.setText(gift.get(0).getAddress() + ", " + gift.get(0).getCity() + " - " + gift.get(0).getPincode() + "\n" + gift.get(0).getState() + "" + gift.get(0).getCountryName());

                    if (description.getLineCount() > 3) {
                        btShowmore.setVisibility(View.VISIBLE);
                    } else {
                        btShowmore.setVisibility(View.GONE);
                    }
                    if (detail_add.getLineCount() > 3) {
                        btShowmore1.setVisibility(View.VISIBLE);
                    } else {
                        btShowmore1.setVisibility(View.GONE);
                    }

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


    public void delete_gift() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "gift_delete");
        map.put("id", id_gift);
        map.put("user_id", sharedPreference.getString(ProductFullView.this, "user_id"));

        System.out.println("print map : " + map);

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<DeleteGift>> call = retrofitAPI.delete_gift(map);
        call.enqueue(new Callback<ArrayList<DeleteGift>>() {
            @Override
            public void onResponse(Call<ArrayList<DeleteGift>> call, Response<ArrayList<DeleteGift>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    Utils_Class.toast_center(getApplicationContext(), "Your product deleted...");
                    SellerProfileProductList.adapter.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<DeleteGift>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


}