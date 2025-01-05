package com.finance.common.service;

import com.finance.common.util.StringUtil;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourcesMessageSource implements LocalizedMessageSource {
    private final MessageSource messageSource;

    @Override
    public String getLocalizedMessage(final String errorCode, final String[] args) {
        if (StringUtil.isNullOrEmpty(errorCode)) {
            return null;
        }

        final Locale contextLocal = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, args, contextLocal);
    }
}
