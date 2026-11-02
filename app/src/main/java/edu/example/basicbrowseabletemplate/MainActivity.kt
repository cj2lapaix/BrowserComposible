package edu.example.basicbrowseabletemplate

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.compose.material.icons.Icons
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import edu.example.basicbrowseabletemplate.ui.theme.BasicBrowseableTemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicBrowseableTemplateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BasicBrowser(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BasicBrowser(modifier: Modifier = Modifier) {

    var url by remember { mutableStateOf("") }
    var currentUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = url,
                onValueChange = { url = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("Enter website address") }
            )

            IconButton(
                onClick = {
                    currentUrl = parseUrl(url)
                }
            ){

            }
        }


        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->

                WebView(context).apply {
                    Log.d("Browser", "browser")
                    setBackgroundColor(111111)
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(
                            view: WebView?,
                            url: String?
                        ) {
                            super.onPageFinished(view, url)
                            url?.let {
                                this@apply.evaluateJavascript(
                                    "document.location.href"
                                ) { }
                            }
                        }
                    }
                }
            },

            update = { webView ->

                if (currentUrl.isNotEmpty() &&
                    webView.url != currentUrl
                ) {
                    webView.loadUrl(currentUrl)
                }
            }
        )
    }
}

fun parseUrl(url: String): String {
    return if (url.startsWith("https://")) {
        url
    } else {
        "https://$url"
    }
}



@Preview(showBackground = true)
@Composable
fun BasicBrowserPreview() {
    BasicBrowser()
}
