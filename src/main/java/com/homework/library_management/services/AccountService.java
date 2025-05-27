package com.homework.library_management.services;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.dto.DTO_ChangePassword;
import com.homework.library_management.dto.DTO_GenerateForgotPassword;
import com.homework.library_management.dto.DTO_VerifyEmail;
import com.homework.library_management.entities.common.OtpStorage;
import com.homework.library_management.enums.StatusMsgEnum;
import com.homework.library_management.enums.VerifyEmailOptions;
import com.homework.library_management.exception.AppException;
import com.homework.library_management.repositories.AccountRepository;
import com.homework.library_management.repositories.LibrarianRepository;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.homework.library_management.enums.VerifyEmailOptions.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    GlobalLogger logger;
    EmailService emailService;
    OtpStorage otpStorage;
    AccountRepository accountRepository;
    LibrarianRepository librarianRepository;
    Map<String, String> emailCustom = Map.of(
        "otp", this.randOTP().toUpperCase(),
        "msg", """
                <div>
                    <p style="font-size: 18px">Không chia sẻ thông tin tài khoản phía dưới cho ai khác.
                        Vui lòng bảo mật tốt đoạn mã sau!</p>
                    <h2>Tài khoản: <b>%s</b></h2>
                    <h2>OTP: <b>%s</b></h2>
                </div>
            """,
        "subject", "%s Thư Viện ABC"
    );
    int DEFAULT_OTP_AGE = 300;

    public ResponseEntity<Map> getOtpToVerifyEmail(HttpServletRequest request, DTO_VerifyEmail dto) throws AppException {
        logger.handling(request, "AccountService.getOtpToVerifyEmail");
        if (accountRepository.findByEmail(dto.getEmail()).isEmpty())
            throw new AppException("Email không tồn tại");

        String[] savedKey = new String[1];
        switch (VerifyEmailOptions.valueOf(dto.getOption())) {
            case PUBLIC_FORGOT_PASSWORD:
                savedKey[0] = dto.getEmail() + PUBLIC_FORGOT_PASSWORD;
                otpStorage.put(savedKey[0], emailCustom.get("otp"));
                emailService.sendSimpleEmail(dto.getEmail(),
                    String.format(emailCustom.get("subject"), "Mã OTP quên mật khẩu"),
                    String.format(emailCustom.get("msg"), dto.getEmail(), emailCustom.get("otp"))
                );
                break;
            case PRIVATE_CHANGE_PASSWORD:
                savedKey[0] = dto.getEmail() + PRIVATE_CHANGE_PASSWORD;
                otpStorage.put(savedKey[0], emailCustom.get("otp"));
                emailService.sendSimpleEmail(dto.getEmail(),
                    String.format(emailCustom.get("subject"), "Mã OTP đổi mật khẩu"),
                    String.format(emailCustom.get("msg"), dto.getEmail(), emailCustom.get("otp"))
                );
                break;
            default:
                //--Weird error.
                throw new AppException("Quá trình xác thực email không hợp lệ_/home");
        }
        scheduler.schedule(() -> {
            otpStorage.remove(savedKey[0]);
            log.info("OTP for {} has expired and been removed.", dto.getEmail());
        }, DEFAULT_OTP_AGE, TimeUnit.MINUTES);
        return ResponseEntity.ok(Map.of(
            "SUCCESS", "Gửi thành công OTP tới email của bạn",
            "otpAge", DEFAULT_OTP_AGE
        ));
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public ResponseEntity<Map> generatePasswordForUserForgot(HttpServletRequest request, DTO_GenerateForgotPassword dto)
        throws AppException {
        logger.handling(request, "AccountService.generatePasswordForUserForgot");
        var accountQuery = accountRepository.findByEmail(dto.getEmail());
        if (accountQuery.isEmpty())
            throw new AppException("Email không tồn tại");

        var account = accountQuery.get();
        String otp = otpStorage.get(dto.getEmail() + PUBLIC_FORGOT_PASSWORD);
        if (Objects.isNull(otp))
            throw new AppException("OTP không tồn tại hoặc đã hết hạn");

        dto.setOtp(dto.getOtp().toUpperCase());
        if (!otp.equals(dto.getOtp()))
            throw new AppException("OTP không chính xác");

        var newPassword = this.randOTP();
        account.setPassword(newPassword);
        accountRepository.save(account);

        emailService.sendSimpleEmail(dto.getEmail(),
            String.format(emailCustom.get("subject"), "Mật khẩu mới từ"),
            String.format(emailCustom.get("msg"), dto.getEmail(), newPassword));
        otpStorage.remove(dto.getEmail() + PUBLIC_FORGOT_PASSWORD);
        return StatusMsgEnum.fastSuccess("Mật khẩu đã được tạo thành công");
    }

    public String randOTP() {
        return String.valueOf(System.currentTimeMillis() % 1_000_000);
    }

    public void changePassword(DTO_ChangePassword dto, HttpServletRequest request) {
        logger.handling(request, "AccountService.changePassword");
        Long librarianId = Long.parseLong(request.getSession().getAttribute("librarianId").toString());
        //--Always found "id", because it filtered by interceptor.
        var librarian = librarianRepository.findById(librarianId)
            .orElseThrow(() -> {
                APIHelper.clearSession(request);
                return new AppException("Phiên đăng nhập hết hạn_/login");
            });
        var account = librarian.getAccount();
        if (!account.getEmail().equals(dto.getEmail())) {
            APIHelper.clearSession(request);
            throw new AppException("Email thủ thư bị xáo trộn_/login");
        }
        if (account.getPassword().equals(dto.getNewPassword()))
            throw new AppException("Không thể dùng mật khẩu cũ_/user-info");
        String otp = otpStorage.get(dto.getEmail() + PRIVATE_CHANGE_PASSWORD);
        if (Objects.isNull(otp))
            throw new AppException("OTP không tồn tại hoặc đã hết hạn_/user-info");
        if (!otp.equals(dto.getOtp()))
            throw new AppException("OTP không chính xác_/user-info");
        otpStorage.remove(dto.getEmail() + PRIVATE_CHANGE_PASSWORD);
        account.setPassword(dto.getNewPassword());
        accountRepository.save(account);
    }
}
