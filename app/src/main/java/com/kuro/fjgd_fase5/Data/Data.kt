package com.kuro.fjgd_fase5.Data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Data(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val  DATABASE_NAME = "dam.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PELICULAS = "peliculas"
        const val COLUMN_ID = "id"
        const val COLUMN_TITULO = "titulo"
        const val COLUMN_DIRECTOR = "director"
        const val COLUMN_ANIO = "anio"
        const val COLUMN_ES_VISTA = "es_vista"
        const val COLUMN_ES_FAVORITA = "es_favorita"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_PELICULAS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITULO TEXT NOT NULL,
                $COLUMN_DIRECTOR TEXT NOT NULL,
                $COLUMN_ANIO INTEGER NOT NULL,
                $COLUMN_ES_VISTA INTEGER NOT NULL DEFAULT 0,
                $COLUMN_ES_FAVORITA INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())
    }


    override fun onUpgrade(db: SQLiteDatabase, oldversion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PELICULAS")
        onCreate(db)
    }
}