package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nithra.gift.suggestion.shop.birthday.marriage.MainActivity;
import nithra.gift.suggestion.shop.birthday.marriage.R;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;


public class ST_Activity extends AppCompatActivity {
    SharedPreference sharedPreference = new SharedPreference();
    WebView content_view;
    SQLiteDatabase myDB, myDB1;
    String tablenew = "noti_cal", title = "", message = "", str_title = "";
    AppCompatButton btn_close;
    int show_id = 0, show_ads = 0;
    java.util.List<ResolveInfo> listApp;
    PackageManager pManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_lay);
        myDB = openOrCreateDatabase("myDB", 0, null);
        myDB1 = openOrCreateDatabase("myDB1", 0, null);
        pManager = getPackageManager();
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) DEFAULT 0,type VARCHAR,"
                + "bm VARCHAR,ntype VARCHAR,url VARCHAR);");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS notify_mark (uid integer NOT NULL PRIMARY KEY AUTOINCREMENT,id integer);");
        myDB1.execSQL("CREATE TABLE IF NOT EXISTS notify_saved (uid integer NOT NULL PRIMARY KEY AUTOINCREMENT,id integer,title VARCHAR,message VARCHAR);");

        content_view = findViewById(R.id.web);
        //content_view.getSettings().setDomStorageEnabled(true);


        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            int idd = savedInstanceState.getInt("idd");
            int adss = savedInstanceState.getInt("Noti_add");
            title = savedInstanceState.getString("title");
            message = savedInstanceState.getString("message");
            show_id = idd;
            show_ads = adss;
            sharedPreference.putInt(getApplicationContext(), "Noti_add", show_ads);
            str_title = title;
        }
        content_view.setOnLongClickListener(v -> true);
        TextView tit_txt = findViewById(R.id.sticky);
        tit_txt.setText("" + str_title);
        WebSettings ws = content_view.getSettings();
        ws.setJavaScriptEnabled(true);
        String bodyFont = "<style> body { font-size:20px; } table { font-size:20px; <font face='bamini'> }</style><style> @font-face { font-family:'bamini'; src: url('file:///android_asset/baamini.ttf') }</style>";
        String summary = "<!DOCTYPE html><html><head>" + bodyFont + "</head><body><br><br><br>" + message + "</body></html>";
        if (message != null) {
            if (message.length() > 4) {
                String str = "" + message.substring(0, 4);
                if (str.equals("http")) {
                    content_view.loadUrl("" + message);
                } else {
                    content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null);
                }
            } else {
                content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null);
            }
        } else {
            content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null);
        }
        content_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.addCategory(Intent.CATEGORY_BROWSABLE);
                    i.setData(Uri.parse("" + url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try {
                    Utils.mProgress(ST_Activity.this, "Loading...", true).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    Utils.mProgress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onPageFinished(view, url);
            }
        });
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(v -> onBackPressed());
        final FloatingActionButton mFab = findViewById(R.id.fab_share);

        mFab.setOnClickListener(v -> {
            String result = Html.fromHtml("" + message).toString();
            result = str_title + "\n" + CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, result);
            final Dialog shareDialog = new Dialog(ST_Activity.this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
            shareDialog.setContentView(R.layout.share_dialog);
            ListView share_list = shareDialog.findViewById(R.id.share_list);
            listApp = showAllShareApp();
            if (listApp != null) {
                share_list.setAdapter(new MyAdapter(ST_Activity.this, pManager, listApp));
                final String finalResult = result;
                share_list.setOnItemClickListener((parent, view, position, id) -> {
                    share(listApp.get(position), finalResult);
                    shareDialog.dismiss();
                });
            }
            shareDialog.show();
        });

        myDB.execSQL("update " + tablenew + " set isclose='1' where id='" + show_id + "'");
    }



    private void share(ResolveInfo appInfo, String sharefinal) {
        if (appInfo.activityInfo.packageName.equals("com.whatsapp")) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/*");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            sendIntent.setPackage("com.whatsapp");
            Uri uriUrl = Uri.parse("whatsapp://send?text=Install this free Inventions & Inventors app to know more about inventions & inventors given in different categories."+"\n\nClick the below link to download this app:"+"\n\n"+ "https://goo.gl/GPeiaQ");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setData(uriUrl);
            startActivity(sendIntent);
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title);
            sendIntent.putExtra(Intent.EXTRA_TEXT, " Install this free Inventions & Inventors app to know more about inventions & inventors given in different categories."+"\n\nClick the below link to download this app:"+"\n\n"+ "https://goo.gl/GPeiaQ");
            sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
            sendIntent.setType("text/*");
            startActivity(sendIntent);
        }
    }

    @Override
    public void onBackPressed() {

        if (sharedPreference.getInt(ST_Activity.this, "Noti_add") == 1) {
            sharedPreference.putInt(getApplicationContext(), "Noti_add", 0);
            Intent i = new Intent(ST_Activity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            finish();
        }



    }



    @SuppressLint("WrongConstant")
    private java.util.List<ResolveInfo> showAllShareApp() {
        java.util.List<ResolveInfo> mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        Typeface currentTypeFace = paint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
        paint.setTypeface(bold);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


}
