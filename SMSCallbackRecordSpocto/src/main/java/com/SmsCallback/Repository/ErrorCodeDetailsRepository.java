package com.SmsCallback.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SmsCallback.Model.ErrorCodeDetails;
import java.util.List;
import java.util.Optional;


@Transactional
public interface ErrorCodeDetailsRepository extends JpaRepository<ErrorCodeDetails, Long>{

	
	@Query(value = "Select * from error_code_details_curefit where smpp_error_code =:smpp_error_code", nativeQuery = true)
	Optional<List<ErrorCodeDetails>> findBySmpp_error_code(String smpp_error_code);
}
