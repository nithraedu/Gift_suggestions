package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputEditText
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GetGift
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GiftFor
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Occasion
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient.retrofit
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

class ProductEdit : AppCompatActivity() {
    var productname: TextInputEditText? = null
    var prodPrize: TextInputEditText? = null
    var offerPrize: TextInputEditText? = null
    var offerPercentage: TextInputEditText? = null
    var prodDes: TextInputEditText? = null
    var save: TextView? = null
    private var spinOccaction: Spinner? = null
    private var spinGender: Spinner? = null
    private var myproduct: TextView? = null
    var IVPreviewImage: ImageView? = null
    var IVPreviewImage1: ImageView? = null
    var IVPreviewImage2: ImageView? = null
    var sharedPreference = SharedPreference()
    var giftName: String? = null
    private var giftOccasion: String? = null
    private var giftGender: String? = null
    var giftAmount: String? = null
    var discount: String? = null
    var totalAmount: String? = null
    private var giftDescription: String? = null
    private var spin: ArrayList<String?>? = null
    private var spin1: ArrayList<String?>? = null
    var giftfor: ArrayList<GiftFor>? = null
    var occasion: ArrayList<Occasion>? = null
    var listGift: ArrayList<GetGift>? = null
    var map1 = HashMap<String, String?>()
    var map2 = HashMap<String, String>()
    var path = ""
    private var intent1: Intent? = null
    var extra: Bundle? = null
    private var idGift: String? = null
    var back: ImageView? = null
    var remove: ImageView? = null
    var remove1: ImageView? = null
    var remove2: ImageView? = null
    var textView: TextView? = null
    var textView1: TextView? = null
    lateinit var selectedLanguage: BooleanArray
    lateinit var selectedLanguage1: BooleanArray
    lateinit var cat: Array<String?>
    lateinit var catId: Array<String?>
    lateinit var cat1: Array<String?>
    lateinit var catId1: Array<String?>
    var langList = ArrayList<Int>()
    var langList1 = ArrayList<Int>()
    private var fileArray = arrayOfNulls<File>(3)
    var uri: Uri? = null
    private var uri1: Uri? = null
    private var uri2: Uri? = null
    lateinit var separated: Array<String?>
    private var imageChange = ""
    private var img1: Int? = null
    private var img2: Int? = null
    private var img3: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_edit)
        productname = findViewById(R.id.productname)
        spinOccaction = findViewById(R.id.spin_occaction)
        spinGender = findViewById(R.id.spin_gender)
        prodPrize = findViewById(R.id.prod_prize)
        offerPrize = findViewById(R.id.offer_prize)
        offerPercentage = findViewById(R.id.offer_percentage)
        prodDes = findViewById(R.id.prod_des)
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
        back = findViewById(R.id.back)
        giftfor = ArrayList()
        listGift = ArrayList()
        occasion = ArrayList()
        textView = findViewById(R.id.textView)
        textView1 = findViewById(R.id.textView1)
        intent1 = intent
        extra = intent1!!.extras
        idGift = extra!!.getString("id")
        textView!!.text = ""
        textView!!.tag = ""
        textView1!!.text = ""
        textView1!!.tag = ""
        Utils_Class.mProgress(this@ProductEdit)!!.show()
        giftedit()
        remove!!.visibility = View.GONE
        remove1!!.visibility = View.GONE
        remove2!!.visibility = View.GONE
        remove!!.setOnClickListener {
            IVPreviewImage!!.setImageResource(R.drawable.ic_image_upload)
            fileArray[0] = null
            img1 = 0
            remove!!.visibility = View.GONE
            if (!imageChange.contains(separated[0]!!)) {
                imageChange += separated[0] + ","
            }
        }
        remove1!!.setOnClickListener {
            IVPreviewImage1!!.setImageResource(R.drawable.ic_image_upload)
            fileArray[1] = null
            img2 = 0
            remove1!!.visibility = View.GONE
            if (separated.size > 1) {
                if (!imageChange.contains(separated[1]!!)) {
                    imageChange += separated[1] + ","
                }
            }
        }
        remove2!!.setOnClickListener {
            IVPreviewImage2!!.setImageResource(R.drawable.ic_image_upload)
            fileArray[2] = null
            img3 = 0
            remove2!!.visibility = View.GONE
            if (separated.size > 2) {
                if (!imageChange.contains(separated[2]!!)) {
                    imageChange += separated[2] + ","
                }
            }
        }
        textView!!.setOnClickListener {
            if (Utils_Class.isNetworkAvailable(this@ProductEdit)) {
                for (i in occasion!!.indices) {
                    cat[i] = occasion!![i].category
                    catId[i] = occasion!![i].id
                }
                val builder = AlertDialog.Builder(this@ProductEdit)
                builder.setTitle("Select Occasion")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(cat, selectedLanguage) { _, i, b ->
                    if (b) {
                        selectedLanguage[i] = true
                        langList.add(i)
                        langList.sort()
                    } else {
                        langList.remove(Integer.valueOf(i))
                    }
                }
                builder.setPositiveButton("OK") { _, _ ->
                    val stringBuilder = StringBuilder()
                    val id = StringBuilder()
                    for (j in langList.indices) {
                        stringBuilder.append(cat[langList[j]])
                        id.append(catId[langList[j]])
                        if (j != langList.size - 1) {
                            stringBuilder.append(",")
                            id.append(",")
                        }
                    }
                    textView!!.text = stringBuilder.toString()
                    textView!!.tag = id.toString()
                }
                builder.setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                builder.setNeutralButton("Clear All") { _, _ ->
                    for (j in selectedLanguage.indices) {
                        selectedLanguage[j] = false
                        langList.clear()
                        textView!!.text = ""
                        textView!!.tag = ""
                    }
                }
                builder.show()
            } else {
                Utils_Class.toast_normal(this@ProductEdit, "Please connect to your internet")
            }
        }
        textView1!!.setOnClickListener {
            if (Utils_Class.isNetworkAvailable(this@ProductEdit)) {
                for (i in giftfor!!.indices) {
                    cat1[i] = giftfor!![i].people
                    catId1[i] = giftfor!![i].id
                }
                val builder = AlertDialog.Builder(this@ProductEdit)
                builder.setTitle("Select Gift For")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(cat1, selectedLanguage1) { _, i, b ->
                    if (b) {
                        selectedLanguage1[i] = true
                        langList1.add(i)
                        langList1.sort()
                    } else {
                        langList1.remove(Integer.valueOf(i))
                    }
                }
                builder.setPositiveButton("OK") { _, _ ->
                    val stringBuilder = StringBuilder()
                    val id = StringBuilder()
                    for (j in langList1.indices) {
                        stringBuilder.append(cat1[langList1[j]])
                        id.append(catId1[langList1[j]])
                        if (j != langList1.size - 1) {
                            stringBuilder.append(",")
                            id.append(",")
                        }
                    }
                    textView1!!.text = stringBuilder.toString()
                    textView1!!.tag = id.toString()
                }
                builder.setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                builder.setNeutralButton("Clear All") { _, _ ->
                    for (j in selectedLanguage1.indices) {
                        selectedLanguage1[j] = false
                        langList1.clear()
                        textView1!!.text = ""
                        textView1!!.tag = ""
                    }
                }
                builder.show()
            } else {
                Utils_Class.toast_normal(this@ProductEdit, "Please connect to your internet")
            }
        }
        back!!.setOnClickListener { finish() }
        save!!.setOnClickListener {
            giftName = productname!!.text.toString().trim { it <= ' ' }
            giftOccasion = textView!!.tag.toString().trim { it <= ' ' }
            giftGender = textView1!!.tag.toString().trim { it <= ' ' }.replace("\r\n", "")
            totalAmount = prodPrize!!.text.toString().trim { it <= ' ' }
            discount = offerPercentage!!.text.toString().trim { it <= ' ' }
            giftAmount = offerPrize!!.text.toString().trim { it <= ' ' }
            giftDescription = prodDes!!.text.toString().trim { it <= ' ' }
            if (giftName == "") {
                Utils_Class.toast_center(this@ProductEdit, "Please Enter Gift Name...")
            } else if (giftOccasion == "") {
                Utils_Class.toast_center(this@ProductEdit, "Please select Occasion...")
            } else if (giftGender == "") {
                Utils_Class.toast_center(this@ProductEdit, "Please select Gender...")
            } else if (img1 == 0 || img2 == 0 || img3 == 0) {
                Utils_Class.toast_center(this@ProductEdit, "Please set Gift image ...")
            } else if (giftDescription == "") {
                Utils_Class.toast_center(this@ProductEdit, "Please Enter Gift Description...")
            } else {
                if (Utils_Class.isNetworkAvailable(this@ProductEdit)) {
                    submitRes()
                } else {
                    Utils_Class.toast_normal(this@ProductEdit, "Please connect to your internet")
                }
            }
        }
        IVPreviewImage!!.setOnClickListener {
            val dialog = Dialog(
                this@ProductEdit,
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
        IVPreviewImage1!!.setOnClickListener {
            val dialog = Dialog(
                this@ProductEdit,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView = dialog.findViewById(R.id.camera)
            val gallery: ImageView = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                imagePick1()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                imagePick1Gal()
                dialog.dismiss()
            }
        }
        IVPreviewImage2!!.setOnClickListener {
            val dialog: Dialog = Dialog(
                this@ProductEdit,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.cam_gallery)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val camera: ImageView = dialog.findViewById(R.id.camera)
            val gallery: ImageView = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                imagePick2()
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                imagePick2Gal()
                dialog.dismiss()
            }
        }
    }

    private fun imagePick() {
        mLauncher.launch(
            with(this@ProductEdit)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    private fun imagePickGal() {
        mLauncher.launch(
            with(this@ProductEdit)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    private fun imagePick1() {
        mLauncher1.launch(
            with(this@ProductEdit)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    private fun imagePick1Gal() {
        mLauncher1.launch(
            with(this@ProductEdit)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    private fun imagePick2() {
        mLauncher2.launch(
            with(this@ProductEdit)
                .crop()
                .cameraOnly()
                .createIntent()
        )
    }

    private fun imagePick2Gal() {
        mLauncher2.launch(
            with(this@ProductEdit)
                .crop()
                .galleryOnly()
                .createIntent()
        )
    }

    private var mLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri = result.data!!.data
            if (uri != null) {
                IVPreviewImage!!.setImageURI(uri)
                if (!imageChange.contains(separated[0]!!)) {
                    imageChange += separated[0] + ","
                }
                try {
                    val file = getFile(this@ProductEdit, uri, "img1.jpg")
                    path = file.path.replace(file.name, "")
                    fileArray[0] = file
                    remove!!.visibility = View.VISIBLE
                    img1 = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private var mLauncher1 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri1 = result.data!!.data
            if (uri1 != null) {
                IVPreviewImage1!!.setImageURI(uri1)
                if (separated.size > 1) {
                    if (!imageChange.contains(separated[1]!!)) {
                        imageChange += separated[1] + ","
                    }
                }
                try {
                    val file = getFile(this@ProductEdit, uri1, "img2.jpg")
                    path = file.path.replace(file.name, "")
                    fileArray[1] = file
                    remove1!!.visibility = View.VISIBLE
                    img2 = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private var mLauncher2 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            uri2 = result.data!!.data
            if (uri2 != null) {
                IVPreviewImage2!!.setImageURI(uri2)
                if (separated.size > 2) {
                    if (!imageChange.contains(separated[2]!!)) {
                        imageChange += separated[2] + ","
                    }
                }
                try {
                    val file = getFile(this@ProductEdit, uri2, "img3.jpg")
                    path = file.path.replace(file.name, "")
                    fileArray[2] = file
                    remove2!!.visibility = View.VISIBLE
                    img3 = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun submitRes() {
        map1.clear()
        map2.clear()
        map1["action"] = "add_gift"
        map1["user_id"] = sharedPreference.getString(applicationContext, "user_id")
        map1["gift_category"] = giftOccasion
        map1["gift_for"] = giftGender
        map1["gift_name"] = giftName
        map1["id"] = idGift
        map1["gift_description"] = giftDescription
        map1["img_delete"] = imageChange

        try {
            var setImg = 0
            for (i in fileArray.indices) {
                if (fileArray[i] != null) {
                    map2["gift_image[$setImg]"] = "" + Uri.fromFile(fileArray[i])
                    setImg++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        uploadAsync()
    }

    private fun giftedit() {
        val map = HashMap<String, String?>()
        map["action"] = "get_gift"
        map["id"] = idGift
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.getgift(map)
        call.enqueue(object : Callback<ArrayList<GetGift>> {
            override fun onResponse(
                call: Call<ArrayList<GetGift>>,
                response: Response<ArrayList<GetGift>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        listGift!!.addAll(response.body()!!)
                        genderGift()
                        giftOccasion()
                        val currentString = listGift!![0].giftImage
                        separated =
                            currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        if (separated.isNotEmpty()) {
                            Glide.with(applicationContext).load(separated[0])
                                .error(R.drawable.ic_gift_default_img)
                                .placeholder(R.drawable.ic_gift_default_img)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(IVPreviewImage!!)
                            remove!!.visibility = View.VISIBLE
                        }
                        if (separated.size > 1) {
                            if (separated[1] != null) {
                                Glide.with(applicationContext).load(separated[1])
                                    .error(R.drawable.ic_gift_default_img)
                                    .placeholder(R.drawable.ic_gift_default_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVPreviewImage1!!)
                                remove1!!.visibility = View.VISIBLE
                            }
                        }
                        if (separated.size > 2) {
                            if (separated[2] != null) {
                                Glide.with(applicationContext).load(separated[2])
                                    .error(R.drawable.ic_gift_default_img)
                                    .placeholder(R.drawable.ic_gift_default_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVPreviewImage2!!)
                                remove2!!.visibility = View.VISIBLE
                            }
                        }
                        productname!!.setText(listGift!![0].giftName)
                        prodPrize!!.setText(listGift!![0].totalAmount)
                        offerPrize!!.setText(listGift!![0].giftAmount)
                        offerPercentage!!.setText(listGift!![0].discount)
                        prodDes!!.setText(listGift!![0].giftDescription)
                        textView!!.text = listGift!![0].giftCat
                        textView1!!.text = listGift!![0].giftForPeople
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<GetGift>>, t: Throwable) {
            }
        })
    }

    fun genderGift() {
        val map = HashMap<String, String>()
        map["action"] = "gift_for"
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.gift_giftfor(map)
        call.enqueue(object : Callback<ArrayList<GiftFor>?> {
            override fun onResponse(
                call: Call<ArrayList<GiftFor>?>,
                response: Response<ArrayList<GiftFor>?>
            ) {
                if (response.isSuccessful) {
                    giftfor!!.addAll(response.body()!!)
                    cat1 = arrayOfNulls(giftfor!!.size)
                    catId1 = arrayOfNulls(giftfor!!.size)
                    selectedLanguage1 = BooleanArray(giftfor!!.size)
                    val temp = listGift!![0].giftFor!!.replace("\r\n", "").split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (i in giftfor!!.indices) {
                        for (j in temp.indices) {
                            if (giftfor!![i].id == temp[j]) {
                                selectedLanguage1[i] = true
                                cat1[i] = giftfor!![i].people
                                catId1[i] = giftfor!![i].id
                                langList1.add(i)
                                break
                            } else {
                                selectedLanguage1[i] = false
                            }
                        }
                    }
                    textView1!!.text = listGift!![0].giftForPeople
                    textView1!!.tag = listGift!![0].giftFor
                }
            }

            override fun onFailure(call: Call<ArrayList<GiftFor>?>, t: Throwable) {
            }
        })
    }

    fun giftOccasion() {
        val map = HashMap<String, String?>()
        map["action"] = "category"
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.gift_occasion(map)
        call.enqueue(object : Callback<ArrayList<Occasion>?> {
            override fun onResponse(
                call: Call<ArrayList<Occasion>?>,
                response: Response<ArrayList<Occasion>?>
            ) {
                if (response.isSuccessful) {
                    occasion!!.clear()
                    occasion!!.addAll(response.body()!!)
                    cat = arrayOfNulls(occasion!!.size)
                    catId = arrayOfNulls(occasion!!.size)
                    selectedLanguage = BooleanArray(occasion!!.size)
                    val temp = listGift!![0].giftCategory!!.split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (i in occasion!!.indices) {
                        for (j in temp.indices) {
                            if (occasion!![i].id == temp[j]) {
                                selectedLanguage[i] = true
                                cat[i] = occasion!![i].category
                                catId[i] = occasion!![i].id
                                langList.add(i)
                                break
                            } else {
                                selectedLanguage[i] = false
                            }
                        }
                    }
                    textView!!.text = listGift!![0].giftCat
                    textView!!.tag = listGift!![0].giftCategory
                }
            }

            override fun onFailure(call: Call<ArrayList<Occasion>?>, t: Throwable) {
            }
        })
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
                    if (applicationContext != null) {
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
                                    //IVPreviewImage.setImageResource(R.drawable.gallery);
                                    textView!!.text = ""
                                    textView!!.tag = ""
                                    textView1!!.text = ""
                                    textView1!!.tag = ""
                                    productname!!.text!!.clear()
                                    prodPrize!!.text!!.clear()
                                    offerPercentage!!.text!!.clear()
                                    offerPrize!!.text!!.clear()
                                    prodDes!!.text!!.clear()
                                    Toast.makeText(
                                        applicationContext,
                                        "Your product Updated successfully, Thank you",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    SellerProfileProductList.adapter!!.notifyDataSetChanged()
                                    sharedPreference.putInt(
                                        this@ProductEdit,
                                        "finish_product",
                                        1
                                    )
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
                                var file: File?
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

    override fun onBackPressed() {
        finish()
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