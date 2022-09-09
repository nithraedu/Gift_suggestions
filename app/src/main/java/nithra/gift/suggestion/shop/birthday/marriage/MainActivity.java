package nithra.gift.suggestion.shop.birthday.marriage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nithra.gift.suggestion.shop.birthday.marriage.Feedback.Feedback;
import nithra.gift.suggestion.shop.birthday.marriage.Feedback.Method;
import nithra.gift.suggestion.shop.birthday.marriage.Feedback.RetrofitClient;
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Favourite;
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Home;
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Location;
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Settings;
import nithra.gift.suggestion.shop.birthday.marriage.Otp.OtpSend;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Androidid;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements InstallReferrerStateListener,BottomNavigationView.OnItemSelectedListener, FragMove {
    ViewPager2 viewpager2;
    Frag_Adapter frag_adapter;
    ImageView home, favourite, location, settings;
    FloatingActionButton add_shop;
    BottomAppBar bottomAppBar;
    SharedPreferences pref;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<Androidid> and_id;
    AppUpdateManager appUpdateManager;
    InstallReferrerClient mReferrerClient;
    static SharedPreference sp = new SharedPreference();
    String source = "", medium = "", comp = "";
    int a = 0;
    SharedPreferences spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("register", Context.MODE_PRIVATE);
        System.out.println("android_id" + Utils_Class.android_id(this));

        viewpager2 = findViewById(R.id.viewpager2);
        home = findViewById(R.id.home);
        favourite = findViewById(R.id.favourite);
        location = findViewById(R.id.location);
        settings = findViewById(R.id.settings);
        add_shop = findViewById(R.id.add_shop);
        viewpager2.setUserInputEnabled(false);
        frag_adapter = new Frag_Adapter(this);
        frag_adapter.addFragment(new Home());
        frag_adapter.addFragment(new Favourite());
        //frag_adapter.addFragment(new Add());
        frag_adapter.addFragment(new Location());
        frag_adapter.addFragment(new Settings());
        /*frag_adapter.addFragment(new Product());
        frag_adapter.addFragment(new SendOTP());
        frag_adapter.addFragment(new EnterOTP());*/
        bottomAppBar = findViewById(R.id.bottomAppBar);
        and_id = new ArrayList<Androidid>();

        if (sharedPreference.getInt(getApplicationContext(), "android_id_check") == 0) {
            android();
        }

        spa = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        /*if (spa.getInt("yes", 0) == 0) {
            privacy();
        }*/

        //in_app cods start
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            200);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        Date cdate = Calendar.getInstance().getTime();
        System.out.println("Current time => " + cdate);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String currentdate = df.format(cdate);
        System.out.println("current date==" + currentdate);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterdaydate = df.format(cal.getTime());
        System.out.println("yesterday date==" + df.format(cal.getTime()));
        if (sp.getInt(MainActivity.this, currentdate) == 0) {
            sp.putInt(MainActivity.this, currentdate, 1);
            if (sp.getInt(MainActivity.this, yesterdaydate) != 0) {
                sp.removeInt(MainActivity.this, yesterdaydate);
            }
        } else {

            if (sp.getString(MainActivity.this, "review_time").equals("")) {
                Calendar current_date = Calendar.getInstance();
                long currentdate_mills = current_date.getTimeInMillis();
                sp.putString(MainActivity.this, "review_time", "" + currentdate_mills);
            }
            app_update_manager();

        }
