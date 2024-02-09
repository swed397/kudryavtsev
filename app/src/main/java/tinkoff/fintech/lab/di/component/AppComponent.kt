package tinkoff.fintech.lab.di.component

import dagger.Component
import tinkoff.fintech.lab.di.module.NetworkModule
import tinkoff.fintech.lab.ui.list.ListViewModel
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface AppComponent {

    val listViewModelFactory: ListViewModel.Factory
}