package com.khalti.checkOut.EBanking.helper;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.khalti.R;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.StringUtil;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<BankPojo> banks;
    private List<BankPojo> banksBackUp = new ArrayList<>();

    private PublishSubject<HashMap<String, String>> itemClickObservable = PublishSubject.create();

    public BankAdapter(Context context, List<BankPojo> banks) {
        this.context = context;
        this.banks = banks;
        banksBackUp.addAll(banks);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bank_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String iconName = StringUtil.getNameIcon(banks.get(position).getName());

        holder.tvBankId.setText(banks.get(position).getIdx());
        holder.tvBankLogo.setText(banks.get(position).getLogo());
        holder.tvBankName.setText(banks.get(position).getShortName());
        holder.tvBankFullName.setText(banks.get(position).getName());

        if (EmptyUtil.isNotNull(banks.get(position).getLogo()) && EmptyUtil.isNotEmpty(banks.get(position).getLogo())) {
            Picasso.get()
                    .load(banks.get(position).getLogo())
                    .noFade()
                    .into(holder.ivBankLogo, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.flBankLogo.setVisibility(View.VISIBLE);
                            holder.flBankTextIcon.setVisibility(View.GONE);
                            holder.tvBankIcon.setText(iconName);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            holder.flBankLogo.setVisibility(View.GONE);
                            holder.flBankTextIcon.setVisibility(View.VISIBLE);
                            holder.tvBankIcon.setText(iconName);
                        }
                    });
        } else {
            holder.flBankLogo.setVisibility(View.GONE);
            holder.flBankTextIcon.setVisibility(View.VISIBLE);
            holder.tvBankIcon.setText(iconName);
        }
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public Observable<HashMap<String, String>> getItemClickObservable() {
        return itemClickObservable;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvBankName, tvBankFullName, tvBankId, tvBankIcon, tvBankLogo;
        FrameLayout flContainer, flBankTextIcon, flBankLogo;
        ImageView ivBankLogo;

        MyViewHolder(View itemView) {
            super(itemView);

            tvBankIcon = itemView.findViewById(R.id.tvBankIcon);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvBankFullName = itemView.findViewById(R.id.tvBankFullName);
            tvBankId = itemView.findViewById(R.id.tvBankId);
            flContainer = itemView.findViewById(R.id.flContainer);
            flBankLogo = itemView.findViewById(R.id.flBankLogo);
            flBankTextIcon = itemView.findViewById(R.id.flBankTextIcon);
            ivBankLogo = itemView.findViewById(R.id.ivBankLogo);
            tvBankLogo = itemView.findViewById(R.id.tvBankLogo);

            flContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickObservable.onNext(new HashMap<String, String>() {{
                        put("idx", tvBankId.getText() + "");
                        put("name", tvBankFullName.getText() + "");
                        put("icon", tvBankIcon.getText() + "");
                        put("logo", tvBankLogo.getText() + "");
                    }});
                }
            });
        }
    }

    public Integer setFilter(String queryText) {
        Integer count;
        if (queryText.length() > 0) {
            List<BankPojo> filteredBanks = new ArrayList<>();
            for (int i = 0; i < banksBackUp.size(); i++) {
                if (banksBackUp.get(i).getName().toLowerCase().contains(queryText.toLowerCase()) ||
                        banksBackUp.get(i).getShortName().toLowerCase().contains(queryText.toLowerCase())) {
                    filteredBanks.add(banksBackUp.get(i));
                }
            }
            banks.clear();
            banks.addAll(filteredBanks);
            count = filteredBanks.size();
        } else {
            banks.clear();
            banks.addAll(banksBackUp);
            count = -1;
        }
        notifyDataSetChanged();
        return count;
    }
}
