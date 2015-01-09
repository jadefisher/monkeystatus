package net.jadefisher.monkeystatus.respository;

import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorHistoryRepository extends
		PagingAndSortingRepository<MonitorRecording, String> {

	Page<MonitorRecording> findByServiceKey(String serviceKey, Pageable pagable);

	Page<MonitorRecording> findByMonitorKey(String monitorKey, Pageable pagable);

	MonitorRecording findOneByMonitorKey(String monitorKey, Sort sort);
}
