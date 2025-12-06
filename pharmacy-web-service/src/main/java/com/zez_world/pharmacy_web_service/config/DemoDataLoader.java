package com.zez_world.pharmacy_web_service.config;

import com.zez_world.pharmacy_web_service.entity.*;
import com.zez_world.pharmacy_web_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Component
public class DemoDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (userRepository.count() == 0) {
            loadDemoData();
        }
    }

    private void loadDemoData() {
        System.out.println("=== ЗАГРУЗКА ДЕМО-ДАННЫХ ДЛЯ АПТЕКИ (БЕЛАРУСЬ) ===");

        // 1. Создаем пользователей с белорусскими номерами
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@pharmacy.by");
        admin.setPhone("+375291234567");
        admin.setRole(Role.ROLE_ADMIN);

        User user1 = new User();
        user1.setUsername("user");
        user1.setPassword(passwordEncoder.encode("user"));
        user1.setEmail("user@mail.by");
        user1.setPhone("+375297654321");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user2.setUsername("ivanov");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setEmail("ivanov@gmail.com");
        user2.setPhone("+375336789012");
        user2.setRole(Role.ROLE_USER);

        userRepository.saveAll(Arrays.asList(admin, user1, user2));

        // 2. Создаем список продуктов
        List<Product> products = new ArrayList<>();

        // Группа 1: Обезболивающие и жаропонижающие (с аналогами)
        // Аналоги на основе Ибупрофена
        products.add(createProduct("Нурофен Экспресс", "Рекитт Бенкизер Хелскэр", "Капсулы", 200,
                LocalDate.of(2025, 12, 31), PrescriptionStatus.NON_PRESCRIPTION,
                8.50, 45, "Ибупрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Ибупрофен-Акрихин", "Акрихин", "Таблетки 200мг", 20,
                LocalDate.of(2025, 10, 15), PrescriptionStatus.NON_PRESCRIPTION,
                3.20, 120, "Ибупрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("МИГ 400", "Берлин-Хеми", "Таблетки 400мг", 10,
                LocalDate.of(2025, 8, 30), PrescriptionStatus.NON_PRESCRIPTION,
                5.80, 75, "Ибупрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Фаспик", "Замбон", "Гранулы 400мг", 10,
                LocalDate.of(2025, 11, 20), PrescriptionStatus.NON_PRESCRIPTION,
                7.90, 40, "Ибупрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Ибупрофен-Белмед", "Белмедпрепараты", "Таблетки 200мг", 20,
                LocalDate.of(2026, 1, 15), PrescriptionStatus.NON_PRESCRIPTION,
                2.90, 200, "Ибупрофен", "Обезболивающие и жаропонижающие"));

        // Аналоги на основе Парацетамола
        products.add(createProduct("Панадол", "ГлаксоСмитКляйн", "Таблетки 500мг", 12,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.NON_PRESCRIPTION,
                4.20, 150, "Парацетамол", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Эффералган", "UPSA", "Шипучие таблетки 500мг", 16,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                6.50, 90, "Парацетамол", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Парацетамол-Белмед", "Белмедпрепараты", "Таблетки 500мг", 10,
                LocalDate.of(2025, 12, 20), PrescriptionStatus.NON_PRESCRIPTION,
                1.80, 300, "Парацетамол", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Цефекон Д", "Нижфарм", "Свечи 100мг", 10,
                LocalDate.of(2025, 6, 25), PrescriptionStatus.NON_PRESCRIPTION,
                3.40, 60, "Парацетамол", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Калпол", "ГлаксоСмитКляйн", "Суспензия 120мг/5мл", 1,
                LocalDate.of(2025, 5, 20), PrescriptionStatus.NON_PRESCRIPTION,
                8.90, 45, "Парацетамол", "Обезболивающие и жаропонижающие"));

        // Аналоги на основе Кетопрофена
        products.add(createProduct("Кетонал Дуо", "Сандоз", "Капсулы 150мг", 20,
                LocalDate.of(2025, 5, 20), PrescriptionStatus.NON_PRESCRIPTION,
                12.50, 35, "Кетопрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Кетопрофен Велфарм", "Велфарм", "Гель 2.5% 50г", 1,
                LocalDate.of(2025, 7, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.80, 50, "Кетопрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Флексен", "Италфармако", "Свечи 100мг", 10,
                LocalDate.of(2025, 4, 10), PrescriptionStatus.PRESCRIPTION,
                9.20, 25, "Кетопрофен", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Артрозилен", "Домпе", "Спрей 10%", 1,
                LocalDate.of(2025, 9, 5), PrescriptionStatus.NON_PRESCRIPTION,
                11.40, 30, "Кетопрофен", "Обезболивающие и жаропонижающие"));

        // Аналоги на основе Нимесулида
        products.add(createProduct("Найз", "Д-р Реддис", "Таблетки 100мг", 20,
                LocalDate.of(2025, 3, 15), PrescriptionStatus.NON_PRESCRIPTION,
                6.70, 85, "Нимесулид", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Нимид", "Ипка", "Таблетки 100мг", 20,
                LocalDate.of(2025, 6, 20), PrescriptionStatus.NON_PRESCRIPTION,
                4.90, 95, "Нимесулид", "Обезболивающие и жаропонижающие"));
        products.add(createProduct("Нимесулид-Тева", "Тева", "Таблетки 100мг", 30,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                8.20, 60, "Нимесулид", "Обезболивающие и жаропонижающие"));

        // Группа 2: Антибиотики (с аналогами)
        // Аналоги на основе Амоксициллина/клавулановой кислоты
        products.add(createProduct("Амоксиклав 625", "Сандоз", "Таблетки 625мг", 14,
                LocalDate.of(2024, 11, 30), PrescriptionStatus.PRESCRIPTION,
                18.50, 25, "Амоксициллин/клавулановая кислота", "Антибиотики"));
        products.add(createProduct("Аугментин 625", "ГлаксоСмитКляйн", "Таблетки 625мг", 14,
                LocalDate.of(2025, 1, 15), PrescriptionStatus.PRESCRIPTION,
                20.30, 20, "Амоксициллин/клавулановая кислота", "Антибиотики"));
        products.add(createProduct("Флемоклав Солютаб 625", "Астеллас", "Таблетки диспергируемые 625мг", 10,
                LocalDate.of(2025, 2, 28), PrescriptionStatus.PRESCRIPTION,
                22.80, 18, "Амоксициллин/клавулановая кислота", "Антибиотики"));

        // Аналоги на основе Цефиксима
        products.add(createProduct("Супракс", "Астеллас", "Капсулы 400мг", 6,
                LocalDate.of(2025, 3, 15), PrescriptionStatus.PRESCRIPTION,
                28.90, 18, "Цефиксим", "Антибиотики"));
        products.add(createProduct("Цефикс", "Здравле", "Капсулы 400мг", 6,
                LocalDate.of(2025, 5, 20), PrescriptionStatus.PRESCRIPTION,
                24.50, 22, "Цефиксим", "Антибиотики"));
        products.add(createProduct("Иксим Люпин", "Люпин", "Порошок для суспензии 100мг/5мл", 1,
                LocalDate.of(2025, 4, 10), PrescriptionStatus.PRESCRIPTION,
                19.80, 15, "Цефиксим", "Антибиотики"));

        // Аналоги на основе Азитромицина
        products.add(createProduct("Сумамед", "Плива", "Таблетки 500мг", 3,
                LocalDate.of(2025, 6, 30), PrescriptionStatus.PRESCRIPTION,
                15.60, 40, "Азитромицин", "Антибиотики"));
        products.add(createProduct("Азитромицин-Белмед", "Белмедпрепараты", "Капсулы 500мг", 3,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.PRESCRIPTION,
                12.40, 55, "Азитромицин", "Антибиотики"));
        products.add(createProduct("Хемомицин", "Хемофарм", "Капсулы 500мг", 3,
                LocalDate.of(2025, 7, 20), PrescriptionStatus.PRESCRIPTION,
                14.20, 35, "Азитромицин", "Антибиотики"));
        products.add(createProduct("Зитрокс", "Верофарм", "Капсулы 500мг", 3,
                LocalDate.of(2025, 9, 25), PrescriptionStatus.PRESCRIPTION,
                13.80, 42, "Азитромицин", "Антибиотики"));

        // Аналоги на основе Цефтриаксона
        products.add(createProduct("Цефтриаксон-КМП", "Красфарма", "Порошок для инъекций 1г", 1,
                LocalDate.of(2025, 10, 30), PrescriptionStatus.PRESCRIPTION,
                4.20, 120, "Цефтриаксон", "Антибиотики"));
        products.add(createProduct("Роцефин", "Хоффманн-ля Рош", "Порошок для инъекций 1г", 1,
                LocalDate.of(2025, 12, 15), PrescriptionStatus.PRESCRIPTION,
                9.80, 65, "Цефтриаксон", "Антибиотики"));
        products.add(createProduct("Триаксон", "Белмедпрепараты", "Порошок для инъекций 1г", 1,
                LocalDate.of(2025, 11, 20), PrescriptionStatus.PRESCRIPTION,
                3.90, 150, "Цефтриаксон", "Антибиотики"));

        // Группа 3: Витамины (с аналогами)
        // Аналоги на основе Аскорбиновой кислоты
        products.add(createProduct("Витамин C 1000mg", "Солгар", "Капсулы", 120,
                LocalDate.of(2026, 3, 20), PrescriptionStatus.NON_PRESCRIPTION,
                32.50, 60, "Аскорбиновая кислота", "Витамины и БАДы"));
        products.add(createProduct("Аскорбиновая кислота с глюкозой", "Марбиофарм", "Таблетки 25мг", 10,
                LocalDate.of(2025, 11, 10), PrescriptionStatus.NON_PRESCRIPTION,
                1.20, 200, "Аскорбиновая кислота", "Витамины и БАДы"));
        products.add(createProduct("Асвитол", "Борисовский завод", "Таблетки жевательные 50мг", 30,
                LocalDate.of(2025, 12, 15), PrescriptionStatus.NON_PRESCRIPTION,
                3.80, 85, "Аскорбиновая кислота", "Витамины и БАДы"));

        // Аналоги на основе Поливитаминов
        products.add(createProduct("Компливит", "Фармстандарт", "Таблетки", 60,
                LocalDate.of(2025, 11, 10), PrescriptionStatus.NON_PRESCRIPTION,
                9.50, 85, "Поливитамины", "Витамины и БАДы"));
        products.add(createProduct("Витрум", "Юнифарм", "Таблетки", 30,
                LocalDate.of(2025, 10, 5), PrescriptionStatus.NON_PRESCRIPTION,
                14.20, 60, "Поливитамины", "Витамины и БАДы"));
        products.add(createProduct("Супрадин", "Байер", "Таблетки шипучие", 20,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                11.80, 75, "Поливитамины", "Витамины и БАДы"));
        products.add(createProduct("Мульти-табс Классик", "Пфайзер", "Таблетки", 30,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                13.40, 55, "Поливитамины", "Витамины и БАДы"));

        // Аналоги на основе Витамина D
        products.add(createProduct("Аквадетрим", "Медана Фарма", "Капли 15000МЕ/мл", 1,
                LocalDate.of(2025, 7, 20), PrescriptionStatus.NON_PRESCRIPTION,
                7.90, 90, "Колекальциферол", "Витамины и БАДы"));
        products.add(createProduct("Вигантол", "Мерк", "Капли 20000МЕ/мл", 1,
                LocalDate.of(2025, 6, 15), PrescriptionStatus.NON_PRESCRIPTION,
                8.50, 70, "Колекальциферол", "Витамины и БАДы"));
        products.add(createProduct("Ультра-Д", "Орион Фарма", "Таблетки 25мкг", 30,
                LocalDate.of(2025, 9, 10), PrescriptionStatus.NON_PRESCRIPTION,
                12.30, 45, "Колекальциферол", "Витамины и БАДы"));

        // Группа 4: Сердечно-сосудистые (с аналогами)
        // Аналоги на основе Ацетилсалициловой кислоты
        products.add(createProduct("Кардиомагнил", "Такеда", "Таблетки 75мг", 30,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                11.50, 80, "Ацетилсалициловая кислота", "Сердечно-сосудистые"));
        products.add(createProduct("Тромбо АСС", "Г.Л.Фарма", "Таблетки 100мг", 28,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 65, "Ацетилсалициловая кислота", "Сердечно-сосудистые"));
        products.add(createProduct("Аспирин Кардио", "Байер", "Таблетки 100мг", 28,
                LocalDate.of(2025, 10, 15), PrescriptionStatus.NON_PRESCRIPTION,
                10.20, 75, "Ацетилсалициловая кислота", "Сердечно-сосудистые"));

        // Аналоги на основе Пропранолола
        products.add(createProduct("Анаприлин", "Озон", "Таблетки 40мг", 50,
                LocalDate.of(2025, 7, 25), PrescriptionStatus.PRESCRIPTION,
                3.20, 45, "Пропранолол", "Сердечно-сосудистые"));
        products.add(createProduct("Пропранолол-Белмед", "Белмедпрепараты", "Таблетки 40мг", 50,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.PRESCRIPTION,
                2.90, 60, "Пропранолол", "Сердечно-сосудистые"));
        products.add(createProduct("Обзидан", "АстраЗенека", "Таблетки 40мг", 50,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.PRESCRIPTION,
                4.10, 35, "Пропранолол", "Сердечно-сосудистые"));

        // Аналоги на основе Аторвастатина
        products.add(createProduct("Липримар", "Пфайзер", "Таблетки 20мг", 30,
                LocalDate.of(2025, 11, 30), PrescriptionStatus.PRESCRIPTION,
                25.80, 40, "Аторвастатин", "Сердечно-сосудистые"));
        products.add(createProduct("Аторвастатин-Тева", "Тева", "Таблетки 20мг", 30,
                LocalDate.of(2025, 10, 25), PrescriptionStatus.PRESCRIPTION,
                18.40, 55, "Аторвастатин", "Сердечно-сосудистые"));
        products.add(createProduct("Торвакард", "Зентива", "Таблетки 20мг", 30,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.PRESCRIPTION,
                21.50, 48, "Аторвастатин", "Сердечно-сосудистые"));

        // Аналоги на основе Эналаприла
        products.add(createProduct("Энап", "КРКА", "Таблетки 5мг", 20,
                LocalDate.of(2025, 6, 30), PrescriptionStatus.PRESCRIPTION,
                5.60, 85, "Эналаприл", "Сердечно-сосудистые"));
        products.add(createProduct("Эналаприл-Белмед", "Белмедпрепараты", "Таблетки 5мг", 20,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.PRESCRIPTION,
                4.20, 120, "Эналаприл", "Сердечно-сосудистые"));
        products.add(createProduct("Ренитек", "Мерк", "Таблетки 5мг", 14,
                LocalDate.of(2025, 7, 20), PrescriptionStatus.PRESCRIPTION,
                7.80, 65, "Эналаприл", "Сердечно-сосудистые"));

        // Группа 5: Желудочно-кишечные (с аналогами)
        // Аналоги на основе Панкреатина
        products.add(createProduct("Мезим Форте", "Берлин-Хеми", "Таблетки", 20,
                LocalDate.of(2025, 6, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.90, 95, "Панкреатин", "Желудочно-кишечные"));
        products.add(createProduct("Панкреатин-Белмед", "Белмедпрепараты", "Таблетки", 20,
                LocalDate.of(2025, 8, 20), PrescriptionStatus.NON_PRESCRIPTION,
                5.40, 150, "Панкреатин", "Желудочно-кишечные"));
        products.add(createProduct("Креон 10000", "Эбботт", "Капсулы", 20,
                LocalDate.of(2025, 9, 25), PrescriptionStatus.NON_PRESCRIPTION,
                15.80, 70, "Панкреатин", "Желудочно-кишечные"));
        products.add(createProduct("Фестал", "Санофи", "Таблетки", 20,
                LocalDate.of(2025, 7, 30), PrescriptionStatus.NON_PRESCRIPTION,
                8.50, 85, "Панкреатин", "Желудочно-кишечные"));

        // Аналоги на основе Симетикона
        products.add(createProduct("Эспумизан", "Берлин-Хеми", "Капсулы 40мг", 25,
                LocalDate.of(2025, 10, 5), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 65, "Симетикон", "Желудочно-кишечные"));
        products.add(createProduct("Симетикон-Белмед", "Белмедпрепараты", "Капсулы 40мг", 30,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                6.50, 95, "Симетикон", "Желудочно-кишечные"));
        products.add(createProduct("Боботик", "Юнифарм", "Капли 66мг/мл", 1,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.NON_PRESCRIPTION,
                8.20, 55, "Симетикон", "Желудочно-кишечные"));

        // Аналоги на основе Омепразола
        products.add(createProduct("Омез", "Д-р Реддис", "Капсулы 20мг", 30,
                LocalDate.of(2025, 12, 10), PrescriptionStatus.NON_PRESCRIPTION,
                8.90, 80, "Омепразол", "Желудочно-кишечные"));
        products.add(createProduct("Омепразол-Белмед", "Белмедпрепараты", "Капсулы 20мг", 30,
                LocalDate.of(2025, 11, 5), PrescriptionStatus.NON_PRESCRIPTION,
                6.20, 120, "Омепразол", "Желудочно-кишечные"));
        products.add(createProduct("Лосек Мапс", "АстраЗенека", "Таблетки 20мг", 14,
                LocalDate.of(2025, 10, 30), PrescriptionStatus.PRESCRIPTION,
                12.50, 45, "Омепразол", "Желудочно-кишечные"));

        // Аналоги на основе Лоперамида
        products.add(createProduct("Имодиум", "Джонсон & Джонсон", "Таблетки 2мг", 10,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.NON_PRESCRIPTION,
                5.60, 75, "Лоперамид", "Желудочно-кишечные"));
        products.add(createProduct("Лоперамид-Белмед", "Белмедпрепараты", "Таблетки 2мг", 20,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.NON_PRESCRIPTION,
                3.20, 110, "Лоперамид", "Желудочно-кишечные"));
        products.add(createProduct("Диара", "Татхимфармпрепараты", "Таблетки 2мг", 10,
                LocalDate.of(2025, 7, 25), PrescriptionStatus.NON_PRESCRIPTION,
                4.10, 85, "Лоперамид", "Желудочно-кишечные"));

        // Группа 6: Противовирусные (с аналогами)
        // Аналоги на основе Умифеновира
        products.add(createProduct("Арбидол", "Фармстандарт", "Капсулы 100мг", 10,
                LocalDate.of(2025, 4, 18), PrescriptionStatus.NON_PRESCRIPTION,
                13.50, 40, "Умифеновир", "Противовирусные"));
        products.add(createProduct("Арпетол", "Белмедпрепараты", "Таблетки 50мг", 20,
                LocalDate.of(2025, 6, 20), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 60, "Умифеновир", "Противовирусные"));
        products.add(createProduct("Арбивир", "Вертекс", "Капсулы 100мг", 10,
                LocalDate.of(2025, 5, 15), PrescriptionStatus.NON_PRESCRIPTION,
                11.20, 45, "Умифеновир", "Противовирусные"));

        // Аналоги на основе Кагоцела
        products.add(createProduct("Кагоцел", "Ниармедик", "Таблетки 12мг", 10,
                LocalDate.of(2025, 8, 22), PrescriptionStatus.NON_PRESCRIPTION,
                12.80, 55, "Кагоцел", "Противовирусные"));
        products.add(createProduct("Кагоцел-Белмед", "Белмедпрепараты", "Таблетки 12мг", 10,
                LocalDate.of(2025, 10, 15), PrescriptionStatus.NON_PRESCRIPTION,
                10.50, 70, "Кагоцел", "Противовирусные"));

        // Аналоги на основе Осельтамивира
        products.add(createProduct("Тамифлю", "Хоффманн-ля Рош", "Капсулы 75мг", 10,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.PRESCRIPTION,
                35.80, 25, "Осельтамивир", "Противовирусные"));
        products.add(createProduct("Осельтамивир-Канон", "Канонфарма", "Капсулы 75мг", 10,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.PRESCRIPTION,
                28.40, 35, "Осельтамивир", "Противовирусные"));
        products.add(createProduct("Номидес", "Фармстандарт", "Капсулы 75мг", 10,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.PRESCRIPTION,
                30.20, 30, "Осельтамивир", "Противовирусные"));

        // Группа 7: Антигистаминные (с аналогами)
        // Аналоги на основе Лоратадина
        products.add(createProduct("Кларитин", "Шеринг-Плау", "Таблетки 10мг", 10,
                LocalDate.of(2025, 7, 15), PrescriptionStatus.NON_PRESCRIPTION,
                8.90, 65, "Лоратадин", "Антигистаминные"));
        products.add(createProduct("Лоратадин-Белмед", "Белмедпрепараты", "Таблетки 10мг", 10,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.NON_PRESCRIPTION,
                4.20, 120, "Лоратадин", "Антигистаминные"));
        products.add(createProduct("Ломилан", "КРКА", "Таблетки 10мг", 10,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                6.50, 85, "Лоратадин", "Антигистаминные"));

        // Аналоги на основе Цетиризина
        products.add(createProduct("Зиртек", "ЮСБ Фарма", "Капли 10мг/мл", 1,
                LocalDate.of(2025, 6, 30), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 55, "Цетиризин", "Антигистаминные"));
        products.add(createProduct("Цетрин", "Д-р Реддис", "Таблетки 10мг", 20,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.20, 75, "Цетиризин", "Антигистаминные"));
        products.add(createProduct("Цетиризин-Белмед", "Белмедпрепараты", "Таблетки 10мг", 20,
                LocalDate.of(2025, 10, 10), PrescriptionStatus.NON_PRESCRIPTION,
                5.40, 95, "Цетиризин", "Антигистаминные"));

        // Аналоги на основе Дезлоратадина
        products.add(createProduct("Эриус", "Байер", "Таблетки 5мг", 10,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                11.50, 50, "Дезлоратадин", "Антигистаминные"));
        products.add(createProduct("Дезал", "Актавис", "Таблетки 5мг", 10,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                9.20, 65, "Дезлоратадин", "Антигистаминные"));

        // Группа 8: Антидепрессанты и успокоительные
        products.add(createProduct("Афобазол", "Фармстандарт", "Таблетки 10мг", 60,
                LocalDate.of(2025, 12, 20), PrescriptionStatus.NON_PRESCRIPTION,
                15.80, 40, "Фабомотизол", "Антидепрессанты и успокоительные"));
        products.add(createProduct("Глицин", "Биотики", "Таблетки 100мг", 50,
                LocalDate.of(2025, 10, 15), PrescriptionStatus.NON_PRESCRIPTION,
                2.50, 200, "Глицин", "Антидепрессанты и успокоительные"));
        products.add(createProduct("Ново-Пассит", "Тева", "Таблетки", 30,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                12.40, 55, "Растительный экстракт", "Антидепрессанты и успокоительные"));
        products.add(createProduct("Персен", "Сандоз", "Таблетки", 40,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 70, "Растительный экстракт", "Антидепрессанты и успокоительные"));
        products.add(createProduct("Фенибут", "Олайнфарм", "Таблетки 250мг", 20,
                LocalDate.of(2025, 7, 20), PrescriptionStatus.PRESCRIPTION,
                8.50, 45, "Аминофенилмасляная кислота", "Антидепрессанты и успокоительные"));

        // Группа 9: Средства для лечения диабета
        products.add(createProduct("Метформин", "Тева", "Таблетки 850мг", 30,
                LocalDate.of(2025, 11, 30), PrescriptionStatus.PRESCRIPTION,
                6.80, 90, "Метформин", "Средства для лечения диабета"));
        products.add(createProduct("Глюкофаж", "Мерк", "Таблетки 850мг", 30,
                LocalDate.of(2025, 10, 25), PrescriptionStatus.PRESCRIPTION,
                8.90, 75, "Метформин", "Средства для лечения диабета"));
        products.add(createProduct("Сиофор 850", "Берлин-Хеми", "Таблетки 850мг", 30,
                LocalDate.of(2025, 9, 20), PrescriptionStatus.PRESCRIPTION,
                7.50, 80, "Метформин", "Средства для лечения диабета"));

        // Группа 10: Гормональные препараты
        products.add(createProduct("Дюфастон", "Эбботт", "Таблетки 10мг", 20,
                LocalDate.of(2025, 12, 15), PrescriptionStatus.PRESCRIPTION,
                22.80, 35, "Дидрогестерон", "Гормональные препараты"));
        products.add(createProduct("Утрожестан", "Безинг", "Капсулы 100мг", 28,
                LocalDate.of(2025, 11, 10), PrescriptionStatus.PRESCRIPTION,
                18.50, 40, "Прогестерон", "Гормональные препараты"));
        products.add(createProduct("Л-Тироксин", "Берлин-Хеми", "Таблетки 100мкг", 50,
                LocalDate.of(2025, 10, 5), PrescriptionStatus.PRESCRIPTION,
                5.20, 120, "Левотироксин", "Гормональные препараты"));

        // Группа 11: Противогрибковые
        products.add(createProduct("Флуконазол", "Вертекс", "Капсулы 150мг", 1,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.PRESCRIPTION,
                3.80, 85, "Флуконазол", "Противогрибковые"));
        products.add(createProduct("Дифлюкан", "Пфайзер", "Капсулы 150мг", 1,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.PRESCRIPTION,
                12.50, 45, "Флуконазол", "Противогрибковые"));
        products.add(createProduct("Микосист", "Гедеон Рихтер", "Капсулы 150мг", 1,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.PRESCRIPTION,
                8.90, 60, "Флуконазол", "Противогрибковые"));
        products.add(createProduct("Клотримазол", "Белмедпрепараты", "Крем 1% 20г", 1,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                4.20, 95, "Клотримазол", "Противогрибковые"));

        // Группа 12: Средства для лечения заболеваний кожи
        products.add(createProduct("Бепантен", "Байер", "Крем 5% 30г", 1,
                LocalDate.of(2025, 12, 20), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 70, "Декспантенол", "Дерматологические средства"));
        products.add(createProduct("Д-Пантенол", "Мерк", "Мазь 5% 25г", 1,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.50, 85, "Декспантенол", "Дерматологические средства"));
        products.add(createProduct("Солкосерил", "Меда Фарма", "Мазь 5% 20г", 1,
                LocalDate.of(2025, 10, 10), PrescriptionStatus.NON_PRESCRIPTION,
                12.40, 45, "Депротеинизированный гемодериват", "Дерматологические средства"));
        products.add(createProduct("Акридерм", "Акрихин", "Мазь 0.05% 15г", 1,
                LocalDate.of(2025, 9, 5), PrescriptionStatus.PRESCRIPTION,
                8.90, 55, "Бетаметазон", "Дерматологические средства"));

        // Группа 13: Глазные капли
        products.add(createProduct("Визин Классический", "Джонсон & Джонсон", "Капли 0.05% 15мл", 1,
                LocalDate.of(2025, 8, 30), PrescriptionStatus.NON_PRESCRIPTION,
                6.80, 65, "Тетризолин", "Офтальмологические средства"));
        products.add(createProduct("Систейн Ультра", "Алкон", "Капли 10мл", 1,
                LocalDate.of(2025, 9, 25), PrescriptionStatus.NON_PRESCRIPTION,
                8.50, 75, "Полиэтиленгликоль", "Офтальмологические средства"));
        products.add(createProduct("Тауфон", "Московский эндокринный завод", "Капли 4% 10мл", 1,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.NON_PRESCRIPTION,
                5.20, 90, "Таурин", "Офтальмологические средства"));

        // Группа 14: Ушные капли
        products.add(createProduct("Отипакс", "Биокодекс", "Капли 16г", 1,
                LocalDate.of(2025, 7, 15), PrescriptionStatus.NON_PRESCRIPTION,
                9.80, 50, "Лидокаин/феназон", "Оториноларингологические средства"));
        products.add(createProduct("Анауран", "Замбон", "Капли 25мл", 1,
                LocalDate.of(2025, 8, 10), PrescriptionStatus.PRESCRIPTION,
                12.50, 35, "Лидокаин/полимиксин", "Оториноларингологические средства"));

        // Группа 15: Средства для лечения простуды и гриппа
        products.add(createProduct("Терафлю", "ГлаксоСмитКляйн", "Порошок 10 пакетов", 1,
                LocalDate.of(2025, 6, 30), PrescriptionStatus.NON_PRESCRIPTION,
                8.90, 85, "Парацетамол/фенирамин/фенилэфрин", "Простудные заболевания"));
        products.add(createProduct("Колдрекс", "ГлаксоСмитКляйн", "Порошок 10 пакетов", 1,
                LocalDate.of(2025, 7, 25), PrescriptionStatus.NON_PRESCRIPTION,
                9.50, 75, "Парацетамол/фенилэфрин", "Простудные заболевания"));
        products.add(createProduct("Ринза", "Д-р Реддис", "Таблетки 10 шт", 1,
                LocalDate.of(2025, 8, 20), PrescriptionStatus.NON_PRESCRIPTION,
                6.80, 95, "Парацетамол/кофеин", "Простудные заболевания"));
        products.add(createProduct("Стоптуссин", "Тева", "Таблетки 20 шт", 1,
                LocalDate.of(2025, 9, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.40, 65, "Гвайфенезин/бутамират", "Простудные заболевания"));

        // Группа 16: Средства для лечения кашля
        products.add(createProduct("Амбробене", "Мерк", "Сироп 100мг/100мл", 1,
                LocalDate.of(2025, 10, 10), PrescriptionStatus.NON_PRESCRIPTION,
                6.90, 80, "Амброксол", "Средства от кашля"));
        products.add(createProduct("Лазолван", "Берингер Ингельхайм", "Сироп 30мг/5мл", 1,
                LocalDate.of(2025, 11, 5), PrescriptionStatus.NON_PRESCRIPTION,
                8.20, 70, "Амброксол", "Средства от кашля"));
        products.add(createProduct("АЦЦ 200", "Гедеон Рихтер", "Шипучие таблетки 200мг", 20,
                LocalDate.of(2025, 12, 20), PrescriptionStatus.NON_PRESCRIPTION,
                7.50, 90, "Ацетилцистеин", "Средства от кашля"));
        products.add(createProduct("Флуимуцил", "Замбон", "Гранулы 200мг", 20,
                LocalDate.of(2025, 9, 15), PrescriptionStatus.NON_PRESCRIPTION,
                8.80, 65, "Ацетилцистеин", "Средства от кашля"));

        // Группа 17: Средства для лечения насморка
        products.add(createProduct("Називин", "Мерк", "Спрей 0.05% 10мл", 1,
                LocalDate.of(2025, 8, 30), PrescriptionStatus.NON_PRESCRIPTION,
                5.80, 110, "Оксиметазолин", "Средства от насморка"));
        products.add(createProduct("Риностоп", "Лекко", "Спрей 0.1% 15мл", 1,
                LocalDate.of(2025, 9, 25), PrescriptionStatus.NON_PRESCRIPTION,
                4.90, 95, "Ксилометазолин", "Средства от насморка"));
        products.add(createProduct("Тизин Ксило", "ГлаксоСмитКляйн", "Спрей 0.1% 10мл", 1,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.NON_PRESCRIPTION,
                6.20, 85, "Ксилометазолин", "Средства от насморка"));
        products.add(createProduct("Аква Марис", "Ядран", "Спрей 30мл", 1,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                7.50, 120, "Морская вода", "Средства от насморка"));

        // Группа 18: Средства для лечения горла
        products.add(createProduct("Стрепсилс", "Рекитт Бенкизер", "Пастилки 24 шт", 1,
                LocalDate.of(2025, 7, 20), PrescriptionStatus.NON_PRESCRIPTION,
                6.90, 95, "Амилметакрезол/дихлорбензиловый спирт", "Средства для лечения горла"));
        products.add(createProduct("Фарингосепт", "Фармстандарт", "Таблетки 20 шт", 1,
                LocalDate.of(2025, 8, 15), PrescriptionStatus.NON_PRESCRIPTION,
                4.20, 150, "Амбазон", "Средства для лечения горла"));
        products.add(createProduct("Гексорал", "Джонсон & Джонсон", "Спрей 40мл", 1,
                LocalDate.of(2025, 9, 10), PrescriptionStatus.NON_PRESCRIPTION,
                8.80, 75, "Гексэтидин", "Средства для лечения горла"));
        products.add(createProduct("Тантум Верде", "Анджелини", "Спрей 30мл", 1,
                LocalDate.of(2025, 10, 5), PrescriptionStatus.NON_PRESCRIPTION,
                11.20, 60, "Бензидамин", "Средства для лечения горла"));

        // Группа 19: Средства для лечения зубной боли
        products.add(createProduct("Дентинокс", "Дентинокс", "Гель 10г", 1,
                LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                7.80, 55, "Лидакоин/экстракт ромашки", "Стоматологические средства"));
        products.add(createProduct("Калгель", "ГлаксоСмитКляйн", "Гель 10г", 1,
                LocalDate.of(2025, 8, 25), PrescriptionStatus.NON_PRESCRIPTION,
                9.50, 45, "Лидакоин/целпиридиния хлорид", "Стоматологические средства"));
        products.add(createProduct("Холисал", "Польфа", "Гель 10г", 1,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.NON_PRESCRIPTION,
                8.90, 50, "Холина салицилат/цеталкония хлорид", "Стоматологические средства"));

        // Группа 20: Средства для лечения аллергии на коже
        products.add(createProduct("Фенистил", "Новартис", "Гель 30г", 1,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                9.20, 65, "Диметинден", "Средства от кожной аллергии"));
        products.add(createProduct("Псило-бальзам", "Штада", "Гель 20г", 1,
                LocalDate.of(2025, 10, 10), PrescriptionStatus.NON_PRESCRIPTION,
                7.80, 70, "Дифенгидрамин", "Средства от кожной аллергии"));
        products.add(createProduct("Гистан", "Фармстандарт", "Крем 30мл", 1,
                LocalDate.of(2025, 9, 5), PrescriptionStatus.NON_PRESCRIPTION,
                6.50, 85, "Растительные экстракты", "Средства от кожной аллергии"));

        // Группа 21: Средства для лечения варикоза
        products.add(createProduct("Детралекс", "Сервье", "Таблетки 500мг", 30,
                LocalDate.of(2025, 12, 20), PrescriptionStatus.NON_PRESCRIPTION,
                28.50, 45, "Диосмин/гесперидин", "Средства от варикоза"));
        products.add(createProduct("Венарус", "ОЗОН", "Таблетки 500мг", 30,
                LocalDate.of(2025, 11, 15), PrescriptionStatus.NON_PRESCRIPTION,
                22.80, 55, "Диосмин/гесперидин", "Средства от варикоза"));
        products.add(createProduct("Флебодиа 600", "Иннотера", "Таблетки 600мг", 15,
                LocalDate.of(2025, 10, 10), PrescriptionStatus.NON_PRESCRIPTION,
                18.90, 40, "Диосмин", "Средства от варикоза"));
        products.add(createProduct("Троксевазин", "Балканфарма", "Гель 2% 40г", 1,
                LocalDate.of(2025, 9, 5), PrescriptionStatus.NON_PRESCRIPTION,
                7.50, 80, "Троксерутин", "Средства от варикоза"));

        // Группа 22: Противоглистные средства
        products.add(createProduct("Пирантел", "Польфа", "Суспензия 250мг/5мл", 1,
                LocalDate.of(2025, 8, 30), PrescriptionStatus.NON_PRESCRIPTION,
                5.80, 60, "Пирантел", "Противоглистные средства"));
        products.add(createProduct("Вермокс", "Гедеон Рихтер", "Таблетки 100мг", 6,
                LocalDate.of(2025, 9, 25), PrescriptionStatus.NON_PRESCRIPTION,
                6.90, 55, "Мебендазол", "Противоглистные средства"));
        products.add(createProduct("Немозол", "Ипка", "Таблетки 400мг", 1,
                LocalDate.of(2025, 10, 20), PrescriptionStatus.PRESCRIPTION,
                8.20, 45, "Альбендазол", "Противоглистные средства"));

        productRepository.saveAll(products);

        System.out.println("✅ Демо-данные успешно загружены!");
        System.out.println("👤 Пользователи:");
        System.out.println("   - Админ: admin / admin (тел: +375291234567)");
        System.out.println("   - Пользователь: user / user (тел: +375297654321)");
        System.out.println("   - Пользователь: ivanov / password (тел: +375336789012)");
        System.out.println("💊 Загружено " + products.size() + " товаров");
        System.out.println("💰 Цены указаны в белорусских рублях (BYN)");
        System.out.println("🌐 API доступен на: http://localhost:2222");
        System.out.println("\n📊 Статистика по категориям:");
        System.out.println("   - Обезболивающие и жаропонижающие: 17 препаратов");
        System.out.println("   - Антибиотики: 15 препаратов");
        System.out.println("   - Витамины и БАДы: 11 препаратов");
        System.out.println("   - Сердечно-сосудистые: 13 препаратов");
        System.out.println("   - Желудочно-кишечные: 15 препаратов");
        System.out.println("   - Противовирусные: 9 препаратов");
        System.out.println("   - Антигистаминные: 9 препаратов");
        System.out.println("   - Другие категории: 133 препарата");
    }

    private Product createProduct(String name, String manufacturer, String form,
                                  Integer stock, LocalDate expiry, PrescriptionStatus status,
                                  Double price, Integer quantity, String substance, String category) {
        Product product = new Product();
        product.setName(name);
        product.setManufacturer(manufacturer);
        product.setReleaseForm(form);
        product.setStockQuantity(quantity);
        product.setExpiryDate(expiry);
        product.setPrescriptionStatus(status);
        product.setPrice(price);
        product.setActiveSubstance(substance);
        product.setCategory(category);
        product.setVisible(true);
        return product;
    }
}