package com.example.namatara

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.namatara.databinding.RegisterActivityBinding
import com.example.namatara.data.model.RegisterRequest
import com.example.namatara.data.model.RegisterResponse
import com.example.namatara.remote.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    // Tambahkan variabel untuk menyimpan tanggal dalam format yang dibutuhkan API (yyyy-MM-dd)
    private var dateOfBirthApiFormat: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            // Ambil semua field yang dibutuhkan
            val username = binding.etUsername.text.toString().trim()
            val fullName = binding.etFullname.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Dapatkan format tanggal yang benar dari variabel kelas
            val dobApiString = dateOfBirthApiFormat

            // Validasi field kosong (termasuk dobApiString)
            if (username.isEmpty() || fullName.isEmpty() || dobApiString.isNullOrEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Password
            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak sesuai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi API Register menggunakan dobApiString
            performRegister(username, fullName, dobApiString, password)
        }

        binding.tvSignInButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.etDob.setOnClickListener {
            showDatePickerDialog()
        }
    }

    // Fungsi terpisah untuk menampilkan Date Picker
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                // 1. PENTING: Format untuk API (yyyy-MM-dd)
                val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateOfBirthApiFormat = apiDateFormat.format(selectedDate.time) // <-- SIMPAN DI VARIABEL KELAS

                // 2. Format untuk User Interface (dd/MM/yyyy)
                val userDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etDob.setText(userDateFormat.format(selectedDate.time))
            },
            year,
            month,
            day
        ).show()
    }

    // Fungsi untuk memanggil API Register
    private fun performRegister(username: String, fullName: String, dobString: String, password: String) {

        // dobString kini berisi format yang benar (yyyy-MM-dd)
        val registerRequest = RegisterRequest(username, fullName, dobString, password)

        // ... di dalam performRegister()
        ApiClient.apiService.signUpUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    // Pendaftaran BERHASIL
                    Toast.makeText(this@RegisterActivity, "Pendaftaran Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Pendaftaran GAGAL
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code() // <-- Status code (400, 409, dll.)

                    // Catat log kesalahan ke Logcat
                    Log.e("API_REGISTER", "Gagal Pendaftaran. Status Code: $statusCode, Body: $errorBody")

                    // Tampilkan pesan kegagalan umum
                    Toast.makeText(this@RegisterActivity, "Pendaftaran Gagal. Coba Username lain atau periksa log.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Logika GAGAL KONEKSI
                Toast.makeText(this@RegisterActivity, "Kesalahan koneksi: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}