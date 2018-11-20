package com.revolut;

import io.dropwizard.testing.ResourceHelpers;

import java.io.File;
import java.io.IOException;

public class IntegrationTest {
    protected static final String TMP_FILE = createTempFile();
    protected static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-app.yml");
    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
