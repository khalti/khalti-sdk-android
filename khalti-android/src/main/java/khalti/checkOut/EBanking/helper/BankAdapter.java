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
import java.util.HashMap;
import java.util.List;

import khalti.R;
import khalti.carbonX.widget.FrameLayout;
import khalti.utils.EmptyUtil;
import khalti.utils.LogUtil;
import khalti.utils.StringUtil;
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
        String shortName = banks.get(position).getShortName();
        String name = banks.get(position).getName();
        String iconName = StringUtil.getNameIcon(banks.get(position).getName());

        LogUtil.log("name", name);
        LogUtil.log("short name", shortName);

        holder.tvBankId.setText(banks.get(position).getIdx());
        holder.tvBankLogo.setText(banks.get(position).getLogo());
        if (EmptyUtil.isNotNull(banks.get(position).getLogo()) && EmptyUtil.isNotEmpty(banks.get(position).getLogo())) {
            Picasso.with(context)
                    .load(banks.get(position).getLogo())
                    .noFade()
                    .into(holder.ivBankLogo, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.flBankLogo.setVisibility(View.VISIBLE);
                            holder.flBankTextIcon.setVisibility(View.GONE);
                            holder.tvBankIcon.setText(iconName);
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
        } else {
            holder.flBankLogo.setVisibility(View.GONE);
            holder.flBankTextIcon.setVisibility(View.VISIBLE);
            holder.tvBankIcon.setText(iconName);
            holder.tvBankName.setText(name);
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
        AppCompatTextView tvBankName, tvBankId, tvBankIcon, tvBankLogo;
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
            tvBankLogo = itemView.findViewById(R.id.tvBankLogo);

            flContainer.setOnClickListener(view -> itemClickObservable.onNext(new HashMap<String, String>() {{
                put("idx", tvBankId.getText() + "");
                put("name", tvBankName.getText() + "");
                put("icon", tvBankIcon.getText() + "");
                put("logo", tvBankLogo.getText() + "");
            }}));
        }
    }

    public interface BankControls {
        void chooseBank(String bankName, String bankId);
    }

    public void setFilter(String queryText) {
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
        } else {
            banks.clear();
            banks.addAll(banksBackUp);
        }
        notifyDataSetChanged();
    }
}
