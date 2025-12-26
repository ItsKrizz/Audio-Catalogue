import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CatalogManager manager = new CatalogManager();

    public static void main(String[] args) {
        // Автоматично зареждане при старт
        System.out.println("🎵 Добре дошли в Аудио Организатор!");
        manager.loadCatalogFromFile("catalog.txt");
        manager.loadAllPlaylists("playlists.txt");
        
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addNewItem();
                        break;
                    case "2":
                        manager.showAll();
                        break;
                    case "3":
                        searchMenu();
                        break;
                    case "4":
                        deleteItem();
                        break;
                    case "5":
                        filterMenu();
                        break;
                    case "6":
                        sortMenu();
                        break;
                    case "7":
                        createPlaylistMenu();
                        break;
                    case "8":
                        addToPlaylistMenu();
                        break;
                    case "9":
                        removeFromPlaylistMenu();
                        break;
                    case "10":
                        showPlaylistMenu();
                        break;
                    case "11":
                        manager.listAllPlaylists();
                        break;
                    case "12":
                        deletePlaylistMenu();
                        break;
                    case "13":
                        saveMenu();
                        break;
                    case "14":
                        loadMenu();
                        break;
                    case "15":
                        manager.showStatistics();
                        break;
                    case "0":
                        // Автоматично запазване при изход
                        manager.saveToFile("catalog.txt", manager.getCatalog());
                        manager.saveAllPlaylists("playlists.txt");
                        System.out.println("\n👋 Довиждане!");
                        running = false;
                        break;
                    default:
                        System.out.println("❌ Невалидна опция! Опитай отново.");
                }
            } catch (Exception e) {
                System.out.println("❌ Грешка: " + e.getMessage());
            }

            if (running) {
                System.out.println("\n[Натисни Enter за продължаване...]");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    // ==================== МЕНЮ ====================

    private static void printMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║       🎵 АУДИО ОРГАНИЗАТОР 2.0 🎵              ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  ОСНОВНИ ОПЕРАЦИИ                              ║");
        System.out.println("║  1. ➕ Добави нов обект                        ║");
        System.out.println("║  2. 📋 Покажи целия каталог                    ║");
        System.out.println("║  3. 🔍 Търсене                                 ║");
        System.out.println("║  4. 🗑️  Изтриване от каталога                  ║");
        System.out.println("║  5. 📂 Филтриране                              ║");
        System.out.println("║  6. 🔤 Сортиране                               ║");
        System.out.println("║                                                ║");
        System.out.println("║  ПЛЕЙЛИСТИ                                     ║");
        System.out.println("║  7. ➕ Създай плейлист                         ║");
        System.out.println("║  8. ⬆️  Добави в плейлист                       ║");
        System.out.println("║  9. ⬇️  Махни от плейлист                       ║");
        System.out.println("║  10. 👁️  Преглед на плейлист                    ║");
        System.out.println("║  11. 📋 Списък на плейлистите                  ║");
        System.out.println("║  12. 🗑️  Изтрий плейлист                        ║");
        System.out.println("║                                                ║");
        System.out.println("║  ФАЙЛОВЕ & СТАТИСТИКА                          ║");
        System.out.println("║  13. 💾 Запис                                  ║");
        System.out.println("║  14. 📂 Зареждане                              ║");
        System.out.println("║  15. 📊 Статистика                             ║");
        System.out.println("║                                                ║");
        System.out.println("║  0. 🚪 Изход                                   ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("👉 Избери опция: ");
    }

    // ==================== ДОБАВЯНЕ ====================

    private static void addNewItem() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         ➕ ДОБАВЯНЕ НА НОВ ОБЕКТ               ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.println("Избери тип:");
        System.out.println("1. 🎵 Песен");
        System.out.println("2. 💿 Албум");
        System.out.println("3. 🎙️  Подкаст");
        System.out.println("4. 📚 Аудиокнига");
        System.out.print("\nТип: ");
        
        String type = scanner.nextLine().trim();

        try {
            // Общи полета
            System.out.print("\nЗаглавие: ");
            String title = scanner.nextLine();

            System.out.print("Автор/Изпълнител: ");
            String author = scanner.nextLine();

            System.out.print("Жанр: ");
            String genre = scanner.nextLine();

            System.out.print("Година: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Продължителност (минути): ");
            double duration = Double.parseDouble(scanner.nextLine());

            AudioItem item = null;

            // Специфични полета според типа
            switch (type) {
                case "1":
                    System.out.print("Албум (или 'Single'): ");
                    String album = scanner.nextLine();
                    item = new Song(title, author, genre, year, duration, album);
                    break;

                case "2":
                    System.out.print("Брой песни в албума: ");
                    int tracks = Integer.parseInt(scanner.nextLine());
                    item = new Album(title, author, genre, year, duration, tracks);
                    break;

                case "3":
                    System.out.print("Епизод №: ");
                    int episode = Integer.parseInt(scanner.nextLine());
                    System.out.print("Водещ: ");
                    String host = scanner.nextLine();
                    item = new Podcast(title, author, genre, year, duration, episode, host);
                    break;

                case "4":
                    System.out.print("Разказвач: ");
                    String narrator = scanner.nextLine();
                    System.out.print("Брой глави: ");
                    int chapters = Integer.parseInt(scanner.nextLine());
                    item = new Audiobook(title, author, genre, year, duration, narrator, chapters);
                    break;

                default:
                    System.out.println("❌ Невалиден тип!");
                    return;
            }

            manager.addItem(item);

        } catch (NumberFormatException e) {
            System.out.println("❌ Невалидно число!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    // ==================== ТЪРСЕНЕ ====================

    private static void searchMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         🔍 ТЪРСЕНЕ                             ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Въведи текст за търсене (заглавие/автор/жанр): ");
        String query = scanner.nextLine();
        manager.searchGeneral(query);
    }

    // ==================== ИЗТРИВАНЕ ====================

    private static void deleteItem() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         🗑️  ИЗТРИВАНЕ                           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Заглавие на обект за изтриване: ");
        String title = scanner.nextLine();
        manager.deleteItem(title);
    }

    // ==================== ФИЛТРИРАНЕ ====================

    private static void filterMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         📂 ФИЛТРИРАНЕ                          ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.println("Филтрирай по:");
        System.out.println("1. Категория (Песен/Албум/Подкаст/Аудиокнига)");
        System.out.println("2. Жанр");
        System.out.println("3. Автор");
        System.out.println("4. Година");
        System.out.println("5. Период (от-до)");
        System.out.print("\nИзбор: ");
        
        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    System.out.println("\n0. Песен");
                    System.out.println("1. Албум");
                    System.out.println("2. Подкаст");
                    System.out.println("3. Аудиокнига");
                    System.out.print("Избери: ");
                    int catIndex = Integer.parseInt(scanner.nextLine());
                    AudioCategory category = AudioCategory.values()[catIndex];
                    manager.filterByCategory(category);
                    break;

                case "2":
                    System.out.print("Жанр: ");
                    manager.filterByGenre(scanner.nextLine());
                    break;

                case "3":
                    System.out.print("Автор: ");
                    manager.filterByAuthor(scanner.nextLine());
                    break;

                case "4":
                    System.out.print("Година: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    manager.filterByYear(year);
                    break;

                case "5":
                    System.out.print("От година: ");
                    int startYear = Integer.parseInt(scanner.nextLine());
                    System.out.print("До година: ");
                    int endYear = Integer.parseInt(scanner.nextLine());
                    manager.filterByYearRange(startYear, endYear);
                    break;

                default:
                    System.out.println("❌ Невалиден избор!");
            }
        } catch (Exception e) {
            System.out.println("❌ Грешка: " + e.getMessage());
        }
    }

    // ==================== СОРТИРАНЕ ====================

    private static void sortMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         🔤 СОРТИРАНЕ                           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.println("Сортирай по:");
        System.out.println("1. Заглавие (А-Я)");
        System.out.println("2. Автор (А-Я)");
        System.out.println("3. Година (възходящо)");
        System.out.println("4. Продължителност");
        System.out.print("\nИзбор на критерий: ");
        
        String choice = scanner.nextLine().trim();
        String criteria;

        switch (choice) {
            case "1":
                criteria = "title";
                break;
            case "2":
                criteria = "author";
                break;
            case "3":
                criteria = "year";
                break;
            case "4":
                criteria = "duration";
                break;
            default:
                System.out.println("❌ Невалиден избор на критерий!");
                return;
        }

        System.out.println("\nКое искаш да сортираш?");
        System.out.println("1. Целия каталог");
        System.out.println("2. Конкретен плейлист");
        System.out.print("\nИзбор: ");

        String target = scanner.nextLine().trim();

        switch (target) {
            case "1":
                manager.sortCatalog(criteria);
                break;
            case "2":
                System.out.print("Име на плейлист: ");
                String playlistName = scanner.nextLine();
                manager.sortPlaylist(playlistName, criteria);
                break;
            default:
                System.out.println("❌ Невалиден избор за цел на сортиране!");
        }
    }

    // ==================== ПЛЕЙЛИСТИ ====================

    private static void createPlaylistMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         ➕ СЪЗДАВАНЕ НА ПЛЕЙЛИСТ               ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Име на новия плейлист: ");
        String name = scanner.nextLine();
        manager.createPlaylist(name);
    }

    private static void addToPlaylistMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         ⬆️  ДОБАВЯНЕ В ПЛЕЙЛИСТ                 ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Име на плейлист: ");
        String playlist = scanner.nextLine();
        System.out.print("Заглавие на обекта: ");
        String title = scanner.nextLine();
        manager.addToPlaylist(playlist, title);
    }

    private static void removeFromPlaylistMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         ⬇️  ПРЕМАХВАНЕ ОТ ПЛЕЙЛИСТ              ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Име на плейлист: ");
        String playlist = scanner.nextLine();
        System.out.print("Заглавие за премахване: ");
        String title = scanner.nextLine();
        manager.removeFromPlaylist(playlist, title);
    }

    private static void showPlaylistMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         👁️  ПРЕГЛЕД НА ПЛЕЙЛИСТ                 ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Име на плейлист: ");
        String name = scanner.nextLine();
        manager.showPlaylist(name);
    }

    private static void deletePlaylistMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         🗑️  ИЗТРИВАНЕ НА ПЛЕЙЛИСТ               ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("Име на плейлист за изтриване: ");
        String name = scanner.nextLine();
        manager.deletePlaylist(name);
    }

    // ==================== ФАЙЛОВЕ ====================

    private static void saveMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         💾 ЗАПАЗВАНЕ                           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.println("Какво да запазя?");
        System.out.println("1. Целия каталог");
        System.out.println("2. Конкретен плейлист");
        System.out.print("\nИзбор: ");
        
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                manager.saveToFile("catalog.txt", manager.getCatalog());
                break;
            case "2":
                System.out.print("Име на плейлист: ");
                String plName = scanner.nextLine();
                if (manager.getPlaylist(plName) != null) {
                    System.out.print("Име на файл (без разширение): ");
                    String fileName = scanner.nextLine();
                    manager.saveToFile(fileName + ".txt", manager.getPlaylist(plName));
                } else {
                    System.out.println("❌ Няма такъв плейлист.");
                }
                break;
            default:
                System.out.println("❌ Невалиден избор!");
        }
    }

    private static void loadMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         📂 ЗАРЕЖДАНЕ                           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.println("Какво да заредя?");
        System.out.println("1. Целия каталог (catalog.txt)");
        System.out.println("2. Плейлист от файл");
        System.out.print("\nИзбор: ");
        
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                manager.loadCatalogFromFile("catalog.txt");
                break;
            case "2":
                System.out.print("Име на файл (с .txt): ");
                String fileName = scanner.nextLine();
                System.out.print("Име на новия плейлист: ");
                String plName = scanner.nextLine();
                manager.loadPlaylistFromFile(fileName, plName);
                break;
            default:
                System.out.println("❌ Невалиден избор!");
        }
    }

    // ==================== ПОМОЩНИ МЕТОДИ ====================

    private static void clearScreen() {
        // За Windows
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            // За Linux/Mac или ако Windows командата не работи
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}