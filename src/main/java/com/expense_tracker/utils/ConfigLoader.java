/**
 * Responsible for loading the config.properties file and providing its values to callers
 */

package com.expense_tracker.utils;

import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;

public class ConfigLoader {
    
    private Properties props;
    private FileInputStream input;

    private static ConfigLoader instance;

    private static final String PATH_TO_PROPERTIES_FILE = "src\\main\\java\\com\\expense_tracker\\resources\\config.properties";

    /**
     * Expose the ConfigLoader instance to the rest of the application with a specific config file path
     * If there is no instance, create it before returning
     * @param filePath The filepath to the config file to load
     * @return the ConfigLoader instance
     */
    public static ConfigLoader getInstance(String filePath) {
        
        if (instance == null) {
            try {
                instance = new ConfigLoader(filePath);
            } catch (IOException e) {
                System.out.println("Error loading config file: " + e.getMessage());
            }
        }

        return instance;
    }

    /**
     * expose instance to rest of app, loading the default config file
     * @return
     */
    public static ConfigLoader getInstance() {
        
        if (instance == null) {
            try {
                instance = new ConfigLoader();
            } catch (IOException e) {
                System.out.println("Error loading config file: " + e.getMessage());
            }
        }

        return instance;
    }

    /**
     * called only for unit testing. instantiates and returns a separate ConfigLoader to the given test properties file
     * @param path file path to the test config file. see getter functions below for keys the file is expected to have
     * @return a configloader for the given file path, or null if exception is thrown
     */
    public static ConfigLoader createTestInstance(String path) {
        try {
            return new ConfigLoader(path);
        } catch(Exception e) {
            System.out.println("Error creating test config loader: " + e.getMessage());
        }
        return null;
    }

    /**
     * Constructor to load a specific config file
     * @param filePath path to the config file
     * @throws IOException
     */
    private ConfigLoader(String filePath) throws IOException{
        this.props = new Properties();
        this.input = new FileInputStream(filePath);
        props.load(input);
        this.input.close();
    }

    /**
     * Default constructor to use the expected config file
     * @throws IOException
     */
    private ConfigLoader() throws IOException{
        this(PATH_TO_PROPERTIES_FILE);
    }

    public String getDBUser() {
        return this.props.getProperty("db.username");
    }

    public String getDBPassword() {
        return this.props.getProperty("db.password");
    }

    public String getDBUrl() {
        return this.props.getProperty("db.url");
    }

    public String getTableSuffix() {
        return this.props.getProperty("table_suffix");
    }

    /*general getter to avoid having to update this with every property added later */
    public String get(String key) {
        return this.props.getProperty(key);
    }
    
}
