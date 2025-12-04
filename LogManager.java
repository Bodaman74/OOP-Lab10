package sudoku;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class LogManager {

    private final Path logFile; // e.g. .../current/log.txt

    public LogManager(Path currentFolder) {
        this.logFile = currentFolder.resolve("log.txt");
    }

    
    public void append(LogEntry e) {
        try {
            Files.createDirectories(logFile.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bw.write(e.toString());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("LogManager.append error: " + ex.getMessage());
        }
    }

    
    public List<LogEntry> readAll() {
        List<LogEntry> out = new ArrayList<>();
        if (!Files.exists(logFile)) return out;
        try {
            List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                LogEntry le = LogEntry.fromString(line);
                if (le != null) out.add(le);
            }
        } catch (IOException e) {
            System.out.println("LogManager.readAll error: " + e.getMessage());
        }
        return out;
    }

    
    public LogEntry popLast() {
        if (!Files.exists(logFile)) return null;
        try {
            List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
            if (lines.isEmpty()) return null;
            String last = lines.remove(lines.size() - 1);
            LogEntry le = LogEntry.fromString(last);

            
            Path tmp = logFile.resolveSibling("log.tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
            Files.move(tmp, logFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return le;
        } catch (IOException e) {
            System.out.println("LogManager.popLast error: " + e.getMessage());
            return null;
        }
    }


    public void clear() {
        try {
            if (Files.exists(logFile)) {
                Files.delete(logFile);
            }
            
            Files.createFile(logFile);
        } catch (IOException e) {
            System.out.println("LogManager.clear error: " + e.getMessage());
        }
    }


    public void ensureExists() {
        try {
            Files.createDirectories(logFile.getParent());
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            System.out.println("LogManager.ensureExists error: " + e.getMessage());
        }
    }

    /**
     * Delete the log file if exists.
     */
    public void delete() {
        try {
            Files.deleteIfExists(logFile);
        } catch (IOException e) {
            System.out.println("LogManager.delete error: " + e.getMessage());
        }
    }


    public Path getLogFilePath() {
        return logFile;
    }
}
