package co.websarva.wings.android.myquotations.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import co.websarva.wings.android.myquotations.quotations.Quotations;

@Database(entities = {Quotations.class}, version = 2)
public abstract class QuotationDatabase extends RoomDatabase{
    public static final String DATABASE_NAME = "quotation_db";

    private static QuotationDatabase instance;

    static QuotationDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    QuotationDatabase.class,
                    DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
            .build();
        }
        return instance;
    }

    public  abstract  QuotationDao getQuotationDao();

    //Migration
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE quotations"
                    + " ADD COLUMN checkBox TEXT");
        }
    };
}

