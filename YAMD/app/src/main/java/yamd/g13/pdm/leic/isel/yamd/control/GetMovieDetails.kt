package yamd.g13.pdm.leic.isel.yamd.control

import org.json.JSONObject
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail

/**
 * Created by tony_mendes on 28/11/2017.
 */
class GetMovieDetails : Command(){
    override fun<D> parseResult(jsonResult: String): List<D> {
        if(jsonResult.isEmpty()){
            return ArrayList<D>()
        }
        var result = ArrayList<D>(1)

        val md = JSONObject(jsonResult)
        result.add(MovieDetail(md.getInt("id"),
                md.getString("title"),
                md.getDouble("vote_average"),
                md.getInt("vote_count"),
                md.getInt("popularity"),
                md.getString("poster_path"),
                md.getString("overview"),
                md.getString("release_date")) as D)
        return result
    }

    override fun getUri(endpoint: Endpoint): String {
        return "$API_BASE_URL${endpoint.getPath()}?api_key=$API_KEY"
    }
}