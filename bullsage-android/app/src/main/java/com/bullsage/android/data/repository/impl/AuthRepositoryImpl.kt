package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.auth.UserAuthManager
import com.bullsage.android.data.db.WatchlistDao
import com.bullsage.android.data.model.AuthRequest
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.UserAuthDetails
import com.bullsage.android.data.model.getErrorMessage
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val bullsageApi: BullsageApi,
    private val userAuthManager: UserAuthManager,
    private val watchlistDao: WatchlistDao
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val response = bullsageApi.login(
                authRequest = AuthRequest(
                    email = email,
                    password = password
                )
            )
            userAuthManager.saveAuthDetails(
                userAuthDetails = UserAuthDetails(
                    token = response.token,
                    email = email
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.getErrorMessage())
        } catch (_: Exception) {
            Result.Error()
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val response = bullsageApi.signup(
                authRequest = AuthRequest(
                    email = email,
                    password = password
                )
            )
            userAuthManager.saveAuthDetails(
                userAuthDetails = UserAuthDetails(
                    token = response.token,
                    email = email
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.getErrorMessage())
        } catch (_: Exception) {
            Result.Error()
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            bullsageApi.logout()
            userAuthManager.deleteAuthDetails()
            watchlistDao.emptyWatchlist()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}