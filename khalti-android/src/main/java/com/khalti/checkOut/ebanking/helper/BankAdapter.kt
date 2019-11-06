package com.khalti.checkOut.ebanking.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.khalti.R
import com.khalti.signal.Signal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.StringUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bank_item.view.*
import java.util.*

class BankAdapter(private val banks: MutableList<BankPojo>) : RecyclerView.Adapter<BankAdapter.MyViewHolder>() {
    private val banksBackUp = ArrayList<BankPojo>()
    private lateinit var context: Context

    private val signal = Signal<Map<String, String>>()

    init {
        banksBackUp.addAll(banks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.bank_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val iconName = StringUtil.getNameIcon(banks[position].name)

        holder.tvBankId.text = banks[position].idx
        holder.tvBankLogo.text = banks[position].logo
        holder.tvBankName.text = banks[position].shortName
        holder.tvBankFullName.text = banks[position].name

        if (EmptyUtil.isNotNull(banks[position].logo) && EmptyUtil.isNotEmpty(banks[position].logo)) {
            Picasso.get()
                    .load(banks[position].logo)
                    .noFade()
                    .into(holder.ivBankLogo, object : Callback {
                        override fun onSuccess() {
                            holder.flBankLogo.visibility = View.VISIBLE
                            holder.flBankTextIcon.visibility = View.GONE
                            holder.tvBankIcon.text = iconName
                        }

                        override fun onError(e: Exception) {
                            e.printStackTrace()
                            holder.flBankLogo.visibility = View.GONE
                            holder.flBankTextIcon.visibility = View.VISIBLE
                            holder.tvBankIcon.text = iconName
                        }
                    })
        } else {
            holder.flBankLogo.visibility = View.GONE
            holder.flBankTextIcon.visibility = View.VISIBLE
            holder.tvBankIcon.text = iconName
        }

        holder.flContainer.setOnClickListener {
            signal.emit(object : HashMap<String, String>() {
                init {
                    put("idx", holder.tvBankId.text.toString() + "")
                    put("name", holder.tvBankFullName.text.toString() + "")
                    put("icon", holder.tvBankIcon.text.toString() + "")
                    put("logo", holder.tvBankLogo.text.toString() + "")
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return banks.size
    }

    fun getClickedItem(): Signal<Map<String, String>> {
        return signal
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvBankName: AppCompatTextView = itemView.tvBankName
        var tvBankFullName: AppCompatTextView = itemView.tvBankFullName
        var tvBankId: AppCompatTextView = itemView.tvBankId
        var tvBankIcon: AppCompatTextView = itemView.tvBankIcon
        var tvBankLogo: AppCompatTextView = itemView.tvBankLogo
        var flContainer: FrameLayout = itemView.flContainer
        var flBankTextIcon: FrameLayout = itemView.flBankTextIcon
        var flBankLogo: FrameLayout = itemView.flBankLogo
        var ivBankLogo: ImageView = itemView.ivBankLogo
    }

    fun setFilter(queryText: String): Int? {
        val count: Int?
        count = if (queryText.isNotEmpty()) {
            val filteredBanks = ArrayList<BankPojo>()
            for (i in banksBackUp.indices) {
                if (banksBackUp[i].name.toLowerCase().contains(queryText.toLowerCase()) || banksBackUp[i].shortName.toLowerCase().contains(queryText.toLowerCase())) {
                    filteredBanks.add(banksBackUp[i])
                }
            }
            banks.clear()
            banks.addAll(filteredBanks)
            filteredBanks.size
        } else {
            banks.clear()
            banks.addAll(banksBackUp)
            -1
        }
        notifyDataSetChanged()
        return count
    }
}
