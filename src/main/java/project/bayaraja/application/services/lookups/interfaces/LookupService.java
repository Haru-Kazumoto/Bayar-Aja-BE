package project.bayaraja.application.services.lookups.interfaces;

import org.springframework.data.jpa.domain.Specification;
import project.bayaraja.application.services.lookups.LookupEntity;
import project.bayaraja.application.services.lookups.dto.request.CreateLookupDto;
import project.bayaraja.application.services.lookups.dto.request.FilterSearchParam;

public interface LookupService {
    LookupEntity createMajorForClass(CreateLookupDto createDto);
    Specification<LookupEntity> findLookupBy(FilterSearchParam filterParam);
}
