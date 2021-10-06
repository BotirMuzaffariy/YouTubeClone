package uz.lycr.lesson26mvvmyoutubeapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.lycr.lesson26mvvmyoutubeapi.databinding.ItemExploreTopBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.exploretop.ExploreTopM

class ExploreTopAdapter(var list: List<ExploreTopM>):RecyclerView.Adapter<ExploreTopAdapter.ExploreTopVh>() {

    inner class ExploreTopVh(var itemBinding: ItemExploreTopBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(item: ExploreTopM) {
            itemBinding.tvCategoryName.text = item.name
            itemBinding.ivIcon.setImageResource(item.icon)
            itemBinding.ivBackground.setImageResource(item.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreTopVh {
        return ExploreTopVh(ItemExploreTopBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExploreTopVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}