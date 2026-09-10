package vn.itstar.services.impl;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.itstar.entity.User;
import vn.itstar.models.UserModel;
import vn.itstar.repositories.IUserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final IUserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(IUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<User> search(String keyword) {
        String term = keyword == null ? "" : keyword.trim();
        return term.isEmpty() ? repository.findAll()
                : repository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term, term);
    }

    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"));
    }

    public boolean usernameTaken(String username, Integer id) {
        return repository.existsByUsernameIgnoreCaseAndUserIdNot(username.trim(), id == null ? -1 : id);
    }

    public boolean emailTaken(String email, Integer id) {
        return repository.existsByEmailIgnoreCaseAndUserIdNot(email.trim(), id == null ? -1 : id);
    }

    @Transactional
    public void save(Integer id, UserModel form) {
        User user = id == null ? new User() : findById(id);
        user.setUsername(form.getUsername().trim());
        user.setFullName(form.getFullName().trim());
        user.setEmail(form.getEmail().trim());
        user.setRole(form.getRole());
        user.setStatus(form.getStatus());
        // Khi sửa và để trống mật khẩu, giữ nguyên mật khẩu đã mã hóa.
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(encoder.encode(form.getPassword()));
        }
        repository.saveAndFlush(user);
    }

    @Transactional
    public void delete(Integer id) {
        repository.delete(findById(id));
        repository.flush();
    }
}

