package com.esan.pc2_andrea_perez

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.esan.pc2_andrea_perez.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val txt_dni = findViewById<EditText>(R.id.et_create_dni)
        val txt_name = findViewById<EditText>(R.id.et_create_name)
        val txt_pass = findViewById<EditText>(R.id.et_create_pass)
        val txt_pass2 = findViewById<EditText>(R.id.et_create_pass2)
        val btn_create: Button = findViewById(R.id.bt_create_create)

        btn_create.setOnClickListener {
            val dni: String = txt_dni.text.toString()
            val name: String = txt_name.text.toString()
            val password: String = txt_pass.text.toString()
            val password2: String = txt_pass2.text.toString()
            val newUser = UserModel(dni, name, password)

            if (validations(dni, name, password, password2)) {
                val db = Firebase.firestore

                db.collection("user")
                    .whereEqualTo("dni", dni)
                    .get()
                    .addOnSuccessListener { document ->
                        if (!document.isEmpty) {
                            Toast.makeText(this, "ya existe el DNI, " +
                                    "no se puede volver a registrar", Toast.LENGTH_LONG).show()
                        } else {
                            //Registra al usuario
                            db.collection("user")
                                .add(newUser)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_LONG)
                                        .show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Error al registrar usuario",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(RegistroActivity(), "error", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "get failed with ", exception)
                    }
            }
        }

    }
    //retorna verdadero si todo esta OK
    private fun validations(dni: String, name: String, password: String, password2: String): Boolean {
        if( dni.isEmpty()){
            Toast.makeText(this, "Debe ingresar un DNI", Toast.LENGTH_LONG).show()
            return false
        }
        if(dni.toInt() <= 9999999){
            Toast.makeText(this, "El campo DNI debe tener 8 digitos", Toast.LENGTH_LONG).show()
            return false
        }

        if( name.isEmpty()){
            Toast.makeText(this, "Debe ingresar un nombre", Toast.LENGTH_LONG).show()
            return false
        }
        if( password.isEmpty()){
            Toast.makeText(this, "Debe ingresar una clave", Toast.LENGTH_LONG).show()
            return false
        }
        if(!password.equals(password2)){
            Toast.makeText(this, "La clave debe ser la misma en ambos campos", Toast.LENGTH_LONG).show()
            return false
        }



        return true
    }

}