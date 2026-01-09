package com.ptithcm.lexigo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.models.ChatMessage;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    private List<ChatMessage> messages;

    public ChatMessageAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (message.isUser()) {
            holder.tvUserMessage.setText(message.getMessage());
            holder.cardUserMessage.setVisibility(View.VISIBLE);
            holder.cardBotMessage.setVisibility(View.GONE);
        } else {
            holder.tvBotMessage.setText(message.getMessage());
            holder.cardBotMessage.setVisibility(View.VISIBLE);
            holder.cardUserMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMessage, tvBotMessage;
        View cardUserMessage, cardBotMessage;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvBotMessage = itemView.findViewById(R.id.tvBotMessage);
            cardUserMessage = itemView.findViewById(R.id.cardUserMessage);
            cardBotMessage = itemView.findViewById(R.id.cardBotMessage);
        }
    }
}
