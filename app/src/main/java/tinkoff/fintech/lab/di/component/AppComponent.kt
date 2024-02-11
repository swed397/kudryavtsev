package tinkoff.fintech.lab.di.component

import dagger.Component
import tinkoff.fintech.lab.di.module.DbModule
import tinkoff.fintech.lab.di.module.NetworkModule
import tinkoff.fintech.lab.ui.details.DetailsViewModel
import tinkoff.fintech.lab.ui.list.ListViewModel
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, DbModule::class])
@Singleton
interface AppComponent {

    val listViewModelFactory: ListViewModel.Factory
    val detailsViewModelFactory: DetailsViewModel.Factory
}