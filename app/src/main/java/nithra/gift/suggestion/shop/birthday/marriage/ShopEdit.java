package nithra.gift.suggestion.shop.birthday.marriage;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nithra.gift.suggestion.shop.birthday.marriage.Otp.ShopAdd;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GetCountry;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GiftEdit;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import nithra.gift.suggestion.shop.birthday.marriage.crop_image.CropImage;
import nithra.gift.suggestion.shop.birthday.marriage.crop_image.CropImageView;
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.ImagePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopEdit extends AppCompatActivity {

    TextInputEditText sellername, shopname, shopaddress, mobilenumber, city, state, latitude, longitude, pincode, district, mailid, website, anothermobilenumber;
    String sell_name, shop_name, shop_add, mob_num, another_mob_num, shop_city, shop_country, shop_state, shop_pincode, shop_district, shop_latitude, shop_longitude, mail, web, emailPattern,country_text;
    TextView save;
    ImageView remove, edit_img;
    ImageView IVPreviewImage;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<GiftEdit> list_shop;
    Uri uri_1;
    HashMap<String, String> map1 = new HashMap<>();
    HashMap<String, String> map2 = new HashMap<>();
    String path = "";
    ImageView back;
    Spinner spin_country;
    ArrayList<GetCountry> country_get;
    ArrayList<String> spin;
    String coun_try, country_id;
    File[] file_array = new File[1];
    TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop_edit);
        sellername = findViewById(R.id.sellername);
        shopname = findViewById(R.id.shopname);
        shopaddress = findViewById(R.id.shopaddress);
        mobilenumber = findViewById(R.id.mobilenumber);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        save = findViewById(R.id.save);
        remove = findViewById(R.id.remove);
        edit_img = findViewById(R.id.edit_img);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        pincode = findViewById(R.id.pincode);
        pincode = findViewById(R.id.pincode);
        district = findViewById(R.id.district);
        anothermobilenumber = findViewById(R.id.anothermobilenumber);
        list_shop = new ArrayList<GiftEdit>();

        back = findViewById(R.id.back);
        mailid = findViewById(R.id.mailid);
        website = findViewById(R.id.website);
        spin_country = findViewById(R.id.spin_country);
        country_get = new ArrayList<GetCountry>();
        spin = new ArrayList<>();


        testView = findViewById(R.id.testView);

        testView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ShopEdit.this);
                dialog.setContentView(R.layout.searchable_spinner);
                dialog.getWindow().setLayout(650, 800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);


                for (int i = 0; i < country_get.size(); i++) {
                    spin.add(country_get.get(i).getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShopEdit.this, android.R.layout.simple_list_item_1, spin);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        testView.setText(adapter.getItem(position));
                        testView.setTag(country_get.get(position).getId());
                        dialog.dismiss();
                    }
                });
            }
        });

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Utils_Class.mProgress(this, "Loading please wait...", false).show();
        shopedit();
        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openSomeActivityForResult();
                //choose_imge();
                Dialog dialog;
                dialog = new Dialog(ShopEdit.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.cam_gallery);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                ImageView camera, gallery;
                camera = dialog.findViewById(R.id.camera);
                gallery = dialog.findViewById(R.id.gallery);
                dialog.show();
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_pick();
                        dialog.dismiss();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_pick_gal();
                        dialog.dismiss();

                    }
                });
            }
        });
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openSomeActivityForResult();
                //choose_imge();
                Dialog dialog;
                dialog = new Dialog(ShopEdit.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.cam_gallery);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                ImageView camera, gallery;
                camera = dialog.findViewById(R.id.camera);
                gallery = dialog.findViewById(R.id.gallery);
                dialog.show();
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_pick();
                        dialog.dismiss();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_pick_gal();
                        dialog.dismiss();

                    }
                });
            }
        });

        spin_country.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        //remove.setVisibility(View.GONE);

       /* remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVPreviewImage.setImageResource(R.drawable.ic_default_user_icon);
                file_array[0] = null;
                remove.setVisibility(View.GONE);

              *//*  if (!image_change.contains(separated[0])) {
                    image_change += separated[0] + ",";
                }*//*
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sell_name = sellername.getText().toString().trim();
                shop_name = shopname.getText().toString().trim();
                shop_add = shopaddress.getText().toString().trim();
                mob_num = mobilenumber.getText().toString().trim();
                another_mob_num = anothermobilenumber.getText().toString().trim();
                mail = mailid.getText().toString().trim();
                web = website.getText().toString().trim();
                shop_city = city.getText().toString().trim();
                shop_state = state.getText().toString().trim();
                // shop_district = district.getText().toString().trim();
                shop_pincode = pincode.getText().toString().trim();
                country_text=testView.getTag().toString().trim();
               /* shop_latitude = latitude.getText().toString().trim();
                shop_longitude = longitude.getText().toString().trim();*/
                /*if (uri_1 == null) {
                    IVPreviewImage.setImageResource(R.drawable.ic_default_user_icon);
                }*/
                if (sell_name.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Seller Name...");
                } else if (shop_name.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Shop name...");
                } else if (mob_num.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Correct Mobile Number...");
                } else if (mail.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your Email...");
                } else if (!mail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                } else if (country_text.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Select Your country...");
                } else if (shop_state.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your state...");
                } else if (shop_city.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your city...");
                } else if (shop_add.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your address...");
                } else if (shop_pincode.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your pin/postal code...");
                } else {
                    if (Utils_Class.isNetworkAvailable(ShopEdit.this)) {
                        submit_res();
                    } else {
                        Utils_Class.toast_normal(ShopEdit.this, "Please connect to your internet");
                    }
                }
            }
        });
    }


    public void image_pick() {
        // req_code = req_code1;
        mLauncher.launch(ImagePicker.Companion.with(ShopEdit.this)
                .crop()
                .cameraOnly()
                .createIntent());
    }

    public void image_pick_gal() {
        // req_code = req_code1;
        mLauncher.launch(ImagePicker.Companion.with(ShopEdit.this)
                .crop()
                .galleryOnly()
                .createIntent());
    }

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    System.out.println("----------- new activity result" + result);
                    uri_1 = result.getData().getData();
                    if (uri_1 != null) {
                       // uri_1 = result.getUri();
                        IVPreviewImage.setImageURI(uri_1);
               /* if (!image_change.contains(separated[0])) {
                    image_change += separated[0] + ",";
                }*/

                        try {
                            File file = getFile(ShopEdit.this, uri_1, "img.jpg");
                            path = file.getPath().replace(file.getName(), "");
                            System.out.println("---file name : " + file.getName());
                            System.out.println("---file path : " + path);
                            System.out.println("---file path : " + file.getAbsolutePath());

                            file_array[0] = file;
                            //remove.setVisibility(View.VISIBLE);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("print_uri== ");

                    }
                }
            });

    public void spin_country() {

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_country");
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GetCountry>> call = retrofitAPI.country(map);
        call.enqueue(new Callback<ArrayList<GetCountry>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCountry>> call, Response<ArrayList<GetCountry>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    country_get.addAll(response.body());

                    //spinner1();


                    //adapter.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GetCountry>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void shopedit() {

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_id");
        map.put("id", sharedPreference.getString(this, "user_id"));

        System.out.println("print_map " + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GiftEdit>> call = retrofitAPI.edit_gift(map);
        call.enqueue(new Callback<ArrayList<GiftEdit>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftEdit>> call, Response<ArrayList<GiftEdit>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        list_shop.addAll(response.body());
                      /*  if (list_shop.get(0).getLogo() != null) {
                            remove.setVisibility(View.VISIBLE);
                        }*/
                        Glide.with(getApplicationContext()).load(list_shop.get(0).getLogo())
                                .error(R.drawable.ic_gift_default_img)
                                .placeholder(R.drawable.ic_gift_default_img)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(IVPreviewImage);
                        sellername.setText(list_shop.get(0).getName());
                        shopname.setText(list_shop.get(0).getShopName());
                        shopaddress.setText(list_shop.get(0).getAddress());
                        mobilenumber.setText(list_shop.get(0).getSellerMobile());
                        anothermobilenumber.setText(list_shop.get(0).getSellerMobile2());
                        mailid.setText(list_shop.get(0).getShopEmail());
                        website.setText(list_shop.get(0).getShopWebsite());
                        city.setText(list_shop.get(0).getCity());
                        state.setText(list_shop.get(0).getState());
                        latitude.setText(list_shop.get(0).getLatitude());
                        longitude.setText(list_shop.get(0).getLongitude());
                        pincode.setText(list_shop.get(0).getPincode());
                        //district.setText(list_shop.get(0).getDistrict());
                        country_id = list_shop.get(0).getCountry_name();
                        testView.setText(country_id);
                        testView.setTag(list_shop.get(0).getCountry());

               /*         for (int j = 0; j < country_get.size(); j++) {
                            System.out.println("id1== " + country_get.get(j).getId());
                            System.out.println("id2== " + country_id);
                            if (country_get.get(j).getId().trim().equals(country_id)) {
                                System.out.println("id3== " + country_get.get(j).getId());
                                //spin_country.setSelection(j + 1);
                                testView.setText(country_id);
                                break;
                            }

                        }*/

                        spin_country();


                        System.out.println("one");
                    }
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GiftEdit>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void spinner1() {
        spin.add(0, "Select country");
        for (int i = 0; i < country_get.size(); i++) {
            spin.add(country_get.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShopEdit.this, android.R.layout.simple_spinner_item, spin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_country.setAdapter(adapter);

        spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    coun_try = country_get.get(i - 1).getName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();


        for (int j = 0; j < country_get.size(); j++) {
            System.out.println("id1== " + country_get.get(j).getId());
            System.out.println("id2== " + country_id);
            if (country_get.get(j).getId().trim().equals(country_id)) {
                System.out.println("id3== " + country_get.get(j).getId());
                spin_country.setSelection(j + 1);
                break;
            }

        }


    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher1.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // There are no request codes
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            IVPreviewImage.setImageBitmap(bitmap);
                            System.out.println("print_bitmap== " + bitmap);
                            System.out.println("printbit_uri== " + imageUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("print_error== " + e);

                        }

                        uri_1 = data.getData();
                    }
                }
            });

    public void choose_imge() {
        try {
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ShopEdit.this, 100);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            System.out.println("print_Catch== " + e);
            Utils_Class.toast_center(ShopEdit.this, "Try again...");
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of CropImageActivity

        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            uri_1 = result.getUri();
            IVPreviewImage.setImageURI(uri_1);
               *//* if (!image_change.contains(separated[0])) {
                    image_change += separated[0] + ",";
                }*//*

            try {
                File file = getFile(ShopEdit.this, uri_1, "img.jpg");
                path = file.getPath().replace(file.getName(), "");
                System.out.println("---file name : " + file.getName());
                System.out.println("---file path : " + path);
                System.out.println("---file path : " + file.getAbsolutePath());

                file_array[0] = file;
                //remove.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("print_uri== ");

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Utils_Class.toast_center(this, "Cropping failed: " + result.getError());
        }

    }

*/
    public void submit_res() {
        map1.clear();
        map2.clear();
        map1.put("action", "add_seller");
        map1.put("user_id", sharedPreference.getString(getApplicationContext(), "user_id"));
        map1.put("shop_name", shop_name);
        map1.put("seller_mobile", mob_num);
        map1.put("seller_mobile2", another_mob_num);
        map1.put("shop_email", mail);
        map1.put("shop_website", web);
        map1.put("name", sell_name);
        //map1.put("country", shop_country);
        //map1.put("country", country_get.get(spin_country.getSelectedItemPosition() - 1).getId());
        map1.put("country",  country_text);
        map1.put("state", shop_state);
        map1.put("address", shop_add);
        map1.put("pincode", shop_pincode);
       /* map1.put("latitude", shop_latitude);
        map1.put("longitude", shop_longitude);*/
       // map1.put("district", shop_district);
        map1.put("city", shop_city);


        File file = null;
        try {
           /* file = getFile(getApplicationContext(), uri_1);
            path = file.getPath().replace(file.getName(), "");
            System.out.println("---file name : " + file.getName());
            System.out.println("---file path : " + path);
            System.out.println("---file path : " + file.getAbsolutePath());
            map2.put("logo[]", "" + Uri.fromFile(file));*/


            int set_img = 0;
            for (int i = 0; i < file_array.length; i++) {
                System.out.println("check_loop");
                if (file_array[i] != null) {
                    map2.put("logo[" + set_img + "]", "" + Uri.fromFile(file_array[i]));
                    set_img++;
                    System.out.println("print_image==" + Uri.fromFile(file_array[i]));
                }
            }
            System.out.println("check_size== " + file_array.length);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("printerror" + e);
        }

        System.out.println("print map1 : " + map1);
        System.out.println("print map2 : " + map2);

        UploadAsync();

    }


    public static File getFile(Context context, Uri uri, String image) throws IOException {
        String root = context.getFilesDir().getPath() + File.separatorChar + "Images";
        File folder = new File(root);
        // have the object build the directory structure, if needed.
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File destinationFilename = new File(root + File.separatorChar + image);
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }


    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


    public void UploadAsync() {
        ProgressDialog progressDialog = new ProgressDialog(ShopEdit.this);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading... ");
        // progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Handler handler = new Handler(Looper.myLooper()) {
            public void handleMessage(final Message msg) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        //post execute
                        if (getApplicationContext() != null) {
                            System.out.println("====msg result : " + msg.obj.toString());
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (msg.obj != null && msg.obj.toString().length() != 0) {
                                System.out.println("result : " + msg.obj.toString());


                                String result = msg.obj.toString();

                                JSONArray jsonArray = null;
                                JSONObject jsonObject = null;
                                try {
                                    jsonArray = new JSONArray(result);
                                    jsonObject = jsonArray.getJSONObject(0);


                                    System.out.println("---output : " + jsonObject.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (jsonObject.getString("status").contains("Success")) {
                                        IVPreviewImage.setImageResource(R.drawable.add);
                                        sellername.getText().clear();
                                        shopname.getText().clear();
                                        shopaddress.getText().clear();
                                        mobilenumber.getText().clear();
                                        anothermobilenumber.getText().clear();
                                        mailid.getText().clear();
                                        website.getText().clear();
                                        city.getText().clear();
                                        state.getText().clear();
                                        latitude.getText().clear();
                                        longitude.getText().clear();
                                        pincode.getText().clear();
                                        //district.getText().clear();
                                        //sharedPreference.putInt(getApplicationContext(), "yes", 1);
                                        Toast.makeText(getApplicationContext(), "Your shop Updated successfully, Thank you", Toast.LENGTH_SHORT).show();
                                        sharedPreference.putInt(ShopEdit.this, "finish", 1);
                                       /* Intent i =new Intent(ShopEdit.this,SellerProfile.class);
                                        startActivity(i);*/
                                        finish();
                                        /*Intent i =new Intent(ShopEdit.this,MyProduct.class);
                                        startActivity(i);*/

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        }
                    }
                };
                runOnUiThread(runnable);
            }
        };
        final Thread checkUpdate = new Thread() {
            public void run() {
                //do in back ground
                String response = null;
                try {
                    String requestURL = "https://nithra.mobi/gift_app/api/data.php";
                    String path = "Images/";

                    final String boundary;
                    String tail = "";
                    final String LINE_END = "\r\n";
                    final String TWOHYPEN = "--";
                    HttpURLConnection httpConn;
                    String charset = "UTF-8";
                    PrintWriter writer;
                    OutputStream outputStream;
                    String paramsPart = "";
                    String fileHeader = "";
                    String filePart = "";
                    long fileLength = 0;
                    int maxBufferSize = 1024;

                    try {
                        boundary = "===" + System.currentTimeMillis() + "===";
                        tail = LINE_END + TWOHYPEN + boundary + TWOHYPEN + LINE_END;
                        URL url = new URL(requestURL);
                        httpConn = (HttpURLConnection) url.openConnection();
                        httpConn.setDoOutput(true);
                        httpConn.setDoInput(true);
                        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);


                        ArrayList<String> paramHeaders = new ArrayList<>();
                        for (Map.Entry<String, String> entry : map1.entrySet()) {

                            String param = TWOHYPEN + boundary + LINE_END
                                    + "Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END
                                    + "Content-Type: text/plain; charset=" + charset + LINE_END
                                    + LINE_END
                                    + entry.getValue() + LINE_END;
                            paramsPart += param;
                            paramHeaders.add(param);
                        }


                        ArrayList<File> filesAL = new ArrayList<>();
                        ArrayList<String> fileHeaders = new ArrayList<>();
                        try {
                            for (Map.Entry<String, String> entry : map2.entrySet()) {
                                String file_name = entry.getValue().substring(entry.getValue().lastIndexOf("/") + 1);
                                File file = null;

                                System.out.println("===error1 " + entry.getValue());
                                System.out.println("===error2 " + file_name);
                                //  System.out.println("===error path " + path);
                                //file = new File(entry.getValue());

                                file = new File(getFilesDir().getPath(), path + file_name);

                                fileHeader = TWOHYPEN + boundary + LINE_END
                                        + "Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + file.getName() + "\"" + LINE_END
                                        + "Content-Type: " + URLConnection.guessContentTypeFromName(file.getAbsolutePath()) + LINE_END
                                        + "Content-Transfer-Encoding: binary" + LINE_END
                                        + LINE_END;
                                fileLength += file.length() + LINE_END.getBytes(charset).length;
                                filePart += fileHeader;

                                fileHeaders.add(fileHeader);
                                filesAL.add(file);


                            }
                            String partData = paramsPart + filePart;

                            long requestLength = partData.getBytes(charset).length + fileLength + tail.getBytes(charset).length;
                            httpConn.setRequestProperty("Content-length", "" + requestLength);
                            httpConn.setFixedLengthStreamingMode((int) requestLength);
                            httpConn.connect();

                            outputStream = new BufferedOutputStream(httpConn.getOutputStream());
                            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

                            for (int i = 0; i < paramHeaders.size(); i++) {
                                writer.append(paramHeaders.get(i));
                                writer.flush();
                            }

                            int totalRead = 0;
                            int bytesRead;
                            byte buf[] = new byte[maxBufferSize];
                            for (int i = 0; i < filesAL.size(); i++) {
                                writer.append(fileHeaders.get(i));
                                writer.flush();
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filesAL.get(i)));
                                while ((bytesRead = bufferedInputStream.read(buf)) != -1) {

                                    outputStream.write(buf, 0, bytesRead);
                                    writer.flush();
                                    totalRead += bytesRead;
                                    int progress = (int) ((totalRead * 100) / requestLength);
                                    //update progress
                                    //  publishProgress(progress,(i + 1), filesAL.size());
                                    final long finalTotal = totalRead;
                                    final long finalFileLength = fileLength;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.setMessage("Loading...");
                                            }
                                        }
                                    });

                                }
                                outputStream.write(LINE_END.getBytes());
                                outputStream.flush();
                                bufferedInputStream.close();
                            }
                            writer.append(tail);
                            writer.flush();
                            writer.close();

                        } catch (Exception e) {
                            System.out.println("===error3 " + e.getMessage());
                        }
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        try {
                            // checks server's status code first
                            int status = httpConn.getResponseCode();
                            if (status == HttpURLConnection.HTTP_OK) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"), 8);

                                while ((line = reader.readLine()) != null) {
                                    //  sb.append(line).append("\n");
                                    sb.append(line);
                                }
                                reader.close();
                                httpConn.disconnect();
                            } else {
                                throw new IOException("Server returned non-OK status: " + status + " " + httpConn.getResponseMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("===error4 " + e.getMessage());
                        }
                        try {
                            System.out.println("===error5 " + sb.toString());

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            System.out.println("===error6 " + e.getMessage());
                        }
                        response = sb.toString();
                        System.out.println("result from server : " + response);
                    } catch (IOException e) {
                        System.out.println("===result error1 : " + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("====msg error : " + e.getMessage());
                }
                Message message = new Message();
                message.obj = response;
                // handler.sendEmptyMessage(0);
                handler.sendMessage(message);
            }
        };
        checkUpdate.start();
    }


}