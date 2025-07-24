package in.software.billingSoftware.Service.imple;

import in.software.billingSoftware.Entity.CategoryEntity;
import in.software.billingSoftware.Repository.CategoryRepository;
import in.software.billingSoftware.Repository.ItemRepository;
import in.software.billingSoftware.Service.CategoryService;
import in.software.billingSoftware.Service.FileUploadService;
import in.software.billingSoftware.io.CategoryRequest;
import in.software.billingSoftware.io.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImple implements CategoryService {

    private final CategoryRepository repository;
    private final FileUploadService fileUploadService;
    private final ItemRepository itemRepository;


    @Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file) {
        String imgUrl=fileUploadService.uploadFile(file);
        CategoryEntity newCategory = convertToEntity(request);
        newCategory.setImgUrl(imgUrl);
        newCategory = repository.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return repository.findAll()
                .stream()
                .map(categoryEntity -> convertToResponse(categoryEntity))
                .toList();
    }

    @Override
    public void delete(String categoryId) {
        CategoryEntity existingCategory=repository.findByCategoryId(categoryId)
                .orElseThrow(()->new RuntimeException("Category not found"));
        fileUploadService.deleteFile(existingCategory.getImgUrl());
        repository.delete(existingCategory);
    }


    private CategoryEntity convertToEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }
    private CategoryResponse convertToResponse(CategoryEntity entity) {
        Integer itemsCount=itemRepository.countByCategoryId(entity.getId());
        return CategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .description(entity.getDescription())
                .bgColor(entity.getBgColor())
                .imgUrl(entity.getImgUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(itemsCount)
                .build();
    }
}
