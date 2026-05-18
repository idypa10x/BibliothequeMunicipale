package com.example.myisidorapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface LivreDao {

    // Récupérer tous les livres triés par identifiant
    @Query("SELECT * FROM table_livres ORDER BY id ASC")
    List<Livre> getAllLivres();

    // Insérer un nouveau livre dans la base de données
    @Insert
    void insert(Livre livre);

    // Mettre à jour les informations d'un livre existant
    @Update
    void update(Livre livre);

    // Supprimer un livre de la base de données
    @Delete
    void delete(Livre livre);
}