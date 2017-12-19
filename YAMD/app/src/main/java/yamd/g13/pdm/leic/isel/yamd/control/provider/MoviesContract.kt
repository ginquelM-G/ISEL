package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by tony_mendes on 09/12/2017.
 */
object MoviesContract {
    val AUTHORITY = "yamd.g13.pdm.leic.isel.yamd.control.provider"

    val CONTENT_URI = Uri.parse("content://" + AUTHORITY)

    val MEDIA_BASE_SUBTYPE = "/vnd.movies."

    val COL_ID = BaseColumns._ID

    object Categories : BaseColumns {
        val RESOURCE = "categories"

        val CONTENT_URI = Uri.withAppendedPath(
                MoviesContract.CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE
        val NAME = "name"
        val DESCRIPTION = "description"
        val PROJECT_ALL = arrayOf(COL_ID, NAME, DESCRIPTION)

        val DEFAULT_SORT_ORDER = NAME + " ASC"
    }


    object Movies : BaseColumns {
        val RESOURCE = "movies"

        val CONTENT_URI = Uri.withAppendedPath(
                MoviesContract.CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE
        val MOVIE_ID = "id"
        val POSTER_PATH = "poster_path"
        val CATEGORY = "category"
        val PROJECT_ALL = arrayOf(COL_ID, MOVIE_ID, POSTER_PATH, CATEGORY)

        val DEFAULT_SORT_ORDER = MOVIE_ID + " ASC"
    }

    object Details : BaseColumns{
        val RESOURCE = "details"

        val CONTENT_URI = Uri.withAppendedPath(
                MoviesContract.CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val MOVIE_ID = "id"
        val POSTER_PATH = "poster_path"
        val TITLE = "title"
        val VOTE_AVERAGE= "vote_average"
        val VOTE_COUNT = "vote_count"
        val POPULARITY = "popularity"
        val OVERVIEW = "overview"
        val RESLEASE_DATE = "release_date"

        val PROJECT_ALL = arrayOf(COL_ID, MOVIE_ID, POSTER_PATH, TITLE, VOTE_AVERAGE, VOTE_COUNT,
                POPULARITY, OVERVIEW, RESLEASE_DATE)



        val DEFAULT_SORT_ORDER = MOVIE_ID + " ASC"
    }
}