package p.tecza.dcnds.contollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import p.tecza.dcnds.security.VirusScanService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class FileUploadController {

  private final VirusScanService virusScanService;

  public FileUploadController(VirusScanService virusScanService) {
    this.virusScanService = virusScanService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      return ResponseEntity.badRequest().body("Nie przesłano żadnych plików");
    }
    List<String> corruptedFiles = new ArrayList<>();
    try {
      // Tu możesz zapisać plik na dysk lub dalej przetworzyć

      files.forEach(file -> {
        if(!this.virusScanService.isFileNotInfected(file)) {
          corruptedFiles.add(file.getOriginalFilename());
        }
      });

      if(!corruptedFiles.isEmpty()) {
        log.warn("Some files did not pass AV scanning: {}", corruptedFiles);
      }

      //TODO tutaj

      String fileName = file.getOriginalFilename();
      byte[] bytes = file.getBytes();

      // Na przykład zapis do lokalnego folderu "uploads"
      Path path = Paths.get("uploads/" + fileName);
      Files.createDirectories(path.getParent()); // tworzy folder, jeśli nie istnieje
      Files.write(path, bytes);

      return ResponseEntity.ok("Plik " + fileName + " został przesłany pomyślnie.");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Wystąpił błąd przy zapisie pliku: " + e.getMessage());
    }
  }


}
