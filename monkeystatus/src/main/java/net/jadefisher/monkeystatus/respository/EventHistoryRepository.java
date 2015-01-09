package net.jadefisher.monkeystatus.respository;

import net.jadefisher.monkeystatus.model.service.ServiceEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventHistoryRepository extends
		PagingAndSortingRepository<ServiceEvent, String> {
	Page<ServiceEvent> findByServiceKey(String serviceKey, Pageable pageable);
}
