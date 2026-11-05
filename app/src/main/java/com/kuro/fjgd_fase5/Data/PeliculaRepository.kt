package com.kuro.fjgd_fase5.Data



import android.content.ContentValues
import com.kuro.fjgd_fase5.model.Pelicula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeliculaRepository(private val dbHelper: Data) {


    suspend fun obtainAll(): List<Pelicula> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Pelicula>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${Data.TABLE_PELICULAS} ORDER BY ${Data.COLUMN_ID} DESC",
            null
        )

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    lista.add(
                        Pelicula(
                            id = c.getInt(c.getColumnIndexOrThrow(Data.COLUMN_ID)),
                            titulo = c.getString(c.getColumnIndexOrThrow(Data.COLUMN_TITULO)),
                            director = c.getString(c.getColumnIndexOrThrow(Data.COLUMN_DIRECTOR)),
                            anio = c.getInt(c.getColumnIndexOrThrow(Data.COLUMN_ANIO)),
                            esVista = c.getInt(c.getColumnIndexOrThrow(Data.COLUMN_ES_VISTA)) == 1,
                            esFavorita = c.getInt(c.getColumnIndexOrThrow(Data.COLUMN_ES_FAVORITA)) == 1
                        )
                    )
                } while (c.moveToNext())
            }
        }
        lista
    }



    suspend fun insertar(pelicula: Pelicula): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Data.COLUMN_TITULO, pelicula.titulo)
            put(Data.COLUMN_DIRECTOR, pelicula.director)
            put(Data.COLUMN_ANIO, pelicula.anio)
            put(Data.COLUMN_ES_VISTA, if (pelicula.esVista) 1 else 0)
            put(Data.COLUMN_ES_FAVORITA, if (pelicula.esFavorita) 1 else 0)
        }
        db.insert(Data.TABLE_PELICULAS, null, values)
    }


    suspend fun actualizar(pelicula: Pelicula): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Data.COLUMN_TITULO, pelicula.titulo)
            put(Data.COLUMN_DIRECTOR, pelicula.director)
            put(Data.COLUMN_ANIO, pelicula.anio)
            put(Data.COLUMN_ES_VISTA, if (pelicula.esVista) 1 else 0)
            put(Data.COLUMN_ES_FAVORITA, if (pelicula.esFavorita) 1 else 0)
        }
        db.update(
            Data.TABLE_PELICULAS,
            values,
            "${Data.COLUMN_ID} = ?",
            arrayOf(pelicula.id.toString())
        )
    }

}