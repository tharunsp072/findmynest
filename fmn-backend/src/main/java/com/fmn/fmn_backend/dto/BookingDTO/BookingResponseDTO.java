package com.fmn.fmn_backend.dto.BookingDTO;

import java.time.LocalDate;
import java.util.List;

import com.fmn.fmn_backend.dto.PaymentDTO;
import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.dto.TenantDTO.TenantDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class BookingResponseDTO {
    private Long bookingId;
    private TenantDTO tenant;
    private PropertyDTO property;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
        private List<PaymentDTO> payments;
  

}

