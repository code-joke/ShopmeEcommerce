package poly.shopme.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.shopme.common.entity.OrderTrack;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {

}
