package com.develetter.develetter.conference.repository;

import com.develetter.develetter.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
