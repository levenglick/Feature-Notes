package bez.dev.featurenotes.misc

import android.app.Application
import android.content.Context
import android.content.Intent
import bez.dev.featurenotes.data.NoteDatabase
import bez.dev.featurenotes.koin_injection.appModule
import bez.dev.featurenotes.koin_injection.viewModelModule
import bez.dev.featurenotes.services.OnClearFromRecentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAsync()
        initKoin()
    }


    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, viewModelModule))
        }
    }


    private fun initAsync() {
        CoroutineScope(Dispatchers.Default).launch {
            appContext = applicationContext
            database = NoteDatabase.getInstance(appContext)

            startService(Intent(baseContext, OnClearFromRecentService::class.java))
        }
    }


    companion object {
        lateinit var appContext: Context
        lateinit var database: NoteDatabase
    }


}
