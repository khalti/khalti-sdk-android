package com.khalti.checkout.banking.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.khalti.R
import com.khalti.databinding.BankItemBinding
import com.khalti.signal.Signal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.StringUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
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
        private val binding = BankItemBinding.bind(itemView)
        var tvBankName: AppCompatTextView = binding.tvBankName
        var tvBankFullName: AppCompatTextView = binding.tvBankFullName
        var tvBankId: AppCompatTextView = binding.tvBankId
        var tvBankIcon: AppCompatTextView = binding.tvBankIcon
        var tvBankLogo: AppCompatTextView = binding.tvBankLogo
        var flContainer: FrameLayout = binding.flContainer
        var flBankTextIcon: FrameLayout = binding.flBankTextIcon
        var flBankLogo: FrameLayout = binding.flBankLogo
        var ivBankLogo: ImageView = binding.ivBankLogo
    }

    fun filter(queryText: String): Int {
        val count: Int = if (queryText.isNotEmpty()) {
            val filteredBanks = banksBackUp
                    .asSequence()
                    .filter {
                        it.name.lowercase(Locale.getDefault()).contains(queryText.lowercase(Locale.getDefault())) || it.shortName.lowercase(
                            Locale.getDefault()
                        )
                            .contains(queryText.lowercase(Locale.getDefault()))
                    }
                    .toList()

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
