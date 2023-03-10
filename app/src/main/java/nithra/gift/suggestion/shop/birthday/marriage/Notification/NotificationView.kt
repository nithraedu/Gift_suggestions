package nithra.gift.suggestion.shop.birthday.marriage.Notification

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.text.Html
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.isNetworkAvailable
import java.util.*

class NotificationView : AppCompatActivity() {
    var sharedPreference: SharedPreference? = null
    lateinit var myDB: SQLiteDatabase
    var tablenew = "noti_cal"
    var checkk_val = ""
    var txtNoNotification: RelativeLayout? = null
    var listView: ListView? = null
    lateinit var title: Array<String?>
    lateinit var message: Array<String?>
    lateinit var msgType: Array<String?>
    lateinit var msgTime: Array<String?>
    lateinit var ntype: Array<String?>
    lateinit var urll: Array<String?>
    lateinit var bm: Array<String?>
    lateinit var Id: IntArray
    lateinit var isclose: IntArray
    lateinit var ads_view: IntArray
    lateinit var ismarkk: IntArray
    private var _menu: Menu? = null
    var chk_val = false
    var long_val = false
    var chk_all = false
    var chkd_val = 0
    var players: ArrayList<HashMap<String, Any?>>? = null
    var inflater: LayoutInflater? = null
    var adapter: CustomAdapter? = null
    var checkBoxState = BooleanArray(1)
    var `val` = 0
    var acount = 0
    lateinit var action_delete1: MenuItem
    lateinit var action_delete2: MenuItem
    lateinit var action_delete3: MenuItem
    lateinit var action_delete4: MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notify_view)
        inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder1 = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder1.build())
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        FirebaseApp.initializeApp(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sharedPreference = SharedPreference()
        myDB = openOrCreateDatabase("myDB", 0, null)
        myDB.execSQL(
            "CREATE TABLE IF NOT EXISTS " + tablenew
                    + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR,"
                    + "bm VARCHAR,ntype VARCHAR,url VARCHAR);"
        )
        val toolbar = findViewById<Toolbar>(R.id.app_bar)
        toolbar.title = Html.fromHtml("<b>Notification</b>")
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_white)
        toolbar.setNavigationOnClickListener { v: View? ->
            chk_all = false
            onBackPressed()
        }
        setSupportActionBar(toolbar)
        txtNoNotification = findViewById(R.id.txtNoNotification)
        listView = findViewById(R.id.listView1)
        setada()
    }

    fun delet_fun(id: String, title: Int) {
        val no_datefun = BottomSheetDialog(
            this@NotificationView,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        no_datefun.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        no_datefun.setContentView(R.layout.nodate_dia)
        no_datefun.setCancelable(false)
        val btnSet = no_datefun.findViewById<Button>(R.id.btnSet)
        val btnok = no_datefun.findViewById<Button>(R.id.btnok)
        val head_txt = no_datefun.findViewById<TextView>(R.id.head_txt)
        val editText1 = no_datefun.findViewById<TextView>(R.id.editText1)
        btnSet!!.text = "Yes"
        btnok!!.text = "No"
        head_txt!!.visibility = View.GONE
        if (title == 0) {
            editText1!!.text = "Delete selected notification?"
        } else {
            editText1!!.text = "Delete all notifications?"
        }
        btnSet.setOnClickListener { v: View? ->
            sharedPreference!!.removeString(this@NotificationView, "imgURL$id")
            if (title == 0) {
                myDB!!.execSQL("delete from noti_cal where " + id.substring(4) + "")
            } else {
                myDB!!.execSQL("delete from noti_cal ")
            }
            action_delete1 = _menu!!.findItem(R.id.action_delete)
            action_delete2 = _menu!!.findItem(R.id.action_refresh)
            action_delete3 = _menu!!.findItem(R.id.action_no)
            action_delete4 = _menu!!.findItem(R.id.action_all)
            action_delete1.setVisible(false)
            action_delete3.setVisible(false)
            action_delete4.setVisible(false)
            action_delete2.setVisible(true)
            checkk_val = ""
            chk_val = false
            long_val = false
            chk_all = false
            chkd_val = 0
            setada()
            no_datefun.dismiss()
        }
        btnok.setOnClickListener { v: View? -> no_datefun.dismiss() }
        no_datefun.show()
    }

    fun setada() {
        val c = myDB!!.rawQuery("select * from $tablenew order by id desc ", null)
        if (c.count == 0) {
            txtNoNotification!!.visibility = View.VISIBLE
            listView!!.visibility = View.GONE
            `val` = 1
        } else {
            if (isNetworkAvailable(this@NotificationView)) {
            } else {
            }
            `val` = 0
            txtNoNotification!!.visibility = View.GONE
            players = ArrayList()
            Id = IntArray(c.count)
            ismarkk = IntArray(c.count)
            isclose = IntArray(c.count)
            title = arrayOfNulls(c.count)
            message = arrayOfNulls(c.count)
            msgType = arrayOfNulls(c.count)
            msgTime = arrayOfNulls(c.count)
            bm = arrayOfNulls(c.count)
            urll = arrayOfNulls(c.count)
            ntype = arrayOfNulls(c.count)
            ads_view = IntArray(c.count)
            var temp: HashMap<String, Any?>
            for (i in 0 until c.count) {
                c.moveToPosition(i)
                Id[i] = c.getInt(c.getColumnIndexOrThrow("id"))
                val c1 = myDB!!.rawQuery("select * from noti_cal where id =" + Id[i] + " ", null)
                if (c1.count == 0) {
                    ismarkk[i] = 0
                } else {
                    ismarkk[i] = 1
                }
                c1.close()
                title[i] = c.getString(c.getColumnIndexOrThrow("title"))
                message[i] = c.getString(c.getColumnIndexOrThrow("message"))
                msgType[i] = c.getString(c.getColumnIndexOrThrow("type"))
                isclose[i] = c.getInt(c.getColumnIndexOrThrow("isclose"))
                bm[i] = c.getString(c.getColumnIndexOrThrow("bm"))
                urll[i] = c.getString(c.getColumnIndexOrThrow("url"))
                ntype[i] = c.getString(c.getColumnIndexOrThrow("ntype"))
                msgTime[i] = c.getString(c.getColumnIndexOrThrow("date")) + "," + c.getString(
                    c.getColumnIndexOrThrow("time")
                )
                temp = HashMap()
                temp["idd"] = Id[i]
                temp["title"] = title[i]
                temp["isclose"] = isclose[i]
                temp["msgTime"] = msgTime[i]
                temp["message"] = message[i]
                temp["bm"] = bm[i]
                temp["msgType"] = msgType[i]
                temp["ntype"] = ntype[i]
                temp["urll"] = urll[i]
                temp["ismarkk"] = ismarkk[i]
                //add the row to the ArrayList
                players!!.add(temp)
            }
            var check = false
            for (b in checkBoxState) {
                if (b == true) {
                    check = true
                    break
                }
            }
            if (!check) {
                checkBoxState = BooleanArray(players!!.size)
            }
            adapter = CustomAdapter(this, R.layout.notify_item, players!!)
            listView!!.adapter = adapter
        }
        c.close()
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        _menu = menu
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                if (`val` == 0) {
                    item.isVisible = false
                    val action_delete = _menu!!.findItem(R.id.action_delete)
                    val action_deletee = _menu!!.findItem(R.id.action_all)
                    val action_deleteee = _menu!!.findItem(R.id.action_no)
                    action_delete.isVisible = true
                    action_deletee.isVisible = true
                    action_deleteee.isVisible = false
                    checkk_val = ""
                    chk_val = true
                    long_val = false
                    chkd_val = 0
                    if (chk_val == true) {
                        checkBoxState = BooleanArray(players!!.size)
                        var i = 0
                        while (i < players!!.size) {
                            if (long_val == true) {
                                checkBoxState[i] = chkd_val == i
                            } else checkBoxState[i] = chk_all == true
                            i++
                        }
                    } else {
                        checkBoxState = BooleanArray(players!!.size)
                    }
                    adapter!!.notifyDataSetChanged()
                }
                return true
            }
            R.id.action_delete -> {
                if (checkk_val != "") {
                    if (chk_all == true) {
                        delet_fun(checkk_val, 1)
                    } else {
                        delet_fun(checkk_val, 0)
                    }
                }
                return true
            }
            R.id.action_all -> {
                val action_delete11 = _menu!!.findItem(R.id.action_delete)
                val action_delete21 = _menu!!.findItem(R.id.action_refresh)
                val action_delete31 = _menu!!.findItem(R.id.action_all)
                val action_delete41 = _menu!!.findItem(R.id.action_no)
                action_delete11.isVisible = true
                action_delete41.isVisible = true
                action_delete31.isVisible = false
                action_delete21.isVisible = false
                checkk_val = ""
                for (j in Id) {
                    checkk_val += " or id='$j'"
                }
                chk_all = true
                chk_val = true
                long_val = false
                checkBoxState = BooleanArray(players!!.size)
                var i = 0
                while (i < players!!.size) {
                    checkBoxState[i] = true
                    i++
                }
                adapter!!.notifyDataSetChanged()
                return true
            }
            R.id.action_no -> {
                val action_delete111 = _menu!!.findItem(R.id.action_delete)
                val action_delete211 = _menu!!.findItem(R.id.action_refresh)
                val action_delete311 = _menu!!.findItem(R.id.action_no)
                val action_delete411 = _menu!!.findItem(R.id.action_all)
                action_delete111.isVisible = true
                action_delete411.isVisible = true
                action_delete311.isVisible = false
                action_delete211.isVisible = false
                checkk_val = ""
                chk_all = false
                chk_val = true
                long_val = false
                checkBoxState = BooleanArray(players!!.size)
                var i = 0
                while (i < players!!.size) {
                    checkBoxState[i] = false
                    i++
                }
                adapter!!.notifyDataSetChanged()
                return true
            }
            android.R.id.home -> {
                if (chk_val == true) {
                    val action_delete1 = _menu!!.findItem(R.id.action_delete)
                    val action_delete2 = _menu!!.findItem(R.id.action_refresh)
                    val action_delete3 = _menu!!.findItem(R.id.action_all)
                    val action_delete4 = _menu!!.findItem(R.id.action_no)
                    action_delete1.isVisible = false
                    action_delete3.isVisible = false
                    action_delete4.isVisible = false
                    action_delete2.isVisible = true
                    checkk_val = ""
                    chk_val = false
                    long_val = false
                    chk_all = false
                    chkd_val = 0
                    checkBoxState = BooleanArray(players!!.size)
                    var i = 0
                    while (i < players!!.size) {
                        checkBoxState[i] = false
                        i++
                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (chk_val == true) {
                    val action_delete1 = _menu!!.findItem(R.id.action_delete)
                    val action_delete2 = _menu!!.findItem(R.id.action_refresh)
                    val action_delete3 = _menu!!.findItem(R.id.action_all)
                    val action_delete4 = _menu!!.findItem(R.id.action_no)
                    action_delete1.isVisible = false
                    action_delete3.isVisible = false
                    action_delete4.isVisible = false
                    action_delete2.isVisible = true
                    checkk_val = ""
                    chk_val = false
                    long_val = false
                    chkd_val = 0
                    chk_all = false
                    checkBoxState = BooleanArray(players!!.size)
                    var i = 0
                    while (i < players!!.size) {
                        checkBoxState[i] = false
                        i++
                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun convert_str(datee: String): String {
        val separated = datee.split(":").toTypedArray()
        var HOUR = separated[0].toInt()
        val MIN = separated[1]
        var AM_PM = "AM"
        if (HOUR >= 12) {
            HOUR = HOUR - 12
            AM_PM = "PM"
        } else {
            AM_PM = "AM"
        }
        if (HOUR == 0) {
            HOUR = 12
        }
        var SOUND = HOUR.toString()
        SOUND = if (HOUR.toString().length == 1) {
            "0$HOUR"
        } else {
            HOUR.toString()
        }
        return "$SOUND:$MIN $AM_PM"
    }

    inner class CustomAdapter(
        context: Context,
        textViewResourceId: Int,
        players: ArrayList<HashMap<String, Any?>>
    ) : ArrayAdapter<HashMap<String, Any?>>(context, textViewResourceId, players) {
        var viewHolder: ViewHolder? = null

        inner class ViewHolder {
            var textView1: TextView? = null
            var time_txt: TextView? = null
            var cunt: TextView? = null
            var chbk: AppCompatCheckBox? = null
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var view = view
            acount = position + 1
            if (view == null) {
                view = inflater!!.inflate(R.layout.notify_item, null)
                viewHolder = ViewHolder()
                viewHolder!!.textView1 = view.findViewById(R.id.textView1)
                viewHolder!!.time_txt = view.findViewById(R.id.time_txt)
                viewHolder!!.cunt = view.findViewById(R.id.cunt)
                viewHolder!!.chbk = view.findViewById(R.id.checkk)
                view.tag = viewHolder
            } else viewHolder = view.tag as ViewHolder
            val date_time = players!![position]["msgTime"].toString().split(",").toTypedArray()
            val timeeee = convert_str(date_time[1])
            viewHolder!!.time_txt!!.text = "" + date_time[0] + "     " + timeeee
            if (chk_val == false) {
                viewHolder!!.chbk!!.visibility = View.GONE
            } else {
                viewHolder!!.chbk!!.visibility = View.VISIBLE
            }
            viewHolder!!.chbk!!.isChecked = checkBoxState[position]
            viewHolder!!.cunt!!.text = "" + acount
            println("acount==$acount")
            val ran = Random()
            val color = Color.argb(255, ran.nextInt(256), ran.nextInt(256), ran.nextInt(256))
            val drawable1 = viewHolder!!.cunt!!.background.current as GradientDrawable
            drawable1.setColor(color)
            viewHolder!!.textView1!!.text = "" + players!![position]["bm"]
            //viewHolder.time_txt.setText("" + players.get(position).get("msgTime"));
            viewHolder!!.chbk!!.setOnClickListener { v: View ->
                chk_all = false
                val action_delete11 = _menu!!.findItem(R.id.action_delete)
                val action_delete21 = _menu!!.findItem(R.id.action_refresh)
                val action_delete31 = _menu!!.findItem(R.id.action_all)
                val action_delete41 = _menu!!.findItem(R.id.action_no)
                action_delete11.isVisible = true
                action_delete41.isVisible = false
                action_delete31.isVisible = true
                action_delete21.isVisible = false
                /*if ((v as CheckBox).isChecked) {
                    checkBoxState[position] = true
                    checkk_val += " or id='" + players!![position]["idd"] + "'"
                    checkBoxState[position] = false
                    checkk_val = checkk_val.replace(" or id='" + players!![position]["idd"] + "'", "")
                }
*/
                if ((v as CheckBox).isChecked) {
                    checkBoxState[position] = true
                    checkk_val += " or id='" + players!![position]["idd"] + "'"
                } else {
                    checkBoxState[position] = false
                    checkk_val =
                        checkk_val.replace(" or id='" + players!![position]["idd"] + "'", "")
                }
                if (check_all_select()) {
                    action_delete31.isVisible = false
                    action_delete41.isVisible = true
                    chk_all = true
                } else {
                    action_delete31.isVisible = true
                    action_delete41.isVisible = false
                }
            }

            if (isclose[position] == 1) {
                view!!.setBackgroundResource(R.drawable.white_noti)
                //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                view!!.setBackgroundResource(R.drawable.unread_noti)
                //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            view.setOnClickListener { v: View ->
                chk_all = false
                if (viewHolder!!.chbk!!.visibility == View.VISIBLE) {
                    val action_delete11 = _menu!!.findItem(R.id.action_delete)
                    val action_delete21 = _menu!!.findItem(R.id.action_refresh)
                    val action_delete31 = _menu!!.findItem(R.id.action_all)
                    val action_delete41 = _menu!!.findItem(R.id.action_no)
                    action_delete11.isVisible = true
                    action_delete41.isVisible = false
                    action_delete31.isVisible = true
                    action_delete21.isVisible = false
                    val appCompatCheckBox: AppCompatCheckBox = v.findViewById(R.id.checkk)
                    if (appCompatCheckBox.isChecked == false) {
                        appCompatCheckBox.isChecked = true
                        checkBoxState[position] = true
                        checkk_val += " or id='" + players!![position]["idd"] + "'"
                    } else {
                        appCompatCheckBox.isChecked = false
                        checkBoxState[position] = false
                        checkk_val =
                            checkk_val.replace(" or id='" + players!![position]["idd"] + "'", "")
                    }
                    if (check_all_select()) {
                        action_delete31.setVisible(false);
                        action_delete41.setVisible(true);
                        chk_all = true;
                    } else {
                        action_delete31.setVisible(true);
                        action_delete41.setVisible(false);
                    }
                } else {
                    isclose[position] = 1
                    myDB!!.execSQL("update " + tablenew + " set isclose='1' where id='" + players!![position]["idd"] + "'")
                    adapter!!.notifyDataSetChanged()
                    val intent = Intent(this@NotificationView, ST_Activity::class.java)
                    intent.putExtra("message", players!![position]["message"] as String?)
                    intent.putExtra("title", players!![position]["bm"] as String?)
                    intent.putExtra("idd", players!![position]["idd"] as Int)
                    intent.putExtra("Noti_add", 0)
                    intent.putExtra("adcheck", 1)
                    intent.putExtra("notifrag", 1)
                    startActivity(intent)
                }
            }
            view.setOnLongClickListener { v: View? ->
                val action_delete1 = _menu!!.findItem(R.id.action_delete)
                val action_delete2 = _menu!!.findItem(R.id.action_refresh)
                val action_delete3 = _menu!!.findItem(R.id.action_all)
                val action_delete4 = _menu!!.findItem(R.id.action_no)
                action_delete1.isVisible = true
                action_delete3.isVisible = true
                action_delete2.isVisible = false
                action_delete4.isVisible = false
                checkk_val += " or id='" + players!![position]["idd"] + "'"
                chk_val = true
                long_val = true
                chk_all = false
                chkd_val = position
                checkBoxState[position] = true
                if (chk_val == true) {
                    checkBoxState = BooleanArray(players!!.size)
                    for (i in players!!.indices) {
                        if (long_val == true) {
                            if (chkd_val == i) {
                                checkBoxState[i] = true;
                            } else {
                                checkBoxState[i] = false;
                            }
                        }
                        else if (chk_all == true) {
                            checkBoxState[i] = true;
                        } else {
                            checkBoxState[i] = false;
                        }
                    }
                    if (check_all_select()) {
                        action_delete3.setVisible(false);
                        action_delete4.setVisible(true);
                        chk_all = true;
                    } else {
                        action_delete3.setVisible(true);
                        action_delete4.setVisible(false);
                    }
                } else {
                    checkBoxState = BooleanArray(players!!.size)
                }
                adapter!!.notifyDataSetChanged()
                false
            }
            return view
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        onDelete()
        return true
    }

    fun onDelete() {
        val c = myDB!!.rawQuery("SELECT * FROM $tablenew ORDER BY id DESC", null)
        if (c.count == 0) {
            val action_delete1 = _menu!!.findItem(R.id.action_refresh)
            action_delete1.isVisible = false
        }
    }

    fun check_all_select(): Boolean {
        for (value in checkBoxState) {
            if (!value) {
                return false
            }
        }
        return true
    }
}