package project.bayaraja.application.services.lookups;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.bayaraja.application.services.lookups.dto.request.CreateLookupDto;
import project.bayaraja.application.services.lookups.dto.request.FilterSearchParam;
import project.bayaraja.application.services.lookups.interfaces.LookupRepository;
import project.bayaraja.application.services.lookups.interfaces.LookupService;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class LookupServiceImpl implements LookupService {
    private final LookupRepository lookupRepository;
    private final ModelMapper modelMapper;

    @Override @Transactional
    public LookupEntity createMajorForClass(CreateLookupDto createDto) {
        var mappedToEntity = this.modelMapper.map(createDto, LookupEntity.class);

        return this.lookupRepository.save(mappedToEntity);
    }

    @Override
    public Specification<LookupEntity> findLookupBy(FilterSearchParam filterParam) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterParam.getType() != null && !filterParam.getType().isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("type"), filterParam.getType()));
            }

            if (filterParam.getKey() != null && !filterParam.getKey().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("key"), filterParam.getKey()));
            }

            if (filterParam.getValue() != null && !filterParam.getValue().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("value"), filterParam.getValue()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
