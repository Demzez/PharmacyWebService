package com.zez_world.pharmacy_web_service.config;

import com.zez_world.pharmacy_web_service.entity.*;
import com.zez_world.pharmacy_web_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

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
        System.out.println("=== ЗАГРУЗКА ДЕМО-ДАННЫХ ДЛЯ АПТЕКИ ===");

        // 1. Создаем пользователей
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@pharmacy.ru");
        admin.setPhone("+79991234567");
        admin.setRole(Role.ROLE_ADMIN);

        User user1 = new User();
        user1.setUsername("user");
        user1.setPassword(passwordEncoder.encode("user"));
        user1.setEmail("user@mail.ru");
        user1.setPhone("+79998887766");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user2.setUsername("ivanov");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setEmail("ivanov@gmail.com");
        user2.setPhone("+79995554433");
        user2.setRole(Role.ROLE_USER);

        userRepository.saveAll(Arrays.asList(admin, user1, user2));

        // 2. Создаем продукты (реальные лекарства из российской аптеки)
        Product[] products = {
                // Обезболивающие и жаропонижающие
                createProduct("Нурофен Экспресс", "Рекитт Бенкизер", "Капсулы", 12,
                        LocalDate.of(2025, 12, 31), PrescriptionStatus.NON_PRESCRIPTION,
                        320.0, 45, "Ибупрофен", "Обезболивающие"),

                createProduct("Панадол", "ГлаксоСмитКляйн", "Таблетки", 20,
                        LocalDate.of(2025, 8, 15), PrescriptionStatus.NON_PRESCRIPTION,
                        190.0, 120, "Парацетамол", "Обезболивающие"),

                createProduct("Кетонал", "Сандоз", "Гель", 1,
                        LocalDate.of(2025, 5, 20), PrescriptionStatus.NON_PRESCRIPTION,
                        450.0, 30, "Кетопрофен", "Обезболивающие"),

                // Антибиотики
                createProduct("Амоксиклав 625", "Сандоз", "Таблетки", 14,
                        LocalDate.of(2024, 11, 30), PrescriptionStatus.PRESCRIPTION,
                        680.0, 25, "Амоксициллин/клавулановая кислота", "Антибиотики"),

                createProduct("Супракс", "Астеллас", "Капсулы", 6,
                        LocalDate.of(2025, 3, 15), PrescriptionStatus.PRESCRIPTION,
                        920.0, 18, "Цефиксим", "Антибиотики"),

                // Витамины
                createProduct("Витамин C 1000mg", "Солгар", "Капсулы", 120,
                        LocalDate.of(2026, 3, 20), PrescriptionStatus.NON_PRESCRIPTION,
                        1250.0, 60, "Аскорбиновая кислота", "Витамины"),

                createProduct("Компливит", "Фармстандарт", "Таблетки", 60,
                        LocalDate.of(2025, 11, 10), PrescriptionStatus.NON_PRESCRIPTION,
                        350.0, 85, "Поливитамины", "Витамины"),

                // Сердечно-сосудистые
                createProduct("Кардиомагнил", "Такеда", "Таблетки", 30,
                        LocalDate.of(2025, 9, 30), PrescriptionStatus.NON_PRESCRIPTION,
                        450.0, 80, "Ацетилсалициловая кислота", "Сердечно-сосудистые"),

                createProduct("Анаприлин", "Озон", "Таблетки", 50,
                        LocalDate.of(2025, 7, 25), PrescriptionStatus.PRESCRIPTION,
                        120.0, 45, "Пропранолол", "Сердечно-сосудистые"),

                // Желудочно-кишечные
                createProduct("Мезим Форте", "Берлин-Хеми", "Таблетки", 20,
                        LocalDate.of(2025, 6, 15), PrescriptionStatus.NON_PRESCRIPTION,
                        290.0, 95, "Панкреатин", "Желудочно-кишечные"),

                createProduct("Эспумизан", "Берлин-Хеми", "Капсулы", 25,
                        LocalDate.of(2025, 10, 5), PrescriptionStatus.NON_PRESCRIPTION,
                        380.0, 65, "Симетикон", "Желудочно-кишечные"),

                // Противовирусные
                createProduct("Арбидол", "Фармстандарт", "Капсулы", 10,
                        LocalDate.of(2025, 4, 18), PrescriptionStatus.NON_PRESCRIPTION,
                        520.0, 40, "Умифеновир", "Противовирусные"),

                createProduct("Кагоцел", "Ниармедик", "Таблетки", 10,
                        LocalDate.of(2025, 8, 22), PrescriptionStatus.NON_PRESCRIPTION,
                        480.0, 55, "Кагоцел", "Противовирусные")
        };

        productRepository.saveAll(Arrays.asList(products));

        System.out.println("✅ Демо-данные успешно загружены!");
        System.out.println("👤 Пользователи:");
        System.out.println("   - Админ: admin / admin");
        System.out.println("   - Пользователь: user / user");
        System.out.println("   - Пользователь: ivanov / password");
        System.out.println("💊 Загружено " + products.length + " товаров");
        System.out.println("🌐 API доступен на: http://localhost:2222");
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