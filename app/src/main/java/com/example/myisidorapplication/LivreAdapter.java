package com.example.myisidorapplication;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.LivreViewHolder> {

    private ArrayList<Livre> listeLivres;

    public LivreAdapter(ArrayList<Livre> listeLivres) {
        this.listeLivres = listeLivres;
    }

    @NonNull
    @Override
    public LivreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);
        return new LivreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivreViewHolder holder, int position) {
        Livre livre = listeLivres.get(position);

        holder.tvTitreLivre.setText(livre.getTitre());
        holder.tvAuteurLivre.setText("Auteur : " + livre.getAuteur());
        holder.tvIsbnLivre.setText("ISBN : " + livre.getIsbn());

        if (livre.isDisponible()) {
            holder.tvDisponibilite.setText("Disponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvDisponibilite.setText("Indisponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#C62828"));
        }

        // Clic simple : ouvre la fiche détail (Lab 5)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("livre", livre);
            v.getContext().startActivity(intent);
        });

        // AJOUT LAB 6 : Clic long pour modifier le livre sélectionné
        // ÉTAPE MODIFIÉE LAB 7 : Boîte de dialogue pour Choisir entre Modifier et Supprimer
        holder.itemView.setOnLongClickListener(v -> {
            if (v.getContext() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) v.getContext();

                // Création d'une boîte de dialogue d'alerte
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mainActivity);
                builder.setTitle("Options pour : " + livre.getTitre());

                // Liste des choix
                String[] options = {"Modifier le livre", "Supprimer le livre"};

                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Choix 1 : Modifier
                        Intent intent = new Intent(mainActivity, AddEditActivity.class);
                        intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
                        intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
                        intent.putExtra(AddEditActivity.EXTRA_POSITION, position);
                        mainActivity.lancerModification(intent);
                    } else if (which == 1) {
                        // Choix 2 : Supprimer
                        mainActivity.supprimerLivreDeRoom(livre);
                    }
                });

                builder.show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listeLivres != null ? listeLivres.size() : 0;
    }

    public static class LivreViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitreLivre;
        TextView tvAuteurLivre;
        TextView tvIsbnLivre;
        TextView tvDisponibilite;

        public LivreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitreLivre = itemView.findViewById(R.id.tvTitreLivre);
            tvAuteurLivre = itemView.findViewById(R.id.tvAuteurLivre);
            tvIsbnLivre = itemView.findViewById(R.id.tvIsbnLivre);
            tvDisponibilite = itemView.findViewById(R.id.tvDisponibilite);
        }
    }
}