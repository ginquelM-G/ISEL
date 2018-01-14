package yamd.g13.pdm.leic.isel.yamd.control.provider

object DbSchema {
    val DB_NAME = "movies.db"
    val DB_VERSION = 4

    object Categories {
        val TBL_NAME = "Categories"

        val COL_NAME = MoviesContract.CategoriesTable.NAME
        val COL_DESCRIPTION = MoviesContract.CategoriesTable.DESCRIPTION
        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_NAME + " TEXT UNIQUE, " +
                        COL_DESCRIPTION + " TEXT )"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object Movies {
        val TBL_NAME = "Movies"

        val COL_MOVIE_ID = MoviesContract.MoviesTable.MOVIE_ID
        val COL_POSTER_PATH = MoviesContract.MoviesTable.POSTER_PATH
        val COL_CATEGORY = MoviesContract.MoviesTable.CATEGORY

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_MOVIE_ID + " INTEGER UNIQUE, " +
                        COL_POSTER_PATH + " TEXT UNIQUE, " +
                        COL_CATEGORY + " REFERENCES CategoriesTable(COL_NAME))"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object Details {
        val TBL_NAME = "Details"


        val COL_MOVIE_ID = MoviesContract.DetailsTable.MOVIE_ID
        val COL_POSTER_PATH = MoviesContract.DetailsTable.POSTER_PATH
        val COL_TITLE = MoviesContract.DetailsTable.TITLE
        val COL_VOTE_AVERAGE = MoviesContract.DetailsTable.VOTE_AVERAGE
        val COL_VOTE_COUNT = MoviesContract.DetailsTable.VOTE_COUNT
        val COL_POPULARITY = MoviesContract.DetailsTable.POPULARITY
        val COL_OVERVIEW = MoviesContract.DetailsTable.OVERVIEW
        val COL_RELEASE_DATE = MoviesContract.DetailsTable.RELEASE_DATE
        val COL_FOLLOW = MoviesContract.DetailsTable.FOLLOW

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        MoviesContract.COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_MOVIE_ID + " UNIQUE REFERENCES MoviesTable(COL_MOVIE_ID), " +
                        COL_POSTER_PATH + " TEXT UNIQUE, " +
                        COL_TITLE + " TEXT,"+
                        COL_VOTE_AVERAGE + " REAL, " +
                        COL_VOTE_COUNT + " INTEGER, " +
                        COL_POPULARITY + " REAL, "+
                        COL_OVERVIEW + " TEXT, " +
                        COL_RELEASE_DATE + " TEXT, " +
                        COL_FOLLOW + " TEXT )"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}