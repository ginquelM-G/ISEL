package yamd.g13.pdm.leic.isel.yamd.control

/**
 * Created by tony_mendes on 11/11/2017.
 */

enum class Endpoint (private var value : String, var query : String, private var pageNumber : Int){
    POPULAR("/movie/popular", "", 1),
    UPCOMING("/movie/upcoming", "", 1),
    NOW_PLAYING("/movie/now_playing", "", 1),
    SEARCH("/search/movie", "", 0),
    MOVIE_DETAIL("/movie/", "", 0);
    var extraPath = ""
    fun setQueryString(query: String) : Endpoint{
       this.query = "&query=$query"
        return this
    }
    fun getPath() : String{
        return "$value$extraPath"
    }
    fun getCurrentPage(): String{
        if(pageNumber > 0){
            var tempPageNum = pageNumber
            return "&page=$tempPageNum"
        }
        return ""
    }
    fun nextPage(): String{
        if(pageNumber > 0){
            var tempPageNum = pageNumber
            ++pageNumber
            return "&page=$tempPageNum"
        }
        return ""
    }
}