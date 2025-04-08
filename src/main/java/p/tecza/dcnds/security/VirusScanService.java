package p.tecza.dcnds.security;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VirusScanService {

  private final ClamavClient client;

  public VirusScanService(ClamavClient client) {
    this.client = client;
  }

  public boolean isFileNotInfected(MultipartFile file) {
    InputStream is;
    try {
      is = file.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    ScanResult res = client.scan(is);
    return res instanceof ScanResult.OK;
  }
}