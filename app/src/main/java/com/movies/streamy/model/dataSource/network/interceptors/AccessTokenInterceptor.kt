package com.movies.streamy.model.dataSource.network.interceptors


import androidx.annotation.VisibleForTesting
import com.movies.streamy.model.dataSource.local.dao.UserDao
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class AccessTokenInterceptor @Inject constructor(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val userDao: UserDao,
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return when (request.url.encodedPath) {
            "onboarding/login_driver" -> chain.proceed(request)
            else -> requestNeedAccessToken(chain)
        }
    }

    private fun requestNeedAccessToken(chain: Interceptor.Chain): Response {
        var accessToken = ""
        try {
            accessToken = userDao.getCurrentUser(0)?.token ?: ""
        } catch (e: Exception) {
            Timber.e(e)
        }
        val request = chain.request()

        val authenticatedRequest = request.newBuilder()
            .header("Authorization", accessToken)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}