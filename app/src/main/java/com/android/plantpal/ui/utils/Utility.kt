package com.android.plantpal.ui.utils


import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog.Builder
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.android.plantpal.BuildConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )

}

fun calculateTimeDifference(updatedAt: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val updatedTime: Date = dateFormat.parse(updatedAt) ?: return "Unknown"

    val now = Date()

    // Calculate the difference in milliseconds
    val diffInMillis = now.time - updatedTime.time

    val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    // Return the appropriate time difference
    return when {
        diffInDays >= 365 -> {
            val years = diffInDays / 365
            "$years tahun"
        }
        diffInDays >= 30 -> {
            val months = diffInDays / 30
            "$months bulan"
        }
        diffInDays >= 1 -> {
            "$diffInDays hari"
        }
        diffInHours >= 1 -> {
            "$diffInHours jam"
        }
        diffInMinutes >= 1 -> {
            "$diffInMinutes menit"
        }
        else -> {
            "$diffInSeconds detik"
        }
    }
}

fun formatToLocalDateTime(dateStr: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = inputFormat.parse(dateStr)

    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getDefault()

    return outputFormat.format(date!!)
}

data class CultivationTip(
    val title: String,
    val sections: List<Section>
)

data class Section(
    val heading: String,
    val content: String,
    val img: String? = null
)

fun cleanHtml(inputHtml: String): String {
    val document = Jsoup.parse(inputHtml)

    return document.html()
}

fun extract(html: String): CultivationTip {
    val doc: Document = Jsoup.parse(html)

    val title = doc.select("h1").text()

    val sections = mutableListOf<Section>()
    var currentHeading: String? = null
    val contentBuilder = StringBuilder()

    for (element in doc.body().children()) {
        when {
            element.tagName() == "h2" || element.tagName() == "h3" -> {
                if (currentHeading != null && contentBuilder.isNotEmpty()) {
                    sections.add(Section(currentHeading, contentBuilder.toString().trim()))
                    contentBuilder.clear()
                }
                currentHeading = element.text()
            }

            element.tagName() == "p" && element.select("img").isNotEmpty() -> {
                val imgSrc = element.select("img").attr("src")
                if (imgSrc.startsWith("http") && currentHeading != null) {
                    sections.add(Section(currentHeading, contentBuilder.toString().trim(), imgSrc))
                    contentBuilder.clear()
                }
            }

            element.tagName() == "ul" || element.tagName() == "ol" || element.tagName() == "p" -> {
                contentBuilder.append(element.outerHtml()).append("\n")
            }

            element.tagName() == "img" -> {
                val imgSrc = element.attr("src")
                if (imgSrc.startsWith("http") && currentHeading != null) {
                    if (contentBuilder.isNotEmpty()) {
                        sections.add(Section(currentHeading, contentBuilder.toString().trim(), imgSrc))
                        contentBuilder.clear()
                    }
                }
            }
        }
    }

    if (currentHeading != null && contentBuilder.isNotEmpty()) {
        sections.add(Section(currentHeading, contentBuilder.toString().trim()))
    }

    return CultivationTip(title, sections)
}

fun showAlertDialog(
    context: Context,
    title: String,
    message: String? = null,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositive: (() -> Unit)? = null,
    onNegative: (() -> Unit)? = null
) {
    Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositive?.invoke()
            dialog.dismiss()
        }
        setNegativeButton(negativeButtonText) { dialog, _ ->
            onNegative?.invoke()
            dialog.dismiss()
        }
        create()
        show()
    }
}





