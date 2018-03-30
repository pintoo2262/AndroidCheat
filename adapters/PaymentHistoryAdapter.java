package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.PaymentHistoryActivity;
import com.app.noan.model.Payment;

import java.util.List;

/**
 * Created by smn on 13/9/17.
 */

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ItemViewHolder> {

    Activity context;
    List<Payment> paymentList;

    public PaymentHistoryAdapter(PaymentHistoryActivity paymentHistoryActivity, List<Payment> paymentList) {
        context = paymentHistoryActivity;
        this.paymentList = paymentList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v =
                    LayoutInflater.from(context).inflate(R.layout.layout_rv_payment_history, parent, false);

        } else {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv_payment_history1, parent, false);
        }

        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        Payment payment = paymentList.get(position);
        holder.txtPaymentDetails.setText(payment.getPaymentDescription());
        holder.txtPaymentTransaction.setText(payment.getPaymentTransactionNo());
        holder.txtPaymentDate.setText(payment.getPaymentdate());
        holder.txtpaymentAmount.setText(payment.getPaymentAmount());

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtPaymentDetails, txtPaymentTransaction, txtPaymentDate, txtpaymentAmount;

        public ItemViewHolder(View view) {
            super(view);
            txtPaymentDetails = (TextView) view.findViewById(R.id.txt_Payment_details);
            txtPaymentTransaction = (TextView) view.findViewById(R.id.txt_Payment_tranasction);
            txtPaymentDate = (TextView) view.findViewById(R.id.txt_Payment_date);
            txtpaymentAmount = (TextView) view.findViewById(R.id.txt_Payment_amount);

        }
    }
}
