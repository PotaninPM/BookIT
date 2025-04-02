package com.prod.bookit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prod.bookit.presentation.screens.RootNavigation
import com.prod.bookit.presentation.theme.FinalDraftForProdTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }
    
    private fun setupUI() {
        enableEdgeToEdge()
        
        setContent {
            FinalDraftForProdTheme {
                KoinAndroidContext {
                    RootNavigation()
                }
            }
        }
    }
}