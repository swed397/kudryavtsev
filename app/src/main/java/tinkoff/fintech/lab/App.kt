package tinkoff.fintech.lab

import android.app.Application
import tinkoff.fintech.lab.di.component.AppComponent
import tinkoff.fintech.lab.di.component.DaggerAppComponent
import tinkoff.fintech.lab.di.module.DbModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().dbModule(DbModule(this)).build()
    }
}