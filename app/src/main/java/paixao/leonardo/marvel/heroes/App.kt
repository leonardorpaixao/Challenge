package paixao.leonardo.marvel.heroes

import android.app.Application
import org.kodein.di.DI
import org.kodein.di.DIAware
import paixao.leonardo.marvel.heroes.feature.network.di.NetworkingModule
import paixao.leonardo.marvel.heroes.lib.di.DataModule

class App : Application(), DIAware {
    override val di = DI {
        import(NetworkingModule.injections)
        import(DataModule.injections)
    }
}