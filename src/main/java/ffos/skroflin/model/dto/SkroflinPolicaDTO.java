/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author svenk
 */
public record SkroflinPolicaDTO(
        @Schema(example = "50") int duzina,
        @Schema(example = "200") int sirina,
        @Schema(example = "3") int visina,
        @Schema(example = "1") int prostorijaSifra
        ) {
    
}
