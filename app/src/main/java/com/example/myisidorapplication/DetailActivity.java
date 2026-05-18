package com.example.myisidorapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Activation du bouton retour dans la barre d'action
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Liaison avec les éléments du fichier XML (activity_detail.xml)
        TextView tvTitre = findViewById(R.id.tvTitre);
        TextView tvAuteur = findViewById(R.id.tvAuteur);
        TextView tvIsbn = findViewById(R.id.tvIsbn);
        TextView tvDisponibilite = findViewById(R.id.tvDisponibilite);

        // Récupération de l'objet Livre envoyé par l'écran précédent
        Livre livre = (Livre) getIntent().getSerializableExtra("livre");

        // Affichage des informations en utilisant les ressources strings.xml
        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText(getString(R.string.label_auteur, livre.getAuteur()));
            tvIsbn.setText(getString(R.string.label_isbn, livre.getIsbn()));

            if (livre.isDisponible()) {
                tvDisponibilite.setText(R.string.label_statut_disponible);
                tvDisponibilite.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            } else {
                tvDisponibilite.setText(R.string.label_statut_indisponible);
                tvDisponibilite.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            }
        }
        if (livre.isDisponible()) {
            tvDisponibilite.setText("Statut : Disponible");
            // On applique le turquoise si disponible
            tvDisponibilite.setTextColor(getResources().getColor(R.color.turquoise));
        } else {
            tvDisponibilite.setText("Statut : Indisponible");
            // On applique un gris foncé si indisponible
            tvDisponibilite.setTextColor(android.graphics.Color.GRAY);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Ferme cette activité et retourne à l'écran précédent
        return true;
    }
}
