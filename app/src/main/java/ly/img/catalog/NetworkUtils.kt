package ly.img.catalog

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// <upload-function>
/**
 * Uploads the file backed by the [uri] to a remote URL using a HTTP POST request.
 * Uses low level classes like [HttpsURLConnection], [InputStream], [OutputStream].
 * In a production app, you might want to use a library like OkHttp to handle this for you.
 *
 * @return boolean indicating whether the request returned 200 OK or not.
 */
suspend fun upload(uri: Uri, contentResolver: ContentResolver): Boolean {
    return withContext(Dispatchers.IO) {
        val url = URL("https://httpbin.org/post")

        val boundary = "Boundary-${System.currentTimeMillis()}"

        val httpsURLConnection = url.openConnection() as HttpsURLConnection
        httpsURLConnection.addRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=$boundary"
        )
        httpsURLConnection.requestMethod = "POST"
        httpsURLConnection.doInput = true
        httpsURLConnection.doOutput = true

        val outputStreamToRequestBody = httpsURLConnection.outputStream
        val httpRequestBodyWriter = BufferedWriter(OutputStreamWriter(outputStreamToRequestBody))

        httpRequestBodyWriter.write("\n--$boundary\n")
        httpRequestBodyWriter.write(
            "Content-Disposition: form-data;"
                + "name=\"media\";"
                + "filename=\"imgly_media\""
                + "\nContent-Type: ${contentResolver.getType(uri)}\n\n"
        )
        httpRequestBodyWriter.flush()

        val inputStreamToFile = contentResolver.openInputStream(uri)!!
        var bytesRead: Int
        val dataBuffer = ByteArray(1024)
        while (inputStreamToFile.read(dataBuffer).also { bytesRead = it } != -1) {
            outputStreamToRequestBody.write(dataBuffer, 0, bytesRead)
        }
        outputStreamToRequestBody.flush()

        httpRequestBodyWriter.write("\n--$boundary--\n")
        httpRequestBodyWriter.flush()

        outputStreamToRequestBody.close()
        httpRequestBodyWriter.close()

        val responseCode = httpsURLConnection.responseCode
        Log.d("IMG.LY", httpsURLConnection.inputStream.bufferedReader().use { it.readText() })
        responseCode == HttpURLConnection.HTTP_OK
    }
}
// <upload-function>