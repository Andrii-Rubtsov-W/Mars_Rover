package com.portugal1576.marsrover.di

import androidx.room.Room
import com.portugal1576.marsrover.data.api.ApiService
import com.portugal1576.marsrover.data.api.RetrofitInstance
import com.portugal1576.marsrover.data.api.RickMortyRepositoryImpl
import com.portugal1576.marsrover.data.local.FavoritesDatabase
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.repository.CharacterRepository
import com.portugal1576.marsrover.domain.usecase.GetCharacterByIdUseCase
import com.portugal1576.marsrover.domain.usecase.GetCharactersUseCase
import com.portugal1576.marsrover.domain.usecase.GetEpisodeByIdUseCase
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenViewModel
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenViewModel
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ApiService> { RetrofitInstance.api }
    single<CharacterRepository> { RickMortyRepositoryImpl(get()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            FavoritesDatabase::class.java,
            "favorites.db"
        ).build()
    }
    single { get<FavoritesDatabase>().favoriteCharacterDao() }
    single { FavoriteList(get()) }
    viewModel { FavoritesScreenViewModel(get()) }

    factory { GetCharactersUseCase(get()) }
    factory { GetCharacterByIdUseCase(get()) }

    viewModel { StartScreenViewModel(get(), get()) }

    factory { GetEpisodeByIdUseCase(get()) }
    viewModel {
        DetailsScreenViewModel(
            savedStateHandle = get(),
            getCharacterById = get(),
            getEpisodeById = get(),
            favoriteList = get()
        )
    }
}
