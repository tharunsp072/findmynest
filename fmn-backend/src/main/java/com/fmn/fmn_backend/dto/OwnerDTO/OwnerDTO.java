package com.fmn.fmn_backend.dto.OwnerDTO;

import java.util.List;

import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.dto.TenantDTO.TenantDTO;
import com.fmn.fmn_backend.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class OwnerDTO {

    private Long ownerId;
    private String fullname;
    private String contact_number;
    private String address;
    private String total_revenue;
    private Long userId;
    private List<PropertyDTO> properties;
    private List<TenantDTO> tenants;
    
    
}
