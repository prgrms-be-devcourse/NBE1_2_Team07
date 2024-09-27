package com.develetter.develetter.conference.service;

import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.conference.dto.ConferenceResDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ConferenceService {
    List<ConferenceResDto> getAllConference();

    void createConference(ConferenceRegisterDto conferenceRegisterDto);

    void updateConference(Long id, ConferenceRegisterDto conferenceRegisterDto);

    void deleteConference(Long id);
}
