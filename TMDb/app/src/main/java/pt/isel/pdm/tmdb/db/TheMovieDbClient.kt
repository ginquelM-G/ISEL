package pt.isel.pdm.tmdb.db

/**
 * Created by User01 on 22/10/2017.
 */
class TheMovieDbClient {

    val API_KEY : String = "b531994cdfaa8e3b441e4086b1c6756d"

    val SEARCH_QUERY: String ="https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s"
    val MOVIE_NOW_PLAYING_QUERY: String ="https://api.themoviedb.org/3/movie/now_playing?api_key=%s"
    val MOVIE_UPCOMING_QUERY: String ="https://api.themoviedb.org/3/movie/upcoming?api_key=%s"
    val MOVIE_POPULAR_QUERY: String ="https://api.themoviedb.org/3/movie/popular?api_key=%s"
    val MOVIE_BY_ID_QUERY: String ="https://api.themoviedb.org/3/movie/%d?api_key=%s"



    /*
    	considere	os	seguintes	endpoints:
            • Pesquisa	de	filmes	por	nome	-	https://developers.themoviedb.org/3/search/search-movies
            • Filmes	em	cartaz	                -	hPps://developers.themoviedb.org/3/movies/get-now-playing
            • Estreias	para	breve	        -	hPps://developers.themoviedb.org/3/movies/get-upcoming
            • Filmes	mais	populares	        -	hPps://developers.themoviedb.org/3/movies/get-popular-movies
            • Informação	do	filme	            -	hPps://developers.themoviedb.org/3/movies/get-movie-details
     */



    /// Pesquisa de	filmes por nome
    /// e.g.: https://api.themoviedb.org/3/search/movie?api_key=*****&query=war%20games
    public fun search(title: String?){
        var query: String = java.lang.String.format(SEARCH_QUERY, API_KEY, title)

        println(query)
    }

    /// Filmes	em	cartaz
    /// e.g.: https://api.themoviedb.org/3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1
    public fun movieNowPlaying(title: String?){
        var query: String = java.lang.String.format(MOVIE_NOW_PLAYING_QUERY, API_KEY)

        println(query)
    }





    /// Estreias para breve
    /// e.g.: https://api.themoviedb.org/3/movie/upcoming?api_key=<<api_key>>&language=en-US&page=1
    public fun movieUpcoming(){
        var query: String = java.lang.String.format(MOVIE_UPCOMING_QUERY, API_KEY)

        println(query)
    }


    /// Pesquisa de	filmes por nome
    /// e.g.: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    public fun moviePopular(title: String?){
        var query: String = java.lang.String.format(MOVIE_POPULAR_QUERY, API_KEY)

        println(query)
    }


    /// Informação do filme
    /// e.g.: https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>
    public fun movieDetails(id: Int?){
        var query: String = java.lang.String.format(MOVIE_BY_ID_QUERY, API_KEY, id)

        println(query)
    }
}