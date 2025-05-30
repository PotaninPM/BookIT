package com.prod.bookit.presentation.screens.welcome.register

import com.yandex.authsdk.YandexAuthResult

fun handleYandexAuthResult(result: YandexAuthResult): String? {
    return when (result) {
        is YandexAuthResult.Success -> result.token.value
        is YandexAuthResult.Failure -> null
        YandexAuthResult.Cancelled -> null
    }
}