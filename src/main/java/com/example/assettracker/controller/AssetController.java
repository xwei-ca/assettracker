package com.example.assettracker.controller;

import com.example.assettracker.constants.AssetStatus;
import com.example.assettracker.model.Asset;
import com.example.assettracker.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<Asset> getAssets(@RequestParam(required = false) AssetStatus status) {
        if (status != null) {
            return assetService.findByStatus(status);
        }
        return assetService.findAll();
    }

    @GetMapping("/{id}")
    public Asset getById(@PathVariable Long id) {
        return assetService.findById(id);
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

    // TO-DO
    // Consider of bringing a Patch end point to handle partial updates
    // Since update() replaces all fields

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        assetService.delete(id);
    }
}