package com.finance.wallet.user.service;

import com.finance.common.dto.AccessUrlDTO;
import com.finance.wallet.user.mapper.AccessUrlMapper;
import com.finance.wallet.user.persistence.entity.AccessUrlEntity;
import com.finance.wallet.user.persistence.repository.AccessUriRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class EndpointService {
    private final AccessUriRepository accessUriRepository;
    private final AccessUrlMapper accessUrlMapper;

    public List<AccessUrlDTO> getPublicEndpoints() {
        log.info("Fetching public endpoints");
        List<AccessUrlEntity> publicUrls = accessUriRepository.findByIsPrivate(false);

        return accessUrlMapper.mapToDTO(publicUrls);
    }

    public List<AccessUrlDTO> getUserEndpoints(final String username) {
        log.info("Fetching endpoints for user: [{}]", username);
        List<AccessUrlEntity> userUrls = accessUriRepository.findByUserRoles(username);

        return accessUrlMapper.mapToDTO(userUrls);
    }
}
