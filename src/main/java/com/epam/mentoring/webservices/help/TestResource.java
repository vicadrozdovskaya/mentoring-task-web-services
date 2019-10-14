package com.epam.mentoring.webservices.help;

import java.io.File;

public interface TestResource {
    static File getTestResourceAsFile (String testResourcesPathToFile) {
        String finalPath = "C:/mentoring/Module 12 Automated testing of Web Services best practices/"+ testResourcesPathToFile;

        File file = new File(finalPath);

        if (file.exists()) return file;
        throw new IllegalArgumentException("File with path: " + testResourcesPathToFile + " doesn't exist");
    }
}
