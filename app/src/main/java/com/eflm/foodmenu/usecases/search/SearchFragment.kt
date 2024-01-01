package com.eflm.foodmenu.usecases.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.databinding.FragmentSearchBinding
import com.eflm.foodmenu.usecases.recomended.RecoAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: AlimentRepository
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as FoodMenuApp).repository

        // Agregar TextWatcher al SearchView
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    realizarBusqueda(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Cancelar la búsqueda existente si aún no ha comenzado
                searchJob?.cancel()

                // Esperar un breve período antes de iniciar la búsqueda
                searchJob = lifecycleScope.launch {
                    delay(300) // Puedes ajustar este valor según tus necesidades
                    realizarBusqueda(newText.orEmpty())
                }

                return true
            }
        })


//        lifecycleScope.launch{
//            val call: Call<List<AlimentoDto>> = repository.searchAliment(query)
//            call.enqueue(object: Callback<List<AlimentoDto>> {
//                override fun onResponse(
//                    call: Call<List<AlimentoDto>>,
//                    response: Response<List<AlimentoDto>>
//                ) {
//
//                    response.body()?.let{aliment ->
//                        binding.recyclerAliSearch.layoutManager = LinearLayoutManager(requireContext())
//                        binding.recyclerAliSearch.adapter = RecoAdapter(aliment)
//                    }
//
//                }
//
//                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
//
//                }
//            })
//        }



    }



    private fun realizarBusqueda(query: String) {
        Log.d("LOGS", "entro a la funcion para la peticion")
        lifecycleScope.launch {
            val call: Call<List<AlimentoDto>> = repository.searchAliment(query)
            call.enqueue(object : Callback<List<AlimentoDto>> {

                override fun onResponse(
                    call: Call<List<AlimentoDto>>,
                    response: Response<List<AlimentoDto>>
                ) {
                    Log.d("LOGS", "respuesta antes del iteracion ${response.errorBody()?.string()}")
                    response.body()?.let { aliment ->
                        Log.d("LOGS", "Mensaje del servidor ${response.body()}")
                        if (aliment.isNotEmpty()) {
                            binding.recyclerAliSearch.layoutManager = LinearLayoutManager(requireContext())
                            val adapter = RecoAdapter(aliment)
                            binding.recyclerAliSearch.adapter = adapter
                            adapter.notifyDataSetChanged()
                            binding.textEmpy.visibility = View.GONE
                        } else {
                            Log.d("LOGS", "La lista de alimentos está vacía")
                            binding.textEmpy.visibility = View.VISIBLE
                        }

                    }
                }

                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
                    Log.e("LOGS", "Error en la solicitud: ${t.message}")
                    // Manejar el fallo de la solicitud de red
                }
            })
        }
    }


}