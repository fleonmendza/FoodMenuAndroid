package com.eflm.foodmenu.usecases.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.ErrorDetail
import com.eflm.foodmenu.data.remote.model.LoginDto
import com.eflm.foodmenu.databinding.FragmentLoginBinding
import com.eflm.foodmenu.usecases.home.HomeActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.IOException
import java.security.GeneralSecurityException


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var repository: AlimentRepository

    private var email = ""
    private var password = ""

    private var uidsp: String? = ""

    //SharedPreferences Encriptadas:
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var encryptedSharedPrefsEditor: SharedPreferences.Editor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        if (isNetworkAvailable(requireContext())) {

        } else {
            showErrorDialog(requireContext(), "No hay conexión a Internet")
        }

        try {
            //Creando la llave para encriptar
            val masterKeyAlias = MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedSharedPreferences = EncryptedSharedPreferences
                .create(
                    requireContext(),
                    "account",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences
        }catch(e: GeneralSecurityException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }


        encryptedSharedPrefsEditor = encryptedSharedPreferences.edit()
        uidsp = encryptedSharedPreferences.getString("uidSp", "0")

        binding.tvlinkRegistro.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_autentication, SignUpFragment.newInstance())
//                .addToBackStack("SingInUpFragment")
//                .commit()
            val signUpFragment = SignUpFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_autentication , signUpFragment)
                .addToBackStack(null) // Opcional: agrega transacciones al historial
                .commit()
        }


        repository = (requireActivity().application as FoodMenuApp).repository

        binding.btIni.setOnClickListener {
            lifecycleScope.launch {
                try {
                    password = binding.etPass.text.toString().trim()
                    email = binding.etMail.text.toString().trim()
                    if (!validaCampos(email, password)) return@launch

                    binding.flProgressBarContainer.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE

                    var dataL = LoginDto(email, password)
                    Log.d("LOGSa", "Datos aceso${dataL}")
                    val result = repository.postDataLogin(dataL)
                    if (result.isSuccessful) {

                        val uid = result.body()?.get("uid")?.asJsonObject?.get("localId")
                        uidsp = uid.toString()
                        encryptedSharedPrefsEditor.putString("uidSp", uidsp)
                        encryptedSharedPrefsEditor.apply()
                        binding.flProgressBarContainer.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        val dataIntent = Intent(requireContext(), HomeActivity::class.java).apply {
                            putExtra("EXTRA_UID", "$uid")

                        }
                        startActivity(dataIntent)
                        requireActivity().finish()
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
                }catch (e: Exception){
                    Log.e("LOGS", "Error en la llamada a la API: ${e.message}")
                    binding.flProgressBarContainer.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE

                    // Puedes mostrar un mensaje de error al usuario o realizar otras acciones
                    showErrorDialog(requireContext(), "Error en la conexión")
                }




            }

        }
    }

    private fun validaCampos(correo: String, pass:String): Boolean{


        if(correo.isEmpty()){
            binding.etMail.error = "Se requiere el correo"
            binding.etMail.requestFocus()
            return false
        }

        if(pass.isEmpty() || pass.length < 6){
            binding.etPass.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.etPass.requestFocus()
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

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }



}