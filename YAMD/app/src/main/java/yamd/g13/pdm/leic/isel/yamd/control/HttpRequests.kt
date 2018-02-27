package yamd.g13.pdm.leic.isel.yamd.control

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by tony_mendes on 29/10/2017.
 */
val maxRetries: Int = 5
class HttpRequests {
    class ImageLoadTask : AsyncTask<Any, Any, ArrayList<String>>()
    {
        override fun doInBackground(vararg p0: Any?): ArrayList<String> {
            while (true) {
                try {
                    var data : ArrayList<String> = ArrayList(1)
                    data.add(getDataFromAPI(p0[0].toString()))
                    return data
                } catch (e: Exception) {
                    continue
                }
            }
        }

        override fun onPostExecute(result: ArrayList<String>?) {

        }

        private fun getDataFromAPI(path: String): String {
            var retries = 0
            while (true) {
                var urlConnection: HttpURLConnection? = null
                var reader: BufferedReader? = null
                try {
                    val url = URL(path)
                    urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.connect()

                    val inputStream = urlConnection.inputStream
                    val buffer = StringBuffer()
                    if (inputStream == null) {
                        return ""
                    }
                    reader = BufferedReader(InputStreamReader(inputStream))
                    var line: String
                    line = reader.readLine()
                    while (line != null) {
                        buffer.append(line + "\n")
                        try {
                            line = reader.readLine()
                        } catch (e: Exception) {
                            break
                        }
                    }
                    return buffer.toString()
                }
                catch (e: Exception) {
                    if(retries++ < maxRetries)
                        continue
                     return ""
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect()
                    if (reader != null) {
                        try {
                            reader.close()
                        } catch (e: IOException) {
                            return ""
                        }
                    }
                }
            }
        }
    }
    fun  get(path : String) : ArrayList<String>{
        return ImageLoadTask().execute(path).get()
    }
}