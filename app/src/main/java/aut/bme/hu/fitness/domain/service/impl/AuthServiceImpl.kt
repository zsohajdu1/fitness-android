package aut.bme.hu.fitness.domain.service.impl

import aut.bme.hu.fitness.domain.service.AuthService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthService {
    override val currentUserId: String get() = firebaseAuth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean get() = firebaseAuth.currentUser != null


    override suspend fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun authenticate(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override suspend fun deleteAccount() {
        firebaseAuth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getAuthToken(): String {
        val user = firebaseAuth.currentUser
        return user?.getIdToken(false)?.await()?.token
            ?: throw Exception("User is not authenticated or token could not be retrieved.")
    }
}