package com.shopme.admin.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.RoleRepository;
import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Toàn quyền hệ thống");
		Role saveRole = repo.save(roleAdmin);
		
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRole() {
		Role roleSalesperson = new Role("Bán hàng", "quản lý giá sản phẩm,"
				+ " khách hàng, giao hàng, đơn đặt hàng, báo cáo bán hàng");
		
		Role roleEditor = new Role("Biên tập", "quản lý loại hàng,"
				+ " thương hiệu, bài viết, menu");
		
		Role roleAssistant = new Role("Chăm sóc khách hàng", "quản lý giá thảo luận"
				+ " và đánh giá");
		
		Role roleShipper = new Role("Giao hàng", "xem sản phẩm,"
				+ " đơn đặt hàng và cập nhật trạng thái đơn hàng");
		
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleAssistant, roleShipper));
	}
}
