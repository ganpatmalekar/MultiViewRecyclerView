package com.gsm.multiviewrecyclerview.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.multiviewrecyclerview.R
import com.gsm.multiviewrecyclerview.databinding.ActivityMainBinding
import com.gsm.multiviewrecyclerview.ui.adapter.MainUiAdapter
import com.gsm.multiviewrecyclerview.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductViewModel by viewModels()

    @Inject
    lateinit var mainUiAdapter: MainUiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show splash screen on all Android versions
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setObservers()
    }

    private fun setupUI() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainUiAdapter
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect {
                    when (it) {
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                it.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            Log.d("MainActivity", "setObservers: ${it.data.size}")
                            binding.progressBar.visibility = View.GONE
                            mainUiAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }
}