package com.generatefiles.service;

import com.generatefiles.entities.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVGeneratorService {

    private final UserService userService;

    public ByteArrayInputStream generateCSVWithOpenCSV() {
        List<User> userList = userService.getAllUsers();

        return userListToCSV(userList);
    }

    public void generateCSVWithCoreJava(HttpServletResponse response) {
        List<User> userList = userService.getAllUsers();
        generateCSV(response, userList);
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

    public void generateCSV(HttpServletResponse response, List<User> userList){
        try {
            OutputStream outputStream = response.getOutputStream();
            String header = "ID, NAME, GENDER, BIRTH DATE\n";
            outputStream.write(header.getBytes());

            for (User user : userList) {
                String row = user.getId().toString() + ", "
                        + user.getName() + ", "
                        + user.getGender() + ", "
                        + user.getBirthDate().toString() + "\n";

                outputStream.write(row.getBytes());
                outputStream.flush();
            }

            outputStream.close();
        }
        catch(Exception e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

}
