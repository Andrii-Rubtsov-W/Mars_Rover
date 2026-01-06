package com.portugal1576.marsrover.di

import com.portugal1576.marsrover.data.model.PlayElement
import com.portugal1576.marsrover.data.api.ApiService
import com.portugal1576.marsrover.data.api.MarsRoverRepository
import com.portugal1576.marsrover.data.api.RetrofitInstance
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenViewModel
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenViewModel
import com.portugal1576.marsrover.presentation.screens.list_screen.ListScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ApiService> { RetrofitInstance.api }

    singleOf(::MarsRoverRepository)

    viewModel { ListScreenViewModel() }

    viewModel { FavoritesScreenViewModel() }

    viewModel { (name: PlayElement) ->
        DetailsScreenViewModel(
            repository = get()
        )
    }
}