package com.example.namatara.remote // PASTIKAN package ini sesuai dengan lokasi file

import ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Alamat IP WAJIB untuk terhubung dari Emulator ke Localhost
    private const val BASE_URL = "https://unconglomerated-zanies-johnette.ngrok-free.dev/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Level BODY akan mencetak request dan response lengkap di Logcat (Berguna untuk debugging)
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        // Tambahkan Interceptor untuk logging
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        // Pastikan Anda sudah mengimplementasikan dependency converter-gson di build.gradle
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Service yang akan dipanggil di Activity/ViewModel
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}