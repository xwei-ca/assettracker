package com.example.assettracker.repository;

import com.example.assettracker.model.Asset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    // Return list of assets by status
    List<Asset> findByStatus(String status);
}