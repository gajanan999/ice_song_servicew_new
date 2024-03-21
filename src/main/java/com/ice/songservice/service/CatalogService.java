package com.ice.songservice.service;

import com.ice.songservice.dto.SongDto;
import com.ice.songservice.entity.Song;
import com.ice.songservice.entity.User;
import com.ice.songservice.constant.ErrorCodeConstant;
import com.ice.songservice.dao.SongRepository;
import com.ice.songservice.dao.UserRepository;
import com.ice.songservice.exception.NoRecordFound;
import com.ice.songservice.exception.RecordAlreadyExistsException;
import com.ice.songservice.exception.UnknownActionException;
import com.ice.songservice.mapper.DtoToEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Catalog service is used for handing the user catalog related data
 */
@Service
public class CatalogService {

    private static final Logger logger = LoggerFactory.getLogger(CatalogService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    MessageSource messageSource;

    @Transactional
    public boolean addSongsToUserCatalog(UUID userId, List<SongDto> songDtos) throws RecordAlreadyExistsException, UnknownActionException, NoRecordFound {
        logger.debug("addSongsToUserCatalog {}",userId.toString());
        for(SongDto songDto: songDtos){
            if (!songRepository.existsBySongUrl(songDto.getSongUrl())) {
                addSongToUserCatalog(userId,songDto);
            }else{
                updateSongFromUserCatalog(userId,songDto);
            }
        }
        return true;
    }

    @Transactional
    public boolean addSongToUserCatalog(UUID userId, SongDto songDto) throws RecordAlreadyExistsException, UnknownActionException, NoRecordFound {
        logger.debug("addSongToUserCatalog {}",userId.toString());
        if (songRepository.existsBySongUrl(songDto.getSongUrl())) {
            throw new RecordAlreadyExistsException(ErrorCodeConstant.ERR_103,messageSource.getMessage(ErrorCodeConstant.ERR_103,null, Locale.ENGLISH));
        }
        User user = userRepository.findById(userId).orElse(null);
        if(null == user){
            throw new NoRecordFound(ErrorCodeConstant.ERR_107, messageSource.getMessage(ErrorCodeConstant.ERR_107, new Object[]{userId}, Locale.ENGLISH));
        }else{
            Song newSong = DtoToEntityMapper.INSTANCE.songDtoToSong(songDto);
            newSong.setSongId(null);
            newSong = songRepository.save(newSong);
            user.addSong(newSong);
        }
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean updateSongFromUserCatalog(UUID userId, SongDto songDto) throws UnknownActionException, NoRecordFound {
            logger.debug("updateSongFromUserCatalog {}",userId.toString());
            Song song = songRepository.findById(songDto.getSongId()).orElse(null);
            if (song == null) {
                throw new NoRecordFound(ErrorCodeConstant.ERR_101, messageSource.getMessage(ErrorCodeConstant.ERR_101, null, Locale.ENGLISH));
            }

            User user = userRepository.findById(userId).orElse(null);
            if(null == user){
                throw new NoRecordFound(ErrorCodeConstant.ERR_107, messageSource.getMessage(ErrorCodeConstant.ERR_107, new Object[]{userId}, Locale.ENGLISH));
            }else{
                Song existingSong = songRepository.findById(songDto.getSongId()).orElse(null);
                user.updateSong(DtoToEntityMapper.INSTANCE.songDtoToSong(songDto),existingSong);
                songRepository.save(existingSong);
            }
            userRepository.save(user);
            return true;
    }

    @Transactional
    public boolean deleteSongFromUserCatalog(UUID userId, UUID songId) throws UnknownActionException {
        logger.debug("deleteSongFromUserCatalog User Id {} song Id{}",userId.toString(), songId.toString());
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                logger.debug("removing songId {} from user userId {}", songId, user.getId());
                user.removeSong(songId);
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting songs from user catalog for userId {}: {}", userId, e.getMessage());
            return false;
        }
    }
}
