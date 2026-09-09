package vn.itstar.controllers;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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

    // Hiển thị danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());

        return "admin/categories/list";
    }

    // Mở form thêm
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new CategoryModel());

        return "admin/categories/add";
    }

    // Nhận dữ liệu form và lưu
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("category") CategoryModel categoryModel,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Nếu dữ liệu không hợp lệ, hiển thị lại form
        if (result.hasErrors()) {
            return "admin/categories/add";
        }

        // Chuyển dữ liệu từ Model sang Entity
        Category category = new Category();

        category.setCategoryname(
                categoryModel.getCategoryname().trim()
        );

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
    
 // Mở form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {

        Category category = categoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục"
                ));

        // Đưa dữ liệu hiện tại vào form
        CategoryModel categoryModel = new CategoryModel();

        categoryModel.setCategoryname(category.getCategoryname());
        categoryModel.setImages(category.getImages());
        categoryModel.setStatus(category.getStatus());

        model.addAttribute("category", categoryModel);
        model.addAttribute("categoryId", id);

        return "admin/categories/edit";
    }

    // Nhận dữ liệu form sửa và cập nhật
    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("category") CategoryModel categoryModel,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Category category = categoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục"
                ));

        if (result.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "admin/categories/edit";
        }

        // Cập nhật trên đối tượng đã có ID
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
    
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra danh mục còn tồn tại không
        if (categoryService.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Danh mục không tồn tại hoặc đã được xóa!"
            );

            return "redirect:/admin/categories";
        }

        categoryService.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "message",
                "Xóa danh mục thành công!"
        );

        return "redirect:/admin/categories";
    }
}