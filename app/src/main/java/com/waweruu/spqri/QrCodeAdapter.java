package com.waweruu.spqri;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QrCodeAdapter extends RecyclerView.Adapter<QrCodeAdapter.QrHolder> {

    private ArrayList<QrCode> qrList;
    private Context context;

    public QrCodeAdapter(Context context, ArrayList<QrCode> qrList) {
        this.context = context;
        this.qrList = qrList;
    }

    @NonNull
    @Override
    public QrCodeAdapter.QrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_row, parent, false);
        return new QrHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QrCodeAdapter.QrHolder holder, int position) {
//        holder.itemRowBinding.qrType.setText(qrList.get(position).getType());
//        holder.itemRowBinding.qrValue.setText(qrList.get(position).getValue());

        holder.qr_code_type.setText(qrList.get(position).getType());
        holder.qr_code_value.setText(qrList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return qrList.size();
    }

    public class QrHolder extends RecyclerView.ViewHolder {

        //        ItemRowBinding itemRowBinding;
        private TextView qr_code_type, qr_code_value;

        public QrHolder(@NonNull View itemView) {
            super(itemView);

            qr_code_type = itemView.findViewById(R.id.qr_type);
            qr_code_value = itemView.findViewById(R.id.qr_value);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(qr_code_type.getText(), qr_code_value.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO:handle action
//                    Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
//                    switch (qr_code_type.getText().toString()) {
//                        case "URL":
//                            Uri url = Uri.parse(qr_code_value.getText().toString());
//                            Intent webPageIntent = new Intent(Intent.ACTION_VIEW, url);
//                            context.startActivity(webPageIntent);
//                            break;
//
//                        case "Text":
//                            Uri text = Uri.parse(qr_code_value.getText().toString());
//                            Intent webSearchIntent = new Intent(Intent.ACTION_WEB_SEARCH, text);
//                            context.startActivity(webSearchIntent);
//                            break;
//
//                        case "Email":
//                            Uri email = Uri.parse("email:" + qr_code_value.getText().toString());
//                            Intent emailIntent = new Intent(Intent.CATEGORY_APP_EMAIL, email);
//                            context.startActivity(emailIntent);
//                            break;
//                    }
//                }
//            });

//            LayoutInflater layoutInflater = LayoutInflater.from(context);
//            itemRowBinding = ItemRowBinding.inflate(layoutInflater);
        }
    }
}
