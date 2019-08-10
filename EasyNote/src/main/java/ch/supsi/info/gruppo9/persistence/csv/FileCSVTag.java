package ch.supsi.info.gruppo9.persistence.csv;

import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.ITagPersistenceLayerService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FileCSVTag implements ITagPersistenceLayerService {
    private static FileCSVTag service;
    /* service that manage the note-tag relations */
    private FileCSVRelationNoteTag fileCSVRelationNoteTag = FileCSVRelationNoteTag.getInstance();
    /* path of tag csv file */
    private final String PATH = FileCSVPathManager.getInstance().getTagFilePath();

    private FileCSVTag() {
    }

    public static FileCSVTag getInstance() {
        if (service == null)
            service = new FileCSVTag();
        return service;
    }

    public List<Tag> load() {
        CSVReader reader;
        List<Tag> allTags = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                final UUID id = UUID.fromString(row[0]);
                final String name = row[1];
                Tag tag = new Tag(id, name);
                allTags.add(tag);
            }
            return allTags;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tag saveAndGet(final String name) {
        final Tag tag = new Tag(UUID.randomUUID(), name);
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(PATH, true));
            String[] row = new String[2];
            row[0] = tag.getId().toString();
            row[1] = tag.getName();
            writer.writeNext(row);
            writer.close();
            return tag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(final Tag tag) {
        if (!fileCSVRelationNoteTag.cleanTag(tag))
            return false;
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            Iterator<String[]> iterator = allRows.iterator();
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                if (tag.getId().toString().equals(row[0])) {
                    iterator.remove();
                    break;
                }
            }
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

}