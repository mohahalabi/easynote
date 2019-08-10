package ch.supsi.info.gruppo9.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ProjectInformationService implements IProjectInformationService {
    private static ProjectInformationService service;
    /* collection that holds projectInfo */
    private HashMap<String, String> projectInfo;

    private ProjectInformationService() {

    }

    public static ProjectInformationService getInstance() {
        if (service == null)
            service = new ProjectInformationService();
        return service;
    }

    public void loadProjectInfo() throws IOException {
        projectInfo = new HashMap<>();
        final Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/properties/project-info.properties"));
        projectInfo.put("groupId", properties.getProperty("groupId"));
        projectInfo.put("artifactId", properties.getProperty("artifactId"));
        projectInfo.put("version", properties.getProperty("version"));

    }

    public String getGroupId() {
        return projectInfo.get("groupId");
    }

    public String getArtifact() {
        return projectInfo.get("artifactId");
    }

    public String getVersion() {
        return projectInfo.get("version");
    }

}
