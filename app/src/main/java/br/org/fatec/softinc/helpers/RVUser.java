package br.org.fatec.softinc.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import br.org.fatec.softinc.R;
import br.org.fatec.softinc.models.User;

public class RVUser extends RecyclerView.Adapter<RVUser.UserViewHolder> {
    private OnUserListener mOnUserListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercard,parent,false);
        return new UserViewHolder(v, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.cardUserName.setText(users.get(position).nome);
        holder.cardUserCpf.setText(users.get(position).cpf);

        new DownloadImageTask(holder.cardUserAvatar).execute(users.get(position).avatar);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    private List<User> users;
    private LayoutInflater mInflater;
    public RVUser(List<User> users, OnUserListener onUserListener){

        this.users = users;
        this.mOnUserListener = onUserListener;
    }

    public void addItem(User user)
    {
        users.add(user);
        int index = users.size()-1;
        this.notifyItemInserted(index);
    }

    public User getUser(int position){
        return users.get(position);
    }

    public static class UserViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardUser;
        TextView cardUserName;
        TextView cardUserCpf;
        ImageView cardUserAvatar;

        OnUserListener onUserListener;

        UserViewHolder(View itemView, OnUserListener onUserListener){
            super(itemView);
            cardUser  = (CardView)itemView.findViewById(R.id.card_user);
            cardUserName = (TextView)itemView.findViewById(R.id.card_user_name);
            cardUserCpf = (TextView)itemView.findViewById(R.id.card_user_cpf);
            cardUserAvatar = (ImageView)itemView.findViewById(R.id.card_user_avatar);

            this.onUserListener = onUserListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserListener.onUserClick(getAdapterPosition());
        }
    }

    public interface OnUserListener{
        void onUserClick(int position);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
