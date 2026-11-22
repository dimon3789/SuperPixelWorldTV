package com.superpixelworld.tv

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var lastBackPressTime = 0L

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep Xiaomi TV screen awake during gameplay
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableImmersiveMode()

        webView = WebView(this).apply {
            setBackgroundColor(Color.parseColor("#060912"))
            isFocusable = true
            isFocusableInTouchMode = true
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                mediaPlaybackRequiresUserGesture = false
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_DEFAULT
                builtInZoomControls = false
                displayZoomControls = false
            }

            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }

        setContentView(webView)
        webView.loadUrl("file:///android_asset/index.html")
        webView.requestFocus()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableImmersiveMode()
            webView.requestFocus()
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
        webView.requestFocus()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    private fun enableImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Handle Android TV Back button gracefully: send Escape to the game first,
        // and exit app only on double-press when on the main Start screen.
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                webView.evaluateJavascript(
                    "(function(){ var s = document.getElementById('startOverlay'); return s && !s.classList.contains('hidden'); })();"
                ) { isStartScreen ->
                    if (isStartScreen == "true") {
                        val now = System.currentTimeMillis()
                        if (now - lastBackPressTime < 2000L) {
                            finish()
                        } else {
                            lastBackPressTime = now
                            Toast.makeText(
                                this,
                                "Нажмите Назад ещё раз для выхода",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        webView.evaluateJavascript(
                            "window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape', code: 'Escape', keyCode: 27, bubbles: true }));",
                            null
                        )
                    }
                }
            }
            return true
        }

        // Map DPAD_CENTER (OK button on Xiaomi TV remote) cleanly to Enter
        if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            val mapped = KeyEvent(
                event.downTime,
                event.eventTime,
                event.action,
                KeyEvent.KEYCODE_ENTER,
                event.repeatCount,
                event.metaState
            )
            return webView.dispatchKeyEvent(mapped)
        }

        return super.dispatchKeyEvent(event)
    }
}
