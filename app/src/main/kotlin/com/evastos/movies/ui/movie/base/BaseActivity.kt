package com.evastos.movies.ui.movie.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.evastos.movies.data.network.connectivity.NetworkConnectivityReceiver
import com.evastos.movies.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.movies.ui.util.extensions.hideIfShown
import com.evastos.movies.ui.util.extensions.showSnackbarForView
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val SNACKBAR_DELAY_MILLIS = 400L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var baseViewModel: BaseViewModel

    private var networkConnectivityObserver: NetworkConnectivityObserver? = null
    private var snackbar: Snackbar? = null

    private val networkConnectivityReceiver = NetworkConnectivityReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        baseViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(BaseViewModel::class.java)
        networkConnectivityReceiver.observable?.let {
            baseViewModel.observeNetworkConnectivity(it)
        }

        if (this is NetworkConnectivityObserver) networkConnectivityObserver = this
    }

    override fun onStart() {
        super.onStart()
        baseViewModel.networkConnectivityLiveData.observe(this,
            Observer { isConnected ->
                if (isConnected == true) {
                    networkConnectivityObserver?.onNetworkConnectivityAcquired()
                } else {
                    networkConnectivityObserver?.onNetworkConnectivityLost()
                }
            }
        )
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkConnectivityReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkConnectivityReceiver)
    }

    protected fun showSnackbar(
        view: View,
        snackbarMessage: String,
        actionMessage: String? = null,
        action: (() -> Unit)? = null
    ) {
        view.postDelayed({
            snackbar = showSnackbarForView(view, snackbarMessage, actionMessage, action)
        }, SNACKBAR_DELAY_MILLIS)
    }

    protected fun hideSnackbar() {
        snackbar.hideIfShown()
    }
}