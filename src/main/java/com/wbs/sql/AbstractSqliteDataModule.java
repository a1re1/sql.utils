package com.wbs.sql;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

public abstract class AbstractSqliteDataModule<DAO> extends AbstractModule {
    protected abstract String getNamespace();
    protected abstract Class<DAO> getClazz();

    @Override
    protected void configure() {
        GuiceBundle.builder()
                .bundles(JdbiBundle.<JdbiAppConfiguration>forDatabase((conf, env) -> conf.getDatabase()));
    }

    @Provides
    DAO provideDao() {
        Jdbi dbi = locateDbi();
        return dbi.open().attach(getClazz());
    }

    Jdbi locateDbi() {
        return Jdbi.create(getNamespace())
                .installPlugin(new SQLitePlugin())
                .installPlugin(new SqlObjectPlugin());
    }
}