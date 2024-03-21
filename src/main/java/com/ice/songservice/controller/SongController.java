package com.ice.songservice.controller;

import com.ice.songservice.constant.ApiStatus;
import com.ice.songservice.constant.ErrorCodeConstant;
import com.ice.songservice.dto.RestResponse;
import com.ice.songservice.dto.SongDto;
import com.ice.songservice.exception.NoRecordFound;
import com.ice.songservice.exception.RecordAlreadyExistsException;
import com.ice.songservice.service.SongService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Validated
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    private SongService songService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    public SongController(SongService songService){
        this.songService = songService;
    }

    /**
     * Get all songs using different kind of filters
     * @param userId
     * @param releaseYear
     * @param artistName
     * @param sortBy
     * @param sortByColumns
     * @return
     */
    @GetMapping("/songs")
    public ResponseEntity<?> getAllSongs(
            @RequestParam(required = false) Optional<UUID> userId,
            @RequestParam(required = false) Optional<String> releaseYear,
            @RequestParam(required = false) Optional<String> artistName,
            @RequestParam(required = false) Optional<String> sortBy,
            @RequestParam(required = false) Optional<List<String>> sortByColumns
            ){
        logger.debug("Got the request for /songs getAllSongs");
        List<String> allowedColumnsForSort = Arrays.asList("releaseYear", "artistName", "songName","songDetails");
        if (sortByColumns.isPresent() && sortBy.isPresent()) {
            for (String column : sortByColumns.get()) {
                if (!allowedColumnsForSort.contains(column)) {
                    String errorMessage = messageSource.getMessage(ErrorCodeConstant.ERR_108,new Object[]{column}, Locale.ENGLISH);
                    logger.info("errorMessage", errorMessage);
                    return ResponseEntity.badRequest().body(new RestResponse(null, ErrorCodeConstant.ERR_108, errorMessage, ApiStatus.FAILED.toString()));
                }
            }
        }
        List<SongDto> songDtos = songService.getAllSongs(userId, releaseYear, artistName, sortBy, sortByColumns);
        return ResponseEntity.ok(songDtos);
    }

    /**
     * Get the song count with optional parameter for userId
     * @param userId
     * @return
     */
    @GetMapping("/songs/count")
    public ResponseEntity<?> getSongsCount(@RequestParam(required = false) Optional<UUID> userId){
        try{
            logger.debug("Got the request for /songs/count getSongsCount");
            Long count = songService.getSongsCount(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new RestResponse(count,"", "", ApiStatus.SUCCESS.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null,"", "", ApiStatus.SUCCESS.toString()));
        }
    }

    /**
     * Add song into database
     * @param songDto
     * @return
     */
    @PostMapping("/songs")
    public ResponseEntity<?> createSong(@Valid @RequestBody SongDto songDto) {
        logger.debug("Got the request for /songs createSong");
        try{
            SongDto songDtos = songService.createSong(songDto);
            return ResponseEntity.ok(songDtos);
        }catch(RecordAlreadyExistsException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse(null,e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update the song into database
     * @param songDto
     * @return
     */
    @PutMapping("/songs")
    public ResponseEntity<?> updateSong(@Valid @RequestBody SongDto songDto) {
        logger.debug("Got the request for /songs updateUser");
        try{
            SongDto songDtos = songService.updateSong(songDto);
            return ResponseEntity.ok(songDtos);
        }catch(NoRecordFound e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null,e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete the song from the database
     * @param songId
     * @return
     */
    @DeleteMapping("/songs/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable UUID songId) {
        logger.info("Got the request for /songs/{songId} deleteUser userId - {}", songId);
        try{
            songService.deleteSong(songId);
            return ResponseEntity.ok().build();
        }catch(NoRecordFound e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null,e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(DataIntegrityViolationException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null,ErrorCodeConstant.ERR_109, messageSource.getMessage(ErrorCodeConstant.ERR_109, null, Locale.ENGLISH), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
