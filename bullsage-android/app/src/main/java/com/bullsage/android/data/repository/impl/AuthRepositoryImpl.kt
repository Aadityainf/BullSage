package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.auth.UserAuthManager
import com.bullsage.android.data.model.AuthRequest
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.UserAuthDetails
import com.bullsage.android.data.model.getErrorMessage
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.AuthRepository
import com.bullsage.android.data.repository.WatchlistRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val bullsageApi: BullsageApi,
    private val userAuthManager: UserAuthManager,
    private val watchlistRepository: WatchlistRepository
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            bullsageApi.login(
                authRequest = AuthRequest(
                    email = email,
                    password = password
                )
            )
            userAuthManager.saveAuthDetails(
                userAuthDetails = UserAuthDetails(
                    token = "token", // TODO: implement real token (depends on backend guy)
                    email = email
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.getErrorMessage())
        } catch (e: Exception) {
            Result.Error()
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            bullsageApi.signup(
                authRequest = AuthRequest(
                    email = email,
                    password = password
                )
            )
            userAuthManager.saveAuthDetails(
                userAuthDetails = UserAuthDetails(
                    token = "token", // TODO: implement real token (depends on backend guy)
                    email = email
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.getErrorMessage())
        } catch (e: Exception) {
            Result.Error()
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            userAuthManager.deleteAuthDetails()
            watchlistRepository.removeAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}