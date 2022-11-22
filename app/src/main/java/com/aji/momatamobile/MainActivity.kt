package com.aji.momatamobile

import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aji.momatamobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var idChip: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idChip = "785495"
        showDeviceMessage()

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
        binding.webView.loadUrl("https://momataiot.000webhostapp.com/webmomata.php?api_key=j5HmvSxl14&idchip=$idChip")
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
                .setPositiveButton(R.string.yes) { _, _ ->
                    finish()
                }.setNegativeButton(R.string.no, null)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_idchip -> {
                showDialogDeviceId()
                true
            }
            R.id.menu_refresh -> {
                loadUrl()
                showDeviceMessage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogDeviceId() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.menu_idchip)

        val input = EditText(this)
        input.setHint(R.string.enter_device_id)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            idChip = input.text.toString()
            loadUrl()
            showDeviceMessage()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showDeviceMessage() {
        Toast.makeText(this, getString(R.string.conected_to_device, idChip), Toast.LENGTH_LONG)
            .show()
    }


}