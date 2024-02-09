package tinkoff.fintech.lab

import android.app.Application
import tinkoff.fintech.lab.di.component.AppComponent
import tinkoff.fintech.lab.di.component.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
    }
}