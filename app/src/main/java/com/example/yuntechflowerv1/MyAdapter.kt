package com.example.yuntechflowerv1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.flowers.FlowerItem
import kotlinx.android.synthetic.main.flower_search_item.view.*

class MyAdapter:RecyclerView.Adapter<MyAdapter.PhotoHolder>(){
    private var FlowerList= FlowerData.allFlower
    private var index:Int=0
    //private val flowerList = mutableListOf<FlowerItem>()
    class PhotoHolder(itemView : View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val image = itemView.imageView
        val text = itemView.textView

        init {
            itemView.setOnClickListener(this)
        }

        fun bindPhoto(item: FlowerItem) {
            image.setImageResource(item.image)
            text.text=item.nameCh
            //text.text = item.text
        }

        override fun onClick(view: View) {

            Toast.makeText(view.context,"click$position",Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context,FlowerDetail::class.java)
            intent.putExtra("index",position)
            view.context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.flower_search_item,parent,false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bindPhoto(FlowerList[position])
        index=position
    }

    override fun getItemCount(): Int {
        return FlowerList.size
    }
}