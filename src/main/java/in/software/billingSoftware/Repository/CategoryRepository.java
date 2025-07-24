package in.software.billingSoftware.Repository;

import in.software.billingSoftware.Entity.CategoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    Optional<CategoryEntity> findByCategoryId(String categoryId);
}
