package com.example.namatara

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.namatara.databinding.ActivityLoginBinding
import com.example.namatara.data.model.LoginRequest // Import Request
import com.example.namatara.data.model.LoginResponse // Import Response
import com.example.namatara.remote.ApiClient // Import ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // PANGGILAN API DIMULAI DI SINI
            performLogin(username, password)
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi yang menangani koneksi API Login
    private fun performLogin(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        ApiClient.apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    // 1. SIMPAN TOKEN OTENTIKASI
                    loginResponse?.token?.let { token ->
                        saveAuthToken(this@LoginActivity, token)
                    }

                    // 2. Tampilkan pesan berhasil dan navigasi
                    Toast.makeText(this@LoginActivity, "Login Berhasil! Selamat datang ${loginResponse?.fullName}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login Gagal (misalnya 401 Unauthorized)
                    Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Gagal koneksi ke server atau API tidak berjalan
                Toast.makeText(this@LoginActivity, "Gagal terhubung ke API: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Helper function (Anda bisa memindahkannya ke kelas utilitas atau Repository)
    private fun saveAuthToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("AUTH_TOKEN", token).apply()
    }
}
