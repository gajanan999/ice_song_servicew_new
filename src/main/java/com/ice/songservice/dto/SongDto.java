package com.ice.songservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ice.songservice.validators.PastYear;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Data
@Setter
@Getter
@ToString
public class SongDto {

    private UUID songId;

    private String songName;

    private String songDetails;

    private String artistName;

    @PastYear(message = "Year must be in the past")
    @JsonFormat(pattern = "yyyy")
    private Integer releaseYear;

    @URL(message = "Invalid URL format")
    private String songUrl;

}
