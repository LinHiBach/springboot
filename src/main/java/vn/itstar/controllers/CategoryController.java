package vn.itstar.controllers;

import jakarta.validation.Valid;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.itstar.entity.Category;
import vn.itstar.models.CategoryModel;
import vn.itstar.services.ICategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. GET /admin/categories – danh sách + tìm kiếm theo keyword
    @GetMapping
    public String list(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            Model model) {

        String searchKeyword = (keyword != null) ? keyword.trim() : "";

        model.addAttribute("categories", categoryService.search(searchKeyword));
        model.addAttribute("keyword", searchKeyword);

        return "admin/categories/list";
    }

    // 2. GET /admin/categories/add – form thêm mới
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new CategoryModel());
        return "admin/categories/add";
    }

    // 3. POST /admin/categories/save – lưu danh mục mới
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("category") CategoryModel categoryModel,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Nếu dữ liệu không hợp lệ, hiển thị lại form
        if (result.hasErrors()) {
            return "admin/categories/add";
        }

        Category category = new Category();
        category.setCategoryname(categoryModel.getCategoryname().trim());
        category.setImages(
                categoryModel.getImages() == null
                        ? ""
                        : categoryModel.getImages().trim()
        );
        category.setStatus(categoryModel.getStatus());

        categoryService.save(category);

        redirectAttributes.addFlashAttribute(
                "message",
                "Thêm danh mục thành công!"
        );

        return "redirect:/admin/categories";
    }

    // 4. GET /admin/categories/edit/{id} – form sửa
    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Category> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục ID #" + id);
            return "redirect:/admin/categories";
        }

        Category category = opt.get();

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryname(category.getCategoryname());
        categoryModel.setImages(category.getImages());
        categoryModel.setStatus(category.getStatus());

        model.addAttribute("category", categoryModel);
        model.addAttribute("categoryId", id);

        return "admin/categories/edit";
    }

    // 5. POST /admin/categories/update/{id} – cập nhật
    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("category") CategoryModel categoryModel,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Category> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục ID #" + id);
            return "redirect:/admin/categories";
        }

        if (result.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "admin/categories/edit";
        }

        Category category = opt.get();
        category.setCategoryname(categoryModel.getCategoryname().trim());
        category.setImages(
                categoryModel.getImages() == null
                        ? ""
                        : categoryModel.getImages().trim()
        );
        category.setStatus(categoryModel.getStatus());

        categoryService.save(category);

        redirectAttributes.addFlashAttribute(
                "message",
                "Cập nhật danh mục thành công!"
        );

        return "redirect:/admin/categories";
    }

    // 6. POST /admin/categories/delete/{id} – xóa
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        if (categoryService.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Danh mục không tồn tại hoặc đã được xóa!"
            );
            return "redirect:/admin/categories";
        }

        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Xóa danh mục thành công!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Không thể xóa danh mục này do đang có dữ liệu liên quan!"
            );
        }

        return "redirect:/admin/categories";
    }
}