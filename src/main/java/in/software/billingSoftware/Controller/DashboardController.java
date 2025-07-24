package in.software.billingSoftware.Controller;

import in.software.billingSoftware.Service.OrderService;
import in.software.billingSoftware.io.DashboardResponse;
import in.software.billingSoftware.io.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderService orderService;

    @GetMapping
    public DashboardResponse getDashboardData() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        Double todaySale = orderService.sumSalesByDate(startOfDay, endOfDay);
        Long orderCount = orderService.countByOrderDate(startOfDay, endOfDay);
        List<OrderResponse> recentOrders = orderService.findRecentOrders();

        return new DashboardResponse(
                todaySale != null ? todaySale : 0.0,
                orderCount != null ? orderCount : 0,
                recentOrders
        );
    }



}
