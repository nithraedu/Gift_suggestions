package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.*
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
import java.util.*

class ProductAdd : AppCompatActivity() {
    var productname: TextInputEditText? = null
    var prod_prize: TextInputEditText? = null
    var offer_prize: TextInputEditText? = null
    var offer_percentage: TextInputEditText? = null
    var prod_des: TextInputEditText? = null
    var save: TextView? = null
    var spin_occaction: Spinner? = null
    var spin_gender: Spinner? = null
    var myproduct: TextView? = null
    var IVPreviewImage: ImageView? = null
    var IVPreviewImage1: ImageView? = null
    var IVPreviewImage2: ImageView? = null
    var sharedPreference = SharedPreference()
    var gift_name: String? = null
    var gift_occasion: String? = null
    var gift_gender: String? = null
    var gift_for: String? = null
    var gift_amount: String? = null
    var discount: String? = null
    var total_amount: String? = null
    var gift_description: String? = null
    var spin: ArrayList<String>? = null
    var spin1: ArrayList<String>? = null
    var giftfor: ArrayList<GiftFor>? = null
    var occasion: ArrayList<Occasion>? = null
    var add_gift: ArrayList<AddGift>? = null
    var map1 = HashMap<String, String?>()
    var map2 = HashMap<String, String>()
    var path = ""
    var back: ImageView? = null
    var remove: ImageView? = null
    var remove1: ImageView? = null
    var remove2: ImageView? = null
    var file_array = arrayOfNulls<File>(3)
    var uri: Uri? = null
    var uri1: Uri? = null
    var uri2: Uri? = null
    var textView: TextView? = null
    var textView1: TextView? = null
    lateinit var selectedLanguage: BooleanArray
    lateinit var selectedLanguage1: BooleanArray
    lateinit var cat: Array<String?>
    lateinit var cat_id: Array<String?>
    lateinit var cat1: Array<String?>
    lateinit var cat_id1: Array<String?>
    var langList = ArrayList<Int>()
    var langList1 = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.fragment_product)
        productname = findViewById(R.id.productname)
        spin_occaction = findViewById(R.id.spin_occaction)
        spin_gender = findViewById(R.id.spin_gender)
        prod_prize = findViewById(R.id.prod_prize)
        offer_prize = findViewById(R.id.offer_prize)
        offer_percentage = findViewById(R.id.offer_percentage)
        prod_des = findViewById(R.id.prod_des)
        save = findViewById(R.id.save)
        myproduct = findViewById(R.id.myproduct)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        IVPreviewImage1 = findViewById(R.id.IVPreviewImage1)
        IVPreviewImage2 = findViewById(R.id.IVPreviewImage2)
        remove = findViewById(R.id.remove)
        remove1 = findViewById(R.id.remove1)
        remove2 = findViewById(R.id.remove2)
        spin = ArrayList()
        spin1 = ArrayList()
        giftfor = ArrayList()
        occasion = ArrayList()
        add_gift = ArrayList()
        back = findViewById(R.id.back)
        textView = findViewById(R.id.textView)
        textView1 = findViewById(R.id.textView1)
        textView!!.setText("")
        textView!!.setTag("")
        textView1!!.setText("")
        textView1!!.setTag("")
        back!!.setOnClickListener({ finish() })
        gender_gift()
        gift_occasion()
        remove!!.setVisibility(View.GONE)
        remove1!!.setVisibility(View.GONE)
        remove2!!.setVisibility(View.GONE)
        remove!!.setOnClickListener({
            IVPreviewImage!!.setImageResource(R.drawable.ic_image_upload)
            file_array[0] = null
            remove!!.setVisibility(View.GONE)
        })
        remove1!!.setOnClickListener({
            IVPreviewImage1!!.setImageResource(R.drawable.ic_image_upload)
            file_array[1] = null
            remove1!!.setVisibility(View.GONE)
        })
        remove2!!.setOnClickListener({
            IVPreviewImage2!!.setImageResource(R.drawable.ic_image_upload)
            file_array[2] = null
            remove2!!.setVisibility(View.GONE)
        })

        textView!!.setOnClickListener({
            if (Utils_Class.isNetworkAvailable(this@ProductAdd)) {
                for (i in occasion!!.indices) {
                    cat[i] = occasion!![i].category
                    cat_id[i] = occasion!![i].id
                }
                val builder = AlertDialog.Builder(this@ProductAdd)
                builder.setTitle("Select Occasion")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(cat, selectedLanguage) { dialogInterface, i, b ->
                    if (b) {
                        selectedLanguage[i] = true
                        langList.add(i)
                        Collections.sort(langList)
                    } else {
                        langList.remove(Integer.valueOf(i))
                    }
                }
                builder.setPositiveButton("OK") { dialogInterface, i ->
                    val stringBuilder = StringBuilder()
                    val id = StringBuilder()
                    for (j in langList.indices) {
                        stringBuilder.append(cat[langList[j]])
                        id.append(cat_id[langList[j]])
                        if (j != langList.size - 1) {
                            stringBuilder.append(",")
                            id.append(",")
                        }
                    }
                    textView!!.setText(stringBuilder.toString())
                    textView!!.setTag(id.toString())
                }
                builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.setNeutralButton("Clear All") { dialogInterface, i ->
                    for (j in selectedLanguage.indices) {
                        selectedLanguage[j] = false
                        langList.clear()
                        textView!!.setText("")
                        textView!!.setTag("")
                    }
                }
                builder.show()
            } else {
                Utils_Class.toast_normal(this@ProductAdd, "Please connect to your internet")
            }
        })
        textView1!!.setOnClickListener({
            if (Utils_Class.isNetworkAvailable(this@ProductAdd)) {
                for (i in giftfor!!.indices) {
                    cat1[i] = giftfor!![i].people
                    cat_id1[i] = giftfor!![i].id
                }
                val builder = AlertDialog.Builder(this@ProductAdd)
                builder.setTitle("Select Gift For")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(cat1, selectedLanguage1) { dialogInterface, i, b ->
                    if (b) {
                        selectedLanguage1[i] = true
                        langList1.add(i)
                        Collections.sort(langList1)
                    } else {
                        langList1.remove(Integer.valueOf(i))
                    }
                }
                builder.setPositiveButton("OK") { dialogInterface, i ->
                    val stringBuilder = StringBuilder()
                    val id = StringBuilder()
                    for (j in langList1.indices) {
                        stringBuilder.append(cat1[langList1[j]])
                        id.append(cat_id1[langList1[j]])
                        if (j != langList1.size - 1) {
                            stringBuilder.append(",")
                            id.append(",")
                        }
                    }
                    textView1!!.setText(stringBuilder.toString())
                    textView1!!.setTag(id.toString())
                }
                builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.setNeutralButton("Clear All") { dialogInterface, i ->
                    for (j in selectedLanguage1.indices) {
                        selectedLanguage1[j] = false
                        langList1.clear()
                        textView1!!.setText("")
                        textView1!!.setTag("")
                    }
                }
                builder.show()
            } else {
                Utils_Class.toast_normal(this@ProductAdd, "Please connect to your internet")
            }
        })
        save!!.setOnClickListener({
            gift_name = productname!!.getText().toString().trim { it <= ' ' }
            gift_occasion = textView!!.getTag().toString().trim { it <= ' ' }
            gift_gender = textView1!!.getTag().toString().trim { it <= ' ' }
            total_amount = prod_prize!!.getText().toString().trim { it <= ' ' }
            discount = offer_percentage!!.getText().toString().trim { it <= ' ' }
            gift_amount = offer_prize!!.getText().toString().trim { it <= ' ' }
            gift_description = prod_des!!.getText().toString().trim { it <= ' ' }
            if (gift_name == "") {
                Utils_Class.toast_center(this@ProductAdd, "Please Enter Gift Name...")
            } else if (gift_occasion == "") {
                Utils_Class.toast_center(this@ProductAdd, "Please select Occasion...")
            } else if (gift_gender == "") {
                Utils_Class.toast_center(this@ProductAdd, "Please select Gender...")
            } else if (file_array[0] == null && file_array[1] == null && file_array[2] == null) {
                Utils_Class.toast_center(this@ProductAdd, "Please set Gift image ...")
            } else if (gift_description == "") {
                Utils_Class.toast_center(this@ProductAdd, "Please Enter Gift Description...")
            } else {
                if (Utils_Class.isNetworkAvailable(this@ProductAdd)) {
                    submit_res()
                } else {
                    Utils_Class.toast_normal(this@ProductAdd, "Please connect to your internet")
                }
            }
        })
        IVPreviewImage!!.setOnClickListener({
            val dialog: Dialog
            dialog = Dialog(
                this@ProductAdd,
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
        IVPreviewImage1!!.setOnClickListener({ //choose_imge1();
            val dialog: Dialog
            dialog = Dialog(
                this@ProductAdd,
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
                image_pick1()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                image_pick1_gal()
                dialog.dismiss()
            }
        })
        IVPreviewImage2!!.setOnClickListener({ //choose_imge2();
            val dialog: Dialog
            dialog = Dialog(
                this@ProductAdd,
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
                image_pick2()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                image_pick2_gal()
                dialog.dismiss()
            }
        })
    }

    fun image_pick() {
        mLauncher.launch(
            with(this@ProductAdd)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    fun image_pick_gal() {
        mLauncher.launch(
            with(this@ProductAdd)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    fun image_pick1() {
        mLauncher1.launch(
            with(this@ProductAdd)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    fun image_pick1_gal() {
        mLauncher1.launch(
            with(this@ProductAdd)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    fun image_pick2() {
        mLauncher2.launch(
            with(this@ProductAdd)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    fun image_pick2_gal() {
        mLauncher2.launch(
            with(this@ProductAdd)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    var mLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri = result.data!!.data
            if (uri != null) {
                IVPreviewImage!!.setImageURI(uri)
                try {
                    val file = getFile(this@ProductAdd, uri, "img1.jpg")
                    path = file.path.replace(file.name, "")
                    file_array[0] = file
                    remove!!.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    var mLauncher1 = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri1 = result.data!!.data
            if (uri1 != null) {
                IVPreviewImage1!!.setImageURI(uri1)
                try {
                    val file = getFile(this@ProductAdd, uri1, "img2.jpg")
                    path = file.path.replace(file.name, "")
                    file_array[1] = file
                    remove1!!.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    var mLauncher2 = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri2 = result.data!!.data
            if (uri2 != null) {
                IVPreviewImage2!!.setImageURI(uri2)
                try {
                    val file = getFile(this@ProductAdd, uri2, "img3.jpg")
                    path = file.path.replace(file.name, "")
                    file_array[2] = file
                    remove2!!.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun submit_res() {
        map1.clear()
        map2.clear()
        map1["action"] = "add_gift"
        map1["user_id"] = sharedPreference.getString(this@ProductAdd, "user_id")
        map1["gift_category"] = gift_occasion
        map1["gift_for"] = gift_gender
        map1["gift_name"] = gift_name
        map1["gift_description"] = gift_description
        try {
            for (i in file_array.indices) {
                if (file_array[i] != null) {
                    map2["gift_image[$i]"] = "" + Uri.fromFile(file_array[i])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        UploadAsync()
    }

    fun gender_gift() {
        val map = HashMap<String, String>()
        map["action"] = "gift_for"
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_giftfor(map)
        call.enqueue(object : Callback<ArrayList<GiftFor>?> {
            override fun onResponse(
                call: Call<ArrayList<GiftFor>?>,
                response: Response<ArrayList<GiftFor>?>
            ) {
                if (response.isSuccessful) {
                    giftfor!!.addAll(response.body()!!)
                    cat1 = arrayOfNulls(giftfor!!.size)
                    cat_id1 = arrayOfNulls(giftfor!!.size)
                    selectedLanguage1 = BooleanArray(giftfor!!.size)
                }
            }

            override fun onFailure(call: Call<ArrayList<GiftFor>?>, t: Throwable) {
            }
        })
    }

    fun gift_occasion() {
        val map = HashMap<String, String?>()
        map["action"] = "category"
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_occasion(map)
        call.enqueue(object : Callback<ArrayList<Occasion>?> {
            override fun onResponse(
                call: Call<ArrayList<Occasion>?>,
                response: Response<ArrayList<Occasion>?>
            ) {
                if (response.isSuccessful) {
                    occasion!!.addAll(response.body()!!)
                    cat = arrayOfNulls(occasion!!.size)
                    cat_id = arrayOfNulls(occasion!!.size)
                    selectedLanguage = BooleanArray(occasion!!.size)
                }
            }

            override fun onFailure(call: Call<ArrayList<Occasion>?>, t: Throwable) {
            }
        })
    }

    fun UploadAsync() {
        val progressDialog = ProgressDialog(this@ProductAdd)
        progressDialog.setMessage("Uploading... ")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val handler: Handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val runnable = Runnable {
                    if (this@ProductAdd != null) {
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        if (msg.obj != null && msg.obj.toString().length != 0) {
                            val result = msg.obj.toString()
                            var jsonArray: JSONArray?
                            var jsonObject: JSONObject? = null
                            try {
                                jsonArray = JSONArray(result)
                                jsonObject = jsonArray.getJSONObject(0)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            try {
                                if (jsonObject!!.getString("status").contains("Success")) {
                                    spin_occaction!!.setSelection(0)
                                    spin_gender!!.setSelection(0)
                                    productname!!.text!!.clear()
                                    prod_prize!!.text!!.clear()
                                    offer_percentage!!.text!!.clear()
                                    offer_prize!!.text!!.clear()
                                    prod_des!!.text!!.clear()
                                    sharedPreference.putString(
                                        this@ProductAdd,
                                        "gift_id",
                                        "" + jsonObject.getString("id")
                                    )
                                    sharedPreference.putInt(this@ProductAdd, "product_add", 1)
                                    Toast.makeText(
                                        this@ProductAdd,
                                        "Your product added successfully, Thank you",
                                        Toast.LENGTH_SHORT
                                    ).show()
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