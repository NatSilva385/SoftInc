package br.org.fatec.softinc.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.org.fatec.softinc.R;
import br.org.fatec.softinc.models.User;

public class RVUser extends RecyclerView.Adapter<RVUser.UserViewHolder> {
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercard,parent,false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.cardUserName.setText(users.get(position).nome);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    List<User> users;
    public RVUser(List<User> users){
        this.users = users;
    }

    public static class UserViewHolder extends  RecyclerView.ViewHolder{
        CardView cardUser;
        TextView cardUserName;

        UserViewHolder(View itemView){
            super(itemView);
            cardUser  = (CardView)itemView.findViewById(R.id.card_user);
            cardUserName = (TextView)itemView.findViewById(R.id.card_user_name);
        }
    }
}
