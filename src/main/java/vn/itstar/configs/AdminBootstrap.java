package vn.itstar.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.itstar.entity.Category;
import vn.itstar.entity.User;
import vn.itstar.repositories.ICategoryRepository;
import vn.itstar.repositories.IUserRepository;

@Configuration
public class AdminBootstrap {
    @Bean
    ApplicationRunner createInitialData(IUserRepository repository,
            ICategoryRepository categoryRepository,
            PasswordEncoder encoder,
            @Value("${app.admin.username:admin}") String username,
            @Value("${app.admin.password:}") String password) {
        return args -> {
            // 1. Khởi tạo tài khoản Admin nếu chưa có
            if (!password.isBlank() && repository.findByUsernameIgnoreCase(username).isEmpty()) {
                if (password.length() < 8 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
                    throw new IllegalArgumentException("APP_ADMIN_PASSWORD cần ít nhất 8 ký tự và tối đa 72 byte UTF-8");
                }
                User user = new User();
                user.setUsername(username);
                user.setFullName("Quản trị viên");
                user.setEmail(username + "@example.local");
                user.setPassword(encoder.encode(password));
                user.setRole("ADMIN");
                user.setStatus(1);
                repository.save(user);
            }

            // 2. Khởi tạo danh mục mẫu nếu bảng Category đang trống
            if (categoryRepository.count() == 0) {
                Category c1 = new Category();
                c1.setCategoryname("Điện thoại & Phụ kiện");
                c1.setImages("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300");
                c1.setStatus(1);

                Category c2 = new Category();
                c2.setCategoryname("Máy tính & Laptop");
                c2.setImages("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=300");
                c2.setStatus(1);

                Category c3 = new Category();
                c3.setCategoryname("Đồng hồ & Trang sức");
                c3.setImages("https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=300");
                c3.setStatus(1);

                Category c4 = new Category();
                c4.setCategoryname("Thời trang nam nữ");
                c4.setImages("https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?w=300");
                c4.setStatus(1);

                categoryRepository.saveAll(List.of(c1, c2, c3, c4));
            }
        };
    }
}
