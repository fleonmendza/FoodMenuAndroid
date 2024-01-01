package com.eflm.foodmenu.usecases.session

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.ErrorDetail
import com.eflm.foodmenu.data.remote.model.LoginDto
import com.eflm.foodmenu.data.remote.model.RegisterDto
import com.eflm.foodmenu.databinding.FragmentSignUpBinding
import com.eflm.foodmenu.usecases.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var repository: AlimentRepository

    private var email = ""
    private var password = ""
    private var name = ""
    private var lastName = ""
    private var age = ""
    private var sex = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        var sexInput = ""

        //RadioGroup
        binding.rgRSex.setOnCheckedChangeListener { view, checkedId ->
            when (checkedId) {
                binding.rbRHombre.id -> {
                    sexInput = "Hombre"
                }

                binding.rbRMujer.id -> {
                    sexInput = "Mujer"
                }

                else -> "Desconocido"
            }
        }

        repository = (requireActivity().application as FoodMenuApp).repository


        binding.btRegister.setOnClickListener {

            lifecycleScope.launch {
                try {
                    password = binding.ipRPassword.text.toString().trim()
                    email = binding.ipREmail.text.toString().trim()
                    name = binding.ipRName.text.toString().trim()
                    lastName = binding.ipRLastName.text.toString().trim()
                    age = binding.ipRAge.text.toString().trim()
                    sex = sexInput

                    if (!validaCampos(email, password, name, lastName, age, sex)) return@launch

                    binding.flProgressBarContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE

                    var dataR = RegisterDto(email, password, name, lastName, age, sex)
                    Log.d("LOGSa", "Datos aceso${dataR}")
                    val result = repository.postRegisterUser(dataR)
                    if (result.isSuccessful) {

                        binding.flProgressBarContainer.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        showSuccessDialog(requireContext())


                    } else {
                        val errorBody = result.errorBody()?.string()
                        binding.flProgressBarContainer.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        if (errorBody != null) {
                            try {
                                val errorDetail = Json.decodeFromString<ErrorDetail>(errorBody)
                                val detail = errorDetail?.detail

                                if (detail != null) {
                                    showErrorDialog(context, detail)
                                }

                                Log.d("LOGS", "Mensaje del servidor mal ${detail}")
                            } catch (e: SerializationException) {
                                Log.e("LOGS", "Error de serialización/deserialización: $e")
                                // Aquí puedes manejar el error de serialización/deserialización según tus necesidades
                            } catch (e: Exception) {
                                Log.e("LOGS", "Otro error: $e")
                                // Aquí puedes manejar otros tipos de errores que puedan ocurrir
                            }
                        }

                        Log.d("LOGS", "Mensaje del servidor mal ${errorBody}")
                    }
                } catch (e: Exception) {
                    Log.e("LOGS", "Error en la llamada a la API: ${e.message}")
                    binding.flProgressBarContainer.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog(requireContext(), "Error en la conexión")
                }
            }

        }
    }


    private fun validaCampos(
        correo: String,
        pass: String,
        name: String,
        lastName: String,
        age: String,
        sex: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.ipRName.error = "Ingresa un nombre"
            binding.ipRName.requestFocus()
            return false
        }
        if (lastName.isEmpty()) {
            binding.ipRLastName.error = "Ingresa tu apellido"
            binding.ipRLastName.requestFocus()
            return false
        }
        if (correo.isEmpty()) {
            binding.ipREmail.error = "Se requiere el correo"
            binding.ipREmail.requestFocus()
            return false
        }
        if (pass.isEmpty() || pass.length < 6) {
            binding.ipRPassword.error =
                "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.ipRPassword.requestFocus()
            return false
        }
        if (sex.isEmpty()) {
            binding.tvRSexo.error = "Seleciona una opcion"
            binding.tvRSexo.requestFocus()
            return false
        }
        if (age.isEmpty()) {
            binding.ipRAge.error = "Ingresa tu edad"
            binding.ipRAge.requestFocus()
            return false
        }
        return true
    }


    private fun showErrorDialog(context: Context?, errorMessage: String) {
        val actualContext = context ?: return

        val alertDialogBuilder = AlertDialog.Builder(actualContext)
        alertDialogBuilder
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showSuccessDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Revisa tu correo para activar tu cuenta.")
        builder.setPositiveButton("OK") { _, _ ->
            val loginFragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_autentication, loginFragment)
                .addToBackStack(null)
                .commit() // Opcional: cerrar la actividad actual si es necesario
        }
        builder.setCancelable(false)
        builder.show()
    }


}