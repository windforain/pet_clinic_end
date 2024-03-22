package com.pet_clinic_end;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class PetClinicEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicEndApplication.class, args);
        test.hello();
    }

}
