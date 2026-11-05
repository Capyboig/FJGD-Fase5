package com.kuro.fjgd_fase5.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kuro.fjgd_fase5.Data.PeliculaRepository
import com.kuro.fjgd_fase5.model.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeliculaViewModel(private val repository: PeliculaRepository) : ViewModel() {

    private val _listaPeliculas = MutableStateFlow<List<Pelicula>>(emptyList())
    val listaPeliculas: StateFlow<List<Pelicula>> = _listaPeliculas.asStateFlow()

    init {
        cargarPeliculas()
    }

    fun cargarPeliculas() {
        viewModelScope.launch {
            _listaPeliculas.value = repository.obtainAll()
        }
    }

    fun agregarPelicula(titulo: String, director: String, anio: Int, esVista: Boolean) {
        viewModelScope.launch {
            val nueva = Pelicula(titulo = titulo, director = director, anio = anio, esVista = esVista)
            repository.insertar(nueva)
            cargarPeliculas()
        }
    }

    fun conmutarFavorito(pelicula: Pelicula) {
        viewModelScope.launch {
            val actualizada = pelicula.copy(esFavorita = !pelicula.esFavorita)
            repository.actualizar(actualizada)
            cargarPeliculas()
        }
    }
}

class PeliculaViewModelFactory(private val repository: PeliculaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculaViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel class no reconocida")
    }
}
