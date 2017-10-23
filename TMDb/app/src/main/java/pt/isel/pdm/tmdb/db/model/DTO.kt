package pt.isel.pdm.tmdb.db.model

/**
 * Created by User01 on 22/10/2017.
 */

data class MovieSearchItem(val id: Int, val title: String) {
    override fun toString(): String = "Id: " + id + "\nTitle: " + title
}
