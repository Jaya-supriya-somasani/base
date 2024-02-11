package com.example.base.app
import dagger.android.DaggerApplication
/***
 * Base App for All narayana Applications which extends [DaggerApplication]
 * Use this app when your app is using [Dagger] for dependency injection
 */
abstract class BaseApp : DaggerApplication() {
}