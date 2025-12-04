package sudoku;


public class LogEntry {

    public final int r;
    public final int c;
    public final int newVal;
    public final int oldVal;

    public LogEntry(int r, int c, int newVal, int oldVal) {
        this.r = r;
        this.c = c;
        this.newVal = newVal;
        this.oldVal = oldVal;
    }

    @Override
    public String toString() {
        return "(" + r + "," + c + "," + newVal + "," + oldVal + ")";
    }


    public static LogEntry fromString(String s) {
        if (s == null) return null;
        try {
            s = s.trim();
            if (s.startsWith("(") && s.endsWith(")")) {
                s = s.substring(1, s.length() - 1);
            }
            String[] p = s.split(",");
            if (p.length != 4) return null;
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
