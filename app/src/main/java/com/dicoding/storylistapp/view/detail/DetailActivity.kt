package com.dicoding.storylistapp.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity: AppCompatActivity() {
    private lateinit var listStoryItem: ListStoryItem
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("listStoryItem", ListStoryItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("listStoryItem")!!
        }

        binding.tvTitle.text = listStoryItem.name
        binding.tvDescription.text = listStoryItem.description

        Glide
            .with(this)
            .load(listStoryItem.photoUrl)
            .fitCenter()
            .into(binding.ivStory)
    }
}