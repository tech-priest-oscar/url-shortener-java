package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.models.User;

public interface OtpService {
    /** Generates a 5-digit OTP, caches its hash for 10 minutes, and returns the plain code. */
    String generateFor(User user);

    /** Checks the code against the cached hash; on success the cached entry is deleted. */
    boolean validate(User user, String otpCode);
}
