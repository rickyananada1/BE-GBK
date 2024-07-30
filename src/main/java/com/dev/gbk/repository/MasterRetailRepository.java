package com.dev.gbk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.dev.gbk.model.MasterRetail;

public interface MasterRetailRepository
                extends JpaRepository<MasterRetail, Long>, JpaSpecificationExecutor<MasterRetail> {
        List<MasterRetail> findAllByArea(String area);

        @Query("SELECT r FROM MasterRetail r WHERE r.tenant_name = ?1")
        Optional<MasterRetail> findByTenantName(String name);

        @Query("SELECT r FROM MasterRetail r WHERE r.tenant_number = ?1")
        Optional<MasterRetail> findByTenantNumber(String number);
}
