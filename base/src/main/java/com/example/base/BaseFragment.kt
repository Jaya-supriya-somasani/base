package com.example.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Base Fragment of all the fragments
 *
 * @param <VM>
 * @param <Binding>
</Binding></VM> */
abstract class BaseFragment<Binding : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    HasAndroidInjector {
    var baseActivity: BaseActivity<*, *>? = null

    @Suppress("PropertyName")
    protected abstract val TAG: String

    @Inject
    lateinit var viewModel: VM

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var dataBinding: Binding

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Base:onCreate - $TAG")
        try {
            /**
             * Dependency Injection
             */
            performDependencyInjection()
            super.onCreate(savedInstanceState)
        } catch (e: Exception) {
            throw Exception(TAG, e)
        }
    }

    private fun performDependencyInjection() {
        try {
            AndroidSupportInjection.inject(this)
        } catch (e: Exception) {
            Log.d(TAG, "$e")
//            showToast("DependencyInjection Failed")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            dataBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
            dataBinding.lifecycleOwner = viewLifecycleOwner
        } catch (e: Exception) {
            throw RuntimeException(TAG, e)
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        initObservers(viewLifecycleOwner)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        Log.d(TAG,"Base:onAttach - $TAG")
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
            context.onAttachFragment(this)
//            context.onFragmentAttached()
        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
        Log.d(TAG,"Base:onDetach - $TAG")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"Base:onResume - $TAG")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"Base:onPause - $TAG")
    }
    abstract fun initObservers(viewLifecycleOwner: LifecycleOwner)
    abstract fun setUp()
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

//    fun showToast(toastMessage: String, toastType: ToastType = ToastType.NONE) {
//        viewModel.showToastMessage(toastMessage, toastType)
//    }

}