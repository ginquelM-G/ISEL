package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import java.util.ArrayList

/**
 * Created by tony_mendes on 09/12/2017.
 */
class MoviesProvider : ContentProvider() {

    private val MOVIES_LST = 1
    private val MOVIE_ID = 2
    private val DETAILS_LST = 3
    private val DETAIL_ID = 4
    private val CATEGORIES_LST = 5
    private val CATEGORY_ID = 6

    private val URI_MATCHER : UriMatcher

    init {
        URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Movies.RESOURCE,
                MOVIES_LST
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Movies.RESOURCE + "/#",
                MOVIE_ID
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Details.RESOURCE,
                DETAILS_LST
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Details.RESOURCE + "/#",
                DETAIL_ID
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Categories.RESOURCE,
                CATEGORIES_LST
        )
        URI_MATCHER.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.Categories.RESOURCE + "/#",
                CATEGORY_ID
        )
    }

    private var dbHelper: DbOpenHelper? = null

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val table : String = when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> DbSchema.Movies.TBL_NAME
            DETAILS_LST -> DbSchema.Details.TBL_NAME
            CATEGORIES_LST -> DbSchema.Categories.TBL_NAME
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        val newId = db.insert(table, null, values)

        if (!inBatchMode.get())
            context.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, newId)
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        var sortOrder = sortOrder

        val qbuilder = SQLiteQueryBuilder()
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> {
                qbuilder.tables = DbSchema.Movies.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MoviesContract.Movies.DEFAULT_SORT_ORDER
                }
            }
            MOVIE_ID -> {
                qbuilder.tables = DbSchema.Movies.TBL_NAME
                qbuilder.appendWhere(DbSchema.Movies.COL_MOVIE_ID + "=" + uri!!.lastPathSegment)
            }
            DETAILS_LST -> {
                qbuilder.tables = DbSchema.Details.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MoviesContract.Details.DEFAULT_SORT_ORDER
                }
            }
            DETAIL_ID -> {
                qbuilder.tables = DbSchema.Details.TBL_NAME
                qbuilder.appendWhere(DbSchema.Details.COL_MOVIE_ID + "=" + uri!!.lastPathSegment)
            }
            CATEGORIES_LST -> {
                qbuilder.tables = DbSchema.Categories.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MoviesContract.Categories.DEFAULT_SORT_ORDER
                }
            }
            CATEGORY_ID -> {
                qbuilder.tables = DbSchema.Categories.TBL_NAME
                qbuilder.appendWhere(DbSchema.Categories.COL_NAME + "=" + uri!!.lastPathSegment)
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.readableDatabase
        val cursor = qbuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        cursor.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun onCreate(): Boolean {
        dbHelper = DbOpenHelper(context)
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val table : String
        when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> {
                table = DbSchema.Movies.TBL_NAME
                if (selection != null) {
                    throw IllegalArgumentException("selection not supported")
                }
            }
            DETAILS_LST -> {
                table = DbSchema.Details.TBL_NAME
                if (selection != null) {
                    throw IllegalArgumentException("selection not supported")
                }
            }
            CATEGORIES_LST -> {
                table = DbSchema.Categories.TBL_NAME
                if (selection != null) {
                    throw IllegalArgumentException("selection not supported")
                }
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        val ndel = db.delete(table, null, null)

        if (ndel > 0 && !inBatchMode.get()) {
            context.contentResolver.notifyChange(uri, null)
        }

        return ndel
    }

    override fun getType(uri: Uri?): String {
        return when (URI_MATCHER.match(uri)) {
            MOVIES_LST -> MoviesContract.Movies.CONTENT_TYPE
            MOVIE_ID -> MoviesContract.Movies.CONTENT_ITEM_TYPE
            CATEGORIES_LST -> MoviesContract.Categories.CONTENT_TYPE
            CATEGORY_ID -> MoviesContract.Categories.CONTENT_ITEM_TYPE
            DETAILS_LST -> MoviesContract.Details.CONTENT_TYPE
            DETAIL_ID -> MoviesContract.Details.CONTENT_ITEM_TYPE
            else -> throw badUri(uri)
        }
    }

    val inBatchMode = object : ThreadLocal<Boolean>() {
        override fun initialValue(): Boolean = false
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>?): Array<ContentProviderResult> {
        val database = dbHelper!!.writableDatabase
        inBatchMode.set(true)
        database.beginTransaction()
        val results : Array<ContentProviderResult>
        try {
            results = super.applyBatch(operations)
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            inBatchMode.set(false)
        }
        context.contentResolver.notifyChange(MoviesContract.CONTENT_URI, null)
        return results
    }

    private fun badUri(uri: Uri?) =
            IllegalArgumentException("unknown uri: $uri")
}