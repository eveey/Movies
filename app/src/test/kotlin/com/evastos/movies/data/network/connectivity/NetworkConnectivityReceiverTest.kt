package com.evastos.movies.data.network.connectivity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkConnectivityReceiverTest {

    private val context = mock<Context>()
    private val connectivityManager = mock<ConnectivityManager>()
    private val networkInfo = mock<NetworkInfo>()

    private lateinit var networkConnectivityReceiver: NetworkConnectivityReceiver

    @Before
    fun setUp() {
        networkConnectivityReceiver = NetworkConnectivityReceiver()
        whenever(context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(connectivityManager)
        whenever(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
    }

    @Test
    fun onReceive_withConnectivityAction_whenConnected_publishesTrue() {
        whenever(networkInfo.isConnected).thenReturn(true)

        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        networkConnectivityReceiver.observable!!.subscribeBy {
            assertTrue(it)
        }
    }

    @Test
    fun onReceive_withConnectivityAction_whenNotConnected_publishesFalse() {
        whenever(networkInfo.isConnected).thenReturn(false)

        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        networkConnectivityReceiver.observable!!.subscribeBy {
            assertFalse(it)
        }
    }

    @Test
    fun onReceive_withConnectivityAction_withoutActiveNetworkInfo_publishesFalse() {
        whenever(connectivityManager.activeNetworkInfo).thenReturn(null)

        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        networkConnectivityReceiver.observable!!.subscribeBy {
            assertFalse(it)
        }
    }

    @Test
    fun onReceive_withIncorrectAction_doesNothing() {
        whenever(networkInfo.isConnected).thenReturn(true)

        networkConnectivityReceiver.onReceive(context, Intent("Some action"))

        networkConnectivityReceiver.observable!!.toList()
                .subscribeBy {
                    assertTrue(it.isEmpty())
                }
    }

    @Test
    fun onReceive_withIncorrectAction_publishesSubsequentEvents() {
        whenever(networkInfo.isConnected).thenReturn(true)
        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        whenever(networkInfo.isConnected).thenReturn(false)
        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        whenever(connectivityManager.activeNetworkInfo).thenReturn(null)
        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        whenever(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        whenever(networkInfo.isConnected).thenReturn(true)
        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.EXTRA_EXTRA_INFO)
        )
        networkConnectivityReceiver.onReceive(
            context,
            Intent(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        networkConnectivityReceiver.observable!!.toList()
                .subscribeBy {
                    assertEquals(3, it.size)
                    assertTrue(it[0])
                    assertFalse(it[1])
                    assertTrue(it[2])
                }
    }
}
