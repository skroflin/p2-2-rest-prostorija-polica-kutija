/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
public record SkroflinKutijaDTO(
        @Schema(example = "2025-06-21") Date datumPohrane,
        @Schema(example = "3.65") BigDecimal obujam,
        @Schema(example = "B54") String oznakaKutije,
        @Schema(example = "1") int policaSifra
        ) {
    
}