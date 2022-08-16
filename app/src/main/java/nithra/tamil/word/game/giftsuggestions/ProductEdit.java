package nithra.tamil.word.game.giftsuggestions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import nithra.tamil.word.game.giftsuggestions.Otp.OtpSend;
import nithra.tamil.word.game.giftsuggestions.Otp.ProductAdd;
import nithra.tamil.word.game.giftsuggestions.Retrofit.AddGift;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GiftFor;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Occasion;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEdit extends AppCompatActivity {
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
    ArrayList<AddGift> list_gift;
    Uri uri_1;
    HashMap<String, String> map1 = new HashMap<>();
    HashMap<String, String> map2 = new HashMap<>();
    String path = "";
    Intent intent;
    Bundle extra;
    String id_gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_edit);
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
        list_gift = new ArrayList<AddGift>();
        occasion = new ArrayList<Occasion>();

        intent = getIntent();
        extra = intent.getExtras();
        id_gift = extra.getString("id");

        giftedit();


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
                } else if (gift_description.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Product Description...");
                } else {

                    submit_res();

                }
            }
        });
        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult();

            }
        });
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
                        IVPreviewImage.setImageURI(data.getData());
                        uri_1 = data.getData();
                    }
                }
            });


    public void submit_res() {
        map1.clear();
        map2.clear();
        map1.put("action", "add_gift");
        map1.put("user_id", sharedPreference.getString(getApplicationContext(), "user_id"));
        map1.put("gift_category", occasion.get(spin_occaction.getSelectedItemPosition()-1).getId());
        map1.put("gift_for", giftfor.get(spin_gender.getSelectedItemPosition()-1).getId());
        map1.put("gift_name", gift_name);
        map1.put("id", id_gift);
        map1.put("gift_description", gift_description);
        map1.put("gift_amount", gift_amount);
        map1.put("discount", discount);
        map1.put("total_amount", total_amount);

        File file = null;
        try {
            file = getFile(getApplicationContext(), uri_1);
            path = file.getPath().replace(file.getName(), "");
            System.out.println("---file name : " + file.getName());
            System.out.println("---file path : " + path);
            System.out.println("---file path : " + file.getAbsolutePath());
            map2.put("gift_image[0]", "" + Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("printerror" + e);
        }

        System.out.println("print map1 : " + map1);
        System.out.println("print map2 : " + map2);


        UploadAsync();
    }


    public void giftedit() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_gift");
        //map.put("user_id", sharedPreference.getString(this, "user_id"));
        map.put("id", id_gift);
        System.out.println("idddd"+id_gift);
        System.out.println("printmap"+map);

        System.out.println("print_map " + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<AddGift>> call = retrofitAPI.add_gift(map);
        call.enqueue(new Callback<ArrayList<AddGift>>() {
            @Override
            public void onResponse(Call<ArrayList<AddGift>> call, Response<ArrayList<AddGift>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        list_gift.addAll(response.body());

                        gender_gift();
                        gift_occasion();

                        Glide.with(getApplicationContext()).load(list_gift.get(0).getGiftImage())
                                //.error(R.drawable.warning)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(IVPreviewImage);
                        productname.setText(list_gift.get(0).getGiftName());
                        prod_prize.setText(list_gift.get(0).getTotalAmount());
                        offer_prize.setText(list_gift.get(0).getGiftAmount());
                        offer_percentage.setText(list_gift.get(0).getDiscount());
                        prod_des.setText(list_gift.get(0).getGiftDescription());
                        System.out.println("======response_check :" );

                       /* for (int i = 0; i < list_gift.size(); i++) {
                            for (int j=0;j<occasion.size();j++) {
                                if (occasion.get(j).getId() == list_gift.get(i).getId()) {
                                    spin_occaction.setSelection(j);
                                }
                            }
                        }

                        for (int i = 0; i < list_gift.size(); i++) {
                            for (int j=0;j<giftfor.size();j++) {
                                if (giftfor.get(j).getId() == list_gift.get(i).getId()) {
                                    spin_gender.setSelection(j);
                                }
                            }
                        }
*/
                        //spin_occaction.setSelection(Integer.parseInt(occasion.get(0).getCategory()));
                        //spin_gender.setSelection(Integer.parseInt(list_gift.get(0).getGiftFor()));

                    }
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<AddGift>> call, Throwable t) {
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
                    for (int i = 0; i < list_gift.size(); i++) {
                        for (int j=0;j<giftfor.size();j++) {
                            if (giftfor.get(j).getId().equals(list_gift.get(i).getGiftFor()) ) {
                                spin_gender.setSelection(j+1);
                            }
                        }
                    }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(adapter);
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    gift_for = giftfor.get(i-1).people;
                }
               /* if (i == 0) {
                    spin_gender.setEnabled(false);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin_1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_gender.setAdapter(adapter);
                } else {
                    spin_gender.setEnabled(true);
                    spin.clear();
                }*/
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
                    System.out.println("check size"+list_gift.size());

                    spinner1();
                    for (int i = 0; i < list_gift.size(); i++) {

                        System.out.println("check loop1");
                        for (int j=0;j<occasion.size();j++) {
                            System.out.println("check loop2");
                            System.out.println("check id" +occasion.get(j).getId() + "=="+list_gift.get(i).getGiftCategory() );

                            if (occasion.get(j).getId().equals(list_gift.get(i).getGiftCategory()) ) {
                                System.out.println("check loop3");
                                System.out.println("check cat" +occasion.get(j).getCategory());
                                spin_occaction.setSelection(j+1);
                            }
                        }
                    }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_occaction.setAdapter(adapter);
        spin_occaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    gift_category = occasion.get(i-1).category;
                }
                /*if (i == 0) {

                    spin_gender.setEnabled(false);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spin_1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_gender.setAdapter(adapter);
                } else {
                    spin_gender.setEnabled(true);
                    spin_1.clear();
                    spinner_1(i);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();

    }


    public static File getFile(Context context, Uri uri) throws IOException {
        String root = context.getFilesDir().getPath() + File.separatorChar + "Images";
        File folder = new File(root);
        // have the object build the directory structure, if needed.
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File destinationFilename = new File(root + File.separatorChar + queryName(context, uri));
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
        ProgressDialog progressDialog = new ProgressDialog(ProductEdit.this);
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
                                        spin_occaction.setSelection(0);
                                        spin_gender.setSelection(0);
                                        productname.getText().clear();
                                        prod_prize.getText().clear();
                                        offer_percentage.getText().clear();
                                        offer_prize.getText().clear();
                                        prod_des.getText().clear();

                                        Toast.makeText(getApplicationContext(), "Your product Updated successfully, Thank you", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), MyProduct.class);
                                        startActivity(i);
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
                    String requestURL = "http://15.206.173.184/upload/gift_suggestion/api/data.php";
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

    @Override
    public void onBackPressed() {
        finish();
    }
}