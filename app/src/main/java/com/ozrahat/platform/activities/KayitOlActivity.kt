package com.ozrahat.platform.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ozrahat.platform.R

class KayitOlActivity : AppCompatActivity() {

    private lateinit var isimEditText: EditText
    private lateinit var kullaniciAdiEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var parolaEditText: EditText
    private lateinit var parola2EditText: EditText

    private lateinit var kayitOlButon: Button
    private lateinit var girisYapButon: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)

        firebaseAuth = Firebase.auth

        widgetHazirla()
    }

    private fun widgetHazirla(){
        isimEditText = findViewById(R.id.kayit_isim_edittext)
        kullaniciAdiEditText = findViewById(R.id.kayit_kullaniciadi_edittext)
        emailEditText = findViewById(R.id.kayit_email_edittext)
        parolaEditText = findViewById(R.id.kayit_parola_edittext)
        parola2EditText = findViewById(R.id.kayit_parola2_edittext)

        kayitOlButon = findViewById(R.id.kayit_kayit_buton)
        girisYapButon = findViewById(R.id.kayit_giris_buton)

        kayitOlButon.setOnClickListener {
            // Kayit ol butonu tiklandi. Kontrolleri yap.
            if(isimEditText.text.isNotEmpty() && kullaniciAdiEditText.text.isNotEmpty()
                    && emailEditText.text.isNotEmpty() && parolaEditText.text.isNotEmpty()
                    && parola2EditText.text.isNotEmpty()){
                // Bilgiler saglandi. Parolalari kontrol et.
                if(parolaEditText.text.toString() == parola2EditText.text.toString()){
                    // Parolalar uyusuyor. Kayit olma islemini baslat.
                    kayitOl(isimEditText.text.toString(), kullaniciAdiEditText.text.toString(),
                    emailEditText.text.toString(), parolaEditText.text.toString())
                }else {
                    // Parolalar uyusmuyor. Uyari yap.
                    Toast.makeText(this, getString(R.string.uyari_parolalar_uyusmuyor), Toast.LENGTH_SHORT).show()
                }
            }else{
                // Yeterli bilgi saglanmadi. Uyari yap.
                Toast.makeText(this, getString(R.string.uyari_yetersiz_bilgi), Toast.LENGTH_SHORT).show()
            }

        }

        girisYapButon.setOnClickListener {
            // Giris yap butonu tiklandi. Giris yap sayfasina yonlendir.
            finish()
            startActivity(Intent(this, GirisYapActivity::class.java))
        }
    }

    private fun kayitOl(isim: String, kullaniciAdi: String, email: String, parola: String){
        firebaseAuth.createUserWithEmailAndPassword(email, parola).addOnCompleteListener {task ->
            if(task.isSuccessful){
                // Islem basarili. Kayit olmaya devam et!

                val db = Firebase.firestore

                val info = hashMapOf(
                        "name" to isim,
                        "username" to kullaniciAdi,
                        "email" to email,
                        "dateJoined" to Timestamp.now()
                )

                db.collection("users").add(info).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        // Islem basarili.
                        Toast.makeText(this, "Başarı ile kayıt oldun.", Toast.LENGTH_SHORT).show()
                    }else {
                        // Islem basarisiz. Hatayi goster.
                        if(task.exception != null){
                            Toast.makeText(this, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }else{
                // Islem basarisiz. Hata mesaji goster.
                if(task.exception != null){
                    Toast.makeText(this, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}