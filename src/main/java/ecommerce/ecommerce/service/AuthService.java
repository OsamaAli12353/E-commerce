package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.OtpVerifyRequest;
import ecommerce.ecommerce.DTO.UserDTO;

public interface AuthService {
    void register(UserDTO req);
    void verifyOtp(OtpVerifyRequest req);

}
