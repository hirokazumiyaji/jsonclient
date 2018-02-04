package com.github.hirokazumiyaji.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import reactor.core.publisher.Mono

open class Client(val http: OkHttpClient, val mapper: ObjectMapper) {
    inline fun <reified T : Any> request(req: Request): Mono<T> {
        return Mono.create<T> {
            try {
                val response = http.newCall(req).execute()
                if (!response.isSuccessful) {
                    throw Exception("response error. status = ${response.code()}")
                }
                val body = response.body()?.string() ?: ""
                it.success(mapper.readValue(body))
            } catch (e: Exception) {
                it.error(e)
                return@create
            }
        }
    }

    inline fun <reified T : Any> get(url: String, headers: Headers? = null): Mono<T> {
        var req = Request.Builder()
            .url(url)
            .get()
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    inline fun <reified T : Any> post(url: String, body: RequestBody, headers: Headers? = null): Mono<T> {
        var req = Request.Builder()
            .url(url)
            .post(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    inline fun <reified T : Any> put(url: String, body: RequestBody, headers: Headers? = null): Mono<T> {
        var req = Request.Builder()
            .url(url)
            .put(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    inline fun <reified T : Any> patch(url: String, body: RequestBody, headers: Headers? = null): Mono<T> {
        var req = Request.Builder()
            .url(url)
            .patch(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    inline fun <reified T : Any> delete(url: String, body: RequestBody? = null, headers: Headers? = null): Mono<T> {
        var req = Request.Builder()
            .url(url)
            .delete(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }
}
