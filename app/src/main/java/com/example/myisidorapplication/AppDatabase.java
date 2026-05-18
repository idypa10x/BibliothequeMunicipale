package com.example.myisidorapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Livre.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Déclaration du DAO associé à cette base
    public abstract LivreDao livreDao();

    private static volatile AppDatabase INSTANCE;

    // Un ExecutorService pour effectuer les tâches sur un thread secondaire (Arrière-plan)
    // C'est obligatoire avec Room pour ne pas bloquer l'affichage de l'application
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // Méthode Singleton pour récupérer proprement l'instance de la base
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "bibliotheque_db")
                            .fallbackToDestructiveMigration() // Recrée la base en cas de changement de structure
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}