//in_app cods end



        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, 15)
                        .setTopLeftCorner(CornerFamily.ROUNDED, 15)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 15)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 15)
                        .build());

        home.setBackgroundResource(R.drawable.background2);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(R.drawable.background2);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);

                viewpager2.setCurrentItem(0, false);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(R.drawable.background2);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);
                viewpager2.setCurrentItem(1, false);
            }
        });

        /*add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sharedPreference.getInt(getApplicationContext(), "yes") == 0) {
                    //viewpager2.setCurrentItem(6, false);
                    Intent i = new Intent(MainActivity.this, OtpSend.class);
                    startActivity(i);

                } else {
                    Intent i = new Intent(MainActivity.this, MyProduct.class);
                    startActivity(i);
                    // viewpager2.setCurrentItem(5, false);
                    //viewpager2.setCurrentItem(2, false);
                }

                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);
              *//*bottomAppBar.setVisibility(View.GONE);
                add_shop.setVisibility(View.GONE);*//*
            }
        });*/

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(R.drawable.background2);
                settings.setBackgroundResource(0);
                viewpager2.setCurrentItem(3, false);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(R.drawable.background2);
                viewpager2.setCurrentItem(4, false);
            }
        });

        viewpager2.setAdapter(frag_adapter);
    }


    public void android() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_android_id");
        map.put("android_id", Utils_Class.android_id(this));
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Androidid>> call = retrofitAPI.androidid(map);
        call.enqueue(new Callback<ArrayList<Androidid>>() {
            @Override
            public void onResponse(Call<ArrayList<Androidid>> call, Response<ArrayList<Androidid>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("success")) {
                        and_id.addAll(response.body());
                        sharedPreference.putInt(getApplicationContext(), "android_id_check", 1);
                        sharedPreference.putString(getApplicationContext(), "android_userid", and_id.get(0).getUserId());
                    }
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Androidid>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }



    public void feedback() {
        EditText email_edt, feedback_edt;
        TextView privacy;
        TextView submit_btn;
        Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.feed_back);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        email_edt = dialog.findViewById(R.id.edit_email);
        feedback_edt = dialog.findViewById(R.id.editText1);
        submit_btn = dialog.findViewById(R.id.btnSend);
        privacy = dialog.findViewById(R.id.policy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils_Class.isNetworkAvailable(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "please connect to the internet...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = feedback_edt.getText().toString().trim();
                String email = email_edt.getText().toString().trim();

                if (feedback.equals("")) {
                    Toast.makeText(MainActivity.this, "Please type your feedback or suggestion, Thank you", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils_Class.isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "please connect to the internet...", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    feedback = URLEncoder.encode(feedback, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("type", "Gift_Suggestions");
                map.put("feedback", feedback);
                map.put("email", email);
                map.put("model", Build.MODEL);
                map.put("vcode", "1.0");
                Method method = RetrofitClient.getRetrofit().create(Method.class);
                Call<List<Feedback>> call = method.getAlldata(map);
                call.enqueue(new Callback<List<Feedback>>() {
                    @Override
                    public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                        if (response.isSuccessful()) {
                            try {
                                List<Feedback> feedbacks = response.body();
                                System.out.println("======response feedbacks:" + feedbacks.get(0).getStatus());

                                JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                System.out.println("======response feedbacks:" + jsonObject.getString("status"));
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Feedback sent, Thank you", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                System.out.println("======response e:" + e.toString());
                                e.printStackTrace();
                            }
                        }
                        System.out.println("======response :" + response);
                    }

                    @Override
                    public void onFailure(Call<List<Feedback>> call, Throwable t) {
                        System.out.println("======response t:" + t);
                    }
                });
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (a == 1) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, ExitScreen.class);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    public void privacy() {
        Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_privacy_policy);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView head = dialog.findViewById(R.id.head);
        TextView first_line = dialog.findViewById(R.id.first_line);
        TextView b1 = dialog.findViewById(R.id.b1);
        TextView b2 = dialog.findViewById(R.id.b2);
        head.setText("Privacy & Terms");
        first_line.setText("Thanks for downloading or updating Inventions And Inventors.\n" + "\n" +
                "By clicking privacy policy tab you can read our privacy policy and agree to the terms of privacy policy to continue using Inventions And Inventors.");
        b1.setText("Privacy Policy");
        b2.setText("Agree & Continue");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils_Class.isNetworkAvailable(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please connect the internet...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editer = spa.edit();
                editer.putInt("yes", 1);
                editer.commit();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {


       /* Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.backpress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button yes, no;
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        if (sp.getInt(this, "ratecheckval") == 0) {
            final Dialog yesratedialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            yesratedialog.setContentView(R.layout.rate1);
            AppCompatButton yesbut1 = yesratedialog.findViewById(R.id.button2);
            AppCompatButton nobut1 = yesratedialog.findViewById(R.id.button1);
            yesbut1.setOnClickListener(v -> {
                try {
                    sp.putInt(getApplicationContext(), "ratecheckval", 1);
                    String appPackageName = getPackageName();
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    startActivity(marketIntent);
                } catch (Exception e) {
                    System.out.println();
                }
                yesratedialog.dismiss();
                finish();
            });
            nobut1.setOnClickListener(v -> {
                sp.putInt(getApplicationContext(), "ratecheckval", 1);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                a = 1;
                feedback();
                // yesratedialog.dismiss();
            });

            dialog.show();

            yesratedialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, ExitScreen.class);
                    startActivity(intent);
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils_Class.date_put(MainActivity.this, "rate_date", 90);
                    yesratedialog.show();
                    dialog.dismiss();
                    //finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ExitScreen.class);
                    startActivity(intent);
                    finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }*/


        if (viewpager2.getCurrentItem() == 1) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } /*else if (viewpager2.getCurrentItem() == 2) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        }*/ else if (viewpager2.getCurrentItem() == 3) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 4) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } /*else if (viewpager2.getCurrentItem() == 5) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 6) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 7) {
            viewpager2.setCurrentItem(6, false);
            bottomAppBar.setVisibility(View.GONE);
            add_shop.setVisibility(View.GONE);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        }*/ else {
            finish();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void product() {
        viewpager2.setCurrentItem(5, false);

    }

    @Override
    public void seller() {
        viewpager2.setCurrentItem(2, false);

    }

    @Override
    public void enterotp() {
        viewpager2.setCurrentItem(7, false);

    }

    @Override
    public void home() {
        viewpager2.setCurrentItem(0, false);
        home.setBackgroundResource(R.drawable.background2);
        favourite.setBackgroundResource(0);
        location.setBackgroundResource(0);
        settings.setBackgroundResource(0);
    }

    @Override
    public void fav() {
        viewpager2.setCurrentItem(1, false);

    }

    public class Frag_Adapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public Frag_Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }


    //in_app cods start

    private void app_update_manager() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            200);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                if (sp.getString(MainActivity.this, "review_complete").equals("")) {
                    if (!sp.getString(MainActivity.this, "review_time").equals("")) {
                        long before_date = Long.parseLong(sp.getString(MainActivity.this, "review_time"));

                        Calendar current_date = Calendar.getInstance();

                        long currentdate_mills = current_date.getTimeInMillis();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        String string_current_date = sdf.format(currentdate_mills);
                        String string_before_date = sdf.format(before_date);


                        //System.out.println("new Version " + date);
                        long timediff = 0;

                        try {
                            timediff = TimeUnit.DAYS.convert(sdf.parse(string_current_date).getTime() - sdf.parse(string_before_date).getTime(), TimeUnit.MILLISECONDS);

                            System.out.println("new Version " + timediff);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        if ((int) timediff >= 10) {
                            System.out.println("new Version 1 " + timediff);
                            if (Utils_Class.isNetworkAvailable(MainActivity.this)) {
                                inapp_review_dialog();
                            }
                        }
                    }

                }
            }

        });

    }


    private void inapp_review_dialog() {
        //        ReviewManager manager =new FakeReviewManager(Main_open.this);
        final ReviewManager manager = ReviewManagerFactory.create(MainActivity.this);
        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                flow.addOnCompleteListener(task1 -> sp.putString(MainActivity.this, "review_complete", "review_completed"));
            }
            //Toast.makeText(Main_open.this, "There was a problem", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        System.out.println("onInstallReferrerSetupFinished");
        System.out.println("InstallReferrerClient responseCode " + responseCode);

        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:

                sp.putInt(MainActivity.this, "referrerCheck", 1);
                System.out.println("Install Referrer conneceted... Successfully");
                System.out.println("Install Referrer conneceted..." + mReferrerClient.isReady());

                /*get response from referrer*/
                try {
                    ReferrerDetails response = mReferrerClient.getInstallReferrer();
                    if (response != null) {
                        String referrerUrl = response.getInstallReferrer();
                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
                        long appInstallTime = response.getInstallBeginTimestampSeconds();
                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        System.out.println("=== ReferrerDetails_url " + referrerUrl);
                        System.out.println("=== ReferrerDetails_click_time " + referrerClickTime);
                        System.out.println("=== ReferrerDetails_ins_time " + appInstallTime);
                        System.out.println("=== ReferrerDetails_launch " + instantExperienceLaunched);


                        if (referrerUrl != null) {
                            if (referrerUrl.length() > 0) {
                                String[] referrerList = referrerUrl.split("&");
                                for (int i = 0; i < referrerList.length; i++) {
                                    if (i == 0) {
                                        source = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer_source===" + source);
                                    } else if (i == 1) {
                                        medium = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer_medium===" + medium);
                                    } else if (i == 2) {
                                        comp = referrerList[i].substring(referrerList[i].indexOf("=") + 1);
                                        System.out.println("Referrer_comp===" + comp);
                                    }
                                }
                            }
                            /*sent data to server*/
                            final Thread checkUpdate = new Thread() {
                                public void run() {
                                    try {
                                        send(MainActivity.this, source, medium, comp);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            };
                            checkUpdate.start();

                        } else {
                            System.out.println("=== Referrer URL NULL");
                        }

                    } else {
                        System.out.println("=== Referrer Details NULL");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    System.out.println("=== error " + e.getMessage());
                }
                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                System.out.println("FEATURE_NOT_SUPPORTED");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                // Connection could not be established
                System.out.println("SERVICE_UNAVAILABLE");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                System.out.println("SERVICE_DISCONNECTED");
                break;
            case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                System.out.println("DEVELOPER_ERROR");
                break;
            default:
                System.out.println("RESPONSE CODE NOT FOUND");

        }

        mReferrerClient.endConnection();
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        System.out.println("onInstallReferrerServiceDisconnected");
        mReferrerClient.startConnection(this);
    }

    public void send(Context context, String utm, String utm1, String utm2) {
        nithra.gift.suggestion.shop.birthday.marriage.Notification.HttpHandler sh = new nithra.gift.suggestion.shop.birthday.marriage.Notification.HttpHandler();
        String url = "https://nithra.mobi/apps/referrer.php";
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("app", "Thiruppavai");
            postDataParams.put("ref", utm);
            postDataParams.put("mm", utm1);
            postDataParams.put("cn", utm2);
            postDataParams.put("email", Utils_Class.android_id(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = sh.makeServiceCall(url, postDataParams);

    }
//in_app cods end


}