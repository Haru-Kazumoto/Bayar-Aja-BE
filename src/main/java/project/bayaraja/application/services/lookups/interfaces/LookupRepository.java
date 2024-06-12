package project.bayaraja.application.services.lookups.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.bayaraja.application.services.lookups.LookupEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends JpaRepository<LookupEntity, Integer> {

    @Query("SELECT lt FROM LookupEntity lt WHERE lt.type = :type")
    List<LookupEntity> findLookupByType(@Param("type") String type);

    @Query("SELECT lu FROM LookupEntity lu WHERE lu.type = :type AND lu.key = :key")
    Optional<LookupEntity> findLookupByTypeAndKey(@Param("type") String type, @Param("key") String key);

    @Query("SELECT lk FROM LookupEntity lk WHERE lk.key = :key")
    Optional<LookupEntity> findLookupByKey(@Param("key") String key);

    @Query("SELECT lv FROM LookupEntity lv WHERE lv.value = :value")
    Optional<LookupEntity> findLookupByValue(@Param("value") String value);

}
