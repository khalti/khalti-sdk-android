package khalti.checkOut.EBanking.chooseBank;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import khalti.R;
import khalti.carbonX.widget.FrameLayout;
import khalti.utils.StringUtil;

public class BankChooserAdapter extends RecyclerView.Adapter<BankChooserAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<BankPojo> banks;
    private List<BankPojo> banksBackUp = new ArrayList<>();

    private BankControls bankControls;

    BankChooserAdapter(Context context, List<BankPojo> banks, BankControls bankControls) {
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
        holder.tvBankName.setText(banks.get(position).getName());
        holder.tvBankId.setText(banks.get(position).getIdx());
        holder.tvBankIcon.setText(StringUtil.getNameIcon(banks.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvBankName, tvBankId, tvBankIcon;
        FrameLayout flContainer;

        MyViewHolder(View itemView) {
            super(itemView);

            tvBankIcon = itemView.findViewById(R.id.tvBankIcon);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvBankId = itemView.findViewById(R.id.tvBankId);
            flContainer = itemView.findViewById(R.id.flContainer);

            flContainer.setOnClickListener(view -> bankControls.chooseBank(((AppCompatTextView) view.findViewById(R.id.tvBankName)).getText() + "",
                    ((AppCompatTextView) view.findViewById(R.id.tvBankId)).getText() + ""));
        }
    }

    interface BankControls {
        void chooseBank(String bankName, String bankId);
    }

    /*Filter logic*/
    int setFilter(String queryText) {
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
