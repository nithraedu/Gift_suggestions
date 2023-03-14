package nithra.gift.suggestion.shop.birthday.marriage.notification

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.HtmlCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nithra.gift.suggestion.shop.birthday.marriage.MainActivity
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.mProgress
import java.util.*

class ST_Activity : AppCompatActivity() {
    var sharedPreference = SharedPreference()
    lateinit var content_view: WebView
    lateinit var myDB: SQLiteDatabase
    lateinit var myDB1: SQLiteDatabase
    var tablenew = "noti_cal"
    var title: String? = ""
    var message: String? = ""
    var str_title: String? = ""
    lateinit var btn_close: AppCompatButton
    var show_id = 0
    var show_ads = 0
    lateinit var listApp: List<ResolveInfo>
    lateinit var pManager: PackageManager

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        var savedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.st_lay)
        myDB = openOrCreateDatabase("myDB", 0, null)
        myDB1 = openOrCreateDatabase("myDB1", 0, null)
        pManager = packageManager
        myDB.execSQL(
            "CREATE TABLE IF NOT EXISTS " + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) DEFAULT 0,type VARCHAR,"
                    + "bm VARCHAR,ntype VARCHAR,url VARCHAR);"
        )
        myDB.execSQL("CREATE TABLE IF NOT EXISTS notify_mark (uid integer NOT NULL PRIMARY KEY AUTOINCREMENT,id integer);")
        myDB1.execSQL("CREATE TABLE IF NOT EXISTS notify_saved (uid integer NOT NULL PRIMARY KEY AUTOINCREMENT,id integer,title VARCHAR,message VARCHAR);")
        content_view = findViewById(R.id.web)
        //content_view.getSettings().setDomStorageEnabled(true);
        savedInstanceState = intent.extras
        if (savedInstanceState != null) {
            val idd = savedInstanceState.getInt("idd")
            val adss = savedInstanceState.getInt("Noti_add")
            title = savedInstanceState.getString("title")
            message = savedInstanceState.getString("message")
            show_id = idd
            show_ads = adss
            sharedPreference.putInt(applicationContext, "Noti_add", show_ads)
            str_title = title
        }
        content_view.setOnLongClickListener({ v: View? -> true })
        val tit_txt = findViewById<TextView>(R.id.sticky)
        tit_txt.text = "" + str_title
        val ws = content_view.getSettings()
        ws.javaScriptEnabled = true
        val bodyFont =
            "<style> body { font-size:20px; } table { font-size:20px; }</style>"
        val summary =
            "<!DOCTYPE html><html><head>$bodyFont</head><body><br><br><br>$message</body></html>"
        if (message != null) {
            if (message!!.length > 4) {
                val str = "" + message!!.substring(0, 4)
                if (str == "http") {
                    content_view.loadUrl("" + message)
                } else {
                    content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null)
                }
            } else {
                content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null)
            }
        } else {
            content_view.loadDataWithBaseURL("", "" + summary, "text/html", "utf-8", null)
        }
        content_view.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    val i = Intent()
                    i.action = Intent.ACTION_VIEW
                    i.addCategory(Intent.CATEGORY_BROWSABLE)
                    i.data = Uri.parse("" + url)
                    startActivity(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                try {
                    mProgress(this@ST_Activity, "\n" +
                            "Please wait...", true)!!
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                try {
                    mProgress!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                super.onPageFinished(view, url)
            }
        })
        btn_close = findViewById(R.id.btn_close)
        btn_close.setOnClickListener({ v: View? -> onBackPressed() })
        val mFab = findViewById<FloatingActionButton>(R.id.fab_share)

        mFab.setOnClickListener { v: View? ->

            val result =
                HtmlCompat.fromHtml("" + message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            val shareDialog = Dialog(
                this@ST_Activity,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            shareDialog.setContentView(R.layout.share_dialog)
            val share_list = shareDialog.findViewById<ListView>(R.id.share_list)
            listApp = showAllShareApp()
            share_list.adapter = MyAdapter(this@ST_Activity, pManager, listApp)
            val finalResult = result
            share_list.onItemClickListener =
                OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    share(listApp[position], finalResult)
                    shareDialog.dismiss()

                }
            shareDialog.show()
        }
        myDB.execSQL("update $tablenew set isclose='1' where id='$show_id'")
    }

    private fun share(appInfo: ResolveInfo, sharefinal: String) {
        if (appInfo.activityInfo.packageName == "com.whatsapp") {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/*"
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title)
            sendIntent.setPackage("com.whatsapp")
            val uriUrl = Uri.parse("whatsapp://send?text=" + "Shared via gift suggestions. Click here to download the app for free: \n https://bit.ly/3LdV0Gj\n\n"+ sharefinal+"\n\nClick the link below to download for gift suggestions\n"+"https://bit.ly/3LdV0Gj")
            sendIntent.action = Intent.ACTION_VIEW
            sendIntent.data = uriUrl
            startActivity(sendIntent)
        } else {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title)
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Shared via gift suggestions. Click here to download the app for free: \n https://bit.ly/3LdV0Gj\n\n"+ sharefinal+"\n\nClick the link below to download for gift suggestions\n"+"https://bit.ly/3LdV0Gj")
            sendIntent.component = ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name)
            sendIntent.type = "text/*"
            startActivity(sendIntent)
        }
    }

    override fun onBackPressed() {
        if (sharedPreference.getInt(this@ST_Activity, "Noti_add") == 1) {
            sharedPreference.putInt(applicationContext, "Noti_add", 0)
            val i = Intent(this@ST_Activity, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        } else {
            finish()
        }
    }

    @SuppressLint("WrongConstant")
    private fun showAllShareApp(): List<ResolveInfo> {
        var mApps: List<ResolveInfo> = ArrayList()
        val intent = Intent(Intent.ACTION_SEND, null)
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        intent.type = "text/plain"
        pManager = packageManager
        mApps =
            pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
        return mApps
    }
}