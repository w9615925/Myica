package uk.ac.tees.mad.w9615925

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PetFoodAdapter(private val petFoodList: List<PetFood>) : RecyclerView.Adapter<PetFoodAdapter.PetFoodViewHolder>() {

    class PetFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(android.R.id.text1)
        val nutritionTextView: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetFoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return PetFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetFoodViewHolder, position: Int) {
        val petFood = petFoodList[position]
        holder.nameTextView.text = petFood.name
        holder.nutritionTextView.text = petFood.nutrition
    }

    override fun getItemCount() = petFoodList.size
}
