package techpriest.Url_Shortener.services.impl;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.services.OtpService;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final Duration OTP_TTL = Duration.ofMinutes(10);
    private static final String KEY_PREFIX = "otp:";

    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateFor(User user) {
        String code = String.format("%05d", random.nextInt(100_000));
        redisTemplate.opsForValue().set(keyFor(user), passwordEncoder.encode(code), OTP_TTL);
        return code;
    }

    @Override
    public boolean validate(User user, String otpCode) {
        String key = keyFor(user);
        String hashedCode = redisTemplate.opsForValue().get(key);
        if (hashedCode == null || !passwordEncoder.matches(otpCode, hashedCode)) {
            return false;
        }
        redisTemplate.delete(key);
        return true;
    }

    private String keyFor(User user) {
        return KEY_PREFIX + user.getId();
    }

}
