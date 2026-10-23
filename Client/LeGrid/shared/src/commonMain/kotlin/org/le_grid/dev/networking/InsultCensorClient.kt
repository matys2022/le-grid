package org.le_grid.dev.networking

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

import org.le_grid.dev.util.NetworkError
import org.le_grid.dev.util.Result

class InsultCensorClient (
    private val httpClient: HttpClient
){
    suspend fun censorWords(uncensored: String) : Result<String, NetworkError> {
        val response = try{
            httpClient.get(
                urlString = "https://www.purgomalum.com/service/json"
            ){
                parameter("text", uncensored)
                contentType(ContentType.Application.Json)
            }
        }catch (e: UnresolvedAddressException){
            return Result.Error(NetworkError.NO_INTERNET)
        }catch (e: SerializationException){
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value){
            in 200 .. 299 -> {

                var censoredText = response.bodyAsText();

                try{
                    censoredText = response.body<CensoredText>().result;
                    Logger.a(tag = "ParsingSuccess") {
                        censoredText
                    }
                }catch (e : Exception){
                    Logger.a(tag = "ParsingCrash") {
                        e.message.toString()
                    }
                }


                Logger.a(tag = "JustInfo") {
                    response.headers.entries()
                        .joinToString(separator = "\n") { "${it.key} : ${it.value}" }
                }
                Result.Success(censoredText)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500 .. 599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

}