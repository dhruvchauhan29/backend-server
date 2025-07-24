package in.software.billingSoftware.Service;

import com.razorpay.RazorpayException;
import in.software.billingSoftware.io.RazorpayOrderResponse;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(Double amount,String currency) throws RazorpayException;
}
