package com.example.assettracker.controller.ai;

import com.example.assettracker.constants.AssetStatus;
import com.example.assettracker.model.Asset;
import com.example.assettracker.service.AssetService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

//AI Add-On — Simple Anomaly Detection Endpoint
@RestController
@RequestMapping("/api/ai")
public class AnomalyController {

    private final AssetService assetService;

    public AnomalyController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Flags assets that look suspicious based on simple, explainable rules.
     * This is a baseline heuristic — a real system would add a trained model later.
     */
    @GetMapping("/anomalies")
    public AnomalyReport findAnomalies() {
        List<Asset> all = assetService.findAll();

        // Rule 1: name appears on more than one asset (possible duplicate/mis-scan)
        Map<String, Long> nameCounts = all.stream()
            .collect(Collectors.groupingBy(Asset::getName, Collectors.counting()));

        // Rule 2: an asset marked IN_USE but with no assignee
        // Rule 3: an asset marked AVAILABLE but still assigned to someone
        List<Anomaly> anomalies = new ArrayList<>();
        for (Asset a : all) {
            if (nameCounts.getOrDefault(a.getName(), 0L) > 1) {
                anomalies.add(new Anomaly(a.getId(), a.getName(),
                    "DUPLICATE_NAME",
                    "Name '" + a.getName() + "' appears on multiple assets"));
            }
            if (AssetStatus.IN_USE.equals(a.getStatus())
                    && (a.getAssignedTo() == null || a.getAssignedTo().isBlank())) {
                anomalies.add(new Anomaly(a.getId(), a.getName(),
                    "IN_USE_WITHOUT_ASSIGNEE",
                    "Status is IN_USE but no assignee is recorded"));
            }
            if (AssetStatus.AVAILABLE.equals(a.getStatus())
                    && a.getAssignedTo() != null && !a.getAssignedTo().isBlank()) {
                anomalies.add(new Anomaly(a.getId(), a.getName(),
                    "AVAILABLE_BUT_ASSIGNED",
                    "Status is AVAILABLE but asset is assigned to " + a.getAssignedTo()));
            }
        }

        return new AnomalyReport(all.size(), anomalies.size(), anomalies);
    }

    public record Anomaly(Long assetId, String assetName,
                          String rule, String explanation) {}

    public record AnomalyReport(int totalAssets, int anomalyCount,
                                List<Anomaly> anomalies) {}

}
