package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.MainActivity
import com.example.budayaku.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_login.*


class UserLoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        currentUser = auth.currentUser

        if (currentUser != null) {
            moveToHome()
        } else {
            btn_submit_login.setOnClickListener {
                val email = edt_emailLogin.text.toString()
                val password = edt_passwordLogin.text.toString()

                if (email.isEmpty()) {
                    edt_emailLogin.error = "Email tidak boleh kosong"
                }

                if (password.isEmpty()) {
                    edt_passwordLogin.error = "Password tidak boleh kosong"
                }

                if (email.isEmpty() || password.isEmpty()) {
                    return@setOnClickListener
                }

                loading_login.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener {
                    //                val settings =
//                    FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()
//
//                firestore.firestoreSettings = settings
//                firestore.collection("user").document(FirebaseAuth.getInstance().currentUser?.uid!!)
//                    .get()
//                    .addOnSuccessListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this, "Nothing Happened", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    } else {
                        moveToHome()
                    }
//                    }
                }.addOnFailureListener {
                    loading_login.visibility = View.GONE
                    Toast.makeText(this, "Login Gagal ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }

            btn_loginClose.setOnClickListener {
                onBackPressed()
            }

            tv_linkRegister.setOnClickListener {
                startActivity(Intent(this@UserLoginActivity, UserRegisterActivity::class.java))
            }
        }
    }

    private fun moveToHome() {
        loading_login.visibility = View.GONE
        Toast.makeText(this, "Login Success ${auth.currentUser?.uid}\n", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
