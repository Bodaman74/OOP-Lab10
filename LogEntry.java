package sudoku;

public class LogEntry {

    public int r;
    public int c;
    public int newVal;
    public int oldVal;

    public LogEntry(int r, int c, int newVal, int oldVal) {
        this.r = r;
        this.c = c;
        this.newVal = newVal;
        this.oldVal = oldVal;
    }

    
    public String toString() {
        return "(" + r + "," + c + "," + newVal + "," + oldVal + ")";
    }

    
    public static LogEntry fromString(String s) {
        try {
            s = s.replace("(", "").replace(")", "");
            String[] p = s.split(",");
            int r = Integer.parseInt(p[0].trim());
            int c = Integer.parseInt(p[1].trim());
            int n = Integer.parseInt(p[2].trim());
            int o = Integer.parseInt(p[3].trim());
            return new LogEntry(r, c, n, o);
        } catch (Exception e) {
            return null; 
        }
    }
}
