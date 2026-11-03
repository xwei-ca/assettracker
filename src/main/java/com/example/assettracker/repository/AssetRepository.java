package com.example.assettracker.repository;

import com.example.assettracker.constants.AssetStatus;
import com.example.assettracker.model.Asset;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    // Return list of assets by status
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByStatusOrderByNameAsc(AssetStatus status);  //Asc/Desc
    List<Asset> findByStatusAndType(AssetStatus status, String type);
    List<Asset> findByAssignedToIgnoreCase(String assignedTo);
    Optional<Asset> findBySerialNumber(String serialNumber); // cleaner for single-result lookups
}