package com.ute.shopping.security.oauth2;

import com.ute.common.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.ute.common.constants.AuthProvider;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.exception.OAuth2AuthenticationProcessingException;
import com.ute.shopping.customer.ICustomerRepository;
import com.ute.shopping.security.UserPrincipal;
import com.ute.shopping.security.authprovider.OAuth2UserInfo;
import com.ute.shopping.security.authprovider.OAuth2UserInfoFactory;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Customer> userOptional = customerRepository.findByEmail(oAuth2UserInfo.getEmail());
        Customer user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private Customer registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Customer user = new Customer();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFullName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setPhotos(oAuth2UserInfo.getImageUrl());
        user.setStatus(Constants.STATUS_ACTIVE);
        return customerRepository.save(user);
    }

    private Customer updateExistingUser(Customer existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setPhotos(oAuth2UserInfo.getImageUrl());
        existingUser.setStatus(Constants.STATUS_ACTIVE);
        customerRepository.updateSessionString(existingUser.getId(), HelperUtil.randomString());
        return customerRepository.save(existingUser);
    }

}
