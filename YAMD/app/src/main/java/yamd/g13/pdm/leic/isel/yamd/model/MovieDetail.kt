package yamd.g13.pdm.leic.isel.yamd.model

import java.io.Serializable

/**
 * Created by tony_mendes on 28/11/2017.
 */
data class MovieDetail(val id : Int,
                       val title : String,
                       val voteAverage : Double,
                       val voteCount : Int,
                       val popularity : Int,
                       val poster_path : String,
                       val overView : String,
                       val releaseDate : String) : Serializable {}