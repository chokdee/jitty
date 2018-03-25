/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.app;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.hibernate.tool.schema.TargetType.SCRIPT;

/**
 * class for exporting the jpa schema to different ddl files
 *
 * @author john.thompson
 */
public class SchemaExporter {
    MetadataSources metadata;

    /**
     * prefix .
     */
    String prefix;

    /**
     * default constructor.
     *
     * @param packageNames to scan
     * @throws Exception if error
     */
    public SchemaExporter(Dialect dialect, final String... packageNames) throws Exception {
        init(dialect, packageNames);
    }

    /**
     * main method.
     *
     * @param args not used
     * @throws Exception if error
     */
    public static void main(final String[] args) throws Exception {
        SchemaExporter gen = new SchemaExporter(Dialect.HSQL, "com.jmelzer.jitty.model");
        gen.setPrefix("hsql");
        gen.generate();
        SchemaExporter gen2 = new SchemaExporter(Dialect.MYSQL, "com.jmelzer.jitty.model");
        gen2.setPrefix("mysql");
        gen2.generate();

    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String generate() {


//        SchemaExport export = new SchemaExport((MetadataImplementor) metadata.buildMetadata());
        SchemaExport export = new SchemaExport();
        export.setDelimiter(";");
        String workingDir = System.getProperty("user.dir");

        String filename = workingDir + "/src/main/resources/db/migration/" + prefix + "/V1__ddl.sql";
        export.setOutputFile(filename);
        new File(filename).delete();

        export.execute(EnumSet.of(SCRIPT), SchemaExport.Action.CREATE, metadata.buildMetadata());
        return filename;
    }

    /**
     * init the class
     *
     * @param packageNames to scan
     * @throws Exception if error
     */
    private void init(Dialect dialect, String... packageNames) throws Exception {
//        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", dialect.getDialectClass())
                        .build());


        final List<Class<?>> classes = new ArrayList<Class<?>>();
        for (String packageName : packageNames) {
            for (final Object clazz : getClasses(packageName, classes)) {
                metadata.addAnnotatedClass((Class) clazz);
            }
        }
    }

    /**
     * .
     *
     * @param packageName bla
     * @param classes     c
     * @return r
     * @throws Exception e
     */
    private List<Class<?>> getClasses(final String packageName, final List<Class<?>> classes) throws Exception {

        File directory = null;
        try {
            final ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            final String path = packageName.replace('.', '/');
            final URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (final NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid "
                    + "package");
        }
        if (directory.exists()) {
            final String[] files = directory.list();
            for (final String file : files) {
                if (file.endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
                }
                if (new File(directory, file).isDirectory()) {
                    getClasses(packageName + "." + file, classes);
                }
            }
        } else {
            throw new ClassNotFoundException(packageName + " is not a valid package");
        }

        return classes;
    }

    /**
     * Holds the classnames of hibernate dialects for easy reference.
     */
    private enum Dialect {
        /**
         * .
         */
        MYSQL("org.hibernate.dialect.MySQL5Dialect"),
        /**
         * .
         */
        HSQL("org.hibernate.dialect.HSQLDialect"),
        H2("org.hibernate.dialect.H2Dialect");

        /**
         * .
         */
        private String dialectClass;

        /**
         * .
         *
         * @param dialectClass .
         */
        Dialect(String dialectClass) {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass() {
            return dialectClass;
        }
    }
}