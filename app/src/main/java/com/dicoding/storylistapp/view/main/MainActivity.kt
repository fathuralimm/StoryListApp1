package com.dicoding.storylistapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storylistapp.view.ViewModelFactory
import com.dicoding.storylistapp.view.add.AddstoryActivity
import com.dicoding.storylistapp.view.welcome.ListAdapter
import com.example.storylistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, ListAdapter::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
        setButtonAdd()
    }

    private fun getSession() {
        viewmodel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                starActivity(Intent(this, ListAdapter::class.java))
                finish()
            } else {
                viewModel.getStoreies(user.token)
            }
        }
    }

    private fun setButtonAdd() {
        binding.btnAddStory.setOnclickListener {
            val intent = Intent(this, AddstoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        viewModel.storyListItem.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Error -> {
                    showLoading(false)
                }

                is Result.Succes -> {
                    showLoading(false)
                    adapter = StoriesAdapter(it.data)
                    binding.rvStories.adapter = adapter
                }
            }
        }
    }

    private fun showloading(isLoading: Boolean) {
        binding.rvStories.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (item.itemId == R.id.Logout) {
            viewModel.Logout()
        }
        return super.onOptionsItemSelected(item)
    }
}