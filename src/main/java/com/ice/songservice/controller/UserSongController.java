package com.ice.songservice.controller;


import com.ice.songservice.constant.ApiStatus;
import com.ice.songservice.constant.ErrorCodeConstant;
import com.ice.songservice.dto.RestResponse;
import com.ice.songservice.dto.SongDto;
import com.ice.songservice.exception.NoRecordFound;
import com.ice.songservice.exception.RecordAlreadyExistsException;
import com.ice.songservice.exception.UnknownActionException;
import com.ice.songservice.service.CatalogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@Validated
public class UserSongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private CatalogService catalogService;

    @Autowired
    MessageSource messageSource;

    /**
     * Add songs to user catalog
     * @param userId
     * @param requests
     * @return
     */
    @PostMapping("/songs/catalogs")
    public ResponseEntity<?> addSongToUserCatalog(@RequestParam UUID userId, @Valid @RequestBody List<SongDto> requests){
        logger.debug("Got the request for /songs/catalogs addSongsToUserCatalog");
        try{
            boolean songsAddedOrDeleted = catalogService.addSongsToUserCatalog(userId, requests);
            if(songsAddedOrDeleted){
                return ResponseEntity.ok(new RestResponse(null, "", "", ApiStatus.SUCCESS.toString()));
            }else{
                throw new Exception("Something went wrong");
            }
        }catch(NoRecordFound e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestResponse(null, e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(RecordAlreadyExistsException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse(null, e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(UnknownActionException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse(null, e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null, ErrorCodeConstant.ERR_105, messageSource.getMessage(ErrorCodeConstant.ERR_105,null, Locale.ENGLISH), ApiStatus.FAILED.toString()));
        }
    }


    /**
     * update the songs to user Catalog
     * @param userId
     * @param request
     * @return
     */
    @PutMapping("/songs/catalogs")
    public ResponseEntity<?> updateSongToUserCatalog(@RequestParam UUID userId, @Valid  @RequestBody SongDto request){
        logger.debug("Got the request for /songs/catalogs addSongsToUserCatalog");
        try{
            boolean songsAddedOrDeleted = catalogService.updateSongFromUserCatalog(userId, request);
            if(songsAddedOrDeleted){
                return ResponseEntity.ok(new RestResponse(null, "", "", ApiStatus.SUCCESS.toString()));
            }else{
                throw new Exception("Something went wrong");
            }
        }catch(UnknownActionException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse(null, e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null, ErrorCodeConstant.ERR_105, messageSource.getMessage(ErrorCodeConstant.ERR_105,null, Locale.ENGLISH), ApiStatus.FAILED.toString()));
        }
    }

    /**
     * Delete the song from user catalog
     * @param userId
     * @param songId
     * @return
     */
    @DeleteMapping("/songs/catalogs")
    public ResponseEntity<?> deleteSongFromUserCatalog(@RequestParam UUID userId,  @RequestParam UUID songId){
        logger.debug("Got the request for /songs/catalogs addSongsToUserCatalog");
        try{
            boolean songsAddedOrDeleted = catalogService.deleteSongFromUserCatalog(userId, songId);
            if(songsAddedOrDeleted){
                return ResponseEntity.ok(new RestResponse(null, "", "", ApiStatus.SUCCESS.toString()));
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null, "", "", ApiStatus.FAILED.toString()));
            }
        }catch(UnknownActionException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse(null, e.getErrorCode(), e.getErrorMessage(), ApiStatus.FAILED.toString()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestResponse(null, ErrorCodeConstant.ERR_105, messageSource.getMessage(ErrorCodeConstant.ERR_105,null, Locale.ENGLISH), ApiStatus.FAILED.toString()));
        }
    }

}
