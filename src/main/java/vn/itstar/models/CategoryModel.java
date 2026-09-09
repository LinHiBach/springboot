package vn.itstar.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryModel {

    @NotBlank(message = "Vui lòng nhập tên danh mục")
    @Size(max = 255, message = "Tên danh mục tối đa 255 ký tự")
    private String categoryname;

    @Size(max = 255, message = "Đường dẫn ảnh tối đa 255 ký tự")
    private String images;

    @Min(value = 0, message = "Trạng thái không hợp lệ")
    @Max(value = 1, message = "Trạng thái không hợp lệ")
    private int status = 1;

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}