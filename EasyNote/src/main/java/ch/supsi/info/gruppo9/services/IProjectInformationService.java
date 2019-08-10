package ch.supsi.info.gruppo9.services;

import java.io.IOException;

public interface IProjectInformationService extends IApplicationService {
    /* function that load the project info from properties file and save these into holder project info */
    void loadProjectInfo() throws IOException;

    /* function that returns groupID */
    String getGroupId();

    /* function that returns artifactID */
    String getArtifact();

    /* function that returns version */
    String getVersion();
}
