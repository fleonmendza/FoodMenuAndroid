package com.eflm.foodmenu.usecases.recomended


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.databinding.FragmentRecomendedBinding
import com.eflm.foodmenu.provider.AlimentoProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecomendedFragment : Fragment() {

    private var _binding: FragmentRecomendedBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: AlimentRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecomendedBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as FoodMenuApp).repository

        lifecycleScope.launch {
            val call: Call<List<AlimentoDto>> = repository.getAllAliments()
            call.enqueue(object : Callback<List<AlimentoDto>> {
                override fun onResponse(
                    call: Call<List<AlimentoDto>>,
                    response: Response<List<AlimentoDto>>
                ) {
                    if (isAdded) {
                        binding.progressBar.visibility = View.GONE
                        response.body()?.let { aliment ->
                            binding.recyclerAliReco.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding.recyclerAliReco.adapter = RecoAdapter(aliment)

                        }
                    }

                }

                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
                    if (isAdded) { // Verificar si el fragmento está adjunto a un contexto
                        showErrorDialog(requireContext(), "Error en la conexión con el servidor")
                    }
                }
            })
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showErrorDialog(context: Context?, errorMessage: String) {
        val actualContext = context ?: return

        val alertDialogBuilder = AlertDialog.Builder(actualContext)
        alertDialogBuilder
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("Cerrar aplicación") { dialog, _ ->
                // Cierra la aplicación
                activity?.finish()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}
