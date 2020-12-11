package com.generatefiles.controller;

import com.generatefiles.service.CSVGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.generatefiles.utils.DateUtils.convertDateToString;
import static com.generatefiles.utils.DateUtils.dateTimeFormat;

@RestController
@Api(value = "Generate Files")
@RequiredArgsConstructor
public class GenerateFilesController {

    private final CSVGeneratorService csvGeneratorService;

    @ApiOperation(value = "Generate report CSV file")
    @GetMapping(value = "/csv-report")
    public ResponseEntity<Resource> generateCSVFile(HttpServletResponse response) {
        String filename = "users_" + convertDateToString(LocalDateTime.now(), dateTimeFormat) + ".csv";
        InputStreamResource file = new InputStreamResource(csvGeneratorService.generateCSVWithOpenCSV());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);

    }

    @ApiOperation(value = "Generate report CSV file")
    @GetMapping(value = "/csv-report-core")
    public void generateCSVFileWithCoreJava(HttpServletResponse response) {
        String filename = "users_" + convertDateToString(LocalDateTime.now(), dateTimeFormat) + ".csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        csvGeneratorService.generateCSVWithCoreJava(response);
    }

}
