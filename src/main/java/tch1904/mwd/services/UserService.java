package tch1904.mwd.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.controllers.request.*;
import tch1904.mwd.entity.*;
import tch1904.mwd.entity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch1904.mwd.repository.*;

import javax.mail.MessagingException;
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
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestAddMoneyRepository requestAddMoneyRepository;

    @Autowired
    private RequestBeSingerRepository beSingerRepository;

    @Autowired
    private RequestPublishProductRepository publishProductRepository;

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

    public void requestBeSinger(RegisterSingerRequest request) {
        try {
            if (beSingerRepository.findByUsernameAndStatus(commonServices.getCurrentUser().getUsername(), 0).isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Request Not Approved"));
            }
            Optional<User> optional = findByUsername(commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            RequestBeSinger beSinger = new RequestBeSinger();
            beSinger.setLinkFile(request.getLinkFile());
            beSinger.setUsername(optional.get().getUsername());
            beSinger.setStatus(0);
            beSinger.setCreate_time(Instant.now());
            beSingerRepository.save(beSinger);
        }catch (Exception e){
            throw e;
        }
    }

    public void requestPublishProduct(PublishProductRequest request) {
        try {
            if (publishProductRepository.findByProductIdAndStatus(request.getProductId(), 0).isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Request For This Product Not Approved"));
            }
            Optional<User> optional = findByUsername(commonServices.getCurrentUser().getUsername());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            RequestPublishProduct publishProduct = new RequestPublishProduct();
            publishProduct.setProductId(request.getProductId());
            publishProduct.setPrice(request.getPrice());
            publishProduct.setUsername(optional.get().getUsername());
            publishProduct.setStatus(0);
            publishProduct.setCreate_time(Instant.now());
            publishProductRepository.save(publishProduct);
        }catch (Exception e){
            throw e;
        }
    }

    public void approveAddMoney(ApproveRequest request) throws MessagingException {
        try {
            checkApproveRequest(request);
            Optional<RequestAddMoney> optional = requestAddMoneyRepository.findById(request.getRequestId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "requestId"));
            }
            if (optional.get().getStatus() != 0) {
                throw new AppResponseException(new Message(AppConstants.APPROVED, "requestId"));
            }
            Optional<User> optionalUser = findByUsername(optional.get().getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
            }
            User user = optionalUser.get();
            if (request.getType() == 1) {
                user.setMoney(user.getMoney() + optional.get().getAmount());
                userRepository.save(user);
            }
            RequestAddMoney addMoney = optional.get();
            addMoney.setStatus(request.getType());
            addMoney.setApprove_time(Instant.now());
            addMoney.setApprove_details(request.getReason());
            requestAddMoneyRepository.save(addMoney);
            if (request.getType() == 2) {
                String detailMessage = "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Reason: " + request.getReason() + "</span></span></span></p>";
                String typeApproveMessage = "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Your Request To Register As New Singer has been: " + "<strong>" + "Denied" + "</strong>" + "</span></span></span></p>";
                sendEmailAfterApprove(detailMessage, typeApproveMessage, user);
            }
        }catch (Exception e){
            throw e;
        }
    }

    public void sendEmailAfterApprove(String detailMessage, String typeApproveMessage, User user) throws MessagingException {
        String mailContent = "<p><strong><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Dear Mr/Mrs " + user.getFullName() + ",</span></span></span></strong></p>" +
                typeApproveMessage +
                detailMessage +
                "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Thank you for using our service,</span></span></span></p>" +
                "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Best regards.</span></span></span></p>";
        String mailHeader = "Request Approved confirm on Mwd website";
        commonServices.sendEmail(user.getEmail(), mailContent, mailHeader);
    }

    public void approveRegisterSinger(ApproveRequest request) throws MessagingException {
        try {
            checkApproveRequest(request);
            Optional<RequestBeSinger> optional = beSingerRepository.findById(request.getRequestId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "requestId"));
            }
            if (optional.get().getStatus() != 0) {
                throw new AppResponseException(new Message(AppConstants.APPROVED, "requestId"));
            }
            Optional<User> optionalUser = findByUsername(optional.get().getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
            }
            User user = optionalUser.get();
            if (request.getType() == 1) {
                user.setRoleId(AppConstants.SINGER_ROLE_ID);
                userRepository.save(user);
            }
            RequestBeSinger beSinger = optional.get();
            beSinger.setStatus(request.getType());
            beSinger.setApprove_time(Instant.now());
            beSinger.setApprove_details(request.getReason());
            beSingerRepository.save(beSinger);
            String typeApprove = request.getType() == 1 ? "Approved" : request.getType() == 2 ? "Denied" :"";
            String detailMessage = request.getType() == 2 ? "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Reason: " + request.getReason() + "</span></span></span></p>" : "";
            String typeApproveMessage = "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Your Request To Register As New Singer has been: " + "<strong>" + typeApprove + "</strong>" + "</span></span></span></p>";
            sendEmailAfterApprove(detailMessage, typeApproveMessage, user);
        }catch (Exception e){
            throw e;
        }
    }

    public void approvePublishProduct(ApproveRequest request) throws MessagingException {
        try {
            checkApproveRequest(request);
            Optional<RequestPublishProduct> optional = publishProductRepository.findById(request.getRequestId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "requestId"));
            }
            if (optional.get().getStatus() != 0) {
                throw new AppResponseException(new Message(AppConstants.APPROVED, "requestId"));
            }
            if (request.getType() == 1) {
                Optional<Product> optionalProduct = productRepository.findById(optional.get().getProductId());
                if (optionalProduct.isEmpty()) {
                    throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "product"));
                }
                if (!optional.get().getUsername().equals(optionalProduct.get().getUsername())) {
                    throw new AppResponseException(new Message(AppConstants.NOT_MATCHED, "username request and product owner"));
                }
                Product product = optionalProduct.get();
                product.setIsPublished(true);
                product.setPrice(optional.get().getPrice());
                productRepository.save(product);
                saveMoney(false, optional.get().getPrice() * AppConstants.PUBLISH_SONG_FEE, optional.get().getUsername());
            }
            RequestPublishProduct publishProduct = optional.get();
            publishProduct.setStatus(request.getType());
            publishProduct.setApprove_time(Instant.now());
            publishProduct.setApprove_details(request.getReason());
            publishProductRepository.save(publishProduct);
            Optional<User> optionalUser = findByUsername(optional.get().getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
            }
            String typeApprove = request.getType() == 1 ? "Approved" : request.getType() == 2 ? "Denied" :"";
            String detailMessage = request.getType() == 2 ? "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Reason: " + request.getReason() + "</span></span></span></p>" : "";
            String typeApproveMessage = "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Your Request To Publish New Song has been: " + "<strong>" + typeApprove + "</strong>" + "</span></span></span></p>";
            sendEmailAfterApprove(detailMessage, typeApproveMessage, optionalUser.get());
        }catch (Exception e){
            throw e;
        }
    }

    public void checkApproveRequest(ApproveRequest request) {
        if (StringUtils.isEmpty(request.getRequestId())) {
            throw new AppResponseException(new Message(AppConstants.NOT_NULL, "requestId"));
        }
        if (StringUtils.isEmpty(request.getType())) {
            throw new AppResponseException(new Message(AppConstants.NOT_NULL, "requestType"));
        }
        if (request.getType() == 0) {
            throw new AppResponseException(new Message(AppConstants.INVALID, "requestType"));
        }
        if (request.getType() == 2 && StringUtils.isEmpty(request.getReason())) {
            throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Reason"));
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

    public void saveMoney(Boolean isAdding, Double money, String username) {
        try {
            Optional<User> optional = findByUsername(username);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            User user = optional.get();
            user.setMoney(isAdding ? optional.get().getMoney() + money : optional.get().getMoney() - money);
            userRepository.save(user);
        } catch (Exception e){
            throw e;
        }
    }

    public void addComments(AddCommentsRequest request) {
        try {
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ProductId"));
            }
            if (StringUtils.isEmpty(request.getCommentData())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "CommentData"));
            }
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product"));
            }
            Comment comment = new Comment();
            comment.setUsername(commonServices.getCurrentUser().getUsername());
            comment.setCommentData(request.getCommentData());
            comment.setProductId(request.getProductId());
            comment.setParentId(request.getParentId());
            comment.setCreateTime(Instant.now());
            commentRepository.save(comment);
            Product product = optionalProduct.get();
            product.setTotalComment(product.getTotalComment() + 1);
            productRepository.save(product);
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteComment(Integer commentId) {
        try {
            if (StringUtils.isEmpty(commentId)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "commentId"));
            }
            Optional<Comment> optional = commentRepository.findById(commentId);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Comment"));
            }
            Comment comment = optional.get();
            comment.setCommentData(null);
            commentRepository.save(comment);
        } catch (Exception e){
            throw e;
        }
    }

    public Page<Comment> loadAllComments(SearchCommentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ProductId"));
            }
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product"));
            }
            Pageable pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getPageSize());
            return commentRepository.findAllByProductIdAndParentIdIsNull(request.getProductId(), pageable);
        } catch (Exception e){
            throw e;
        }
    }
}
