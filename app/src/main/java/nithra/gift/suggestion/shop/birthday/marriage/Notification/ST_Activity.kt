package nithra.gift.suggestion.shop.birthday.marriage.Notification

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
import android.view.View.OnLongClickListener
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
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.mProgress
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
        content_view.setOnLongClickListener(OnLongClickListener { v: View? -> true })
        val tit_txt = findViewById<TextView>(R.id.sticky)
        tit_txt.text = "" + str_title
        val ws = content_view.getSettings()
        ws.javaScriptEnabled = true
        val bodyFont =
            "<style> body { font-size:20px; } table { font-size:20px; }</style>"
        val summary =
            "<!DOCTYPE html><html><head>$bodyFont</head><body><br><br><br>$message</body></html>"
        System.out.println("hello jaasim" + message)
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
        btn_close.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })
        val mFab = findViewById<FloatingActionButton>(R.id.fab_share)
        /* final FloatingActionButton fab_copy = findViewById(R.id.fab_copy);
        fab_copy.setOnClickListener(v -> {
            String result = Html.fromHtml("" + message).toString();
            result = str_title + "\n" + CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, result);
            result = "బేబీ నేమ్స్ అప్లికేషన్‌ను డౌన్‌లోడ్ చేయడానికి ఇక్కడ క్లిక్ చేయండి:\n\nhttps://goo.gl/7us7bX\n\n" + result +
                    "బేబీ నేమ్స్ అప్లికేషన్‌ను డౌన్‌లోడ్ చేయడానికి ఇక్కడ క్లిక్ చేయండి:\n\nhttps://goo.gl/7us7bX";
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", result);
            clipboard.setPrimaryClip(clip);
            Utils.toast_center(ST_Activity.this, "தகவல் நகலெடுக்கப்பட்டது");
        });*/

        mFab.setOnClickListener { v: View? ->

            /* if (message == null) {
                return@setOnClickListener
            }
            //link_data_get(message, 0);
            //link_data_get(message, 0);
            val str = message!!.substring(0, 4)
            System.out.println("suhalkjjd"+message)
            if (str == "http") {

                link_data_get(message, 0)
            } else {*/
            // var result = Html.fromHtml("" + message).toString()
            var result =
                HtmlCompat.fromHtml("" + message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            /* result = """
                $str_title
                //${CodetoTamilUtil.convertToTamil(CodetoTamilUtil.BAMINI, result)}
                """.trimIndent()*/
            val shareDialog = Dialog(
                this@ST_Activity,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            shareDialog.setContentView(R.layout.share_dialog)
            val share_list = shareDialog.findViewById<ListView>(R.id.share_list)
            listApp = showAllShareApp()
            if (listApp != null) {
                share_list.adapter = MyAdapter(this@ST_Activity, pManager, listApp)
                val finalResult = result
                share_list.onItemClickListener =
                    OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                        share(listApp!![position], finalResult)
                        shareDialog.dismiss()

                    }
            }
            shareDialog.show()
            // }
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
            //sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
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

    companion object {
        fun textAsBitmap(text: String?, textSize: Float, textColor: Int): Bitmap {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = textSize
            paint.color = textColor
            paint.textAlign = Paint.Align.LEFT
            val currentTypeFace = paint.typeface
            val bold = Typeface.create(currentTypeFace, Typeface.BOLD)
            paint.typeface = bold
            val baseline = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(text) + 0.0f).toInt() // round
            val height = (baseline + paint.descent() + 0.0f).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(text!!, 0f, baseline, paint)
            return image
        }
    }
}
/*fun link_data_get(url_str: String?, type: Int) {
    val fi_result = arrayOf<String?>("")
    Utils.mProgress(this@ST_Activity, "లోడ్ అవుతోంది... కొద్దిసేపు వేచి ఉండండి...", false)?.show()
    val handler: Handler = object : Handler(Objects.requireNonNull(Looper.myLooper())!!) {
        override fun handleMessage(msg1: Message) {
            val runnable = Runnable {
                Utils.mProgress?.dismiss()
                try {
                    println("linkkkk : " + fi_result[0])
                    fi_result[0] = fi_result[0]!!.replace("<a href=", "")
                    println("linkkkkxyz : " + fi_result[0])
                    if (fi_result[0] != null) {
                        if (type == 1) {
                            var result = Html.fromHtml(fi_result[0]).toString()
                            result = result.replace(">", "")
                            result = result.replace("/>", "")
                            result = """
                              $str_title

                              $result
                              """.trimIndent()
                            result =
                                """ బేబీ నేమ్స్ అప్లికేషన్‌ను డౌన్‌లోడ్ చేయడానికి ఇక్కడ క్లిక్ చేయండి:

 https://bit.ly/3LdV0Gj

 $result

 మా ప్రాసెసర్ ద్వారా మీ ఇంటి పిల్లల అందమైన పేర్లను ఎంచుకోండి! మీ పిల్లల కోసం మరిన్ని నక్షత్రాల పేర్లను సూచించండి మరియు ఇప్పుడే ఉచితంగా డౌన్‌లోడ్ చేసుకోండి!

 https://bit.ly/3LdV0Gj"""
                            val clipboard =
                                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("text label", result)
                            clipboard.setPrimaryClip(clip)
                            Utils.toast_center(this@ST_Activity, "సమాచారం కాపీ చేయబడింది")
                        } else if (type == 0) {
                            var result = Html.fromHtml(fi_result[0]).toString()
                            result = result.replace(">", "")
                            result = result.replace("/>", "")
                            //result=fi_result[0];
                            result = """
                              $str_title

                              $result
                              """.trimIndent()
                            val shareDialog = Dialog(
                                this@ST_Activity,
                                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
                            )
                            shareDialog.setContentView(R.layout.share_dialog)
                            val share_list = shareDialog.findViewById<ListView>(R.id.share_list)
                            listApp = showAllShareApp()
                            if (listApp != null) {
                                share_list.adapter =
                                    MyAdapter(this@ST_Activity, pManager, listApp)
                                val finalResult = result
                                share_list.onItemClickListener =
                                    OnItemClickListener { parent, view, position, id ->
                                        share(listApp[position], finalResult)
                                        shareDialog.dismiss()
                                    }
                            }
                            shareDialog.show()
                        } else {
                            try {
                                var result = Html.fromHtml(fi_result[0]).toString()
                                result = result.replace(">", "")
                                result = result.replace("/>", "")
                                val sendIntent = Intent()
                                sendIntent.action = Intent.ACTION_SEND
                                sendIntent.type = "text/*"
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT, str_title)
                                val uriUrl = Uri.parse(
                                    """whatsapp://send?text=నబేబీ నేమ్స్ అప్లికేషన్‌ను డౌన్‌లోడ్ చేయడానికి ఇక్కడ క్లిక్ చేయండి:

 https://bit.ly/3LdV0Gj

$str_title

${result.replace("&", "%26")}
మా ప్రాసెసర్ ద్వారా మీ ఇంటి పిల్లల అందమైన పేర్లను ఎంచుకోండి! మీ పిల్లల కోసం మరిన్ని నక్షత్రాల పేర్లను సూచించండి మరియు ఇప్పుడే ఉచితంగా డౌన్‌లోడ్ చేసుకోండి!

 https://bit.ly/3LdV0Gj"""
                                )
                                sendIntent.action = Intent.ACTION_VIEW
                                sendIntent.data = uriUrl
                                sendIntent.setPackage("com.whatsapp")
                                // sendIntent.setComponent(new ComponentName(appInfo.activityInfo.packageName, appInfo.activityInfo.name));
                                startActivity(sendIntent)
                            } catch (e: ActivityNotFoundException) {
                                var result = Html.fromHtml(fi_result[0]).toString()
                                result = result.replace(">", "")
                                result = result.replace("/>", "")
                                result = """
                                  $str_title

                                  $result
                                  """.trimIndent()
                                val shareDialog = Dialog(
                                    this@ST_Activity,
                                    android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
                                )
                                shareDialog.setContentView(R.layout.share_dialog)
                                val share_list =
                                    shareDialog.findViewById<ListView>(R.id.share_list)
                                listApp = showAllShareApp()
                                if (listApp != null) {
                                    share_list.adapter =
                                        MyAdapter(this@ST_Activity, pManager, listApp)
                                    val finalResult = result
                                    share_list.onItemClickListener =
                                        OnItemClickListener { parent, view, position, id ->
                                            share(listApp[position], finalResult)
                                            shareDialog.dismiss()
                                        }
                                }
                                shareDialog.show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("linkkkkerror : $e")
                    runOnUiThread { Utils.toast_center(this@ST_Activity, "Try again...") }
                }
            }
            runOnUiThread(runnable)
        }
    }
    val checkUpdate: Thread = object : Thread() {
        override fun run() {
            val sb = StringBuffer("")
            try {
                val url = URL(url_str)
               val reader = BufferedReader(InputStreamReader(url.openStream()))
                var inputLine: String
                while (reader.readLine().run { inputLine = reader.readLine() } != null) {
                    if (inputLine.trim { it <= ' ' }.length != 0) {
                        sb.append(inputLine)
                    }
                    println("linkkkk : $sb")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                println("linkkkkerror1 : $e")
            }
            fi_result[0] = sb.toString()
            println("linkkk=== " + Html.fromHtml(fi_result[0]))
            println("linkkk123=== " + fi_result[0])
            val message = Message()
            message.obj = sb.toString()
            handler.sendEmptyMessage(0)
        }
    }
    checkUpdate.start()
}*/