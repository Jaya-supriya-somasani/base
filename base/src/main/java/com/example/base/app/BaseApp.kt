package com.example.base.app

import androidx.lifecycle.LifecycleEventObserver
import com.google.firebase.FirebaseApp
import dagger.android.DaggerApplication

/***
 * Base App for All narayana Applications which extends [DaggerApplication]
 * Use this app when your app is using [Dagger] for dependency injection
 */
abstract class BaseApp : DaggerApplication() {
}