package ch.supsi.info.gruppo9.persistence.csv;

import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.IGroupTagPersistenceLayerService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FileCSVGroupTag implements IGroupTagPersistenceLayerService {
    private static FileCSVGroupTag service;
    /* path of group tag csv file */
    private final String PATH = FileCSVPathManager.getInstance().getGroupTagFilePath();

    private FileCSVGroupTag() {
    }

    public static FileCSVGroupTag getInstance() {
        if (service == null)
            service = new FileCSVGroupTag();
        return service;
    }

    public List<GroupTag> load(List<Tag> allTag) {
        List<GroupTag> list = new ArrayList<>();
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRowsGT = reader.readAll();

            for (String[] groupTagRecord : allRowsGT) {
                final UUID id = UUID.fromString(groupTagRecord[0]);
                final String name = groupTagRecord[1];
                List<Tag> tags = new ArrayList<>();
                int index = 2;
                while (!groupTagRecord[index].equals("///")) {
                    for (Tag t : allTag) {
                        if (UUID.fromString(groupTagRecord[index]).equals(t.getId())) {
                            tags.add(t);
                            break;
                        }
                    }
                    index++;
                }
                GroupTag groupTag = new GroupTag(id, name, tags);
                list.add(groupTag);
            }
            reader.close();
            return list;
        } catch (IOException e) {
            return null;
        }
    }

    public GroupTag saveAndGet(final String name, List<Tag> tags) {
        final GroupTag groupTag = new GroupTag(UUID.randomUUID(), name, tags);
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(PATH, true));
            StringBuilder sb = new StringBuilder();
            sb.append(groupTag.getId())
                    .append(",")
                    .append(groupTag.getName())
                    .append(",");
            for (final Tag tag : groupTag.getTagList()) {
                sb.append(tag.getId());
                sb.append(",");
            }
            sb.append("///"); //terminator delimiter
            writer.writeNext(sb.toString().split(","));
            writer.close();
            return groupTag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(final GroupTag groupTag) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(PATH));
            List<String[]> allRows = reader.readAll();
            Iterator<String[]> iterator = allRows.iterator();
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                if (groupTag.getId().toString().equals(row[0])) {
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
            return false;
        }
    }
}
