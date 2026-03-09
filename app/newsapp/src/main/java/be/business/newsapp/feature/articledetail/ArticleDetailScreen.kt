package be.business.newsapp.feature.articledetail

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailsScreen(
    articleUrl: String
) {
    val safeUrl = articleUrl.takeIf { it.startsWith("http://") || it.startsWith("https://") }

    if (safeUrl.isNullOrBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Article link is not available.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
        return
    }

    var loading by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = false
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            loading = false
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            progress = newProgress / 100f
                        }
                    }
                    loadUrl(safeUrl)
                }
            },
            update = { webView ->
                if (webView.url != safeUrl) {
                    webView.loadUrl(safeUrl)
                }
            }
        )

        if (loading || progress < 1f) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
