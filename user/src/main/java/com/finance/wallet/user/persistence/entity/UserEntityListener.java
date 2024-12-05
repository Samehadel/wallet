package com.finance.wallet.user.persistence.entity;

import com.finance.common.constants.UserStatusEnum;
import com.finance.wallet.user.util.CifGenerator;

import jakarta.persistence.PrePersist;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener implements ApplicationContextAware {

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        // Empty method because not autowiring any beans
    }

    @PrePersist
    public void prePersist(final UserEntity user) {
        user.setStatus(UserStatusEnum.INACTIVE);
        user.setActivated(Boolean.FALSE);
        user.setLoginTrials(0);
        user.setCif(CifGenerator.generateRandomCif());
    }
}
