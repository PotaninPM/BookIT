package com.prod.bookit.common.di

import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.prod.bookit.presentation.viewModels.BookingViewModel
import com.prod.bookit.presentation.viewModels.CoworkingsViewModel
import com.prod.bookit.presentation.viewModels.ProfileViewModel
import com.prod.bookit.presentation.viewModels.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { AuthViewModel(get()) }

    single { SettingsViewModel(get(), get(), get()) }

    viewModel { CoworkingsViewModel(get()) }

    viewModel { ProfileViewModel(get()) }

    viewModelOf(::BookingViewModel)
}