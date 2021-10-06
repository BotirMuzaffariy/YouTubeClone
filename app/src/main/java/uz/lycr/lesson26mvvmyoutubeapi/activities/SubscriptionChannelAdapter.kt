package uz.lycr.lesson26mvvmyoutubeapi.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.databinding.ItemSubscriptionChannelBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.subscriptionchannel.SubscriptionChannelM

class SubscriptionChannelAdapter(
    var list: List<SubscriptionChannelM>,
    var listener: SubscriptionClickListener
) :
    RecyclerView.Adapter<SubscriptionChannelAdapter.SubscriptionChannelVh>() {

    inner class SubscriptionChannelVh(var itemBinding: ItemSubscriptionChannelBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(item: SubscriptionChannelM) {
            itemBinding.tvChannelName.text = item.name
            itemBinding.vIndicator.visibility = if (item.isOnline) View.VISIBLE else View.INVISIBLE
            Picasso.get().load(item.imgUrl).placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .into(itemBinding.ivChannelImg)

            itemBinding.root.setOnClickListener {
                listener.onItemClick(item.channelId)
            }
        }
    }

    interface SubscriptionClickListener {
        fun onItemClick(channelId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionChannelVh {
        return SubscriptionChannelVh(
            ItemSubscriptionChannelBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SubscriptionChannelVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}