package com.pr7.kotlin_room_save_image

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pr7.kotlin_room_save_image.Constants.TABLE_NAME
import com.pr7.kotlin_room_save_image.databinding.ActivityMainBinding
import com.pr7.kotlin_room_save_image.room.AppDatabase
import com.pr7.kotlin_room_save_image.room.User
import com.pr7.kotlin_room_save_image.room.UserDao
import com.pr7.kotlin_room_save_image.ui.UserAdapter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var imageuri:Uri?=null
    lateinit var userDao: UserDao
    lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "$TABLE_NAME"
        ).allowMainThreadQueries().build()
        userDao=db.userDao()

        binding.imageviewfromgallery.setOnClickListener {
            openFileChooser()
        }

        binding.apply {
            buttonsave.setOnClickListener {
                val user=User(
                    uid = 0,
                    name = "${edittextimagename.text.toString()}",
                    image =bitmapconverttoBytArray(uriconverttoBitmap(imageuri!!))
                )
                savetodatabase(user)
                readfromdatabase()

            }

        }

        readfromdatabase()
        coilimagelibrary()





    }

    fun readfromdatabase(){
        val userAdapter=UserAdapter(this@MainActivity,userDao.getAllUsers() as ArrayList<User>)
        binding.apply {
            recyclerview1.layoutManager=GridLayoutManager(this@MainActivity,4)
            recyclerview1.adapter=userAdapter
        }
    }

    fun savetodatabase(user: User){
        userDao.insertUser(user)
    }

    fun openFileChooser() {
        getContent.launch("image/*")
    }


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent())  { uri: Uri? ->
        binding.imageviewfromgallery.setImageURI(uri)
        imageuri=uri
    }

    fun uriconverttoBitmap(uri: Uri):Bitmap{
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(this@MainActivity.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(this@MainActivity.contentResolver, uri)
        }
        return bitmap
    }

    fun bitmapconverttoBytArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun byteArrayconverttoBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))

        //string to uri
        //Uri myUri = Uri.parse("http://stackoverflow.com")
    }

    fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            this@MainActivity.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }


    fun coilimagelibrary(){
        //load 1
//        binding.imageviewfromgallery.load("https://rus-traktor.ru/upload/iblock/f74/f74f39dbc9b60954c926d72401adf1cc.jpg"){
//            crossfade(true)
//            crossfade(1000)
//            transformations(CircleCropTransformation())
//            transformations(RoundedCornersTransformation(8f))
//
//        }


        //load 2
//        val imageLoader= ImageLoader(this@MainActivity)
//        val request = ImageRequest.Builder(this@MainActivity)
//            .data("https://rus-traktor.ru/upload/iblock/f74/f74f39dbc9b60954c926d72401adf1cc.jpg")
//            .target(binding.imageviewfromgallery)
//            .listener(
//                onStart = {
//                    binding.progressbar1.visibility=View.VISIBLE
//                },
//                onSuccess = { request, metadata ->
//                    binding.progressbar1.visibility=View.INVISIBLE
//
//                }
//            )
//            .build()
//        imageLoader.enqueue(request)

        //converttobitmap


//        ImageView.load calls can be configured with an optional trailing lambda parameter:
//
//        imageView.load("https://www.example.com/image.jpg") {
//            crossfade(true)
//            placeholder(R.drawable.image)
//            transformations(CircleCropTransformation())
//        }


        //use glide

        Glide.with(this@MainActivity)
            .load("https://dizainsantehnika.ru/upload/no_photo.png")
            .listener(object :RequestListener<Drawable>{
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressbar1.visibility=View.GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false

                }

            })
            //.dontAnimate()
            .into(binding.imageviewfromgallery)
    }


}