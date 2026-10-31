package com.example.assettracker.service;

import com.example.assettracker.exception.RecordNotFoundException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Asset findById(Long id) {
        return assetRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Asset not found with ID: " + id));
    }

    public List<Asset> findByStatus(String status) {
        return assetRepository.findByStatus(status);
    }

    public Asset create(Asset asset) {
        return assetRepository.save(asset);
    }

    public Asset update(Long id, Asset updated) {
        return assetRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setType(updated.getType());
                existing.setStatus(updated.getStatus());
                existing.setSerialNumber(updated.getSerialNumber());
                existing.setAssignedTo(updated.getAssignedTo());
                return assetRepository.save(existing);
            })
            .orElseThrow(() -> new RecordNotFoundException("Asset not found: " + id));
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }
}