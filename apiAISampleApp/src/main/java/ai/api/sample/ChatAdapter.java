package ai.api.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by thales on 2/23/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ai.api.sample.ChatAdapter.ViewHolder> {
    private List<Chat> mDataSet;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;


    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private TextView mUsernameView;
        private TextView mTimeView;

        private RelativeLayout mResultView;
        private ImageView productImage;
        private TextView productName;
        private TextView totalPrice;
        private TextView makeOrder;

        private ImageView mapView;



        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tvMessage);
            mUsernameView = (TextView)itemView.findViewById(R.id.tvUsername);
            mTimeView = (TextView)itemView.findViewById(R.id.timeStamp);

            mResultView = (RelativeLayout)itemView.findViewById(R.id.result_layout);
            productImage = (ImageView)itemView.findViewById(R.id.product_img);
            productName = (TextView)itemView.findViewById(R.id.product_name);
            totalPrice = (TextView)itemView.findViewById(R.id.total_price);
            makeOrder = (TextView)itemView.findViewById(R.id.buy);

            mapView = (ImageView)itemView.findViewById(R.id.map);


        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    public ChatAdapter(List<Chat> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public ai.api.sample.ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_chat_right, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_chat_left, parent, false);
            /*try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(parent.getContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position).getId()==1)
            return CHAT_RIGHT;

        return CHAT_LEFT;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Chat chat = mDataSet.get(position);
        if(chat.getId() == 1) {
            holder.mapView.setVisibility(View.GONE);
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(chat.getMessage());
            //holder.mImageView.setImageBitmap(null);
            holder.mResultView.setVisibility(View.GONE);}
        else if(chat.getId() == 2) {
            holder.mapView.setVisibility(View.GONE);
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(chat.getMessage());
            //holder.mImageView.setImageBitmap(null);
            holder.mResultView.setVisibility(View.GONE);
        }else if (chat.getId()==3){
            holder.mapView.setVisibility(View.GONE);
            String sekil = chat.getSekil();
            if (chat.getSekil().equals("şişe")){
                sekil = "sise";
            }
            String uri = "android.resource://ai.api.sample/raw/"+sekil + chat.getUrun()+"";
            holder.productImage.setImageURI(Uri.parse(uri));

            holder.makeOrder.setText("Sipariş Et!");
            holder.mResultView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(null);
            holder.mTextView.setVisibility(View.GONE);
            holder.productName.setText(chat.getNumber()+" "+ chat.getType()+" " +chat.getSekil() +" "+ chat.getUrun());
            holder.totalPrice.setText(1.89 * 24 * Integer.parseInt(chat.getNumber())+"₺");
            holder.makeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(v.getRootView().getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Usta,")
                            .setContentText("Siparişini aldım!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    holder.makeOrder.setText("Sipariş Alındı!");
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });
        }
        else if(chat.getId() == 4){
            holder.mResultView.setVisibility(View.GONE);
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mapView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(chat.getMessage());
            Log.d("chatId", chat.getId()+"");
            final String uri = "geo:"+chat.getLat()+","+chat.getLng()+"?q="+chat.getLat()+"+,"+chat.getLng()+"(Coca Cola İçecek)";

            holder.mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    holder.itemView.getContext().startActivity(intent);
                }
            });



        }

        holder.mUsernameView.setText(chat.getUsername());
        holder.mTimeView.setText(chat.getTime());

       /* try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(holder.mTextView.getContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
