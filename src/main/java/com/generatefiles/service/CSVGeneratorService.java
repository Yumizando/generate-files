package com.generatefiles.service;

import com.generatefiles.entities.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVGeneratorService {

    private final UserService userService;

    public ByteArrayInputStream generateCSV() {
        List<User> userList = userService.getAllUsers();

        return userListToCSV(userList);
    }

    public static ByteArrayInputStream userListToCSV(List<User> usersList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
             ) {

            List<String> header = Arrays.asList("ID", "NAME", "GENDER", "BIRTH DATE");
            csvPrinter.printRecord(header);

            for (User user : usersList) {
                List<String> data = Arrays.asList(
                        String.valueOf(user.getId()),
                        user.getName(),
                        user.getGender(),
                        String.valueOf(user.getBirthDate())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

}
