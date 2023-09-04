package com.sharework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sharework.model.LocationFavorite;

import java.util.List;
import java.util.Optional;

public interface LocationFavoriteDao extends JpaRepository<LocationFavorite, Long> {

    List<LocationFavorite> findByUserId(long userId);

    Optional<LocationFavorite> getByUserId(long userId);

    void deleteByUserId(long userId);

    int countByUserId(long userId);
}
