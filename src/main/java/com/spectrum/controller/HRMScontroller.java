package com.spectrum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.HRMSmaster;
import com.spectrum.service.HRMSservice;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
// @CrossOrigin("http://localhost:3000")

@RequestMapping("/api/master")
public class HRMScontroller {

    HRMSservice masterService;

    @GetMapping
    public ResponseEntity<List<HRMSmaster>> getAllMasters() {
        List<HRMSmaster> masters = masterService.getAllMasters();
        return ResponseEntity.ok().body(masters);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Master> getMasterById(@PathVariable("id") Long id) {
    // Optional<Master> master = masterService.getMasterById(id);
    // return master.map(value -> ResponseEntity.ok().body(value))
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @PostMapping
    public ResponseEntity<HRMSmaster> createMaster(@RequestBody HRMSmaster master) {
        HRMSmaster createdMaster = masterService.createMaster(master);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaster);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<HRMSmaster> updateMaster(@PathVariable("id") Long id,
    // @RequestBody HRMSmaster masterDetails) {
    // HRMSmaster updatedMaster = masterService.updateMaster(id, masterDetails);
    // return ResponseEntity.ok().body(updatedMaster);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaster(@PathVariable("id") Long id) {
        masterService.deleteMaster(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/GetDataByKey/{key}")
    public ResponseEntity<List<HRMSmaster>> callByKey(@PathVariable String key) {
        List<HRMSmaster> val = masterService.callByKey(key);
        return new ResponseEntity<>(val, HttpStatus.OK);
    }

    @DeleteMapping("/key/{keyvalue}")
    public ResponseEntity<Void> deleteMastersByKey(@PathVariable String keyvalue) {
        masterService.deleteMastersByKey(keyvalue);
        return ResponseEntity.noContent().build();
    }
}
