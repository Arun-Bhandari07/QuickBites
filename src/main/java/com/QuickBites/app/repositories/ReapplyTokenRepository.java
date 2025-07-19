package com.QuickBites.app.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.ReapplyToken;

@Repository
public interface ReapplyTokenRepository extends JpaRepository<ReapplyToken,String> {
	
	//find the token by its uuid string
    Optional<ReapplyToken> findByToken(String token);

    // Finds all tokens that expired before a given time
    List<ReapplyToken> findAllByExpiresAtBefore(Instant now);

    //Deletes all tokens that expired before a given time
    void deleteAllByExpiresAtBefore(Instant now);


}
