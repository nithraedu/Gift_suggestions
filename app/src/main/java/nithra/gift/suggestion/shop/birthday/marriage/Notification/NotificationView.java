package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nithra.gift.suggestion.shop.birthday.marriage.R;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;


public class NotificationView extends AppCompatActivity {
    SharedPreference sharedPreference;
    SQLiteDatabase myDB;
    String tablenew = "noti_cal", checkk_val = "";

    RelativeLayout txtNoNotification;
    ListView listView;
    String[] title;
    String[] message;
    String[] msgType;
    String[] msgTime;
    String[] ntype;
    String[] urll;
    String[] bm;
    int[] Id;
    int[] isclose;
    int[] ads_view;
    int[] ismarkk;

    private Menu _menu = null;

    Boolean chk_val = false, long_val = false, chk_all = false;
    int chkd_val = 0;

    ArrayList<HashMap<String, Object>> players;
    LayoutInflater inflater;
    CustomAdapter adapter;

    boolean[] checkBoxState = new boolean[1];

    int val = 0, acount = 0;
    MenuItem action_delete1,action_delete2,action_delete3,action_delete4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_view);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseApp.initializeApp(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreference = new SharedPreference();
        myDB = openOrCreateDatabase("myDB", 0, null);

        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tablenew
                + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR,"
                + "bm VARCHAR,ntype VARCHAR,url VARCHAR);");


        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(Html.fromHtml("<b>Notification</b>"));
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(v -> {
            chk_all=false;
            onBackPressed();
        });

        setSupportActionBar(toolbar);

        txtNoNotification = findViewById(R.id.txtNoNotification);
        listView = findViewById(R.id.listView1);
        setada();
    }

    public void delet_fun(final String id, final int title) {

        BottomSheetDialog no_datefun = new BottomSheetDialog(NotificationView.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        no_datefun.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        no_datefun.setContentView(R.layout.nodate_dia);

        no_datefun.setCancelable(false);

        Button btnSet = no_datefun.findViewById(R.id.btnSet);
        Button btnok = no_datefun.findViewById(R.id.btnok);
        TextView head_txt = no_datefun.findViewById(R.id.head_txt);
        TextView editText1 = no_datefun.findViewById(R.id.editText1);
        btnSet.setText("YES");
        btnok.setText("NO");
        head_txt.setVisibility(View.GONE);

        if (title == 0) {
            editText1.setText("Do you want to delete this notification?");
        } else {
            editText1.setText("Do you want to delete all this notification?");
        }
        btnSet.setOnClickListener(v -> {
            sharedPreference.removeString(NotificationView.this, "imgURL" + id);
            if (title == 0) {
                myDB.execSQL("delete from noti_cal where " + id.substring(4) + "");
            } else {
                myDB.execSQL("delete from noti_cal ");
            }
            action_delete1 = _menu.findItem(R.id.action_delete);
            action_delete2 = _menu.findItem(R.id.action_refresh);

            action_delete3 = _menu.findItem(R.id.action_no);
            action_delete4 = _menu.findItem(R.id.action_all);
            action_delete1.setVisible(false);
            action_delete3.setVisible(false);
            action_delete4.setVisible(false);
            action_delete2.setVisible(true);


            checkk_val = "";
            chk_val = false;
            long_val = false;
            chk_all = false;
            chkd_val = 0;
            setada();
            no_datefun.dismiss();
        });
        btnok.setOnClickListener(v -> no_datefun.dismiss());
        no_datefun.show();
    }

    public void setada() {
        Cursor c = myDB.rawQuery("select * from " + tablenew + " order by id desc ", null);
        if (c.getCount() == 0) {
            txtNoNotification.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            val = 1;
        } else {
            if (Utils.isNetworkAvailable(NotificationView.this)) {
            } else {
            }
            val = 0;
            txtNoNotification.setVisibility(View.GONE);
            players = new ArrayList<>();
            Id = new int[c.getCount()];
            ismarkk = new int[c.getCount()];
            isclose = new int[c.getCount()];
            title = new String[c.getCount()];
            message = new String[c.getCount()];
            msgType = new String[c.getCount()];
            msgTime = new String[c.getCount()];
            bm = new String[c.getCount()];
            urll = new String[c.getCount()];
            ntype = new String[c.getCount()];
            ads_view = new int[c.getCount()];
            HashMap<String, Object> temp;
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                Id[i] = c.getInt(c.getColumnIndexOrThrow("id"));
                Cursor c1 = myDB.rawQuery("select * from noti_cal where id =" + Id[i] + " ", null);
                if (c1.getCount() == 0) {
                    ismarkk[i] = 0;
                } else {
                    ismarkk[i] = 1;
                }
                c1.close();
                title[i] = c.getString(c.getColumnIndexOrThrow("title"));
                message[i] = c.getString(c.getColumnIndexOrThrow("message"));
                msgType[i] = c.getString(c.getColumnIndexOrThrow("type"));
                isclose[i] = c.getInt(c.getColumnIndexOrThrow("isclose"));
                bm[i] = c.getString(c.getColumnIndexOrThrow("bm"));
                urll[i] = c.getString(c.getColumnIndexOrThrow("url"));
                ntype[i] = c.getString(c.getColumnIndexOrThrow("ntype"));
                msgTime[i] = c.getString(c.getColumnIndexOrThrow("date")) + "," + c.getString(c.getColumnIndexOrThrow("time"));


                temp = new HashMap<>();
                temp.put("idd", Id[i]);
                temp.put("title", title[i]);
                temp.put("isclose", isclose[i]);
                temp.put("msgTime", msgTime[i]);
                temp.put("message", message[i]);
                temp.put("bm", bm[i]);
                temp.put("msgType", msgType[i]);
                temp.put("ntype", ntype[i]);
                temp.put("urll", urll[i]);
                temp.put("ismarkk", ismarkk[i]);
                players.add(temp);
            }

            boolean check=false;

            for (boolean b : checkBoxState) {
                if (b == true) {
                    check = true;
                    break;
                }
            }


            if (!check) {
                checkBoxState = new boolean[players.size()];
            }

            adapter = new NotificationView.CustomAdapter(this, R.layout.notify_item, players);
            listView.setAdapter(adapter);
        }
        c.close();
    }


    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        _menu = menu;
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (val == 0) {
                    item.setVisible(false);
                    MenuItem action_delete = _menu.findItem(R.id.action_delete);
                    MenuItem action_deletee = _menu.findItem(R.id.action_all);
                    MenuItem action_deleteee = _menu.findItem(R.id.action_no);
                    action_delete.setVisible(true);
                    action_deletee.setVisible(true);
                    action_deleteee.setVisible(false);

                    checkk_val = "";
                    chk_val = true;
                    long_val = false;
                    chkd_val = 0;
                    if (chk_val == true) {
                        checkBoxState = new boolean[players.size()];
                        for (int i = 0; i < players.size(); i++) {
                            if (long_val == true) {
                                checkBoxState[i] = chkd_val == i;
                            } else checkBoxState[i] = chk_all == true;
                        }
                    } else {
                        checkBoxState = new boolean[players.size()];
                    }
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.action_delete:
                if (!checkk_val.equals("")) {
                    if (chk_all == true) {
                        delet_fun(checkk_val, 1);
                    } else {
                        delet_fun(checkk_val, 0);
                    }
                }
                return true;

            case R.id.action_all:
                MenuItem action_delete11 = _menu.findItem(R.id.action_delete);
                MenuItem action_delete21 = _menu.findItem(R.id.action_refresh);
                MenuItem action_delete31 = _menu.findItem(R.id.action_all);
                MenuItem action_delete41 = _menu.findItem(R.id.action_no);
                action_delete11.setVisible(true);
                action_delete41.setVisible(true);
                action_delete31.setVisible(false);
                action_delete21.setVisible(false);
                checkk_val = "";
                for (int j : Id) {
                    checkk_val += " or id='" + j + "'";
                }
                chk_all = true;
                chk_val = true;
                long_val = false;

                checkBoxState = new boolean[players.size()];
                for (int i = 0; i < players.size(); i++) {
                    checkBoxState[i] = true;
                }

                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_no:
                MenuItem action_delete111 = _menu.findItem(R.id.action_delete);
                MenuItem action_delete211 = _menu.findItem(R.id.action_refresh);
                MenuItem action_delete311 = _menu.findItem(R.id.action_no);
                MenuItem action_delete411 = _menu.findItem(R.id.action_all);
                action_delete111.setVisible(true);
                action_delete411.setVisible(true);
                action_delete311.setVisible(false);
                action_delete211.setVisible(false);
                checkk_val = "";
                chk_all = false;
                chk_val = true;
                long_val = false;

                checkBoxState = new boolean[players.size()];
                for (int i = 0; i < players.size(); i++) {
                    checkBoxState[i] = false;
                }

                adapter.notifyDataSetChanged();
                return true;
            case android.R.id.home:

                if (chk_val == true) {
                    MenuItem action_delete1 = _menu.findItem(R.id.action_delete);
                    MenuItem action_delete2 = _menu.findItem(R.id.action_refresh);
                    MenuItem action_delete3 = _menu.findItem(R.id.action_all);
                    MenuItem action_delete4 = _menu.findItem(R.id.action_no);
                    action_delete1.setVisible(false);
                    action_delete3.setVisible(false);
                    action_delete4.setVisible(false);
                    action_delete2.setVisible(true);

                    checkk_val = "";
                    chk_val = false;
                    long_val = false;
                    chk_all=false;
                    chkd_val = 0;

                    checkBoxState = new boolean[players.size()];
                    for (int i = 0; i < players.size(); i++) {
                        checkBoxState[i] = false;
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (chk_val == true) {
                    MenuItem action_delete1 = _menu.findItem(R.id.action_delete);
                    MenuItem action_delete2 = _menu.findItem(R.id.action_refresh);
                    MenuItem action_delete3 = _menu.findItem(R.id.action_all);
                    MenuItem action_delete4 = _menu.findItem(R.id.action_no);
                    action_delete1.setVisible(false);
                    action_delete3.setVisible(false);
                    action_delete4.setVisible(false);
                    action_delete2.setVisible(true);
                    checkk_val = "";
                    chk_val = false;
                    long_val = false;
                    chkd_val = 0;
                    chk_all = false;
                    checkBoxState = new boolean[players.size()];
                    for (int i = 0; i < players.size(); i++) {
                        checkBoxState[i] = false;
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public String convert_str(String datee) {

        String[] separated = datee.split(":");
        int HOUR = Integer.parseInt((separated[0]));
        String MIN = separated[1];
        String AM_PM = "AM";
        if (HOUR >= 12) {
            HOUR = HOUR - 12;
            AM_PM = "PM";
        } else {
            AM_PM = "AM";

        }
        if (HOUR == 0) {
            HOUR = 12;
        }
        String SOUND = String.valueOf(HOUR);
        if (String.valueOf(HOUR).length() == 1) {
            SOUND = ("0" + HOUR);
        } else {
            SOUND = String.valueOf(HOUR);
        }
        return SOUND + ":" + MIN + " " + AM_PM;
    }



    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>> {
        ViewHolder viewHolder;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, Object>> players) {
            super(context, textViewResourceId, players);
        }

        private class ViewHolder {
            TextView textView1;
            TextView time_txt;
            TextView cunt;
            AppCompatCheckBox chbk;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            acount = position + 1;
            if (view == null) {
                view = inflater.inflate(R.layout.notify_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView1 = view.findViewById(R.id.textView1);
                viewHolder.time_txt = view.findViewById(R.id.time_txt);
                viewHolder.cunt = view.findViewById(R.id.cunt);
                viewHolder.chbk = view.findViewById(R.id.checkk);
                view.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) view.getTag();

            String[] date_time = players.get(position).get("msgTime").toString().split(",");

            String timeeee = String.valueOf(convert_str(date_time[1]));


            viewHolder.time_txt.setText("" + date_time[0] + "     " + timeeee);


            if (chk_val == false) {
                viewHolder.chbk.setVisibility(View.GONE);
            } else {
                viewHolder.chbk.setVisibility(View.VISIBLE);
            }



            viewHolder.chbk.setChecked(checkBoxState[position]);
            viewHolder.cunt.setText("" + acount);
            System.out.println("acount==" + acount);
            Random ran = new Random();
            int color = Color.argb(255, ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
            GradientDrawable drawable1 = (GradientDrawable) viewHolder.cunt.getBackground().getCurrent();
            drawable1.setColor(color);

            viewHolder.textView1.setText("" + players.get(position).get("bm"));
            //viewHolder.time_txt.setText("" + players.get(position).get("msgTime"));


            viewHolder.chbk.setOnClickListener(v -> {
                chk_all = false;
                MenuItem action_delete11 = _menu.findItem(R.id.action_delete);
                MenuItem action_delete21 = _menu.findItem(R.id.action_refresh);
                MenuItem action_delete31 = _menu.findItem(R.id.action_all);
                MenuItem action_delete41 = _menu.findItem(R.id.action_no);
                action_delete11.setVisible(true);
                action_delete41.setVisible(false);
                action_delete31.setVisible(true);
                action_delete21.setVisible(false);
                if (((CheckBox) v).isChecked()) {
                    checkBoxState[position] = true;
                    checkk_val += " or id='" + players.get(position).get("idd") + "'";

                    checkBoxState[position] = false;
                    checkk_val = checkk_val.replace(" or id='" + players.get(position).get("idd") + "'", "");
                }

            });

            if (isclose[position] == 1) {
                view.setBackgroundResource(R.drawable.white_noti);
                //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                view.setBackgroundResource(R.drawable.unread_noti);
                //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            view.setOnClickListener(v -> {
                chk_all = false;
                if (viewHolder.chbk.getVisibility() == View.VISIBLE) {
                    MenuItem action_delete11 = _menu.findItem(R.id.action_delete);
                    MenuItem action_delete21 = _menu.findItem(R.id.action_refresh);
                    MenuItem action_delete31 = _menu.findItem(R.id.action_all);
                    MenuItem action_delete41 = _menu.findItem(R.id.action_no);
                    action_delete11.setVisible(true);
                    action_delete41.setVisible(false);
                    action_delete31.setVisible(true);
                    action_delete21.setVisible(false);
                    AppCompatCheckBox appCompatCheckBox = v.findViewById(R.id.checkk);
                    if (appCompatCheckBox.isChecked() == false) {
                        appCompatCheckBox.setChecked(true);
                        checkBoxState[position] = true;
                        checkk_val += " or id='" + players.get(position).get("idd") + "'";
                    } else {
                        appCompatCheckBox.setChecked(false);
                        checkBoxState[position] = false;
                        checkk_val = checkk_val.replace(" or id='" + players.get(position).get("idd") + "'", "");
                    }
                } else {
                    isclose[position] = 1;
                    myDB.execSQL("update " + tablenew + " set isclose='1' where id='" + players.get(position).get("idd") + "'");
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(NotificationView.this, ST_Activity.class);
                    intent.putExtra("message", (String) players.get(position).get("message"));
                    intent.putExtra("title", (String) players.get(position).get("bm"));
                    intent.putExtra("idd", (int) players.get(position).get("idd"));
                    intent.putExtra("Noti_add", 0);
                    intent.putExtra("adcheck",1);
                    intent.putExtra("notifrag",1);
                    startActivity(intent);
                }
            });

            view.setOnLongClickListener(v -> {
                MenuItem action_delete1 = _menu.findItem(R.id.action_delete);
                MenuItem action_delete2 = _menu.findItem(R.id.action_refresh);
                MenuItem action_delete3 = _menu.findItem(R.id.action_all);
                MenuItem action_delete4 = _menu.findItem(R.id.action_no);
                action_delete1.setVisible(true);
                action_delete3.setVisible(true);
                action_delete2.setVisible(false);
                action_delete4.setVisible(false);
                checkk_val += " or id='" + players.get(position).get("idd") + "'";

                chk_val = true;
                long_val = true;
                chk_all = false;
                chkd_val = position;
                checkBoxState[position] = true;
                if (chk_val == true) {
                    checkBoxState = new boolean[players.size()];
                    for (int i = 0; i < players.size(); i++) {
                        if (long_val == true) {
                            checkBoxState[i] = chkd_val == i;
                        } else checkBoxState[i] = chk_all == true;
                    }
                } else {
                    checkBoxState = new boolean[players.size()];
                }
                adapter.notifyDataSetChanged();
                return false;
            });
            return view;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        onDelete();
        return true;
    }

    public void onDelete() {
        Cursor c = myDB.rawQuery("SELECT * FROM " + tablenew + " ORDER BY id DESC", null);
        if (c.getCount() == 0) {
            MenuItem action_delete1 = _menu.findItem(R.id.action_refresh);
            action_delete1.setVisible(false);
        }
    }
}
