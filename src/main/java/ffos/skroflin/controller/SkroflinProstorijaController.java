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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    
    @Operation(
            summary = "Dohvaća sve prostorije", tags = {"get", "prostorija"},
            description = "Dohvaća sve prostorije sa svim podacima"
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SkroflinProstorija.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(prostorijaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća prostoriju po šifri",
            description = "Dohvaća prostoriju po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji prostorija za danu šifru vraća prazan odgovor",
            tags = {"prostorija", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ prostorije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji student za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Kreira novu prostoriju",
            tags = {"post", "prostorija"},
            description = "Kreira novu prostoriju. Kabinet obavezan")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = SkroflinProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji ime ili prezime ili jmbag)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Unosi broj prostorija za dan broj",
            description = "Masovno dodavanje za dan broj u obliku parametra. "
            + "Pr. ukoliko korisnik odabere 10, unijeti će se 10 zapisa s lažnim podacima pomoću Faker biblioteke.",
            tags = {"prostorija", "masovnoDodavanje", "post"},
            parameters = {
                @Parameter(
                        name = "broj",
                        allowEmptyValue = false,
                        required = true,
                        description = "Broj kojim utvrđujemo koliko zapisa unosimo, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Broj mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Mijenja podatke o prostoriji",
            tags = {"put", "prostorija"},
            description = "Mijenja podatke o prostoriji. Naziv i je li kabinet ili ne obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ prostorije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji naziv)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Dohvaća prostoriju po kabinetu",
            description = "Dohvaća prostoriju po tome je li kabinet ili ne uz sve svoje podatke. "
            + "Ukoliko ne postoji prostorija za dani parametar, vraća prazan odgovor",
            tags = {"prostorija", "get", "getProstorPoKabinetu"},
            parameters = {
                @Parameter(
                        name = "jeKabinet",
                        allowEmptyValue = false,
                        required = true,
                        description = "Booleov tip podatka (true or false)!",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji prostorija za danu vrijednost", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Vrijednost se mora odabrati!", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Dohvaća police unutar prostorije",
            description = "Dohvaća sve police unutar prostoriju, po danoj šifri prostorije sa svim podacima. "
            + "Ukoliko ne postoji prostorija za danu šifru vraća prazan odgovor. Ukoliko prostorija nema police vraća tekst.",
            tags = {"prostorija", "get", "getPoliceUProstoriji"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ prostorije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji student za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
