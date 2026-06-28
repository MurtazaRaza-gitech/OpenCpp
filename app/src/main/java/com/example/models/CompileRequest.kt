package com.example.models

import kotlinx.serialization.Serializable

/**
 * Model representing a C++ code compilation request.
 */
@Serializable
data class CompileRequest(
    val code: String,
    val compiler: String = "g++",
    val optimizationLevel: String = "-O2",
    val standard: String = "c++20"
)

/**
 * Model representing the compilation and execution response from the remote server.
 */
@Serializable
data class CompileResult(
    val isSuccess: Boolean,
    val compilerOutput: String,
    val programOutput: String,
    val exitCode: Int,
    val executionTimeMs: Long
)
