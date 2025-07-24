package in.software.billingSoftware.Service;

import in.software.billingSoftware.io.CategoryRequest;
import in.software.billingSoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request, MultipartFile file);

    List<CategoryResponse>read();

    void delete(String categoryId);
}
