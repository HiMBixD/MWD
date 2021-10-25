package tch1904.mwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RoleAccount;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleAccount, Integer> {
    @Query("from RoleAccount r where r.roleId = :role_id")
    Optional<RoleAccount> findByRoleId(@Param("role_id") Integer roleId);
}
