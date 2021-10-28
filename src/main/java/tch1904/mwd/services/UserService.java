package tch1904.mwd.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.controllers.request.*;
import tch1904.mwd.entity.RequestAddMoney;
import tch1904.mwd.entity.RoleAccount;
import tch1904.mwd.entity.User;
import tch1904.mwd.entity.UserDetailToken;
import tch1904.mwd.entity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch1904.mwd.repository.RequestAddMoneyRepository;
import tch1904.mwd.repository.RoleRepository;
import tch1904.mwd.repository.UserDetailsRepository;
import tch1904.mwd.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RequestAddMoneyRepository requestAddMoneyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommonServices commonServices;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserDetailToken> findByUsernameToken(String username) {
        return userDetailsRepository.findByUsernameToken(username);
    }

    public UserDTO createUser(RegisterRequest request) {
        try {
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
            }

            if (StringUtils.isEmpty(request.getPassword())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "password"));
            }
            if (StringUtils.isEmpty(request.getFullName())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "fullName"));
            }
            if (StringUtils.isEmpty(request.getEmail())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "email"));
            }
            if (StringUtils.isEmpty(request.getPhone())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "phone"));
            }

            if (findByUsername(request.getUsername()).isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "username"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setMoney(0.0);
            user.setRoleId(2);
            user.setActive(false);
            userRepository.save(user);
            return new UserDTO(user);
        }catch (Exception e){
            throw e;
        }
    }

    public void updateProfile(UpdateAccountRequest request) {
        try {
            Optional<User> optional = findByUsername(commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            User user = optional.get();
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
            if (!user.isActive()) {
                user.setEmail(request.getEmail());
            }
            userRepository.save(user);
        } catch (Exception e){
            throw e;
        }
    }

    public List<UserDTO> getUserList(FindUsersRequest request) {
        try {
            List<User> users = userRepository.searchListUsers(
                    request.getUsername(),
                    request.getFullName(),
                    request.getEmail(),
                    request.getRoleId()
            );
            return users.stream().map(UserDTO::new).collect(Collectors.toList());
        } catch (Exception e){
            throw e;
        }
    }

    public UserDTO getUserInfo(String username) {
        try {
            Optional<User> optional = findByUsername(username);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            return new UserDTO(optional.get());
        }catch (Exception e){
            throw e;
        }
    }
    public void saveOtp(String otp, String username) {
        try {
            Optional<User> optional = findByUsername(username);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            User user = optional.get();
            user.setOtp(otp);
            user.setOtpExpire(Instant.now().plusSeconds(AppConstants.OTP_EXPIRATION));
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }

    public void activeAccount(ActiveAccountRequest request, String username) {
        try {
            User user = verifyOtp(request.getOtp(), username);
            user.setActive(true);
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }

    public void changePassword(ChangePasswordRequest request) {
        try {
            User user = verifyOtp(request.getOtp(), request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }
    public void changePasswordAuth(ChangePassRequest request) {
        try {
            Optional<User> optional = findByUsername(commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            if (!passwordEncoder.matches(request.getOldPass(), optional.get().getPassword())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "Old Password"));
            }
            User user = optional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPass()));
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }
    public User verifyOtp(String otp, String username) {
        try {
            Optional<User> optional = findByUsername(username);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            if (!optional.get().getOtp().equals(otp)) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "Otp"));
            }
            if (optional.get().getOtpExpire().compareTo(Instant.now()) < 0 ) {
                throw new AppResponseException(new Message(AppConstants.EXPIRED, "Otp"));
            }
            return optional.get();
        }catch (Exception e){
            throw e;
        }
    }

    public UserDTO findByUsernameAndPassword(String username, String password) {
        Optional<User> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
                return new UserDTO(optionalUser.get());
            }
        }
        return null;
    }

    public void addRequestAddMoney(AddMoneyRequest request) {
        try {
            Optional<User> optional = findByUsername(commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            RequestAddMoney addMoney = new RequestAddMoney();
            addMoney.setUsername(optional.get().getUsername());
            addMoney.setAmount(request.getAmount());
            addMoney.setInformation(request.getInformation());
            addMoney.setStatus(0);
            addMoney.setCreate_time(Instant.now());
            requestAddMoneyRepository.save(addMoney);
        }catch (Exception e){
            throw e;
        }
    }

    public void approveAddMoney(ApproveAddMoneyRequest request) {
        try {
            if (StringUtils.isEmpty(request.getRequestId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "requestId"));
            }
            if (StringUtils.isEmpty(request.getType())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "requestType"));
            }
            if (request.getType() == 0) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "requestType"));
            }
            Optional<RequestAddMoney> optional = requestAddMoneyRepository.findById(request.getRequestId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "requestId"));
            }
            if (optional.get().getStatus() != 0) {
                throw new AppResponseException(new Message(AppConstants.APPROVED, "requestId"));
            }
            if (request.getType() == 1) {
                Optional<User> optionalUser = findByUsername(optional.get().getUsername());
                if (optionalUser.isEmpty()) {
                    throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
                }
                User user = optionalUser.get();
                user.setMoney(user.getMoney() + optional.get().getAmount());
                userRepository.save(user);
            }
            RequestAddMoney addMoney = optional.get();
            addMoney.setStatus(request.getType());
            addMoney.setApprove_time(Instant.now());
            requestAddMoneyRepository.save(addMoney);
        }catch (Exception e){
            throw e;
        }
    }

    public void setUserAvatar(SimpleStringRequest request) {
        try {
            Optional<User> optional = findByUsername(this.commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            User user = optional.get();
            user.setAvatar(request.getString());
            userRepository.save(user);
        } catch (Exception e){
            throw e;
        }
    }
}
