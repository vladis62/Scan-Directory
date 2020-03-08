package scandirectory.model;

public class FileData {
    private String file;
    private String date;
    private String size;

    public FileData(String file, String date, String size) {
        this.file = file;
        this.date = date;
        this.size = size;
    }

    @Override
    public String toString() {
        return "file = " + file + "\n" +
                "date = " + date + "\n" +
                "size = " + size + "]";
    }
}
