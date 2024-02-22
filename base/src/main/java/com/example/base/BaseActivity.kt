package com.example.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


/**
 * Base Activity for the activities
 *
 * @param <Binding>
</Binding> */
abstract class BaseActivity<Binding : ViewDataBinding, VM : BaseViewModel> :
    DaggerAppCompatActivity(),
    LifecycleOwner {

    @Suppress("PropertyName")
    protected abstract val TAG: String

    @Inject
    lateinit var viewModel: VM

    /**
     * Abstract method to init layout of activity
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    lateinit var dataBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)

        try {
            dataBinding = DataBindingUtil.setContentView(this@BaseActivity, getLayoutResource())
        } catch (exception: Exception) {
            throw RuntimeException(TAG, exception)
        }
        dataBinding.lifecycleOwner = this
        setUp()
    }

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    protected abstract fun setUp()
    protected abstract fun initObservers()


}