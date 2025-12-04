package sudoku;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StorageManager {

    private Path base;
    private Path easy;
    private Path medium;
    private Path hard;
    private Path current;

    public StorageManager(String path) {
        base = Paths.get(path);
        easy = base.resolve("easy");
        medium = base.resolve("medium");
        hard = base.resolve("hard");
        current = base.resolve("current");
        makeFolders();
    }

    private void makeFolders() {
        try {
            Files.createDirectories(easy);
            Files.createDirectories(medium);
            Files.createDirectories(hard);
            Files.createDirectories(current);
        } catch (Exception e) {}
    }

    public void saveGame(String level, String name, int[][] board) {
        Path p = getLevel(level).resolve(name + ".txt");
        writeBoard(p, board);
    }

    public String saveGame(String level, int[][] board) {
        String name = "game_" + System.currentTimeMillis();
        saveGame(level, name, board);
        return name;
    }

    public int[][] loadGame(String level, String name) {
        Path p = getLevel(level).resolve(name + ".txt");
        if (!Files.exists(p)) return null;
        return readBoard(p);
    }

    public void saveCurrentGame(int[][] board) {
        Path p = current.resolve("game.txt");
        writeBoard(p, board);
        ensureLogExists();
    }

    public int[][] loadCurrentGame() {
        Path p = current.resolve("game.txt");
        if (!Files.exists(p)) return null;
        return readBoard(p);
    }

    public boolean hasCurrentGame() {
        return Files.exists(current.resolve("game.txt"))
            && Files.exists(current.resolve("log.txt"));
    }

    public void deleteCurrentGame() {
        try {
            Files.deleteIfExists(current.resolve("game.txt"));
            Files.deleteIfExists(current.resolve("log.txt"));
        } catch (Exception e) {}
    }

    public void initializeNewCurrentGame(int[][] board) {
        saveCurrentGame(board);
        clearLog();
    }

    private void ensureLogExists() {
        Path log = current.resolve("log.txt");
        try {
            if (!Files.exists(log)) {
                Files.writeString(log, "");
            }
        } catch (Exception e) {}
    }

    public void addLogEntry(LogEntry e) {
        Path log = current.resolve("log.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log.toFile(), true))) {
            bw.write(e.toString());
            bw.newLine();
        } catch (Exception ex) {}
    }

    public List<LogEntry> readLogs() {
        List<LogEntry> out = new ArrayList<>();
        Path log = current.resolve("log.txt");
        if (!Files.exists(log)) return out;
        try {
            List<String> lines = Files.readAllLines(log);
            for (String s : lines) {
                LogEntry le = LogEntry.fromString(s);
                if (le != null) out.add(le);
            }
        } catch (Exception e) {}
        return out;
    }

    public LogEntry popLastLog() {
        Path log = current.resolve("log.txt");
        if (!Files.exists(log)) return null;
        try {
            List<String> lines = Files.readAllLines(log);
            if (lines.isEmpty()) return null;
            String last = lines.remove(lines.size() - 1);
            LogEntry le = LogEntry.fromString(last);
            Files.write(log, lines);
            return le;
        } catch (Exception e) {
            return null;
        }
    }

    public void clearLog() {
        Path log = current.resolve("log.txt");
        try {
            Files.writeString(log, "");
        } catch (Exception e) {}
    }

    private void writeBoard(Path p, int[][] b) {
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    bw.write(Integer.toString(b[r][c]));
                    if (c < 8) bw.write(",");
                }
                bw.newLine();
            }
        } catch (Exception e) {}
    }

    private int[][] readBoard(Path p) {
        int[][] b = new int[9][9];
        try {
            List<String> lines = Files.readAllLines(p);
            for (int r = 0; r < 9; r++) {
                String[] a = lines.get(r).split(",");
                for (int c = 0; c < 9; c++) {
                    b[r][c] = Integer.parseInt(a[c].trim());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return b;
    }

    private Path getLevel(String level) {
        String l = level.toLowerCase();
        if (l.equals("easy")) return easy;
        if (l.equals("medium")) return medium;
        if (l.equals("hard")) return hard;
        if (l.equals("current")) return current;
        return base.resolve(level);
    }

    public List<String> listGames(String level) {
        List<String> out = new ArrayList<>();
        Path dir = getLevel(level);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : ds) {
                String n = p.getFileName().toString().replace(".txt", "");
                out.add(n);
            }
        } catch (Exception e) {}
        return out;
    }
}
