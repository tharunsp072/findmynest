package com.fmn.fmn_backend.service.OwnerService;

import java.util.List;
import java.util.Map;

import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;

public interface OwnerService {
        OwnerProfile saveOwner(Long user_id, OwnerProfile owner);

        OwnerProfile findById(Long id);

        Property saveProperty(Long ownerId, Property property);

        List<Property> findAllpropertiesOfOwner(Long ownerId);

        List<TenantProfile> findAllTenantsOfOwner(Long ownerId);

        void deleteOwnerProperty(Long propertyId, Long ownerId);

        OwnerProfile updateOwnerDetails(Long userId, OwnerProfile ownerProfile);

        Map<String, Object> getOwnerStats(Long ownerId);
}
