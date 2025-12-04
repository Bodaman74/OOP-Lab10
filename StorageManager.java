package sudoku;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StorageManager {

    private Path base;

    public StorageManager(String path) {
        base = Paths.get(path);
        makeFolders();
    }

    private void makeFolders() {
        try {
            Files.createDirectories(base.resolve("easy"));
            Files.createDirectories(base.resolve("medium"));
            Files.createDirectories(base.resolve("hard"));
            Files.createDirectories(base.resolve("current"));
        } catch (IOException e) {
            System.out.println("folder error: " + e.getMessage());
        }
    }


    public void saveGame(String level, String name, int[][] board) {
        Path p = base.resolve(level).resolve(name + ".txt");

        try (BufferedWriter bw = Files.newBufferedWriter(p)) {

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    bw.write(Integer.toString(board[r][c]));
                    if (c < 8) bw.write(",");
                }
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("save error: " + e.getMessage());
        }
    }


    public int[][] loadGame(String level, String name) {
        int[][] board = new int[9][9];
        Path p = base.resolve(level).resolve(name + ".txt");

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            int r = 0;
            while ((line = br.readLine()) != null && r < 9) {
                String[] parts = line.split(",");
                for (int c = 0; c < 9; c++) {
                    board[r][c] = Integer.parseInt(parts[c].trim());
                }
                r++;
            }
        } catch (IOException e) {
            System.out.println("load error: " + e.getMessage());
        }

        return board;
    }

    
    public void saveCurrentGame(int[][] board) {
        saveGame("current", "game", board);
    }

    public int[][] loadCurrentGame() {
        return loadGame("current", "game");
    }


    public void appendLog(LogEntry e) {
        Path p = base.resolve("current").resolve("log.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(p.toFile(), true))) {
            bw.write(e.toString());
            bw.newLine();
        } catch (IOException ex) {
            System.out.println("log error: " + ex.getMessage());
        }
    }


    public List<String> listGames(String level) {
        List<String> out = new ArrayList<>();
        Path dir = base.resolve(level);

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : ds) {
                String name = p.getFileName().toString();
                name = name.replace(".txt", "");
                out.add(name);
            }
        } catch (IOException e) {
            System.out.println("list error: " + e.getMessage());
        }

        return out;
    }


    public void deleteGame(String level, String name) {
        Path p = base.resolve(level).resolve(name + ".txt");
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            System.out.println("delete error: " + e.getMessage());
        }
    }
}
