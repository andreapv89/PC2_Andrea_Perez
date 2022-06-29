package com.esan.pc2_andrea_perez

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txt_dni = findViewById<EditText>(R.id.et_login_dni)
        val txt_pass = findViewById<EditText>(R.id.et_login_pass)
        val btn_login: Button = findViewById(R.id.bt_login_login)
        val btn_create: Button = findViewById(R.id.bt_login_create)

        val db = Firebase.firestore
        btn_login.setOnClickListener {
            var dni: String = txt_dni.text.toString()
            var pass: String = txt_pass.text.toString()

            db.collection("user")
                .whereEqualTo("dni", dni)
                .whereEqualTo("password", pass)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "listen:error", e)
                        return@addSnapshotListener
                    }
                    if (snapshots!!.documentChanges.size == 0) {
                        Toast.makeText(
                            this, "EL USUARIO Y/O CLAVE\n" +
                                    "NO EXISTE EN EL SISTEMA", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "ACCESO PERMITIDO", Toast.LENGTH_LONG).show()
                    }
                }
        }
        btn_create.setOnClickListener{
            startActivity(Intent(this,RegistroActivity::class.java))
        }
    }
}