package com.example.yuntechflowerv1.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yuntechflowerv1.FlowerDetail
import com.example.yuntechflowerv1.R
import com.example.yuntechflowerv1.flowers.FlowerItem
import com.example.yuntechflowerv1.util.Utils
import kotlinx.android.synthetic.main.flower_search_item.view.*
import kotlinx.android.synthetic.main.flower_search_item_grid.view.*
import org.w3c.dom.Text

class MyAdapter(
    private val context: Context,
    private val flowerItems: MutableList<FlowerItem>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(private val context: Context, v: View) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        val image = itemView.itemImage
        val textCh = itemView.itemtextCh
        val textEn=itemView.itemtextEn
        private var flowerIItem: FlowerItem? = null

        init {
            v.setOnClickListener(this)
        }

        fun bindPhoto(flowerItem: FlowerItem) {
            this.flowerIItem = flowerItem
            image.setImageDrawable(Utils.getDrawable(context, "flower${flowerItem.index}_0"))
            textCh.text = flowerItem.nameCh
            textEn.text=flowerItem.nameEn
        }

        override fun onClick(view: View) {
            //Toast.makeText(context, "click${this.flowerIItem?.index}", Toast.LENGTH_SHORT) .show()
            val intent = Intent(context, FlowerDetail::class.java)
            intent.putExtra("item", this.flowerIItem)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val example = inflater.inflate(R.layout.flower_search_item_grid, parent, false)
        return MyViewHolder(context, example)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindPhoto(flowerItems[position])
    }

    override fun getItemCount(): Int {
        return flowerItems.size
    }
}