package org.example.driverservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "DRIVER_INFO")
public class DriverDetails {
    @Id
    @Column
    String driverId;

    @Column
    String driverName;

    @Column
    String carId;

}
