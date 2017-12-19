package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.app.LoaderManager
import android.content.ContentResolver
import android.content.ContentValues
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
           contentValues.put(MoviesContract.Categories.NAME, categories[i].name)
           contentValues.put(MoviesContract.Categories.DESCRIPTION, categories[i].description)
           try {
               contentResolver.insert(MoviesContract.Categories.CONTENT_URI, contentValues)
           }catch (e:Exception){
               Log.e("WRITE TO CATEGORY", e.message)
           }
           ++i
       }

       /* (0 until categories.size)
                .asSequence()
                .map { categories[it] }
                .map {
                    contentValues.clear()
                    contentValues.put(MoviesContract.Categories.NAME, it.name)
                    contentValues.put(MoviesContract.Categories.DESCRIPTION, it.description)
                    try {
                        contentResolver.insert(MoviesContract.Categories.CONTENT_URI, contentValues)
                    }catch (e:Exception){
                        Log.e("WRITE TO CATEGORY", e.message)
                    }
                }*/

    }

    fun writeToMovies(contentResolver: ContentResolver, movies: List<Movie>){

    }

    fun writeToDetails(loaderManager: LoaderManager, details: List<MovieDetail>){

    }

    fun readCategories(contentResolver: ContentResolver, categories: List<Category>){

    }

    fun readMovies(loaderManager: LoaderManager, movies: List<Movie>){

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