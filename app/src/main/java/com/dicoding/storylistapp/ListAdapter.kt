package com.dicoding.storylistapp

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storylistapp.view.detail.DetailActivity

class ListAdapter(private val  listStories: List<ListStoryItem>
): RecyclerView.Adapter<StoriesAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemRowBinding):
        RecyclerView.ViewHolder(binding.root){
        private var ivStory: ImageView = itemView.findViewById(R.id.iv_story)
        private var tvName: TextView = itemView.findViewById(R.id.tv_tittle)
        fun bind(listStoryItem: ListStoryItem){
            binding.tvTitle.text = listStoryItem.name

            Glide
                .with(itemView.context)
                .load(listStoryItem.photoUrl)
                .fitCenter()
                .into(binding.ivStory)

            binding.itemRow.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("listStoryItem", listSToryItem)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        pair(ivStory, "photo"),
                        pair(tvName, "name")
                    )
                itemView.context.startActivities(intent, optionsCompat.toBundle())


            }
        }
    }
    override fun onCreateViewHolder(parent: ViwGroup, viewType: Int): StoriesAdapter.ListViewHolder {
        return ListViewHolder(
            ItemRowBinding.inflate(
                layoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoriesAdapter.ListViewHolder, position: Int) {
        val item = listStories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listStories.size
    }
}
