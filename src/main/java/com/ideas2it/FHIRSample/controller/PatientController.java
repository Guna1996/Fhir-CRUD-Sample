package com.ideas2it.FHIRSample.controller;

import com.ideas2it.FHIRSample.dto.PatientDto;
import com.ideas2it.FHIRSample.mapper.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * This Patient Controller class is used to get and update
 * information from the patient and also used to get
 * their vitals and appointments.
 * </p>
 *
 * @author Gunaseelan
 * @version 1
 * @since 2022-10-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public boolean addPatient(@Valid @RequestBody PatientDto patientDto) {
        return patientService.createPatient(patientDto);
    }

    @GetMapping("/{id}")
    public PatientDto getPatient(@PathVariable long id) {
        return patientService.getPatient(id);
    }

    @PutMapping("/{id}")
    public PatientDto updatePatient(@PathVariable long id, @Valid @RequestBody PatientDto patientDto) {
        return patientService.updatePatient(id, patientDto);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable long id, @Valid @RequestBody PatientDto patientDto) {
        return patientService.deletePatient(id);
    }

    @PostMapping("/bundle")
    public String createPatientBunlde(@Valid @RequestBody PatientDto patientDto) {
        return patientService.createPatientBundle(patientDto);
    }
}
