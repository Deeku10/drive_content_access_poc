package in.bydeepak.drive_content_access_poc;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleDriveService {

    private final Drive driveService;
    private final String folderId;

    public GoogleDriveService() throws GeneralSecurityException, IOException {

        String apiKey = "";
        String folderUrl = "https://drive.google.com/drive/folders/1rWcuLsEfyKRicbz_RWyqBxJ1A93i4XZD";
        // Use CommonGoogleClientRequestInitializer to securely bind your API Key globally to the client instances
        CommonGoogleClientRequestInitializer keyInitializer = new CommonGoogleClientRequestInitializer(apiKey);

        this.driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                null) // Null parameter because we are bypassing OAuth2 user flows
                .setApplicationName("driver-access-poc")
                .setGoogleClientRequestInitializer(keyInitializer) // Seamless setup across all requests
                .build();

        this.folderId = extractFolderId(folderUrl);
    }

    private String extractFolderId(String url) {
        Pattern pattern = Pattern.compile("folders/([a-zA-Z0-9-_]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid Google Drive folder URL provided.");
    }

    public List<MediaFileDto> listFiles() throws IOException {
        String query = String.format("'%s' in parents and trashed = false and " +
                "(mimeType contains 'image/' or mimeType contains 'video/')", folderId);

        // Explicitly request webViewLink and webContentLink from Google Drive
        FileList result = driveService.files().list()
                .setQ(query)
                .setFields("files(id, name, mimeType, thumbnailLink, webViewLink, webContentLink)")
                .execute();

        List<MediaFileDto> mediaFiles = new ArrayList<>();
        if (result.getFiles() != null) {
            for (com.google.api.services.drive.model.File file : result.getFiles()) {

                MediaFileDto dto = new MediaFileDto(
                        file.getId(),
                        file.getName(),
                        file.getMimeType(),
                        file.getThumbnailLink(),
                        file.getWebViewLink(),
                        file.getWebContentLink()
                );
                mediaFiles.add(dto);
            }
        }
        return mediaFiles;
    }
}
