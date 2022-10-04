package com.aji.momatamobile

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aji.momatamobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                setLoadingState(false)
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                if (newProgress == 100) {
                    setLoadingState(false)
                } else {
                    setLoadingState(true)
                }
            }
        }

        binding.webView.settings.javaScriptEnabled = true

        if (savedInstanceState != null) {
            binding.webView.restoreState(savedInstanceState)
        } else {
            loadUrl()
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.webView.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.webView.visibility = View.VISIBLE
        }
    }

    private fun loadUrl() {
        binding.webView.loadUrl("https://momataiot.000webhostapp.com/webmomata.php?api_key=j5HmvSxl14&idchip=785495")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        binding.webView.saveState(outState)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    finish()
                }.setNegativeButton(android.R.string.no, null)
                .show()
        }

    }
}