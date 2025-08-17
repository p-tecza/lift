package p.tecza.dcnds.contollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import p.tecza.dcnds.security.VirusScanService;
import p.tecza.dcnds.service.EMLParser;
import p.tecza.dcnds.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {

  private final VirusScanService virusScanService;
  private final FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
    log.info("Received file upload request");
    if (files == null || files.isEmpty()) {
      return ResponseEntity.badRequest().body("Nie przesłano żadnych plików");
    }
    List<String> corruptedFiles = new ArrayList<>();
    try {
      // Tu możesz zapisać plik na dysk lub dalej przetworzyć

      files.forEach(file -> {
        if (!this.virusScanService.isFileNotInfected(file)) {
          corruptedFiles.add(file.getOriginalFilename());
        }
      });

      if (!corruptedFiles.isEmpty()) {
        log.warn("Some files did not pass AV scanning: {}", corruptedFiles);
      }

      for (MultipartFile file : files) {
        String fileName = file.getOriginalFilename();
        if(!Objects.requireNonNull(fileName).endsWith(".eml")){
          log.info("Skipping file: {}; currently processing only .EML files.", fileName);
          continue;
        }
        this.fileService.createTicketBasedOnEmlFile(file);
        byte[] bytes = file.getBytes();

        Path path = Paths.get("uploads/" + fileName);
        Files.createDirectories(path.getParent()); // tworzy folder, jeśli nie istnieje
        Files.write(path, bytes);
      }
      return ResponseEntity.ok("Successfully uploaded all files.");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error occurred while saving file: " + e.getMessage());
    }
  }


}
