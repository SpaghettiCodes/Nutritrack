package com.example.chua_33520879.data.patient

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

fun generateSalt(length: Int = 16): String {
    val random = SecureRandom()
    val salt = ByteArray(length)
    random.nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

fun hashPassword(
    password: String,
    salt: String = generateSalt(),
    combine: Boolean = true
): String {
    val combined = salt + password // or password + salt
    val digest = MessageDigest.getInstance("SHA-256").digest(combined.toByteArray())
    val hash = digest.joinToString("") { "%02x".format(it) }
    if (combine)
        return salt + ":" + hash
    else
        return hash
}

fun checkInput(input: String, hashed: String): Boolean {
    val parts =  hashed.split(":")
    if (parts.size != 2 ) return false

    val salt = parts[0]
    val hash = parts[1]

    val inputHash = hashPassword(input, salt, false)
    return inputHash == hash
}