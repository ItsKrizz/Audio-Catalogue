import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogManager {
    private List<AudioItem> catalog;
    private Map<String, List<AudioItem>> playlists;

    public CatalogManager() {
        this.catalog = new ArrayList<>();
        this.playlists = new HashMap<>();
    }

    // ==================== УПРАВЛЕНИЕ НА ОБЕКТИ ====================

    public void addItem(AudioItem item) {
        if (item == null) {
            System.out.println("❌ Невалиден обект!");
            return;
        }
        catalog.add(item);
        System.out.println("✅ Успешно добавено: " + item.getTitle());
    }

    public void deleteItem(String title) {
        boolean removed = catalog.removeIf(item -> 
            item.getTitle().equalsIgnoreCase(title));
        
        if (removed) {
            // Премахваме обекта и от всички плейлисти
            int playlistsAffected = 0;
            for (List<AudioItem> playlist : playlists.values()) {
                boolean playlistRemoved = playlist.removeIf(
                    item -> item.getTitle().equalsIgnoreCase(title)
                );
                if (playlistRemoved) {
                    playlistsAffected++;
                }
            }

            System.out.println("🗑️ Обектът е изтрит от каталога: " + title);
            if (playlistsAffected > 0) {
                System.out.println("ℹ️ Премахнат е и от " + playlistsAffected + " плейлист(а).");
            }
        } else {
            System.out.println("❌ Обектът не е намерен: " + title);
        }
    }

    public void showAll() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║         📚 ЦЕЛИЯТ КАТАЛОГ                      ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        
        if (catalog.isEmpty()) {
            System.out.println("  Каталогът е празен.");
            return;
        }

        // Групиране по категория
        Map<AudioCategory, List<AudioItem>> grouped = catalog.stream()
            .collect(Collectors.groupingBy(AudioItem::getCategory));

        for (AudioCategory cat : AudioCategory.values()) {
            List<AudioItem> items = grouped.get(cat);
            if (items != null && !items.isEmpty()) {
                System.out.println("\n  === " + cat.getBgName() + " (" + items.size() + ") ===");
                items.forEach(item -> System.out.println("  " + item));
            }
        }
        
        System.out.println("\n  Общо обекти: " + catalog.size());
    }

    // ==================== ТЪРСЕНЕ ====================

    public void searchGeneral(String query) {
        System.out.println("\n🔍 Търсене за: '" + query + "'");
        System.out.println("─".repeat(50));
        
        String q = query.toLowerCase();
        List<AudioItem> results = catalog.stream()
            .filter(item -> 
                item.getTitle().toLowerCase().contains(q) ||
                item.getAuthor().toLowerCase().contains(q) ||
                item.getGenre().toLowerCase().contains(q))
            .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("❌ Нищо не е намерено.");
        } else {
            System.out.println("✅ Намерени " + results.size() + " резултата:");
            results.forEach(System.out::println);
        }
    }

    // ==================== ФИЛТРИРАНЕ ====================

    public void filterByCategory(AudioCategory category) {
        System.out.println("\n📂 Филтър: " + category.getBgName());
        System.out.println("─".repeat(50));
        
        List<AudioItem> filtered = catalog.stream()
            .filter(item -> item.getCategory() == category)
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("Няма обекти от тази категория.");
        } else {
            filtered.forEach(System.out::println);
            System.out.println("\nОбщо: " + filtered.size());
        }
    }

    public void filterByGenre(String genre) {
        System.out.println("\n🎸 Филтър по жанр: " + genre);
        System.out.println("─".repeat(50));
        
        catalog.stream()
            .filter(item -> item.getGenre().equalsIgnoreCase(genre))
            .forEach(System.out::println);
    }

    public void filterByAuthor(String author) {
        System.out.println("\n👤 Филтър по автор: " + author);
        System.out.println("─".repeat(50));
        
        catalog.stream()
            .filter(item -> item.getAuthor().equalsIgnoreCase(author))
            .forEach(System.out::println);
    }

    public void filterByYear(int year) {
        System.out.println("\n📅 Филтър по година: " + year);
        System.out.println("─".repeat(50));
        
        catalog.stream()
            .filter(item -> item.getYear() == year)
            .forEach(System.out::println);
    }

    public void filterByYearRange(int startYear, int endYear) {
        System.out.println("\n📅 Филтър: " + startYear + " - " + endYear);
        System.out.println("─".repeat(50));
        
        catalog.stream()
            .filter(item -> item.getYear() >= startYear && item.getYear() <= endYear)
            .forEach(System.out::println);
    }

    // ==================== СОРТИРАНЕ ====================

    public void sortCatalog(String criteria) {
        switch (criteria.toLowerCase()) {
            case "title":
            case "заглавие":
                catalog.sort(Comparator.comparing(AudioItem::getTitle));
                System.out.println("✅ Сортирано по заглавие (А-Я)");
                break;
            case "author":
            case "автор":
                catalog.sort(Comparator.comparing(AudioItem::getAuthor));
                System.out.println("✅ Сортирано по автор (А-Я)");
                break;
            case "year":
            case "година":
                catalog.sort(Comparator.comparingInt(AudioItem::getYear));
                System.out.println("✅ Сортирано по година (възходящо)");
                break;
            case "duration":
            case "времетраене":
                catalog.sort(Comparator.comparingDouble(AudioItem::getDuration));
                System.out.println("✅ Сортирано по продължителност");
                break;
            default:
                System.out.println("❌ Невалиден критерий за сортиране!");
                return;
        }
        showAll();
    }

    public void sortPlaylist(String playlistName, String criteria) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("❌ Няма такъв плейлист.");
            return;
        }

        List<AudioItem> playlist = playlists.get(playlistName);
        
        switch (criteria.toLowerCase()) {
            case "title":
            case "заглавие":
                playlist.sort(Comparator.comparing(AudioItem::getTitle));
                break;
            case "author":
            case "автор":
                playlist.sort(Comparator.comparing(AudioItem::getAuthor));
                break;
            case "year":
            case "година":
                playlist.sort(Comparator.comparingInt(AudioItem::getYear));
                break;
            case "duration":
            case "времетраене":
                playlist.sort(Comparator.comparingDouble(AudioItem::getDuration));
                break;
            default:
                System.out.println("❌ Невалиден критерий!");
                return;
        }
        
        System.out.println("✅ Плейлистът '" + playlistName + "' е сортиран.");
        showPlaylist(playlistName);
    }

    // ==================== ПЛЕЙЛИСТИ ====================

    public void createPlaylist(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Името не може да бъде празно!");
            return;
        }

        if (playlists.containsKey(name)) {
            System.out.println("⚠️ Вече съществува плейлист с това име!");
            return;
        }

        playlists.put(name, new ArrayList<>());
        System.out.println("✅ Плейлист '" + name + "' е създаден.");
    }

    public void deletePlaylist(String name) {
        if (playlists.remove(name) != null) {
            System.out.println("🗑️ Плейлистът '" + name + "' е изтрит.");
        } else {
            System.out.println("❌ Няма такъв плейлист.");
        }
    }

    public void addToPlaylist(String playlistName, String title) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("❌ Няма такъв плейлист.");
            return;
        }

        AudioItem item = catalog.stream()
                .filter(i -> i.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);

        if (item != null) {
            List<AudioItem> playlist = playlists.get(playlistName);
            if (!playlist.contains(item)) {
                playlist.add(item);
                System.out.println("✅ '" + title + "' добавено в плейлист: " + playlistName);
            } else {
                System.out.println("⚠️ Вече е в плейлиста!");
            }
        } else {
            System.out.println("❌ Обектът не е намерен в каталога.");
        }
    }

    public void removeFromPlaylist(String playlistName, String title) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("❌ Няма такъв плейлист.");
            return;
        }

        boolean removed = playlists.get(playlistName).removeIf(
            i -> i.getTitle().equalsIgnoreCase(title));
        
        if (removed) {
            System.out.println("🗑️ Премахнато от плейлиста: " + title);
        } else {
            System.out.println("❌ Обектът не е в този плейлист.");
        }
    }

    public void showPlaylist(String name) {
        if (!playlists.containsKey(name)) {
            System.out.println("❌ Плейлистът не съществува.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  🎵 Плейлист: " + name);
        System.out.println("╚════════════════════════════════════════════════╝");

        List<AudioItem> list = playlists.get(name);
        if (list.isEmpty()) {
            System.out.println("  (празен)");
        } else {
            double totalDuration = list.stream()
                .mapToDouble(AudioItem::getDuration)
                .sum();
            
            System.out.println("  Брой песни: " + list.size());
            System.out.println("  Обща продължителност: " + String.format("%.2f мин", totalDuration));
            System.out.println();
            
            for (int i = 0; i < list.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + list.get(i));
            }
        }
    }

    public void listAllPlaylists() {
        System.out.println("\n📋 Всички плейлисти:");
        if (playlists.isEmpty()) {
            System.out.println("  Няма създадени плейлисти.");
        } else {
            playlists.forEach((name, items) -> 
                System.out.println("  • " + name + " (" + items.size() + " обекта)")
            );
        }
    }

    // ==================== РАБОТА С ФАЙЛОВЕ ====================

    public void saveToFile(String filename, List<AudioItem> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (AudioItem item : list) {
                writer.println(item.toCSV());
            }
            System.out.println("💾 Успешно записано във файл: " + filename);
        } catch (IOException e) {
            System.out.println("❌ Грешка при запис: " + e.getMessage());
        }
    }

    /**
     * Записва всички плейлисти в един текстов файл.
     * Формат:
     * PLAYLIST:Име
     * SONG,...
     * ALBUM,...
     * (празен ред между плейлистите)
     */
    public void saveAllPlaylists(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, List<AudioItem>> entry : playlists.entrySet()) {
                String name = entry.getKey();
                List<AudioItem> items = entry.getValue();

                writer.println("PLAYLIST:" + name);
                for (AudioItem item : items) {
                    writer.println(item.toCSV());
                }
                writer.println(); // празен ред между плейлистите
            }

            System.out.println("💾 Всички плейлисти са записани във файл: " + filename);
        } catch (IOException e) {
            System.out.println("❌ Грешка при запис на плейлисти: " + e.getMessage());
        }
    }

    public void loadCatalogFromFile(String filename) {
        List<AudioItem> loaded = loadListFromFile(filename);
        if (!loaded.isEmpty()) {
            catalog.addAll(loaded);
            System.out.println("📂 Каталогът е зареден: " + loaded.size() + " обекта");
        }
    }

    public void loadPlaylistFromFile(String filename, String playlistName) {
        List<AudioItem> loaded = loadListFromFile(filename);
        if (!loaded.isEmpty()) {
            createPlaylist(playlistName);
            playlists.get(playlistName).addAll(loaded);
            System.out.println("📂 Плейлист '" + playlistName + "' е зареден: " + loaded.size() + " обекта");
        }
    }

    /**
     * Зарежда всички плейлисти от един текстов файл,
     * записан с формата на saveAllPlaylists.
     */
    public void loadAllPlaylists(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentPlaylistName = null;
            List<AudioItem> currentList = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("PLAYLIST:")) {
                    currentPlaylistName = line.substring("PLAYLIST:".length());
                    if (currentPlaylistName == null || currentPlaylistName.trim().isEmpty()) {
                        currentPlaylistName = null;
                        currentList = null;
                        continue;
                    }

                    // ако вече съществува, не я презаписваме, а я допълваме
                    playlists.putIfAbsent(currentPlaylistName, new ArrayList<>());
                    currentList = playlists.get(currentPlaylistName);
                } else if (currentPlaylistName != null && currentList != null) {
                    AudioItem item = parseCSVLine(line);
                    if (item != null) {
                        currentList.add(item);
                    }
                }
            }

            if (!playlists.isEmpty()) {
                System.out.println("📂 Заредени плейлисти от файл: " + filename);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ℹ️ Файлът " + filename + " не е намерен (няма записани плейлисти).");
        } catch (IOException e) {
            System.out.println("❌ Грешка при четене на плейлисти: " + e.getMessage());
        }
    }

    private List<AudioItem> loadListFromFile(String filename) {
        List<AudioItem> result = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AudioItem item = parseCSVLine(line);
                if (item != null) {
                    result.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ℹ️ Файлът " + filename + " не е намерен.");
        } catch (IOException e) {
            System.out.println("❌ Грешка при четене: " + e.getMessage());
        }
        
        return result;
    }

    private AudioItem parseCSVLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length < 2) return null;

            String type = parts[0].toUpperCase();
            
            switch (type) {
                case "SONG":
                    return Song.fromCSV(parts);
                case "ALBUM":
                    return Album.fromCSV(parts);
                case "PODCAST":
                    return Podcast.fromCSV(parts);
                case "AUDIOBOOK":
                    return Audiobook.fromCSV(parts);
                default:
                    System.out.println("⚠️ Непознат тип: " + type);
                    return null;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Грешка при парсиране на ред: " + e.getMessage());
            return null;
        }
    }

    // ==================== СТАТИСТИКА ====================

    public void showStatistics() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║         📊 СТАТИСТИКА                          ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        if (catalog.isEmpty()) {
            System.out.println("  Каталогът е празен.");
            return;
        }

        System.out.println("  Общо обекти: " + catalog.size());
        System.out.println("  Плейлисти: " + playlists.size());
        
        // Статистика по категории
        Map<AudioCategory, Long> categoryCounts = catalog.stream()
            .collect(Collectors.groupingBy(AudioItem::getCategory, Collectors.counting()));
        
        System.out.println("\n  По категории:");
        for (AudioCategory cat : AudioCategory.values()) {
            long count = categoryCounts.getOrDefault(cat, 0L);
            System.out.println("    • " + cat.getBgName() + ": " + count);
        }

        // Най-често срещан жанр
        Map<String, Long> genreCounts = catalog.stream()
            .collect(Collectors.groupingBy(AudioItem::getGenre, Collectors.counting()));
        
        String topGenre = genreCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
        
        System.out.println("\n  Най-популярен жанр: " + topGenre);
        
        // Обща продължителност
        double totalDuration = catalog.stream()
            .mapToDouble(AudioItem::getDuration)
            .sum();
        
        System.out.println("  Обща продължителност: " + String.format("%.2f мин (%.2f часа)", 
            totalDuration, totalDuration / 60));
    }

    // ==================== GETTERS ====================

    public List<AudioItem> getCatalog() { 
        return new ArrayList<>(catalog); // Връщаме копие за безопасност
    }
    
    public List<AudioItem> getPlaylist(String name) { 
        return playlists.get(name);
    }

    public int getCatalogSize() {
        return catalog.size();
    }
}