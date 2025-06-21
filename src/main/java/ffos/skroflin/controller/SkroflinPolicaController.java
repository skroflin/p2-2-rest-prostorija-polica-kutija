/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.service.SkroflinPolicaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */
@Tag(name = "Skroflin -> Polica", description = "Sve dostupne rute koje se odnose na entitet SkroflinPolica. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/skroflinPolica")
public class SkroflinPolicaController {
    private final SkroflinPolicaService policaService;
}
