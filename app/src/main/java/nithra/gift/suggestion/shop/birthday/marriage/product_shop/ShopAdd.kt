package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.Dialog
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
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GetCountry
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
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

class ShopAdd : AppCompatActivity() {
    var sellername: TextInputEditText? = null
    var shopname: TextInputEditText? = null
    var shopaddress: TextInputEditText? = null
    var mobilenumber: TextInputEditText? = null
    var city: TextInputEditText? = null
    var state: TextInputEditText? = null
    var country: TextInputEditText? = null
    var latitude: TextInputEditText? = null
    var longitude: TextInputEditText? = null
    var pincode: TextInputEditText? = null
    var district: TextInputEditText? = null
    var mailid: TextInputEditText? = null
    var website: TextInputEditText? = null
    var anothermobilenumber: TextInputEditText? = null
    var save: TextView? = null
    var remove: ImageView? = null
    private var editImg: ImageView? = null
    private var sellName: String? = null
    var shopName: String? = null
    private var shopAdd: String? = null
    private var mobNum: String? = null
    private var anotherMobNum: String? = null
    private var shopCity: String? = null
    private var shopCountry: String? = null
    private var shopState: String? = null
    private var shopPincode: String? = null
    private var mail: String? = null
    private var web: String? = null
    private var countryText: String? = null
    var IVPreviewImage: ImageView? = null
    var sharedPreference = SharedPreference()
    var back: ImageView? = null
    private var uri1: Uri? = null
    var map1 = HashMap<String, String?>()
    var map2 = HashMap<String, String>()
    var path = ""
    private var spinCountry: Spinner? = null
    var countryGet: ArrayList<GetCountry>? = null
    private var spin: ArrayList<String>? = null
    private var fileArray = arrayOfNulls<File>(1)
    private var testView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add)
        sellername = findViewById(R.id.sellername)
        shopname = findViewById(R.id.shopname)
        shopaddress = findViewById(R.id.shopaddress)
        mobilenumber = findViewById(R.id.mobilenumber)
        city = findViewById(R.id.city)
        state = findViewById(R.id.state)
        country = findViewById(R.id.country)
        save = findViewById(R.id.save)
        remove = findViewById(R.id.remove)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        pincode = findViewById(R.id.pincode)
        pincode = findViewById(R.id.pincode)
        district = findViewById(R.id.district)
        back = findViewById(R.id.back)
        mailid = findViewById(R.id.mailid)
        website = findViewById(R.id.website)
        spinCountry = findViewById(R.id.spin_country)
        editImg = findViewById(R.id.edit_img)
        anothermobilenumber = findViewById(R.id.anothermobilenumber)
        countryGet = ArrayList()
        spin = ArrayList()
        testView = findViewById(R.id.testView)
        testView!!.text = ""
        testView!!.tag = ""
        testView!!.setOnClickListener {
            val dialog = Dialog(this@ShopAdd)
            dialog.setContentView(R.layout.searchable_spinner)
            dialog.window!!.setLayout(650, 800)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.edit_text)
            val listView = dialog.findViewById<ListView>(R.id.list_view)
            for (i in countryGet!!.indices) {
                countryGet!![i].name?.let { it1 -> spin!!.add(it1) }
            }
            val adapter = ArrayAdapter(this@ShopAdd, android.R.layout.simple_list_item_1, spin!!)
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
            listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                testView!!.text = adapter.getItem(position)
                testView!!.tag = countryGet!![position].id
                dialog.dismiss()
            }
        }
        val emailPattern: Regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)".toRegex()

        mailid!!.setText(sharedPreference.getString(this@ShopAdd, "user_mail"))
        back!!.setOnClickListener { finish() }
        spinCountry()
        IVPreviewImage!!.setOnClickListener {
            val dialog = Dialog(
                this@ShopAdd,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView = dialog.findViewById(R.id.camera)
            val gallery: ImageView = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                imagePick()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                imagePickGal()
                dialog.dismiss()
            }
        }
        editImg!!.setOnClickListener {
            val dialog = Dialog(
                this@ShopAdd,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView = dialog.findViewById(R.id.camera)
            val gallery: ImageView = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                imagePick()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                imagePickGal()
                dialog.dismiss()
            }
        }
        save!!.setOnClickListener {
            sellName = sellername!!.text.toString().trim { it <= ' ' }
            shopName = shopname!!.text.toString().trim { it <= ' ' }
            shopAdd = shopaddress!!.text.toString().trim { it <= ' ' }
            mobNum = mobilenumber!!.text.toString().trim { it <= ' ' }
            anotherMobNum = anothermobilenumber!!.text.toString().trim { it <= ' ' }
            mail = mailid!!.text.toString().trim { it <= ' ' }
            web = website!!.text.toString().trim { it <= ' ' }
            shopCity = city!!.text.toString().trim { it <= ' ' }
            shopState = state!!.text.toString().trim { it <= ' ' }
            shopCountry = country!!.text.toString().trim { it <= ' ' }
            shopPincode = pincode!!.text.toString().trim { it <= ' ' }
            countryText = testView!!.tag.toString().trim { it <= ' ' }
            if (sellName == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Seller Name...")
            } else if (shopName == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Shop name...")
            } else if (mobNum == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Correct Mobile Number...")
            } else if (mail == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Your Email...")
            } else if (!mail!!.matches(emailPattern)) {
                Toast.makeText(this@ShopAdd, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else if (countryText == "") {
                Utils_Class.toast_center(this@ShopAdd, "Choose your Country / Region...")
            } else if (shopState == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Your state...")
            } else if (shopCity == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Your city...")
            } else if (shopAdd == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Your address...")
            } else if (shopPincode == "") {
                Utils_Class.toast_center(this@ShopAdd, "Please Enter Your pin/postal code...")
            } else {
                if (Utils_Class.isNetworkAvailable(this@ShopAdd)) {
                    submitRes()
                } else {
                    Utils_Class.toast_normal(this@ShopAdd, "Please connect to your internet")
                }
            }
        }
    }

    private fun imagePick() {
        mLauncher.launch(
            with(this@ShopAdd)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    private fun imagePickGal() {
        mLauncher.launch(
            with(this@ShopAdd)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    private var mLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri1 = result.data!!.data
            if (uri1 != null) {
                IVPreviewImage!!.setImageURI(uri1)
                try {
                    val file = getFile(this@ShopAdd, uri1, "img.jpg")
                    path = file.path.replace(file.name, "")
                    fileArray[0] = file
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun spinCountry() {
        val map = HashMap<String, String>()
        map["action"] = "get_country"
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.country(map)
        call.enqueue(object : Callback<ArrayList<GetCountry>?> {
            override fun onResponse(
                call: Call<ArrayList<GetCountry>?>,
                response: Response<ArrayList<GetCountry>?>
            ) {
                if (response.isSuccessful) {
                    countryGet!!.addAll(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<GetCountry>?>, t: Throwable) {
            }
        })
    }

    private fun submitRes() {
        map1.clear()
        map2.clear()
        map1["action"] = "add_seller"
        map1["user_id"] = sharedPreference.getString(this@ShopAdd, "user_id")
        map1["shop_name"] = shopName
        map1["seller_mobile"] = mobNum
        map1["seller_mobile2"] = anotherMobNum
        map1["shop_email"] = mail
        map1["shop_website"] = web
        map1["name"] = sellName
        map1["country"] = countryText
        map1["state"] = shopState
        map1["address"] = shopAdd
        map1["pincode"] = shopPincode
        try {
            for (i in fileArray.indices) {
                if (fileArray[i] != null) {
                    map2["logo[$i]"] = "" + Uri.fromFile(fileArray[i])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        uploadAsync()
    }

    private fun uploadAsync() {
        val dialog: Dialog?
        dialog = Dialog(
            this,
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
        )
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val load: TextView = dialog.findViewById(R.id.loading)
        load.text = "Uploading..."
        dialog.show()
        val handler: Handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val runnable = Runnable {
                    //post execute
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    if (msg.obj != null && msg.obj.toString().isNotEmpty()) {
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
                                country!!.text!!.clear()
                                pincode!!.text!!.clear()
                                sharedPreference.putInt(this@ShopAdd, "yes", 1)
                                sharedPreference.putInt(this@ShopAdd, "profile", 2)
                                Toast.makeText(
                                    this@ShopAdd,
                                    "Your shop added successfully, Thank you",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val i =
                                    Intent(this@ShopAdd, SellerProfileProductList::class.java)
                                startActivity(i)
                                finish()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        runOnUiThread {
                            try {
                                if (dialog.isShowing) {
                                    dialog.dismiss()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
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
                    val lINEEND = "\r\n"
                    val tWOHYPEN = "--"
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
                        tail = lINEEND + tWOHYPEN + boundary + tWOHYPEN + lINEEND
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
                            val param = (tWOHYPEN + boundary + lINEEND
                                    + "Content-Disposition: form-data; name=\"" + key + "\"" + lINEEND
                                    + "Content-Type: text/plain; charset=" + charset + lINEEND
                                    + lINEEND
                                    + value + lINEEND)
                            paramsPart += param
                            paramHeaders.add(param)
                        }
                        val filesAL = ArrayList<File>()
                        val fileHeaders = ArrayList<String>()
                        try {
                            for ((key, value) in map2) {
                                val fileName = value.substring(value.lastIndexOf("/") + 1)
                                var file: File? = null
                                file = File(filesDir.path, path + fileName)
                                fileHeader = (tWOHYPEN + boundary + lINEEND
                                        + "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.name + "\"" + lINEEND
                                        + "Content-Type: " + URLConnection.guessContentTypeFromName(
                                    file.absolutePath
                                ) + lINEEND
                                        + "Content-Transfer-Encoding: binary" + lINEEND
                                        + lINEEND)
                                fileLength += file.length() + lINEEND.toByteArray(charset(charset)).size
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
                                        if (dialog.isShowing) {
                                            load.text = "Loading..."
                                        }
                                    }
                                }
                                outputStream.write(lINEEND.toByteArray())
                                outputStream.flush()
                                bufferedInputStream.close()
                            }
                            writer.append(tail)
                            writer.flush()
                            writer.close()
                        } catch (_: Exception) {
                        }
                        var line: String?
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

        private fun createFileFromStream(ins: InputStream, destination: File?) {
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