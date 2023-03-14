package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GetCountry
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GiftEdit
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient.retrofit
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.isNetworkAvailable
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.mProgress
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.toast_center
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.toast_normal
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.ImagePicker.Companion.with
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class ShopEdit : AppCompatActivity() {
    var sellername: TextInputEditText? = null
    var shopname: TextInputEditText? = null
    var shopaddress: TextInputEditText? = null
    var mobilenumber: TextInputEditText? = null
    var city: TextInputEditText? = null
    var state: TextInputEditText? = null
    var latitude: TextInputEditText? = null
    var longitude: TextInputEditText? = null
    var pincode: TextInputEditText? = null
    var district: TextInputEditText? = null
    var mailid: TextInputEditText? = null
    var website: TextInputEditText? = null
    var anothermobilenumber: TextInputEditText? = null
    var sell_name: String? = null
    var shop_name: String? = null
    var shop_add: String? = null
    var mob_num: String? = null
    var another_mob_num: String? = null
    var shop_city: String? = null
    var shop_country: String? = null
    var shop_state: String? = null
    var shop_pincode: String? = null
    var shop_district: String? = null
    var shop_latitude: String? = null
    var shop_longitude: String? = null
    var mail: String? = null
    var web: String? = null
    var country_text: String? = null
    var save: TextView? = null
    var remove: ImageView? = null
    var edit_img: ImageView? = null
    var IVPreviewImage: ImageView? = null
    var sharedPreference = SharedPreference()
    var list_shop: ArrayList<GiftEdit>? = null
    var uri_1: Uri? = null
    var map1 = HashMap<String, String?>()
    var map2 = HashMap<String, String>()
    var path = ""
    var back: ImageView? = null
    var spin_country: Spinner? = null
    var country_get: ArrayList<GetCountry>? = null
    var spin: ArrayList<String?>? = null
    var coun_try: String? = null
    var country_id: String? = null
    var file_array = arrayOfNulls<File>(1)
    var testView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_shop_edit)
        sellername = findViewById(R.id.sellername)
        shopname = findViewById(R.id.shopname)
        shopaddress = findViewById(R.id.shopaddress)
        mobilenumber = findViewById(R.id.mobilenumber)
        city = findViewById(R.id.city)
        state = findViewById(R.id.state)
        save = findViewById(R.id.save)
        remove = findViewById(R.id.remove)
        edit_img = findViewById(R.id.edit_img)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        pincode = findViewById(R.id.pincode)
        pincode = findViewById(R.id.pincode)
        district = findViewById(R.id.district)
        anothermobilenumber = findViewById(R.id.anothermobilenumber)
        list_shop = ArrayList()
        back = findViewById(R.id.back)
        mailid = findViewById(R.id.mailid)
        website = findViewById(R.id.website)
        spin_country = findViewById(R.id.spin_country)
        country_get = ArrayList()
        spin = ArrayList()
        testView = findViewById(R.id.testView)
        testView!!.setOnClickListener({
            val dialog = Dialog(this@ShopEdit)
            dialog.setContentView(R.layout.searchable_spinner)
            dialog.window!!.setLayout(650, 800)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.edit_text)
            val listView = dialog.findViewById<ListView>(R.id.list_view)
            for (i in country_get!!.indices) {
                spin!!.add(country_get!![i].name)
            }
            val adapter = ArrayAdapter(this@ShopEdit, android.R.layout.simple_list_item_1, spin!!)
            listView.adapter = adapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })
            listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                testView!!.setText(adapter.getItem(position))
                testView!!.setTag(country_get!![position].id)
                dialog.dismiss()
            }
        })
        //emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val emailPattern: Regex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

        back!!.setOnClickListener({ finish() })
        mProgress(this, "Loading please wait...", false)!!.show()
        shopedit()
        IVPreviewImage!!.setOnClickListener({ //openSomeActivityForResult();
            val dialog: Dialog
            dialog = Dialog(
                this@ShopEdit,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView
            val gallery: ImageView
            camera = dialog.findViewById(R.id.camera)
            gallery = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                image_pick()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                image_pick_gal()
                dialog.dismiss()
            }
        })
        edit_img!!.setOnClickListener({ //openSomeActivityForResult();
            val dialog: Dialog
            dialog = Dialog(
                this@ShopEdit,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView
            val gallery: ImageView
            camera = dialog.findViewById(R.id.camera)
            gallery = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                image_pick()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                image_pick_gal()
                dialog.dismiss()
            }
        })
        spin_country!!.setOnTouchListener({ v, event ->
            val imm =
                applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            false
        })
        save!!.setOnClickListener({
            sell_name = sellername!!.getText().toString().trim { it <= ' ' }
            shop_name = shopname!!.getText().toString().trim { it <= ' ' }
            shop_add = shopaddress!!.getText().toString().trim { it <= ' ' }
            mob_num = mobilenumber!!.getText().toString().trim { it <= ' ' }
            another_mob_num = anothermobilenumber!!.getText().toString().trim { it <= ' ' }
            mail = mailid!!.getText().toString().trim { it <= ' ' }
            web = website!!.getText().toString().trim { it <= ' ' }
            shop_city = city!!.getText().toString().trim { it <= ' ' }
            shop_state = state!!.getText().toString().trim { it <= ' ' }
            shop_pincode = pincode!!.getText().toString().trim { it <= ' ' }
            country_text = testView!!.getTag().toString().trim { it <= ' ' }
            if (sell_name == "") {
                toast_center(applicationContext, "Please Enter Seller Name...")
            } else if (shop_name == "") {
                toast_center(applicationContext, "Please Enter Shop name...")
            } else if (mob_num == "") {
                toast_center(applicationContext, "Please Enter Correct Mobile Number...")
            } else if (mail == "") {
                toast_center(applicationContext, "Please Enter Your Email...")
            } else if (!mail!!.matches(emailPattern)) {
                Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_SHORT)
                    .show()
            } else if (country_text == "") {
                toast_center(applicationContext, "Choose your Country / Region...")
            } else if (shop_state == "") {
                toast_center(applicationContext, "Please Enter Your state...")
            } else if (shop_city == "") {
                toast_center(applicationContext, "Please Enter Your city...")
            } else if (shop_add == "") {
                toast_center(applicationContext, "Please Enter Your address...")
            } else if (shop_pincode == "") {
                toast_center(applicationContext, "Please Enter Your pin/postal code...")
            } else {
                if (isNetworkAvailable(this@ShopEdit)) {
                    submit_res()
                } else {
                    toast_normal(this@ShopEdit, "Please connect to your internet")
                }
            }
        })
    }

    fun image_pick() {
        mLauncher.launch(
            with(this@ShopEdit)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    fun image_pick_gal() {
        mLauncher.launch(
            with(this@ShopEdit)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    var mLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri_1 = result.data!!.data
            if (uri_1 != null) {
                IVPreviewImage!!.setImageURI(uri_1)
                try {
                    val file = getFile(this@ShopEdit, uri_1, "img.jpg")
                    path = file.path.replace(file.name, "")
                    file_array[0] = file
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun spin_country() {
        val map = HashMap<String, String>()
        map["action"] = "get_country"
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.country(map)
        call.enqueue(object : Callback<ArrayList<GetCountry>?> {
            override fun onResponse(
                call: Call<ArrayList<GetCountry>?>,
                response: Response<ArrayList<GetCountry>?>
            ) {
                if (response.isSuccessful) {
                    country_get!!.addAll(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<GetCountry>?>, t: Throwable) {
            }
        })
    }

    fun shopedit() {
        val map = HashMap<String, String?>()
        map["action"] = "get_id"
        map["id"] = sharedPreference.getString(this, "user_id")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.edit_gift(map)
        call.enqueue(object : Callback<ArrayList<GiftEdit>> {
            override fun onResponse(
                call: Call<ArrayList<GiftEdit>>,
                response: Response<ArrayList<GiftEdit>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        list_shop!!.addAll(response.body()!!)
                        Glide.with(applicationContext).load(list_shop!![0].logo)
                            .error(R.drawable.ic_gift_default_img)
                            .placeholder(R.drawable.ic_gift_default_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage!!)
                        sellername!!.setText(list_shop!![0].name)
                        shopname!!.setText(list_shop!![0].shopName)
                        shopaddress!!.setText(list_shop!![0].address)
                        mobilenumber!!.setText(list_shop!![0].sellerMobile)
                        anothermobilenumber!!.setText(list_shop!![0].sellerMobile2)
                        mailid!!.setText(list_shop!![0].shopEmail)
                        website!!.setText(list_shop!![0].shopWebsite)
                        city!!.setText(list_shop!![0].city)
                        state!!.setText(list_shop!![0].state)
                        latitude!!.setText(list_shop!![0].latitude)
                        longitude!!.setText(list_shop!![0].longitude)
                        pincode!!.setText(list_shop!![0].pincode)
                        country_id = list_shop!![0].country_name
                        testView!!.text = country_id
                        testView!!.tag = list_shop!![0].country
                        spin_country()
                    }
                    mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<GiftEdit>>, t: Throwable) {
            }
        })
    }


    fun submit_res() {
        map1.clear()
        map2.clear()
        map1["action"] = "add_seller"
        map1["user_id"] = sharedPreference.getString(applicationContext, "user_id")
        map1["shop_name"] = shop_name
        map1["seller_mobile"] = mob_num
        map1["seller_mobile2"] = another_mob_num
        map1["shop_email"] = mail
        map1["shop_website"] = web
        map1["name"] = sell_name
        map1["country"] = country_text
        map1["state"] = shop_state
        map1["address"] = shop_add
        map1["pincode"] = shop_pincode
        map1["city"] = shop_city
        try {
            var set_img = 0
            for (i in file_array.indices) {
                if (file_array[i] != null) {
                    map2["logo[$set_img]"] = "" + Uri.fromFile(file_array[i])
                    set_img++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        UploadAsync()
    }

    fun UploadAsync() {
        val progressDialog = ProgressDialog(this@ShopEdit)
        progressDialog.setMessage("Uploading... ")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val handler: Handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val runnable = Runnable {
                    if (applicationContext != null) {
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        if (msg.obj != null && msg.obj.toString().length != 0) {
                            val result = msg.obj.toString()
                            val jsonArray: JSONArray?
                            var jsonObject: JSONObject? = null
                            try {
                                jsonArray = JSONArray(result)
                                jsonObject = jsonArray.getJSONObject(0)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            try {
                                if (jsonObject!!.getString("status").contains("Success")) {
                                    IVPreviewImage!!.setImageResource(R.drawable.add)
                                    sellername!!.text!!.clear()
                                    shopname!!.text!!.clear()
                                    shopaddress!!.text!!.clear()
                                    mobilenumber!!.text!!.clear()
                                    anothermobilenumber!!.text!!.clear()
                                    mailid!!.text!!.clear()
                                    website!!.text!!.clear()
                                    city!!.text!!.clear()
                                    state!!.text!!.clear()
                                    latitude!!.text!!.clear()
                                    longitude!!.text!!.clear()
                                    pincode!!.text!!.clear()
                                    Toast.makeText(
                                        applicationContext,
                                        "Your shop Updated successfully, Thank you",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    sharedPreference.putInt(this@ShopEdit, "finish", 1)
                                    finish()
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        } else {
                            runOnUiThread {
                                try {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
                runOnUiThread(runnable)
            }
        }
        val checkUpdate: Thread = object : Thread() {
            override fun run() {
                var response: String? = null
                try {
                    val requestURL = "https://nithra.mobi/gift_app/api/data.php"
                    val path = "Images/"
                    val boundary: String
                    var tail = ""
                    val LINE_END = "\r\n"
                    val TWOHYPEN = "--"
                    val httpConn: HttpURLConnection
                    val charset = "UTF-8"
                    val writer: PrintWriter
                    val outputStream: OutputStream
                    var paramsPart = ""
                    var fileHeader = ""
                    var filePart = ""
                    var fileLength: Long = 0
                    val maxBufferSize = 1024
                    try {
                        boundary = "===" + System.currentTimeMillis() + "==="
                        tail = LINE_END + TWOHYPEN + boundary + TWOHYPEN + LINE_END
                        val url = URL(requestURL)
                        httpConn = url.openConnection() as HttpURLConnection
                        httpConn.doOutput = true
                        httpConn.doInput = true
                        httpConn.setRequestProperty(
                            "Content-Type",
                            "multipart/form-data; boundary=$boundary"
                        )
                        val paramHeaders = ArrayList<String>()
                        for ((key, value) in map1) {
                            val param = (TWOHYPEN + boundary + LINE_END
                                    + "Content-Disposition: form-data; name=\"" + key + "\"" + LINE_END
                                    + "Content-Type: text/plain; charset=" + charset + LINE_END
                                    + LINE_END
                                    + value + LINE_END)
                            paramsPart += param
                            paramHeaders.add(param)
                        }
                        val filesAL = ArrayList<File>()
                        val fileHeaders = ArrayList<String>()
                        try {
                            for ((key, value) in map2) {
                                val file_name = value.substring(value.lastIndexOf("/") + 1)
                                var file: File? = null
                                file = File(filesDir.path, path + file_name)
                                fileHeader = (TWOHYPEN + boundary + LINE_END
                                        + "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.name + "\"" + LINE_END
                                        + "Content-Type: " + URLConnection.guessContentTypeFromName(
                                    file.absolutePath
                                ) + LINE_END
                                        + "Content-Transfer-Encoding: binary" + LINE_END
                                        + LINE_END)
                                fileLength += file.length() + LINE_END.toByteArray(charset(charset)).size
                                filePart += fileHeader
                                fileHeaders.add(fileHeader)
                                filesAL.add(file)
                            }
                            val partData = paramsPart + filePart
                            val requestLength =
                                partData.toByteArray(charset(charset)).size + fileLength + tail.toByteArray(
                                    charset(charset)
                                ).size
                            httpConn.setRequestProperty("Content-length", "" + requestLength)
                            httpConn.setFixedLengthStreamingMode(requestLength.toInt())
                            httpConn.connect()
                            outputStream = BufferedOutputStream(httpConn.outputStream)
                            writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
                            for (i in paramHeaders.indices) {
                                writer.append(paramHeaders[i])
                                writer.flush()
                            }
                            var totalRead = 0
                            var bytesRead: Int
                            val buf = ByteArray(maxBufferSize)
                            for (i in filesAL.indices) {
                                writer.append(fileHeaders[i])
                                writer.flush()
                                val bufferedInputStream = BufferedInputStream(
                                    FileInputStream(
                                        filesAL[i]
                                    )
                                )
                                while (bufferedInputStream.read(buf)
                                        .also { bytesRead = it } != -1
                                ) {
                                    outputStream.write(buf, 0, bytesRead)
                                    writer.flush()
                                    totalRead += bytesRead
                                    runOnUiThread {
                                        if (progressDialog != null && progressDialog.isShowing) {
                                            progressDialog.setMessage("Loading...")
                                        }
                                    }
                                }
                                outputStream.write(LINE_END.toByteArray())
                                outputStream.flush()
                                bufferedInputStream.close()
                            }
                            writer.append(tail)
                            writer.flush()
                            writer.close()
                        } catch (_: Exception) {
                        }
                        var line: String? = null
                        val sb = StringBuilder()
                        try {
                            val status = httpConn.responseCode
                            if (status == HttpURLConnection.HTTP_OK) {
                                val reader = BufferedReader(
                                    InputStreamReader(httpConn.inputStream, "UTF-8"),
                                    8
                                )
                                while (reader.readLine().also { line = it } != null) {
                                    sb.append(line)
                                }
                                reader.close()
                                httpConn.disconnect()
                            } else {
                                throw IOException("Server returned non-OK status: " + status + " " + httpConn.responseMessage)
                            }
                        } catch (_: Exception) {
                        }
                        try {
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                        response = sb.toString()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } catch (_: Exception) {
                }
                val message = Message()
                message.obj = response
                handler.sendMessage(message)
            }
        }
        checkUpdate.start()
    }

    companion object {
        @Throws(IOException::class)
        fun getFile(context: Context, uri: Uri?, image: String): File {
            val root = context.filesDir.path + File.separatorChar + "Images"
            val folder = File(root)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val destinationFilename = File(root + File.separatorChar + image)
            try {
                context.contentResolver.openInputStream(uri!!).use { ins ->
                    createFileFromStream(
                        ins!!, destinationFilename
                    )
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
            return destinationFilename
        }

        fun createFileFromStream(ins: InputStream, destination: File?) {
            try {
                FileOutputStream(destination).use { os ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (ins.read(buffer).also { length = it } > 0) {
                        os.write(buffer, 0, length)
                    }
                    os.flush()
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
        }

    }
}