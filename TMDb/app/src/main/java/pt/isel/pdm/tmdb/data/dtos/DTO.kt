package pt.isel.pdm.tmdb.data.dtos

/**
 * Created by User01 on 22/10/2017.
 */

data class MovieSearchItem(val id: Int, val title: String, val release_date: String?) {
    override fun toString(): String {
        var res =  "Id: " + id + "\nTitle: " + title
        if (release_date != null) res += "\nRelease Date: " + release_date
        return res
    }
}

/*data class MovieSearchItem(val id: Int, val title: String) {
    override fun toString(): String {
        var res =  "Id: " + id + "\nTitle: " + title

        return res
    }
}*/
