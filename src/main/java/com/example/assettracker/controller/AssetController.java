package com.example.assettracker.controller;

import com.example.assettracker.model.Asset;
import com.example.assettracker.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<Asset> getAssets(@RequestParam(required = false) String status) {
        if (status != null && !status.trim().isEmpty()) {
            return assetService.findByStatus(status);
        }
        return assetService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getById(@PathVariable Long id) {
        var asset = assetService.findById(id);
        return ResponseEntity.ok(asset);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Asset create(@Valid @RequestBody Asset asset) {
        return assetService.create(asset);
    }

    @PutMapping("/{id}")
    public Asset update(@PathVariable Long id, @Valid @RequestBody Asset asset) {
        return assetService.update(id, asset);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        assetService.delete(id);
    }
}