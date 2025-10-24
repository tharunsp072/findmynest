package com.fmn.fmn_backend.dto.TenantDTO;


import com.fmn.fmn_backend.entity.TenantProfile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class TenantDTO {
    public TenantDTO(TenantProfile tenant) {
        this.tenantId = tenant.getTenantId();
        this.username = tenant.getUsername();
        this.email = tenant.getUser().getEmail();
        this.contact_number = tenant.getContact_number();
}
    public TenantDTO(Long tenantId, String username, String email, String contact_number) {
        this.tenantId = tenantId;
        this.username = username;
        this.email = email;
        this.contact_number = contact_number;
    }
    public TenantDTO(TenantDTO tenant) {
        //TODO Auto-generated constructor stub
    }
    private Long tenantId;
    private String username;
    private String email;
    private String contact_number;
}
