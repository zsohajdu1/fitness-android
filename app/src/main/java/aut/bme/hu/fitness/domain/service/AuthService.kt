package aut.bme.hu.fitness.domain.service


interface AuthService {
    val currentUserId: String
    val hasUser: Boolean
    suspend fun authenticate(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()
    suspend fun getAuthToken(): String
}