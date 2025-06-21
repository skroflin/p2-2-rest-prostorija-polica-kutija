/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.SkroflinProstorija;
import ffos.skroflin.model.dto.SkroflinPolicaDTO;
import ffos.skroflin.service.SkroflinPolicaService;
import ffos.skroflin.service.SkroflinProstorijaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final SkroflinProstorijaService prostorijaService;

    public SkroflinPolicaController(SkroflinPolicaService policaService, SkroflinProstorijaService prostorijaService) {
        this.policaService = policaService;
        this.prostorijaService = prostorijaService;
    }
    
    @GetMapping("/get")
    public ResponseEntity getAll(){
        try {
            return new ResponseEntity<>(policaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra ne smije biti manja od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            SkroflinPolica polica = policaService.getBySifra(sifra);
            if (polica == null) {
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(polica, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) SkroflinPolicaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni obvezni podaci" + " " + dto, HttpStatus.NO_CONTENT);
            }
            SkroflinProstorija prostorija = prostorijaService.getBySifra(dto.prostorijaSifra());
            if (prostorija == null) {
                return new ResponseEntity<>("Prostorija s navedenom šifrom" + " " + prostorija + " " + "ne postoji", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(policaService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/masovnoDodavanje")
    public ResponseEntity masovnoDodavanje(
            @RequestParam int broj
    ){
        try {
            if (broj <= 0) {
                return new ResponseEntity<>("Broj mora biti veći od 0!" + " " + broj, HttpStatus.BAD_REQUEST);
            }
            policaService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "polica!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/put")
    public ResponseEntity put(
            @RequestParam int sifra,
            @RequestBody(required = true) SkroflinPolicaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni obvezni podaci" + " " + dto, HttpStatus.NO_CONTENT);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            SkroflinProstorija spr = prostorijaService.getBySifra(sifra);
            if (spr == null) {
                return new ResponseEntity<>("Ne postoji prostorija s navedenom šifrom" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            policaService.put(dto, sifra);
            return new ResponseEntity<>("Promijenjena polica sa navedenom šifrom" + " " + sifra, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
