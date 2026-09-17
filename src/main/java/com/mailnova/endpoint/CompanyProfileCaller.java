package com.mailnova.endpoint;

import com.mailnova.DTO.CompanyProfileDto;
import com.mailnova.exceptionmessage.ApiResponse;
import com.mailnova.restEndPointCaller.RestEndPointCaller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CompanyProfileCaller extends RestEndPointCaller {


    public CompanyProfileDto addCompanyProfile(CompanyProfileDto companyProfile) {
        CompanyProfileDto saveResponse = this.postCallerWithRequestBody(CompanyProfileEndpointPath.COMPANY_PROFILE_CREATE, companyProfile, CompanyProfileDto.class);
        return saveResponse;
    }

    public List<CompanyProfileDto> getCompanyProfilesByUserId(int userId) {
        CompanyProfileDto[] companyProfileList = this.getCallerWithPathVariable(CompanyProfileEndpointPath.COMPANY_PROFILE_LIST_BY_USER+"/"+userId, CompanyProfileDto[].class);

        if(companyProfileList!=null && companyProfileList.length>0) {
            return List.of(companyProfileList);
        }
        return new ArrayList<>();
    }

    public CompanyProfileDto getCompanyProfileById(int profileId) {
        CompanyProfileDto companyProfileDto = this.getCallerWithPathVariable(CompanyProfileEndpointPath.COMPANY_PROFILE_BY_ID+"/"+profileId, CompanyProfileDto.class);
        return companyProfileDto;
    }

    public CompanyProfileDto updateCompanyProfile(CompanyProfileDto companyProfile) {
        CompanyProfileDto updateCompanyProfile = this.putCallerWithRequestBody(CompanyProfileEndpointPath.COMPANY_PROFILE_UPDATE, companyProfile, CompanyProfileDto.class);
        return updateCompanyProfile;
    }

    public ApiResponse deleteCompanyProfile(int profileId) {
        ApiResponse response = this.putCallerWithPathVariable(CompanyProfileEndpointPath.COMPANY_PROFILE_DELETE+"/"+profileId, ApiResponse.class);
        return response;

    }
}
