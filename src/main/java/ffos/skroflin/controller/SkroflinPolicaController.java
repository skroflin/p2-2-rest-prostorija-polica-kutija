/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.SkroflinKutija;
import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.SkroflinProstorija;
import ffos.skroflin.model.dto.SkroflinPolicaDTO;
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
    
    @Operation(
            summary = "Dohvaća sve police", tags = {"get", "polica"},
            description = "Dohvaća sve police sa svim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SkroflinPolica.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity getAll(){
        try {
            return new ResponseEntity<>(policaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća policu po šifri",
            description = "Dohvaća policu po danoj šifri sa svim podacima o polici. "
            + "Ukoliko ne postoji polica za danu šifru vraća prazan odgovor",
            tags = {"polica", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ police u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
        @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinPolica.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji polica za danu šifru"),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Kreira novu policu",
            tags = {"polica", "post"},
            description = "Kreira novu policu. Dužina, širina i visina je obavezna!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = SkroflinPolica.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji dužina ili širina ili visina)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Unosi broj polica za dan broj",
            description = "Masovno dodavanje za dan broj u obliku parametra. "
            + "Pr. ukoliko korisnik odabere 10, unijeti će se 10 zapisa s lažnim podacima pomoću Faker biblioteke.",
            tags = {"polica", "masovnoDodavanje", "post"},
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
                return new ResponseEntity<>("Broj mora biti veći od 0!" + " " + broj, HttpStatus.BAD_REQUEST);
            }
            policaService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "polica!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja podatke o polici",
            tags = {"put", "polica"},
            description = "Mijenja podatke o polici. Dužina, širina, visina police, te strani ključ na entite prostorije je obavezan!",
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
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili dužina ili širina ili visina)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
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
    
    @Operation(
            summary = "Dohvaća ukupnu širinu polica", tags = {"get", "polica"},
            description = "Dohvaća ukupnu širinu svih polica."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SkroflinPolica.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/getUkupnaSirinaPolice")
    public ResponseEntity getUkupnaSirinaPolice(){
        try {
            int ukupnaSirina = policaService.getUkupnaSirinaPolice();
            return new ResponseEntity<>("Ukupna širina svih polica u svim prostorijama" + " " + ukupnaSirina, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća sve kutije na polici",
            description = "Dohvaća kutije po danoj šifri police sa svim podacima o polici i kutijama. "
            + "Ukoliko ne postoji polica za danu šifru vraća prazan odgovor. Ukoliko nema kutija vraća prazan odgovor.",
            tags = {"polica", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ police u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SkroflinPolica.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji polica za danu šifru"),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getKutijeNaPolici")
    public ResponseEntity getKutijeNaPolici(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            SkroflinPolica polica = policaService.getBySifra(sifra);
            if (polica == null) {
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" + " " + sifra, HttpStatus.NOT_FOUND);
            }
            
            List<SkroflinKutija> kutije = policaService.getKutijeNaPolici(polica);
            if (kutije.isEmpty()) {
                return new ResponseEntity<>("Nema kutiji na polici sa šifrom" + " " + sifra, HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(kutije, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
