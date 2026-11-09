package com.example.assettracker.test.service;

import com.example.assettracker.constants.AssetStatus;
import com.example.assettracker.exception.RecordNotFoundException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import com.example.assettracker.service.AssetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset sample;

    @BeforeEach
    void setUp() {
        sample = new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice");
        sample.setId(1L);
    }

    @Test
    void findById_returnsAsset_whenFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(sample));

        Asset result = assetService.findById(1L);

        assertThat(result).isSameAs(sample);
        verify(assetRepository).findById(1L);
    }

    @Test
    void findById_throws_whenNotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.findById(99L))
            .isInstanceOf(RecordNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    void findByStatus_delegatesToRepository() {
        when(assetRepository.findByStatus(AssetStatus.IN_USE)).thenReturn(List.of(sample));

        List<Asset> result = assetService.findByStatus(AssetStatus.IN_USE);

        assertThat(result).hasSize(1).containsExactly(sample);
    }

    @Test
    void create_savesAndReturns() {
        when(assetRepository.save(any(Asset.class))).thenReturn(sample);

        Asset result = assetService.create(sample);

        assertThat(result).isSameAs(sample);
        verify(assetRepository).save(sample);
    }

    @Test
    void update_updatesFields_whenFound() {
        Asset updated = new Asset("Dell XPS", "LAPTOP", AssetStatus.IN_REPAIR, "SN-999", "Bob");
        when(assetRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(assetRepository.save(any(Asset.class))).thenAnswer(inv -> inv.getArgument(0));

        Asset result = assetService.update(1L, updated);

        assertThat(result.getName()).isEqualTo("Dell XPS");
        assertThat(result.getStatus()).isEqualTo(AssetStatus.IN_REPAIR);
        assertThat(result.getSerialNumber()).isEqualTo("SN-999");
        assertThat(result.getAssignedTo()).isEqualTo("Bob");
    }

    @Test
    void update_throws_whenNotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.update(99L, sample))
            .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void delete_throws_whenNotFound() {   // ⬅ after you fix delete() per Phase 1 review
        when(assetRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> assetService.delete(99L))
            .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void delete_callsRepository_whenFound() {
        when(assetRepository.existsById(1L)).thenReturn(true);

        assetService.delete(1L);

        verify(assetRepository).deleteById(1L);
    }

}
