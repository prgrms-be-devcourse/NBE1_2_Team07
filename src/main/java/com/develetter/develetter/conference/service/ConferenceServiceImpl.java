package com.develetter.develetter.conference.service;

import com.develetter.develetter.conference.converter.Converter;
import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.conference.dto.ConferenceResDto;
import com.develetter.develetter.conference.entity.Conference;
import com.develetter.develetter.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;

    @Override
    public List<ConferenceResDto> getAllConference() {

        return conferenceRepository.findAll()
                .stream().map(Converter::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void createConference(ConferenceRegisterDto conferenceRegisterDto) {
        Conference conference = Converter.toEntity(conferenceRegisterDto);
        conferenceRepository.save(conference);
    }

    @Transactional
    @Override
    public void updateConference(Long id, ConferenceRegisterDto conferenceRegisterDto) {
        Conference findConference = conferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 컨퍼런스를 찾을 수 없습니다. " + id));

        findConference.updateConference(conferenceRegisterDto);
    }

    @Transactional
    @Override
    public void deleteConference(Long id) {
        if (!conferenceRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 컨퍼런스를 찾을 수 없습니다. " + id);
        }

        conferenceRepository.deleteById(id);
    }
}
