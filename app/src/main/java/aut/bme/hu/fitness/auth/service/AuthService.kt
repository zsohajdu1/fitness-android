package aut.bme.hu.fitness.auth.service


interface AuthService {
    val hasUser: Boolean
    suspend fun authenticate(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    fun getAccessToken(): String
    suspend fun initialize()
}