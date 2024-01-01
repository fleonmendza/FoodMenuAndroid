package com.eflm.foodmenu.usecases.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.databinding.FragmentFavoritesBinding
import com.eflm.foodmenu.databinding.FragmentRecomendedBinding
import com.eflm.foodmenu.provider.AlimentoProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: EncryptedSharedPreferences


    private lateinit var repository: AlimentRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Configuración de las EncryptedSharedPreferences
        val masterKeyAlias = MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            requireContext(),
            "account",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as FoodMenuApp).repository

        val uidWithQuotes = sharedPreferences.getString("uidSp", "0")
        val uid = uidWithQuotes?.replace("\"", "")

        println("UID recuperado en FavoritesFragment: $uid")
//        val uid = "AWxSc9iaJrSXlvFSc1xXksrYJh23"

        lifecycleScope.launch{
            println("UID recuperado : $uid")
            val call: Call<List<AlimentoDto>> = repository.getAlimentsFav(uid)
            call.enqueue(object: Callback<List<AlimentoDto>> {
                override fun onResponse(
                    call: Call<List<AlimentoDto>>,
                    response: Response<List<AlimentoDto>>
                ) {
                    if (isAdded) {
                        binding.progressBar.visibility = View.GONE
                        response.body()?.let{aliment ->
                            val tieneAlimentoConIdCero = aliment.any { it.id == 0 }

                            if (tieneAlimentoConIdCero) {
                                // Mostrar mensaje de texto en lugar del RecyclerView
                                binding.recyclerAliFavo.visibility = View.GONE
                                binding.textViewMensaje.visibility = View.VISIBLE

                            } else {
                                // Mostrar la lista de alimentos en el RecyclerView
                                binding.recyclerAliFavo.visibility = View.VISIBLE
                                binding.textViewMensaje.visibility = View.GONE

                                binding.recyclerAliFavo.layoutManager = GridLayoutManager(context, 2)
                                binding.recyclerAliFavo.adapter = favoritesAlimentAdapter(aliment)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
                    if (isAdded) { // Verificar si el fragmento está adjunto a un contexto
                        showErrorDialog(requireContext(), "Error en la conexión")

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