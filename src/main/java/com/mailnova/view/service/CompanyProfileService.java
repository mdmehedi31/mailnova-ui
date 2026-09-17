package com.mailnova.view.service;

import com.mailnova.DTO.CompanyProfileDto;
import com.mailnova.endpoint.CompanyProfileCaller;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory stand-in for a real persistence layer. Replace with a
 * Spring Data repository backed by a database when wiring this up
 * to a real backend.
 */
@Service
public class CompanyProfileService {

    private final Map<String, CompanyProfileDto> companies = new LinkedHashMap<>();


    private CompanyProfileCaller profileCaller;

    public CompanyProfileService(CompanyProfileCaller profileCaller) {
        this.profileCaller = profileCaller;
    }

    public List<CompanyProfileDto> findAll(Integer userId) {
        return this.profileCaller.getCompanyProfilesByUserId(userId);
    }

    public CompanyProfileDto findById(Integer profileId) {
        return this.profileCaller.getCompanyProfileById(profileId);
    }

    public CompanyProfileDto save(CompanyProfileDto company) {
        return profileCaller.addCompanyProfile(company);
    }


}
