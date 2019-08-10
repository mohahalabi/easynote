package ch.supsi.info.gruppo9.persistence.csv;

import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.IRelationNoteTagPersistenceLayerService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FileCSVRelationNoteTag implements IRelationNoteTagPersistenceLayerService {
    private static FileCSVRelationNoteTag service;
    /* path of relation note tag csv file */
    private final String PATH = FileCSVPathManager.getInstance().getNoteTagRelationFilePath();

    private FileCSVRelationNoteTag() {
    }

    public static FileCSVRelationNoteTag getInstance() {
        if (service == null)
            service = new FileCSVRelationNoteTag();
        return service;
    }

    public List<Tag> loadByNote(final Note note, List<Tag> allTags) {
        List<Tag> tags = new ArrayList<>();
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                if (row[0].equals(note.getId().toString())) {
                    for (Tag tag : allTags) {
                        if (tag.getId().toString().equals(row[1]))
                            tags.add(tag);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tags;
    }

    public boolean addTagToNote(final Note note, final Tag tag) {
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(PATH, true));
            String[] row = new String[2];
            row[0] = note.getId().toString();
            row[1] = tag.getId().toString();
            writer.writeNext(row);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeTagFromNote(final Note note, final Tag tag) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            Iterator iterator = allRows.iterator();
            while (iterator.hasNext()) {
                String[] row = (String[]) iterator.next();
                if (UUID.fromString(row[0]).equals(note.getId())
                        && UUID.fromString(row[1]).equals(tag.getId())) {
                    iterator.remove();
                }
            }
            CSVWriter writer = new CSVWriter(new FileWriter(PATH));
            writer.writeAll(allRows);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cleanNote(final Note note) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            allRows.removeIf(row -> note.getId().toString().equals(row[0]));
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(PATH));
            writer.writeAll(allRows);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cleanTag(final Tag tag) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            allRows.removeIf(row -> tag.getId().toString().equals(row[1]));
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(PATH));
            writer.writeAll(allRows);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}