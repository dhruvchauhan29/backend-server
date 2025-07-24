package in.software.billingSoftware.Service.imple;

import in.software.billingSoftware.Entity.OrderEntity;
import in.software.billingSoftware.Entity.OrderItemEntity;
import in.software.billingSoftware.Repository.OrderEntityRepository;
import in.software.billingSoftware.Service.OrderService;
import in.software.billingSoftware.io.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        OrderEntity newOrder=convertToOrderEntity(orderRequest);

        PaymentDetails paymentDetails=new PaymentDetails();
        paymentDetails.setPaymentStatus(newOrder.getPaymentMethod()== PaymentMethod.CASH?PaymentDetails.PaymentStatus.COMPLETED:PaymentDetails.PaymentStatus.PENDING);
        newOrder.setPaymentDetails(paymentDetails);

        List<OrderItemEntity>orderItems=orderRequest.getCartItems().stream().map(this::convertToOrderItemEntity).collect(Collectors.toList());
        newOrder.setItems(orderItems);
        newOrder=orderEntityRepository.save(newOrder);

        return convertToOrderResponse(newOrder);
    }

    private OrderResponse convertToOrderResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .orderId(newOrder.getOrderId())
                .customerName(newOrder.getCustomerName())
                .phoneNumber(newOrder.getPhoneNumber())
                .subTotal(newOrder.getSubTotal())
                .tax(newOrder.getTax())
                .grandTotal(newOrder.getGrandTotal())
                .paymentMethod(newOrder.getPaymentMethod())
                .items(newOrder.getItems().stream().map(this::convertToItemResponse).collect(Collectors.toList()))
                .paymentDetails(newOrder.getPaymentDetails())
                .createdAt(newOrder.getCreatedAt())
                .build();
    }

    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {
        return OrderResponse.OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getName())
                .price(orderItemEntity.getPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }

    private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest) {
        return OrderItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(orderItemRequest.getName())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .build();
    }

    private OrderEntity convertToOrderEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .customerName(orderRequest.getCustomerName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .subTotal(orderRequest.getSubTotal())
                .tax(orderRequest.getTax())
                .grandTotal(orderRequest.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()))
                .build();
    }


    @Override
    public void deleteOrder(String orderId) {
        OrderEntity existingOrder=orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(()->new RuntimeException("Order Not found"));
        orderEntityRepository.delete(existingOrder);
    }

    @Override
    public List<OrderResponse> getLatestOrders() {
        return orderEntityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse varifyPayment(PaymentVarificationRequest request) {
        OrderEntity existingOrder=orderEntityRepository.findByOrderId(request.getOrderId())
                .orElseThrow(()->new RuntimeException("Order Not found"));

        if(!verifyRazorpaySignature(request.getRazorpayOrderId(),request.getRazorpayPaymentId(),request.getRazorpaySignature())){
            throw new RuntimeException("Payment Varification failed");
        }
        PaymentDetails paymentDetails=existingOrder.getPaymentDetails();
        paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
        paymentDetails.setPaymentStatus(PaymentDetails.PaymentStatus.COMPLETED);
        existingOrder=orderEntityRepository.save(existingOrder);

        return convertToOrderResponse(existingOrder);
    }

    @Override
    public Double sumSalesByDate(LocalDateTime start, LocalDateTime end) {
        return orderEntityRepository.sumSalesByDate(start, end);
    }

    @Override
    public Long countByOrderDate(LocalDateTime start, LocalDateTime end) {
        return orderEntityRepository.countByOrderDate(start, end);
    }



    @Override
    public List<OrderResponse> findRecentOrders() {
        return orderEntityRepository.findRecentOrders()
                .stream()
                .map(orderEntity->convertToOrderResponse(orderEntity))
                .collect(Collectors.toList());
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        return true;
    }
}
