package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.app.LoaderManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Loader
import android.database.Cursor
import android.util.Log
import yamd.g13.pdm.leic.isel.yamd.model.Category
import yamd.g13.pdm.leic.isel.yamd.model.Movie
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail

/**
 * Created by tony_mendes on 17/12/2017.
 */
object DbAdapter{

    fun writeToCategories(contentResolver: ContentResolver, contentValues: ContentValues, categories: List<Category>){
        var i = 0

        while ( i < categories.size){
            contentValues.clear()
            contentValues.put(MoviesContract.CategoriesTable.NAME, categories[i].name)
            contentValues.put(MoviesContract.CategoriesTable.DESCRIPTION, categories[i].description)
            try {
                contentResolver.insert(MoviesContract.CategoriesTable.CONTENT_URI, contentValues)
            }catch (e:Exception){
                Log.e("WRITE TO CAT. TABLE:", e.message)
            }
            ++i
        }
    }

    fun writeToMovies(contentResolver: ContentResolver, contentValues: ContentValues, movies: List<Movie>){
        var i = 0

        while ( i < movies.size){
            contentValues.clear()
            contentValues.put(MoviesContract.MoviesTable.CATEGORY, movies[i].category)
            contentValues.put(MoviesContract.MoviesTable.MOVIE_ID, movies[i].id)
            contentValues.put(MoviesContract.MoviesTable.POSTER_PATH, movies[i].poster_path)
            try {
                contentResolver.insert(MoviesContract.MoviesTable.CONTENT_URI, contentValues)
            }catch (e:Exception){
                Log.e("WRITE TO MOVIES TABLE:", e.message)
            }
            ++i
        }
    }

    fun writeToDetails(loaderManager: LoaderManager, details: List<MovieDetail>){

    }

    fun readCategories(contentResolver: ContentResolver, categories: List<Category>){

    }

    fun readMoviesFromCursor(loader: Loader<Cursor>?, data: Cursor?): List<Movie>{
        var result = ArrayList<Movie>()
        while (data!!.moveToNext()){
            result.add(Movie(data!!.getInt(1),data!!.getString(2), data!!.getString(3)))
        }
        return result
    }

    fun readDetails(loaderManager: LoaderManager, details: List<MovieDetail>){

    }

    fun updateCategories(loaderManager: LoaderManager, categories: List<Category>){

    }

    fun updateMovies(loaderManager: LoaderManager, movies: List<Movie>){

    }

    fun updateDetails(loaderManager: LoaderManager, details: List<MovieDetail>){

    }

}