package com.prod.bookit.common.di

import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.prod.bookit.presentation.viewModels.BookingViewModel
import com.prod.bookit.presentation.viewModels.CoworkingsViewModel
import com.prod.bookit.presentation.viewModels.ProfileViewModel
import com.prod.bookit.presentation.viewModels.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    single { AuthViewModel(get()) }

    single { SettingsViewModel(get(), get(), get()) }

    single { CoworkingsViewModel(get()) }

    single { ProfileViewModel(get()) }

    singleOf(::BookingViewModel)
}