package yamd.g13.pdm.leic.isel.yamd.control.commands

import org.json.JSONObject
import yamd.g13.pdm.leic.isel.yamd.control.Controller
import yamd.g13.pdm.leic.isel.yamd.control.Endpoint
import yamd.g13.pdm.leic.isel.yamd.model.Movie

/**
 * Created by tony_mendes on 01/11/2017.
 */
class GetMovies : Command() {
    override fun getUri(endpoint: Endpoint) : String {
        return "${Controller.API_BASE_URL}${endpoint.getPath()}?api_key=${Controller.API_KEY}"
    }

    override fun<M> parseResult(jsonResult: String): List<M> {
        if(jsonResult.isEmpty()){
            return ArrayList<M>()
        }
        val jsonObject = JSONObject(jsonResult)
        val moviesArray = jsonObject.getJSONArray("results")

        return (0 until moviesArray.length())
                .asSequence()
                .map { moviesArray.getJSONObject(it) }
                .map {
                    Movie(it.getInt("id"), it.getString("poster_path"))
                }
                .toList() as List<M>
    }
}