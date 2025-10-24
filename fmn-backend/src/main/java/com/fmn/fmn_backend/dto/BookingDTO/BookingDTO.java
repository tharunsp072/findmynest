package com.fmn.fmn_backend.dto.BookingDTO;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class BookingDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    // private String paymentMode;
}
