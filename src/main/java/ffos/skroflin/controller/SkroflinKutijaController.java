/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.SkroflinKutija;
import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.dto.SkroflinKutijaDTO;
import ffos.skroflin.service.SkroflinKutijaService;
import ffos.skroflin.service.SkroflinPolicaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
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
@Tag(name = "Skroflin -> Kutija", description = "Sve dostupne rute koje se odnose na entitet SkroflinKutija. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/skroflinKutija")
public class SkroflinKutijaController {
    SkroflinKutijaService kutijaService;
    SkroflinPolicaService policaService;

    public SkroflinKutijaController(SkroflinKutijaService kutijaService, SkroflinPolicaService policaService) {
        this.kutijaService = kutijaService;
        this.policaService = policaService;
    }
    
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(kutijaService.getAll(), HttpStatus.OK);
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
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            SkroflinKutija kutija = kutijaService.getBySifra(sifra);
            if (kutija == null) {
                return new ResponseEntity<>("Kutija s navedenom šifrom" + " " + sifra + " " + "ne postoji!", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(kutija, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) SkroflinKutijaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci!" + " " + dto, HttpStatus.NO_CONTENT);
            }
            if (dto.datumPohrane() == null) {
                return new ResponseEntity<>("Datum obavezan!", HttpStatus.BAD_REQUEST);
            }
            SkroflinPolica polica = policaService.getBySifra(dto.policaSifra());
            if (polica == null) {
                return new ResponseEntity<>("Polica s navedenom šifrom ne postoji!", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(kutijaService.post(dto), HttpStatus.OK);
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
            kutijaService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "polica!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/put")
    public ResponseEntity put(
            @RequestParam int sifra,
            @RequestBody SkroflinKutijaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci!" + " " + dto, HttpStatus.NO_CONTENT);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            SkroflinPolica sp = policaService.getBySifra(sifra);
            if (sp == null) {
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            kutijaService.put(dto, sifra);
            return new ResponseEntity<>("Promijenjena polica s navedenom šifrom" + " " + sifra, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/dodajKutijeSObujmom")
    public ResponseEntity dodajKutijeSObujmom(
            @RequestParam BigDecimal obujam,
            @RequestParam int broj
    ){
        try {
            if (broj <= 0) {
                return new ResponseEntity<>("Broj kutija mora biti veći od 0!" + " " + broj, HttpStatus.BAD_REQUEST);
            }
            kutijaService.dodajKutijeSObujmom(obujam, broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "kutije s obujmom od" + " " + obujam, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
