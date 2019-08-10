package ch.supsi.info.gruppo9.persistence.csv;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/* singleton class only used by cvs class the manage the path of file in the file system */
public class FileCSVPathManager {
    private static FileCSVPathManager service;
    private String path;
    private Properties properties;

    public static FileCSVPathManager getInstance() {
        if (service == null) {
                service = new FileCSVPathManager();
                service.init();
        }
        return service;
    }

    /*  this method build the class by setting the file and directory working space into the
        user file system according to the properties of csv.properties
        no try and catch inside the constructor
     */
    private void init() {
        this.properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/properties/csv.properties"));
            this.path = System.getProperty("user.home");
            File noteFile = new File(getNoteFilePath());
            File tagFile = new File(getTagFilePath());
            File groupTagFile = new File(getGroupTagFilePath());
            File noteTagRelationFile = new File(getNoteTagRelationFilePath());
            File dir = new File(path.concat("/" + properties.getProperty("workingDirectory")));
            if (!dir.exists()) dir.mkdirs();
            noteFile.createNewFile();
            tagFile.createNewFile();
            groupTagFile.createNewFile();
            noteTagRelationFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private FileCSVPathManager() {
    }

    /* this function return the note file path */
    String getNoteFilePath() {
        return path + "/" + properties.getProperty("workingDirectory") + "/" + properties.getProperty("noteFilename");
    }

    /* this function return the tag file path */
    String getTagFilePath() {
        return path + "/" + properties.getProperty("workingDirectory") + "/" + properties.getProperty("tagFilename");
    }

    /* this function return the group tag file path */
    String getGroupTagFilePath() {
        return path + "/" + properties.getProperty("workingDirectory") + "/" + properties.getProperty("groupTagFilename");
    }

    /* this function return the note tag relation file path */
    String getNoteTagRelationFilePath() {
        return path + "/" + properties.getProperty("workingDirectory") + "/" + properties.getProperty("relationFilename");
    }
}
