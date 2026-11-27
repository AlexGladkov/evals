package com.dev.nicehash.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.adapters.WorkerHubAdapter
import com.dev.nicehash.app.presenters.WorkerPresenter
import com.dev.nicehash.app.views.MainView
import com.dev.nicehash.app.views.WorkerView
import com.dev.nicehash.base.BaseContainer
import com.dev.nicehash.domain.models.WorkerHub
import com.dev.nicehash.domain.repositories.MinerRepository
import com.dev.nicehash.enums.Keys
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for workers tab
 */
class WorkersFragment: BaseContainer(), WorkerView {

    // MARK: - Injects
    @Inject lateinit var minerRepository: MinerRepository

    // MARK: - Presenter setup
    @InjectPresenter
    lateinit var workersPresenter: WorkerPresenter
    @ProvidePresenter
    fun provideWorkerPresenter(): WorkerPresenter {
        return WorkerPresenter(minerRepository = minerRepository)
    }

    // MARK: - UI Setup
    private val mAdapter = WorkerHubAdapter()

    companion object {
        fun getNewInstance(name: String, miner: String, minerID: Int): WorkersFragment {
            return BaseContainer.getNewInstance(name = name, miner = miner, minerID = minerID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(fragment = this@WorkersFragment)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workersPresenter.fetchWorkers(miner = arguments?.getString(Keys.Miner.value).orEmpty(),
                id = arguments?.getInt(Keys.MinerID.value) ?: -1,
                isRefresh = mAdapter.getItems().isEmpty())

        (activity as? MainView)?.displayNavigationInfo(leftValue = "", rightValue = "")
    }

    // MARK: - Base implementation
    override fun doubleTap() {

    }

    override fun updateContainer() {

    }

    // MARK: - View implementation
    override fun setupWorkers(data: List<WorkerHub>) {
        if (mAdapter.hasItems) {
            mAdapter.updateItems(itemsList = data)
        } else {
            mAdapter.setList(dataList = data)
        }
    }

    override fun setupNoItems() {

    }

    override fun startLoading() {

    }

    override fun endLoading() {

    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
