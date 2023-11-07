package com.dicoding.storylistapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storylistapp.data.response.ListStoryItem
import com.dicoding.storylistapp.view.detail.DetailActivity
import com.example.storylistapp.R
import com.example.storylistapp.databinding.ItemRowBinding

class ListAdapter(private val  listStories: List<ListStoryItem>
): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemRowBinding):
        RecyclerView.ViewHolder(binding.root){
        private var ivStory: ImageView = itemView.findViewById(R.id.iv_story)
        private var tvName: TextView = itemView.findViewById(R.id.tv_title)
        fun bind(listStoryItem: ListStoryItem){
            binding.tvTitle.text = listStoryItem.name

            Glide
                .with(itemView.context)
                .load(listStoryItem.photoUrl)
                .fitCenter()
                .into(binding.ivStory)

            binding.itemRow.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("listStoryItem", listStoryItem)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivStory, "photo"),
                        Pair(tvName, "name")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())


            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        return ListViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {
        val item = listStories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listStories.size
    }
}
