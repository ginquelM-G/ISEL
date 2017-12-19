package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by tony_mendes on 09/12/2017.
 */
//https://bitbucket.org/grokkingandroid/cpsample
//https://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        //deleteDb(db)
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteDb(db)
        createDb(db)
    }

    private fun createDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Categories.DDL_CREATE_TABLE)
        db.execSQL(DbSchema.Movies.DDL_CREATE_TABLE)
        db.execSQL(DbSchema.Details.DDL_CREATE_TABLE)
        /*db.execSQL(
                "INSERT INTO " + DbSchema.Categories.TBL_NAME + " VALUES ("+ 1 + ", " + "\"popular\", \"popular films\");"
        )
        db.execSQL(
                "INSERT INTO " + DbSchema.Categories.TBL_NAME + " VALUES ("+ 2 +", " + "\"upcoming\", \"upcoming films\");"
        )
        db.execSQL(
                "INSERT INTO " + DbSchema.Categories.TBL_NAME + " VALUES ("+ 3 +", " + "\"now_playing\", \"now playing films\");"
        )*/
    }

    private fun deleteDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Details.DDL_DROP_TABLE)
        db.execSQL(DbSchema.Movies.DDL_DROP_TABLE)
        db.execSQL(DbSchema.Categories.DDL_DROP_TABLE)
    }

}