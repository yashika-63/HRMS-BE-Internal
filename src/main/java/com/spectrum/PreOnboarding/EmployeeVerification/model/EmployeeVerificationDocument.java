package com.spectrum.PreOnboarding.EmployeeVerification.model;

import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeVerificationDocument {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private boolean verificationStatus;

        private String filePath;

        @Column

        private String label;


        @ManyToOne
        @JoinColumn(name = "verification_ticket_id", referencedColumnName = "id", nullable = false)
        private VerificationTicket verificationTicket;

        @ManyToOne
        @JoinColumn(name = "pre_registration_id", referencedColumnName = "id", nullable = false)
        private PreRegistration preRegistration;

        public EmployeeVerificationDocument(String filePath) {
            this.filePath = filePath;
        }
    
        public EmployeeVerificationDocument orElseThrow(Object object) {
            throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
        }
 
}
