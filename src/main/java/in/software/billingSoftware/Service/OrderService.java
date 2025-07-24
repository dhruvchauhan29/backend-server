package in.software.billingSoftware.Service;

import in.software.billingSoftware.io.OrderRequest;
import in.software.billingSoftware.io.OrderResponse;
import in.software.billingSoftware.io.PaymentVarificationRequest;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    void deleteOrder(String orderId);

    List<OrderResponse> getLatestOrders();

    OrderResponse varifyPayment(PaymentVarificationRequest request);

    Double sumSalesByDate(LocalDateTime start, LocalDateTime end);

    Long countByOrderDate(LocalDateTime start, LocalDateTime end);


    List<OrderResponse> findRecentOrders();
}
