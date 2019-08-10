package ch.supsi.info.gruppo9.persistence.csv;

import ch.supsi.info.gruppo9.model.Author;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.persistence.INotePersistenceLayerService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FIleCSVNote implements INotePersistenceLayerService {
    private static FIleCSVNote service;
    /* service that manage the note-tag relations */
    private FileCSVRelationNoteTag fileCSVRelationNoteTag = FileCSVRelationNoteTag.getInstance();
    /* path of note csv file */
    private final String PATH = FileCSVPathManager.getInstance().getNoteFilePath();

    public static FIleCSVNote getInstance() {
        if (service == null)
            service = new FIleCSVNote();
        return service;
    }

    private FIleCSVNote() {
    }

    public List<Note> load() {
        CSVReader reader;
        List<Note> allNotes = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                final UUID id = UUID.fromString(row[0]);
                final Author author = new Author(row[1]);
                final LocalDateTime creation = LocalDateTime.parse(row[2]);
                final LocalDateTime modification = LocalDateTime.parse(row[3]);
                final String text = row[4];
                final Note note = new Note(id, author, creation, modification, text);
                allNotes.add(note);
            }
            reader.close();
            return allNotes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Note saveAndGet(final Author author, LocalDateTime dateTime) {
        final Note note = new Note(UUID.randomUUID(), author, dateTime, dateTime, "");
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(PATH, true));
            String[] row = new String[5];
            row[0] = note.getId().toString();
            row[1] = note.getAuthor().getName();
            row[2] = note.getCreationDate().toString();
            row[3] = note.getModificationDate().toString();
            row[4] = "";
            writer.writeNext(row);
            writer.close();
            return note;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateNote(final Note note, final LocalDateTime dateTime, final String text) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                if (note.getId().toString().equals(row[0])) {
                    row[3] = dateTime.toString();
                    row[4] = text;
                }
            }
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(PATH));
            writer.writeAll(allRows);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean delete(final Note note) {
        if (!fileCSVRelationNoteTag.cleanNote(note))
            return false;
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            Iterator<String[]> iterator = allRows.iterator();
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                if (note.getId().toString().equals(row[0])) {
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
