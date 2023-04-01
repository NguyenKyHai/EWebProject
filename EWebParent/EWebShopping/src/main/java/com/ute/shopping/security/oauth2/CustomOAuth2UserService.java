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

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
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
        Customer customer;
        if(userOptional.isPresent()) {
            customer = userOptional.get();
            if(!customer.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        customer.getProvider() + " account. Please use your " + customer.getProvider() +
                        " account to login.");
            }
            customer = updateExistingUser(customer, oAuth2UserInfo);
        } else {
            customer = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(customer, oAuth2User.getAttributes());
    }

    private Customer registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Customer customer = new Customer();

        customer.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        customer.setProviderId(oAuth2UserInfo.getId());
        customer.setFullName(oAuth2UserInfo.getName());
        customer.setEmail(oAuth2UserInfo.getEmail());
        customer.setPhotos(oAuth2UserInfo.getImageUrl());
        customer.setCreatedTime(new Date());
        customer.setStatus(Constants.STATUS_ACTIVE);
        return customerRepository.save(customer);
    }

    private Customer updateExistingUser(Customer existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setStatus(Constants.STATUS_ACTIVE);
        customerRepository.updateSessionString(existingUser.getId(), HelperUtil.randomString());
        return customerRepository.save(existingUser);
    }

}
