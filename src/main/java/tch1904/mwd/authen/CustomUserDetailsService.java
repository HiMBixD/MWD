package tch1904.mwd.authen;

import tch1904.mwd.constant.AppConstants;

import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.entity.UserDetailToken;
import tch1904.mwd.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Optional<UserDetailToken> optional = userService.findByUsernameToken(username);
        if (optional.isEmpty()) {
            throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
        }
        return CustomUserDetails.fromUserEntityToCustomUserDetails(optional.get());
    }
}
