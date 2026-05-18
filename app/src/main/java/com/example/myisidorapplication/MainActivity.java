package com.example.myisidorapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLivres;
    private LivreAdapter livreAdapter;
    private ArrayList<Livre> listeLivres;

    // Ajout Lab 7 : Instance de la base de données Room
    private AppDatabase database;

    // Lanceur pour récupérer le livre créé ou modifié dans AddEditActivity
    private final androidx.activity.result.ActivityResultLauncher<Intent> formLauncher =
            registerForActivityResult(
                    new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String mode = data.getStringExtra(AddEditActivity.EXTRA_MODE);
                            Livre livre = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);

                            if (livre != null) {
                                if (AddEditActivity.MODE_ADD.equals(mode)) {
                                    // AJOUT ROOM : Insérer le livre en arrière-plan
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        database.livreDao().insert(livre);
                                        // Recharger la liste depuis Room après l'insertion
                                        chargerLivresDepuisRoom();
                                    });
                                } else if (AddEditActivity.MODE_EDIT.equals(mode)) {
                                    // MODIFICATION ROOM : Mettre à jour le livre en arrière-plan
                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        database.livreDao().update(livre);
                                        // Recharger la liste depuis Room après la mise à jour
                                        chargerLivresDepuisRoom();
                                    });
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de la base de données Room
        database = AppDatabase.getDatabase(this);

        // Configuration du RecyclerView
        recyclerViewLivres = findViewById(R.id.recyclerViewLivres);
        recyclerViewLivres.setLayoutManager(new LinearLayoutManager(this));

        listeLivres = new ArrayList<>();
        livreAdapter = new LivreAdapter(listeLivres);
        recyclerViewLivres.setAdapter(livreAdapter);

        // Charger les données de la base Room dès le démarrage
        chargerLivresDepuisRoom();

        // Gestion du bouton flottant pour l'ajout
        FloatingActionButton fabAjouterLivre = findViewById(R.id.fabAjouterLivre);
        fabAjouterLivre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_ADD);
            formLauncher.launch(intent);
        });
    }

    // Méthode Lab 7 : Charger les livres depuis Room en arrière-plan
    private void chargerLivresDepuisRoom() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Récupération de la liste dans la base de données
            List<Livre> livresRoom = database.livreDao().getAllLivres();

            // Si la base de données est complètement vide (premier démarrage de l'appli)
            // On insère quelques livres par défaut pour le test
            if (livresRoom.isEmpty()) {
                database.livreDao().insert(new Livre(0, "Le Petit Prince", "Antoine de Saint-Exupéry", "9780156013987", true));
                database.livreDao().insert(new Livre(0, "L'Étranger", "Albert Camus", "9782070360024", false));
                database.livreDao().insert(new Livre(0, "Les Misérables", "Victor Hugo", "9782253096344", true));
                // On recharge après avoir créé les livres par défaut
                livresRoom = database.livreDao().getAllLivres();
            }

            final List<Livre> listeFinale = livresRoom;

            // Retour sur le thread principal pour mettre à jour l'affichage (UI)
            runOnUiThread(() -> {
                listeLivres.clear();
                listeLivres.addAll(listeFinale);
                livreAdapter.notifyDataSetChanged();
            });
        });
    }

    // Permet à l'adapter de lancer le formulaire de modification
    public void lancerModification(Intent intent) {
        formLauncher.launch(intent);
    }
    // MÉTHODE FINALE LAB 7 : Supprimer un livre de la base de données Room
    public void supprimerLivreDeRoom(Livre livre) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Suppression dans la base SQLite locale via le DAO
            database.livreDao().delete(livre);

            // Rechargement immédiat de la liste mise à jour
            chargerLivresDepuisRoom();
        });
    }
}