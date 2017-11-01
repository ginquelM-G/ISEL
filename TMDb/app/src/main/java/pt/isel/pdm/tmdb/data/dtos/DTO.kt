package pt.isel.pdm.tmdb.data.dtos

/**
 *  Created by User01 on 22/10/2017.
 */

/**
 * Movie related data
 **/
data class MovieSearchItem(val id: Int, val title: String, val release_date: String?) {
    override fun toString(): String {
        var res =  "Id: " + id + "\nTitle: " + title
        if (release_date != null) res += "\nRelease Date: " + release_date
        return res
    }
}


data class MovieDetails(val id: Int, val title: String, val popularity: Double,val vote_average: Double,  val release_date: String?, val overview: String) {
    override fun toString(): String {
        var res =  "Id: " + id + "\n\nTitle: " + title + "\n\nPopularity: " +popularity+
                "\n\nVote Average: " +vote_average+ "\n\nRelease Date: " +release_date+ "\n\nOverview: " + overview

        return res
    }
}
