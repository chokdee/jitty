package com.jmelzer.jitty.utl;

import org.hibernate.dialect.HSQLDialect;

/**
 * Created by J. Melzer on 29.07.2016.
 * http://stackoverflow.com/questions/12054422/unsuccessful-alter-table-xxx-drop-constraint-yyy-in-hibernate-jpa-hsqldb-standa
 */
public class CustomHSQLDialect extends HSQLDialect {
    @Override
    protected String getDropSequenceString(String sequenceName) {
        return "drop sequence if exists " + sequenceName;
    }
    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
        return false;
    }
}
