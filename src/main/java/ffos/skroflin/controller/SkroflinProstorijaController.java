/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.SkroflinProstorija;
import ffos.skroflin.model.dto.SkroflinProstorijaDTO;
import ffos.skroflin.service.SkroflinPolicaService;
import ffos.skroflin.service.SkroflinProstorijaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@Tag(name = "Skroflin -> Prostorija", description = "Sve dostupne rute koje se odnose na entitet SkroflinProstorija. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/skroflinProstorija")
public class SkroflinProstorijaController {
    private final SkroflinProstorijaService prostorijaService;
    private final SkroflinPolicaService policaService;

    public SkroflinProstorijaController(SkroflinProstorijaService prostorijaService, SkroflinPolicaService policaService) {
        this.prostorijaService = prostorijaService;
        this.policaService = policaService;
    }
    
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(prostorijaService.getAll(), HttpStatus.OK);
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
            SkroflinProstorija prostorija = prostorijaService.getBySifra(sifra);
            if (prostorija == null) {
                return new ResponseEntity<>("Prostorija s navedenom šifrom ne postoji" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(prostorija, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) SkroflinProstorijaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Nije unesen naziv kabineta" + " " + dto.naziv(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(prostorijaService.post(dto), HttpStatus.OK);
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
                return new ResponseEntity<>("Broj mora biti veći od 0!" +  " " + broj, HttpStatus.BAD_REQUEST);
            }
            prostorijaService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "prostorija", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/put")
    public ResponseEntity put(
            @RequestParam int sifra,
            @RequestBody (required = true) SkroflinProstorijaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci" + " " + dto, HttpStatus.NO_CONTENT);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + sifra, HttpStatus.BAD_REQUEST);
            }
            
            SkroflinProstorija sp = prostorijaService.getBySifra(sifra);
            if (sp == null) {
                return new ResponseEntity<>("Ne postoji prostorija s navedenom šifrom" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv prostorije je obavezan!" + " " + dto.naziv(), HttpStatus.BAD_REQUEST);
            }
            prostorijaService.put(dto, sifra);
            return new ResponseEntity<>("Promijenjena prostorija sa šifrom:" + " " + sifra, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getProstorPoKabinetu")
    public ResponseEntity getProstorPoKabinetu(
            @RequestParam boolean jeKabinet
    ){
        try {
            List<SkroflinProstorija> prostorije = prostorijaService.getProstorPoKabinetu(jeKabinet);
            if (prostorije.isEmpty()) {
                return new ResponseEntity<>("Ne postoje prostorije s navedenim uvjetom" + " " + jeKabinet, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(prostorije, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getPoliceUProstoriji")
    public ResponseEntity getPoliceUProstoriji(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra prostorije mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            SkroflinProstorija prostorija = prostorijaService.getBySifra(sifra);
            if (prostorija == null) {
                return new ResponseEntity<>("Prostorija s navedenom šifrom" + " " + sifra + " " + "ne postoji!", HttpStatus.NOT_FOUND);
            }
            List<SkroflinPolica> police = policaService.getPoliceUProstoriji(prostorija);
            if (police.isEmpty()) {
                return new ResponseEntity<>("Nema polica u navedenoj prostoriji sa šifrom" + " " + sifra, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(police, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
