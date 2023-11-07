package com.dicoding.storylistapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storylistapp.ListAdapter
import com.dicoding.storylistapp.view.ViewModelFactory
import com.dicoding.storylistapp.view.add.AddStoryActivity
import com.dicoding.storylistapp.view.welcome.WelcomeActivity
import com.example.storylistapp.databinding.ActivityMainBinding
import com.dicoding.storylistapp.data.retrofit.Result
import com.example.storylistapp.R

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        getSession()
        setRecyclerView()
        setButtonAdd()
    }


//    private fun getSession() {
//        viewModel.getSession().observe(this) { user ->
//        if (!user.isLogin) {
//            startActivity(Intent(this, WelcomeActivity::class.java))
//            finish()
//        } else {
//            viewModel.getStories(user.token)
//        }
//    }
//}
    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getStories(user.token)
            }
        }
    }

    private fun setButtonAdd() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        viewModel.StoryListItem.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Error -> {
                    showLoading(false)
                }

                is Result.Success -> {
                    showLoading(false)
                    adapter = ListAdapter(it.data)
                    binding.rvStories.adapter = adapter
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.rvStories.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }
}