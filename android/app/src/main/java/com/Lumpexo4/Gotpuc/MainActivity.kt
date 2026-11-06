package com.Lumpexo4.Gotpuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Lumpexo4.Gotpuc.core.di.ServiceLocator
import com.Lumpexo4.Gotpuc.core.theme.AppTheme
import com.Lumpexo4.Gotpuc.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
