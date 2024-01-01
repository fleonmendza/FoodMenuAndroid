package com.eflm.foodmenu.usecases.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import com.eflm.foodmenu.data.remote.model.UserDto
import com.eflm.foodmenu.databinding.FragmentRecomendedBinding
import com.eflm.foodmenu.databinding.FragmentUserBinding
import com.eflm.foodmenu.usecases.favorites.favoritesAlimentAdapter
import com.eflm.foodmenu.usecases.session.AutenticationActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding?= null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: EncryptedSharedPreferences

    private lateinit var repository: AlimentRepository


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

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

        lifecycleScope.launch{
            println("UID recuperado : $uid")
            val call: Call<UserDto> = repository.getUserData(uid)
            call.enqueue(object: Callback<UserDto> {
                override fun onResponse(
                    call: Call<UserDto>,
                    response: Response<UserDto>
                ) {
                    if (isAdded) {

                        val alergiasConGuion = response.body()?.allergies?.map { "- $it" } // Agrega un guion antes de cada ingrediente
                        val alergiasConcatenados = alergiasConGuion?.joinToString("\n") // Concatena los ingredientes con saltos de línea


                        val nombre = "Hola ${response.body()?.name}"
                        val email = "Tu correo: ${response.body()?.email}"
                        val age = "Tu edad ${response.body()?.age} años"
                        val sex = "Sexo: ${response.body()?.sexo}"
                        val alergis = "Aergias: \n${alergiasConcatenados}"



                        binding.apply {
                            name.text = nombre
                            correo.text = email
                            edad.text = age
                            sexo.text = sex
                            alergias.text = alergis

                        }
                    }

                }

                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    if (isAdded) { // Verificar si el fragmento está adjunto a un contexto

                        showErrorDialog(requireContext(), "Error en la conexión")

                    }
                }

            })
        }





        binding.closeSession.setOnClickListener {
            println("cerarr sesion")
            // Eliminar el valor actual asociado con la clave "uidSp"
            sharedPreferences.edit().remove("uidSp").apply()
            startActivity(Intent(requireContext(), AutenticationActivity::class.java))
            requireActivity().finish()
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