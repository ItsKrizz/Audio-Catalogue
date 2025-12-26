import java.io.Serializable;

// Enum за категориите
enum AudioCategory {
    SONG("Песен"),
    ALBUM("Албум"),
    PODCAST("Подкаст"),
    AUDIOBOOK("Аудиокнига");

    private final String bgName;

    AudioCategory(String bgName) {
        this.bgName = bgName;
    }

    public String getBgName() {
        return bgName;
    }
}

// Базов клас за всички аудио обекти
public abstract class AudioItem implements Serializable {
    protected String title;
    protected String author;
    protected String genre;
    protected int year;
    protected double duration; // в минути
    protected AudioCategory category;

    public AudioItem(String title, String author, String genre, int year, double duration) {
        // Валидация
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Заглавието не може да бъде празно!");
        }
        if (year < 1900 || year > 2025) {
            throw new IllegalArgumentException("Невалидна година!");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Продължителността трябва да е положително число!");
        }

        this.title = title;
        this.author = author != null ? author : "Неизвестен";
        this.genre = genre != null ? genre : "Неопределен";
        this.year = year;
        this.duration = duration;
    }

    // Абстрактен метод за CSV - всеки подклас ще го имплементира
    public abstract String toCSV();

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public double getDuration() { return duration; }
    public AudioCategory getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("[%s] '%s' - %s (%d) | %s | %.2f мин.",
                category.getBgName(), title, author, year, genre, duration);
    }

    // Помощен метод за форматиране на времето
    protected String formatDuration() {
        int minutes = (int) duration;
        int seconds = (int) ((duration - minutes) * 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}

// ============= ПОДКЛАСОВЕ =============

class Song extends AudioItem {
    private String album;

    public Song(String title, String author, String genre, int year, double duration, String album) {
        super(title, author, genre, year, duration);
        this.category = AudioCategory.SONG;
        this.album = album != null ? album : "Single";
    }

    public String getAlbum() { return album; }

    @Override
    public String toCSV() {
        return String.format("SONG,%s,%s,%s,%d,%.2f,%s",
                title, author, genre, year, duration, album);
    }

    @Override
    public String toString() {
        return super.toString() + " [Албум: " + album + "]";
    }

    // Създаване от CSV
    public static Song fromCSV(String[] parts) {
        if (parts.length < 7) return null;
        return new Song(parts[1], parts[2], parts[3],
                Integer.parseInt(parts[4]),
                Double.parseDouble(parts[5]),
                parts[6]);
    }
}

class Album extends AudioItem {
    private int tracksCount;

    public Album(String title, String author, String genre, int year, double duration, int tracksCount) {
        super(title, author, genre, year, duration);
        this.category = AudioCategory.ALBUM;
        this.tracksCount = tracksCount > 0 ? tracksCount : 1;
    }

    public int getTracksCount() { return tracksCount; }

    @Override
    public String toCSV() {
        return String.format("ALBUM,%s,%s,%s,%d,%.2f,%d",
                title, author, genre, year, duration, tracksCount);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [%d песни]", tracksCount);
    }

    public static Album fromCSV(String[] parts) {
        if (parts.length < 7) return null;
        return new Album(parts[1], parts[2], parts[3],
                Integer.parseInt(parts[4]),
                Double.parseDouble(parts[5]),
                Integer.parseInt(parts[6]));
    }
}

class Podcast extends AudioItem {
    private int episodeNumber;
    private String host;

    public Podcast(String title, String author, String genre, int year, double duration,
                   int episodeNumber, String host) {
        super(title, author, genre, year, duration);
        this.category = AudioCategory.PODCAST;
        this.episodeNumber = episodeNumber > 0 ? episodeNumber : 1;
        this.host = host != null ? host : author;
    }

    public int getEpisodeNumber() { return episodeNumber; }
    public String getHost() { return host; }

    @Override
    public String toCSV() {
        return String.format("PODCAST,%s,%s,%s,%d,%.2f,%d,%s",
                title, author, genre, year, duration, episodeNumber, host);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Еп. %d | Водещ: %s]", episodeNumber, host);
    }

    public static Podcast fromCSV(String[] parts) {
        if (parts.length < 8) return null;
        return new Podcast(parts[1], parts[2], parts[3],
                Integer.parseInt(parts[4]),
                Double.parseDouble(parts[5]),
                Integer.parseInt(parts[6]),
                parts[7]);
    }
}

class Audiobook extends AudioItem {
    private String narrator;
    private int chapters;

    public Audiobook(String title, String author, String genre, int year, double duration,
                     String narrator, int chapters) {
        super(title, author, genre, year, duration);
        this.category = AudioCategory.AUDIOBOOK;
        this.narrator = narrator != null ? narrator : "Неизвестен";
        this.chapters = chapters > 0 ? chapters : 1;
    }

    public String getNarrator() { return narrator; }
    public int getChapters() { return chapters; }

    @Override
    public String toCSV() {
        return String.format("AUDIOBOOK,%s,%s,%s,%d,%.2f,%s,%d",
                title, author, genre, year, duration, narrator, chapters);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Разказвач: %s | %d глави]", narrator, chapters);
    }

    public static Audiobook fromCSV(String[] parts) {
        if (parts.length < 8) return null;
        return new Audiobook(parts[1], parts[2], parts[3],
                Integer.parseInt(parts[4]),
                Double.parseDouble(parts[5]),
                parts[6],
                Integer.parseInt(parts[7]));
    }
}