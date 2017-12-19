package yamd.g13.pdm.leic.isel.yamd.control.provider

object DbSchema {
    val DB_NAME = "movies.db"
    val DB_VERSION = 4

    object Categories {
        val TBL_NAME = "Categories"

        val COL_NAME = MoviesContract.Categories.NAME
        val COL_DESCRIPTION = MoviesContract.Categories.DESCRIPTION
        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_NAME + " TEXT UNIQUE, " +
                        COL_DESCRIPTION + " TEXT )"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object Movies {
        val TBL_NAME = "Movies"

        val COL_MOVIE_ID = MoviesContract.Movies.MOVIE_ID
        val COL_POSTER_PATH = MoviesContract.Movies.POSTER_PATH
        val COL_CATEGORY = MoviesContract.Movies.CATEGORY

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_MOVIE_ID + " INTEGER UNIQUE, " +
                        COL_POSTER_PATH + " TEXT UNIQUE, " +
                        COL_CATEGORY + " REFERENCES Categories(COL_NAME))"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object Details {
        val TBL_NAME = "Details"


        val COL_MOVIE_ID = MoviesContract.Details.MOVIE_ID
        val COL_POSTER_PATH = MoviesContract.Details.POSTER_PATH
        val COL_TITLE = MoviesContract.Details.TITLE
        val COL_VOTE_AVERAGE = MoviesContract.Details.VOTE_AVERAGE
        val COL_VOTE_COUNT = MoviesContract.Details.VOTE_COUNT
        val COL_POPULARITY = MoviesContract.Details.POPULARITY
        val COL_OVERVIEW = MoviesContract.Details.OVERVIEW
        val COL_RESLEASE_DATE = MoviesContract.Details.RESLEASE_DATE

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_MOVIE_ID + " UNIQUE REFERENCES Movies(COL_MOVIE_ID), " +
                        COL_POSTER_PATH + " TEXT UNIQUE, " +
                        COL_TITLE + " TEXT,"+
                        COL_VOTE_AVERAGE + " REAL, " +
                        COL_VOTE_COUNT + " INTEGER, " +
                        COL_POPULARITY + " REAL, "+
                        COL_OVERVIEW + " TEXT, " +
                        COL_RESLEASE_DATE + " TEXT )"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}