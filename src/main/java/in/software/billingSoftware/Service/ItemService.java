package in.software.billingSoftware.Service;

import in.software.billingSoftware.io.ItemRequest;
import in.software.billingSoftware.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    ItemResponse add(ItemRequest request, MultipartFile file);

    List<ItemResponse> fetchItems();

    void delteItem(String itemId);
}
