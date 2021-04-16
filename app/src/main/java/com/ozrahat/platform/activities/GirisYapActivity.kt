package com.ozrahat.platform.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ozrahat.platform.R

class GirisYapActivity : AppCompatActivity() {

    private lateinit var kullaniciAdiEdittext: EditText
    private lateinit var parolaEditText: EditText

    private lateinit var girisYapButon: Button
    private lateinit var kayitOlButon: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris_yap)

        firebaseAuth = Firebase.auth

        widgetTanimla()
    }

    private fun widgetTanimla(){
        kullaniciAdiEdittext = findViewById(R.id.giris_email_edittext)
        parolaEditText = findViewById(R.id.giris_parola_edittext)
        girisYapButon = findViewById(R.id.giris_giris_buton)
        kayitOlButon = findViewById(R.id.giris_kayit_buton)

        girisYapButon.setOnClickListener {
            // Giris yap butoni tiklandi, kontrolleri yap.
            if(kullaniciAdiEdittext.text.isNotEmpty() && parolaEditText.text.isNotEmpty()){
                // Giris yapmaya haziriz.
                girisYap(kullaniciAdiEdittext.text.toString(), parolaEditText.text.toString())
            }else {
                // Kullanici yeterli bilgi girmedi.
                Toast.makeText(this, "Yeterli bilgi girilmedi", Toast.LENGTH_SHORT).show()
            }

        }

        kayitOlButon.setOnClickListener {
            // Kayit ol butonu tiklandi. Kayit olma sayfasina gonder.
            finish()
            startActivity(Intent(this, KayitOlActivity::class.java))
        }
    }

    private fun girisYap(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "Giris yapildi", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}