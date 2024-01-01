package com.eflm.foodmenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.eflm.foodmenu.usecases.home.HomeActivity
import com.eflm.foodmenu.usecases.session.AutenticationActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: EncryptedSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Configuración de las EncryptedSharedPreferences
        val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "account",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences

        val uid = sharedPreferences.getString("uidSp", "0")

        if (uid== "0"){
            startActivity(Intent(this, AutenticationActivity::class.java))
            finish()
        }else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }
}