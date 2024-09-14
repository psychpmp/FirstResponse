package com.example.android.firstresponse

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FirstAidKitAdapter(private val firstAidKits: List<FirstAidKit>) : RecyclerView.Adapter<FirstAidKitAdapter.FirstAidKitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstAidKitViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_first_aid_kit, parent, false)
        return FirstAidKitViewHolder(view)
    }

    override fun onBindViewHolder(holder: FirstAidKitViewHolder, position: Int) {
        val firstAidKit = firstAidKits[position]
        holder.bind(firstAidKit)
    }

    override fun getItemCount(): Int = firstAidKits.size

    inner class FirstAidKitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kitImage: ImageView = itemView.findViewById(R.id.kitImage)
        private val kitTitle: TextView = itemView.findViewById(R.id.kitTitle)
        private val kitDescription: TextView = itemView.findViewById(R.id.kitDescription)
        private val kitButton: Button = itemView.findViewById(R.id.kitButton)

        fun bind(firstAidKit: FirstAidKit) {
            kitImage.setImageResource(firstAidKit.imageResId)
            kitTitle.text = firstAidKit.title
            kitDescription.setText(firstAidKit.descriptionResId) // Use setText() with resource ID
            kitButton.text = firstAidKit.buttonText

            kitButton.setOnClickListener {
                val url = "https://shopee.ph" // Replace with the specific Shopee URL if needed
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }
    }
}
