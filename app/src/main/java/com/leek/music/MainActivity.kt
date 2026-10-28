package com.leek.music

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private val backgroundColor = Color.rgb(11, 13, 18)
    private val accentColor = Color.rgb(88, 166, 255)
    private lateinit var browser: WebView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = backgroundColor
        window.navigationBarColor = backgroundColor

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 16)
            setBackgroundColor(backgroundColor)
        }

        val logo = TextView(this).apply {
            text = "LEEK"
            textSize = 36f
            setTextColor(Color.WHITE)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            letterSpacing = 0.16f
        }
        root.addView(logo)

        val tagline = TextView(this).apply {
            text = "YOUR MUSIC. YOUR WORLD."
            textSize = 11f
            letterSpacing = 0.15f
            setTextColor(Color.LTGRAY)
            setPadding(0, 4, 0, 24)
        }
        root.addView(tagline)

        val search = EditText(this).apply {
            hint = "Search songs or artists"
            setHintTextColor(Color.GRAY)
            setTextColor(Color.WHITE)
            textSize = 16f
            singleLine = true
            setPadding(16, 14, 16, 14)
            setBackgroundColor(Color.rgb(28, 31, 40))
            imeOptions = EditorInfo.IME_ACTION_SEARCH
        }
        root.addView(
            search,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val searchButton = Button(this).apply {
            text = "SEARCH MUSIC"
            setTextColor(Color.BLACK)
            setBackgroundColor(accentColor)
            isAllCaps = false
        }
        val buttonParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        buttonParams.setMargins(0, 12, 0, 12)
        root.addView(searchButton, buttonParams)

        status = TextView(this).apply {
            text = "Search for your next favourite song."
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(0, 4, 0, 12)
        }
        root.addView(status)

        browser = WebView(this).apply {
            setBackgroundColor(backgroundColor)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }

        root.addView(
            browser,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)

        fun searchMusic() {
            val query = search.text.toString().trim()
            if (query.isEmpty()) {
                status.text = "Type a song or artist first."
                return
            }
            status.text = "Searching music..."
            browser.loadUrl(
                "https://music.youtube.com/search?q=" + Uri.encode(query)
            )
        }

        searchButton.setOnClickListener { searchMusic() }
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMusic()
                true
            } else {
                false
            }
        }
    }

    override fun onBackPressed() {
        if (::browser.isInitialized && browser.canGoBack()) {
            browser.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::browser.isInitialized) {
            browser.destroy()
        }
        super.onDestroy()
    }
}
