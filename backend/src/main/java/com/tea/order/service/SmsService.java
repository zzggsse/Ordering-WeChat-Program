package com.tea.order.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class SmsService {

    private final Map<String, SmsCode> codes = new ConcurrentHashMap<>();

    @Value("${app.sms.mock:true}")
    private boolean mock;

    @Value("${app.sms.mock-code:123456}")
    private String mockCode;

    @Value("${app.sms.expire-minutes:5}")
    private long expireMinutes;

    /** 生成并“发送”验证码；mock 模式下返回验证码便于演示。 */
    public String sendCode(String phone) {
        clear(phone);
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        codes.put(phone, new SmsCode(code, LocalDateTime.now().plusMinutes(expireMinutes)));
        log.info("[SMS-MOCK] 验证码 {} -> {}", phone, code);
        return code;
    }

    public boolean verify(String phone, String code) {
        SmsCode stored = codes.get(phone);
        boolean storedOk = stored != null && stored.isValid(code);
        // mock 模式下固定验证码也视为正确，方便演示
        return storedOk || (mock && mockCode.equals(code));
    }

    public void clear(String phone) {
        codes.remove(phone);
    }

    @Data
    @AllArgsConstructor
    private static class SmsCode {
        private String code;
        private LocalDateTime expiresAt;

        boolean isValid(String input) {
            return code.equals(input) && expiresAt.isAfter(LocalDateTime.now());
        }
    }
}
