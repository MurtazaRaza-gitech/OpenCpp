package com.example.network

import com.example.models.CompileRequest
import com.example.models.CompileResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers

/**
 * Retrofit interface representing remote C++ compilation and sandbox execution APIs.
 */
interface CompileService {

    @POST("compile")
    @Headers("Content-Type: application/json")
    suspend fun compileCode(
        @Body request: CompileRequest
    ): CompileResult
}
