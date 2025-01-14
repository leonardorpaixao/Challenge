package paixao.leonardo.marvel.heroes.feature.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

val Context.unwrapped: Context
    get() {
        var ctx = this
        while (ctx !is FragmentActivity && ctx is ContextWrapper) {
            ctx = ctx.baseContext
        }
        return ctx
    }

val View.activity: FragmentActivity
    get() = context.unwrapped as FragmentActivity

val View.lifecycleOwner: LifecycleOwner
    get() = activity

val View.lifecycleScope: LifecycleCoroutineScope
    get() = lifecycleOwner.lifecycleScope

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(coming) = if (coming) this.visibility = View.VISIBLE else visibility = View.GONE

var View.isInvisble
    get() = visibility == View.VISIBLE
    set(coming) = if (!coming) this.visibility = View.VISIBLE else visibility = View.INVISIBLE
