package vn.itstar;

import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.itstar.repositories.ICategoryRepository;
import vn.itstar.repositories.IUserRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdminCrudHttpTests {
    @LocalServerPort int port;
    @Autowired ICategoryRepository categories;
    @Autowired IUserRepository users;
    @Autowired PasswordEncoder encoder;

    private HttpClient client() {
        return HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .followRedirects(HttpClient.Redirect.NEVER).build();
    }

    private HttpResponse<String> get(HttpClient client, String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> post(HttpClient client, String path, Map<String, String> fields) throws Exception {
        String body = fields.entrySet().stream().map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private String token(String body) {
        var matcher = Pattern.compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"").matcher(body);
        assertTrue(matcher.find(), "JSP phải render CSRF token: " + body);
        return matcher.group(1);
    }

    private HttpClient login(String username, String password) throws Exception {
        var client = client();
        var page = get(client, "/login");
        assertEquals(200, page.statusCode(), page.body());
        var response = post(client, "/login", Map.of("username", username, "password", password, "_csrf", token(page.body())));
        assertEquals(302, response.statusCode(), response.body());
        assertFalse(response.headers().firstValue("location").orElse("").contains("error"));
        return client;
    }

    @Test
    void categoryCrudSearchValidationAndJsp() throws Exception {
        var client = login("admin", "Admin123!");
        var add = get(client, "/admin/categories/add");
        assertEquals(200, add.statusCode(), add.body());
        assertTrue(add.body().contains("class=\"sidebar\""), "SiteMesh phải render layout admin: " + add.body());
        var fields = new HashMap<>(Map.of("categoryname", "Điện thoại Kiểm thử", "images", "", "status", "1", "_csrf", token(add.body())));
        assertEquals(302, post(client, "/admin/categories/save", fields).statusCode());
        var category = categories.findByCategorynameContainingIgnoreCase("Kiểm thử").get(0);
        var list = get(client, "/admin/categories?keyword=" + URLEncoder.encode("điện thoại", StandardCharsets.UTF_8));
        assertEquals(200, list.statusCode(), list.body());
        assertTrue(list.body().contains("Điện thoại Kiểm thử"));

        fields.put("categoryname", "  ");
        var invalid = post(client, "/admin/categories/update/" + category.getCategoryId(), fields);
        assertEquals(200, invalid.statusCode(), invalid.body());
        assertTrue(invalid.body().contains("Vui lòng nhập tên danh mục"));
        assertTrue(invalid.body().contains("/update/" + category.getCategoryId()));
        fields.put("categoryname", "Danh mục đã sửa");
        assertEquals(302, post(client, "/admin/categories/update/" + category.getCategoryId(), fields).statusCode());
        assertEquals("Danh mục đã sửa", categories.findById(category.getCategoryId()).orElseThrow().getCategoryname());
        assertEquals(302, post(client, "/admin/categories/delete/" + category.getCategoryId(), Map.of("_csrf", token(list.body()))).statusCode());
        assertFalse(categories.existsById(category.getCategoryId()));
        assertEquals(404, get(client, "/admin/categories/edit/999999").statusCode());
    }

    @Test
    void userCrudSearchPasswordAndDuplicateValidation() throws Exception {
        var client = login("admin", "Admin123!");
        var add = get(client, "/admin/users/add");
        assertEquals(200, add.statusCode(), add.body());
        var fields = new HashMap<>(Map.of("username", "student", "fullName", "Nguyễn Văn An",
                "email", "student@example.com", "password", "Password123!", "role", "USER", "status", "1", "_csrf", token(add.body())));
        assertEquals(302, post(client, "/admin/users/save", fields).statusCode());
        var user = users.findByUsernameIgnoreCase("student").orElseThrow();
        assertTrue(encoder.matches("Password123!", user.getPassword()));
        var duplicate = post(client, "/admin/users/save", fields);
        assertEquals(200, duplicate.statusCode());
        assertTrue(duplicate.body().contains("Tên đăng nhập đã tồn tại."));
        assertTrue(duplicate.body().contains("Email đã tồn tại."));
        assertFalse(duplicate.body().contains("value=\"Password123!\""));
        for (String term : List.of("STUDENT", "Nguyễn", "student@example.com")) {
            var list = get(client, "/admin/users?keyword=" + URLEncoder.encode(term, StandardCharsets.UTF_8));
            assertEquals(200, list.statusCode(), list.body());
            assertTrue(list.body().contains("Nguyễn Văn An"));
            assertFalse(list.body().contains(user.getPassword()));
        }
        var normal = login("student", "Password123!");
        assertEquals(403, get(normal, "/admin/users").statusCode());
        assertEquals(403, get(normal, "/admin/categories").statusCode());
        fields.put("password", "");
        fields.put("fullName", "Tên đã sửa");
        assertEquals(302, post(client, "/admin/users/update/" + user.getUserId(), fields).statusCode());
        var updated = users.findById(user.getUserId()).orElseThrow();
        assertEquals("Tên đã sửa", updated.getFullName());
        assertEquals(user.getPassword(), updated.getPassword());
        fields.put("password", "NewPassword123!");
        assertEquals(302, post(client, "/admin/users/update/" + user.getUserId(), fields).statusCode());
        assertTrue(encoder.matches("NewPassword123!", users.findById(user.getUserId()).orElseThrow().getPassword()));
        assertEquals(302, post(client, "/admin/users/delete/" + user.getUserId(), Map.of("_csrf", token(add.body()))).statusCode());
        assertFalse(users.existsById(user.getUserId()));
        assertEquals(404, get(client, "/admin/users/edit/999999").statusCode());
    }

    @Test
    void authenticationCsrfAndSelfProtection() throws Exception {
        assertEquals(302, get(client(), "/admin/categories").statusCode());
        var client = login("admin", "Admin123!");
        var admin = users.findByUsernameIgnoreCase("admin").orElseThrow();
        assertEquals(403, post(client, "/admin/users/delete/" + admin.getUserId(), Map.of()).statusCode());
        var edit = get(client, "/admin/users/edit/" + admin.getUserId());
        assertEquals(200, edit.statusCode(), edit.body());
        assertEquals(302, post(client, "/admin/users/delete/" + admin.getUserId(), Map.of("_csrf", token(edit.body()))).statusCode());
        assertTrue(users.existsById(admin.getUserId()));
        var result = post(client, "/admin/users/update/" + admin.getUserId(), Map.of("username", "admin",
                "fullName", admin.getFullName(), "email", admin.getEmail(), "password", "", "role", "USER", "status", "0",
                "_csrf", token(edit.body())));
        assertEquals(200, result.statusCode());
        assertEquals("ADMIN", users.findById(admin.getUserId()).orElseThrow().getRole());
        assertEquals(1, users.findById(admin.getUserId()).orElseThrow().getStatus());
    }
}
