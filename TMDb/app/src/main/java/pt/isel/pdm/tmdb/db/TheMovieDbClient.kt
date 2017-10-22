package pt.isel.pdm.tmdb.db

/**
 * Created by User01 on 22/10/2017.
 */
class TheMovieDbClient {

    val API_KEY : String = "b531994cdfaa8e3b441e4086b1c6756d"
    val SEARCH_QUERY: String ="https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s"


    /// Pesquisa de	filmes por nome
    /// e.g.: https://api.themoviedb.org/3/search/movie?api_key=*****&query=war%20games
    public fun search(title: String?){

        var query: String = java.lang.String.format(SEARCH_QUERY, API_KEY, title)

        println(query)
    }
}