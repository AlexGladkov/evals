package com.dev.nicehash.app.activities

import android.os.Bundle
import android.util.Log
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.base.BaseActivity
import com.dev.nicehash.domain.models.Configuration
import com.dev.nicehash.domain.repositories.ConfigurationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for notifications settings
 */
class NotificationsActivity : BaseActivity() {
    private val TAG = NotificationsActivity::class.java.simpleName

    // MARK: - Presenter setup
    @Inject lateinit var configurationRepository: ConfigurationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(activity = this@NotificationsActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ configuration ->
                    setupConfiguration(configuration = configuration)
                }, { error ->
                    error.printStackTrace()
                    setupConfiguration(configuration = Configuration.defaultInstance())
                })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
    }

    private lateinit var configuration: Configuration
    private fun setupConfiguration(configuration: Configuration) {
        this.configuration = configuration
        Log.e(TAG, "configuration bottom ${configuration.isBottom}")
    }
}
