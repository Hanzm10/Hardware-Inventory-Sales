package com.github.hanzm_10.murico.swingapp.lib.database;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMigration {

    public interface Migration {
        String getName();

        void run();
    }

    protected List<Migration> migrations = new ArrayList<>();

    public void addMigration(Migration migration) {
        migrations.add(migration);
    }

    protected abstract void doesMigrationExist(String migrationName);

    protected abstract void ensureMigrationsTable();

    public abstract Class<? extends AbstractMigration> getResourceClass();

    protected abstract void insertMigration(String migrationName);

    public void migrate() {
        ensureMigrationsTable();

        for (var migration : migrations) {
            doesMigrationExist(migration.getName());

            migration.run();

            insertMigration(migration.getName());
        }
    }
}
