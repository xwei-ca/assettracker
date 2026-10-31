package com.example.assettracker;

import org.junit.jupiter.api.BeforeEach;

import com.example.assettracker.model.Asset;

public abstract class BaseAssetControllerTest {
    protected Asset asset;

    @BeforeEach
    void setUpSharedResources() {
        // This method will run BEFORE every single test method 
        // in any subclass that extends this class.
        asset = new Asset("Dell Laptop","LAPTOP","IN_USE","SN-001","Alice");
        asset.setId(1L);
    }
    

}
