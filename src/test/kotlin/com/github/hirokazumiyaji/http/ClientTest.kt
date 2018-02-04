package com.github.hirokazumiyaji.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test

class ClientTest {
    private val mapper = jacksonObjectMapper()
    private val client = Client(OkHttpClient(), mapper)
    private val server = MockWebServer()

    data class TestModel(val id: Int)

    @Test
    fun testGetOK() {
        val expected = listOf(TestModel(1000), TestModel(2000))
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mapper.writeValueAsString(expected[0]))
        })

        val actual = client.get<TestModel>(server.url("").toString()).block()
        Assert.assertEquals(expected[0], actual)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(mapper.writeValueAsString(expected))
        })
        val actual2 = client.get<List<TestModel>>(server.url("").toString()).block()
        Assert.assertEquals(expected, actual2)
    }
}