package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.PhotoReport;
import com.assistance.DogShelter.db.model.Report;
import com.assistance.DogShelter.db.repository.PhotoReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с фото отчетами.
 */
@Service
@Transactional
public class PhotoReportService {

    @Value("/photoReports}") // Правильный способ использования значения из application.properties
    private String reportDir;

    private final PhotoReportRepository photoReportRepository;
    private final ReportService reportService;

    @Autowired
    public PhotoReportService(PhotoReportRepository photoReportRepository, ReportService reportService) {
        this.photoReportRepository = photoReportRepository;
        this.reportService = reportService;
    }

    public void uploadCover(Long reportId, MultipartFile file) throws IOException {
        Report report = reportService.findReportById(reportId);
        Path filePath = Paths.get(reportDir, reportId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
            is.transferTo(os);
        }

        PhotoReport photoReport = findPicture(reportId);
        photoReport.setReport(report);
        photoReport.setFilePath(filePath.toString());
        photoReport.setFileSize(file.getSize());
        photoReport.setMediaType(file.getContentType());
        photoReport.setData(generateImagePreview(filePath));
        photoReportRepository.save(photoReport);
    }

    public PhotoReport findPicture(Long reportId) {
        return photoReportRepository.findByReportId(reportId).orElse(new PhotoReport());
    }
    private byte[] generateImagePreview(Path filePath) throws IOException {
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight()/(image.getWidth()/100);
            BufferedImage preview =  new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}