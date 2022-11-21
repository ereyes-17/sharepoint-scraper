package prototype.builder;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SharepointFileContentBuilder {

    protected final String DESTINATION_BASE_PATH = System.getProperty("user.dir")
            + File.separator
            + "site-file-content";
    public SharepointFileContentBuilder() throws IOException {
        Path destinationPath = Paths.get(DESTINATION_BASE_PATH);
        if (!Files.exists(destinationPath)) {
            Files.createDirectory(destinationPath);
        }
    }

    public void saveFile(byte[] fileBytes, String currentSiteTitle, String fileName) throws IOException {
        currentSiteTitle = currentSiteTitle.replace("/", "_").replace("+", "-").replace(" ", "");
        String siteDirectory = DESTINATION_BASE_PATH + File.separator + currentSiteTitle;
        Path sitePath = Paths.get(siteDirectory);
        if (!Files.exists(sitePath)) {
            Files.createDirectory(sitePath);
        }
        String fileDestination = siteDirectory + File.separator + fileName;
        Files.write(Paths.get(fileDestination), fileBytes);
    }
}
