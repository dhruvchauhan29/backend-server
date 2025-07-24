package in.software.billingSoftware.Controller;

import com.razorpay.RazorpayException;
import in.software.billingSoftware.Service.OrderService;
import in.software.billingSoftware.Service.RazorpayService;
import in.software.billingSoftware.io.OrderResponse;
import in.software.billingSoftware.io.PaymentRequest;
import in.software.billingSoftware.io.PaymentVarificationRequest;
import in.software.billingSoftware.io.RazorpayOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayService razorpayService;
    private final OrderService orderService;

    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException{
        return razorpayService.createOrder(request.getAmount(),request.getCurrency());
    }

    @PostMapping("/verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVarificationRequest request){
        return orderService.varifyPayment(request);
    }

}
