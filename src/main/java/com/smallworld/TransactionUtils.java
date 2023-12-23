package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TransactionUtils {

    public static List<Transaction> loadFromJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = System.getProperty("user.dir") + File.separator + fileName;
        try (InputStream inputStream = Files.newInputStream(Path.of(filePath))) {
            List<Transaction> transactions = objectMapper.readValue(inputStream, new TypeReference<List<Transaction>>() {});
            return transactions;
        }
    }

}
