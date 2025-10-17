package com.fmn.fmn_backend.service.OwnerService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Payment;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.repository.BookingRepository;
import com.fmn.fmn_backend.repository.OwnerRepository;
import com.fmn.fmn_backend.repository.PaymentRepository;
import com.fmn.fmn_backend.repository.PropertyRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.repository.UserRepository;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Value("${image.path}")
    private String imageFolderPath;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    public OwnerProfile saveOwner(Long user_id, OwnerProfile owner) {
        User user = userRepo.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        owner.setUser(user);
        return ownerRepo.save(owner);
    }

    @Override
    public OwnerProfile findById(Long id) {
        Optional<OwnerProfile> owner = ownerRepo.findById(id);
        if (owner.isPresent()) {
            return owner.get();
        }
        return owner.orElseThrow(() -> new RuntimeException("Owner not found"));
    }

        // @Override
        // public Property saveProperty(Long userId, Property property, MultipartFile imageFile) {
        //     User user = userRepo.findById(userId)
        //             .orElseThrow(() -> new RuntimeException("User not found"));
        //     OwnerProfile owner = ownerRepo.findByUser(user)
        //             .orElseThrow(() -> new RuntimeException("Owner profile not found"));
        //     property.setOwner(owner);

        //     try {
        //         File folder = new File(imageFolderPath);
        //         if (!folder.exists())
        //             folder.mkdirs();

        //         String fileName = imageFile.getOriginalFilename();
        //         Path filePath = Paths.get(imageFolderPath, fileName);
        //         imageFile.transferTo(filePath);

        //         property.setImgUrl(fileName);
        //     } catch (IOException e) {
        //         throw new RuntimeException("Failed to save image");
        //     }

        //     return propertyRepo.save(property);
        // }

        @Override
        public Property saveProperty(Long userId, Property property) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            OwnerProfile owner = ownerRepo.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Owner profile not found"));

            property.setOwner(owner);

            return propertyRepo.save(property);
        }

    @Override
    public List<Property> findAllpropertiesOfOwner(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        OwnerProfile owner = ownerRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Owner profile not found"));
        List<Property> properties = propertyRepo.findByOwner(owner);
        return properties;
    }

    @Override
    public List<TenantProfile> findAllTenantsOfOwner(Long ownerId) {
        List<TenantProfile> tenants = tenantRepo.findByOwnerOwnerId(ownerId);
        return tenants;
    }

    @Override
    public void deleteOwnerProperty(Long propertyId, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        OwnerProfile owner = ownerRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Owner not found"));

        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        if (!property.getOwner().equals(owner)) {
            throw new RuntimeException("Property does not belong to this owner");
        }
        propertyRepo.delete(property);
    }

    @Override
    public OwnerProfile updateOwnerDetails(Long userId, OwnerProfile ownerProfile) {
       User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
       OwnerProfile owner = ownerRepo.findByUser(user).orElseThrow(()->new RuntimeException("Owner not found"));
       owner.setFullname(Optional.ofNullable(ownerProfile.getFullname()).orElse(owner.getFullname()));
       owner.setAddress(Optional.ofNullable(ownerProfile.getAddress()).orElse(owner.getAddress()));
       owner.setContact_number(Optional.ofNullable(ownerProfile.getContact_number()).orElse(owner.getContact_number()));

       return ownerRepo.save(owner);
    }

    public Map<String, Object> getOwnerStats(Long ownerId) {
    Map<String, Object> stats = new HashMap<>();
    int totalProperties = propertyRepo.findByOwner_OwnerId(ownerId).size();
    int totalBookings = bookingRepo.findByProperty_Owner_OwnerId(ownerId).size();
    Double totalRevenue = paymentRepo.findByBooking_Property_Owner_OwnerId(ownerId)
            .stream().mapToDouble(Payment::getPaidAmount).sum();

    stats.put("totalProperties", totalProperties);
    stats.put("totalBookings", totalBookings);
    stats.put("totalRevenue", totalRevenue);
    return stats;
}

}
