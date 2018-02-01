package khalti.checkOut.EBanking.helper;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import khalti.R;
import khalti.carbonX.widget.FrameLayout;
import khalti.checkOut.EBanking.chooseBank.BankPojo;
import khalti.utils.StringUtil;

public class BankChooserAdapter extends RecyclerView.Adapter<BankChooserAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<BankPojo> banks;
    private List<BankPojo> banksBackUp = new ArrayList<>();

    private BankControls bankControls;

    public BankChooserAdapter(Context context, List<BankPojo> banks, BankControls bankControls) {
        this.context = context;
        this.banks = banks;
        banksBackUp.addAll(banks);
        this.bankControls = bankControls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bank_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String shortName = banks.get(position).getShortName();
        String name = banks.get(position).getName();
        String iconName = StringUtil.getNameIcon(banks.get(position).getName());

        holder.tvBankId.setText(banks.get(position).getIdx());
        Picasso.with(context)
                .load(banks.get(position).getLogo())
                .noFade()
                .into(holder.ivBankLogo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.flBankLogo.setVisibility(View.VISIBLE);
                        holder.flBankTextIcon.setVisibility(View.GONE);
                        holder.tvBankName.setText(shortName);
                    }

                    @Override
                    public void onError() {
                        holder.flBankLogo.setVisibility(View.GONE);
                        holder.flBankTextIcon.setVisibility(View.VISIBLE);
                        holder.tvBankIcon.setText(iconName);
                        holder.tvBankName.setText(name);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvBankName, tvBankId, tvBankIcon;
        FrameLayout flContainer, flBankTextIcon, flBankLogo;
        ImageView ivBankLogo;

        MyViewHolder(View itemView) {
            super(itemView);

            tvBankIcon = itemView.findViewById(R.id.tvBankIcon);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvBankId = itemView.findViewById(R.id.tvBankId);
            flContainer = itemView.findViewById(R.id.flContainer);
            flBankLogo = itemView.findViewById(R.id.flBankLogo);
            flBankTextIcon = itemView.findViewById(R.id.flBankTextIcon);
            ivBankLogo = itemView.findViewById(R.id.ivBankLogo);

            flContainer.setOnClickListener(view -> bankControls.chooseBank(((AppCompatTextView) view.findViewById(R.id.tvBankName)).getText() + "",
                    ((AppCompatTextView) view.findViewById(R.id.tvBankId)).getText() + ""));
        }
    }

    public interface BankControls {
        void chooseBank(String bankName, String bankId);
    }

    /*Filter logic*/
    public int setFilter(String queryText) {
        if (queryText.length() > 0) {
            List<BankPojo> filteredAddress = new ArrayList<>();
            for (int i = 0; i < banksBackUp.size(); i++) {
                if (banksBackUp.get(i).getName().toLowerCase().contains(queryText.toLowerCase())) {
                    filteredAddress.add(banksBackUp.get(i));
                }
            }
            banks.clear();
            banks.addAll(filteredAddress);
            notifyDataSetChanged();
            return banks.size();
        } else {
            banks.clear();
            banks.addAll(banksBackUp);
            notifyDataSetChanged();
            return banks.size();
        }
    }
}
