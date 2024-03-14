package org.sici;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.sici.mapper.SysUserMapper;
import org.sici.model.system.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici
 * @className: TestMapper
 * @author: 749291
 * @description: TODO
 * @date: 1/30/2024 8:03 PM
 * @version: 1.0
 */

@SpringBootTest
public class TestMapper {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Test
    public void test() {
        List<SysUser> sysUsers = sysUserMapper.selectList(null);
        System.out.println(sysUsers);
        System.out.println(sqlSessionTemplate);
    }
}
