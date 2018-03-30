package com.app.noan.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.PaymentHistoryActivity;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.PaymentHistoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 16/2/18.
 */

public class PaymentHistoryAdpater1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int Credit = 0;
    private static final int Debit = 1;
    private static final int LOADING = 2;


    public List<PaymentHistoryModel> paymentHistoryModelList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private String errorMsg;

    PaginationAdapterCallback mCallback;

    public PaymentHistoryAdpater1(PaymentHistoryActivity paymentHistoryActivity) {
        this.context = paymentHistoryActivity;
        this.paymentHistoryModelList = new ArrayList<>();
//        mCallback = (PaginationAdapterCallback) this;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem;

        switch (viewType) {
            case Credit:
                viewItem = inflater.inflate(R.layout.layout_rv_payment_history, parent, false);
                viewHolder = new ItemViewHolder(viewItem);
                break;
            case Debit:
                viewItem = inflater.inflate(R.layout.layout_rv_payment_history1, parent, false);
                viewHolder = new ItemViewHolder(viewItem);
                break;
            case LOADING:
                viewItem = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewItem);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        PaymentHistoryModel paymentHistoryModel = paymentHistoryModelList.get(position);

        switch (getItemViewType(position)) {
            case Credit:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.txtPaymentDetails.setText("Order: Sales from Order #" + paymentHistoryModel.getOrderId());
                itemViewHolder.txtPaymentTransaction.setText("Transaction No:" + paymentHistoryModel.getTransaction_no());
                itemViewHolder.txtPaymentDate.setText(paymentHistoryModel.getCreated());
                itemViewHolder.txtpaymentAmount.setText("$ " + paymentHistoryModel.getWalletBalance());
                break;
            case Debit:
                final ItemViewHolder itemViewHolder1 = (ItemViewHolder) holder;
                if (!paymentHistoryModel.getOrderId().equals("0") && !paymentHistoryModel.getStatus().equals("pending")) {
                    itemViewHolder1.txtPaymentDetails.setText("Order: Wallet balance used for Order #" + paymentHistoryModel.getOrderId());
                } else {
                    itemViewHolder1.txtPaymentDetails.setText("Offer: Wallet balance used for Order #" + paymentHistoryModel.getOffer_id());
                }
                itemViewHolder1.txtPaymentTransaction.setText("Transaction No:" + paymentHistoryModel.getTransaction_no());
                itemViewHolder1.txtPaymentDate.setText(paymentHistoryModel.getCreated());
                itemViewHolder1.txtpaymentAmount.setText("$ " + paymentHistoryModel.getWalletBalance());
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;


        }
    }

    @Override
    public int getItemCount() {
        return paymentHistoryModelList == null ? 0 : paymentHistoryModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == paymentHistoryModelList.size() - 1 && isLoadingAdded) {
            return LOADING;
        } else {
            if (paymentHistoryModelList.get(position).getTransferType().equals("credit")) {
                return Credit;
            } else {
                return Debit;
            }
        }
    }

   /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(PaymentHistoryModel r) {
        paymentHistoryModelList.add(r);
        notifyItemInserted(paymentHistoryModelList.size() - 1);
    }

    public void addAll(List<PaymentHistoryModel> moveResults) {
        for (PaymentHistoryModel result : moveResults) {
            add(result);
        }
    }

    public void remove(PaymentHistoryModel r) {
        int position = paymentHistoryModelList.indexOf(r);
        if (position > -1) {
            paymentHistoryModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PaymentHistoryModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if (paymentHistoryModelList.size() > 0) {
            int position = paymentHistoryModelList.size() - 1;
            PaymentHistoryModel result = getItem(position);
            if (result != null) {
                paymentHistoryModelList.remove(position);
                notifyItemRemoved(position);
            }
        }

    }

    public void clearData() {
        paymentHistoryModelList.removeAll(paymentHistoryModelList);
        notifyDataSetChanged();
    }


    public PaymentHistoryModel getItem(int position) {
        return paymentHistoryModelList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(paymentHistoryModelList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
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

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }


}
