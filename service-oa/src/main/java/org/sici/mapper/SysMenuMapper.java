package org.sici.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.sici.model.system.SysMenu;

import java.util.List;

/**
* @author 20148
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2024-01-30 19:45:08
* @Entity org.sici.pojo.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findUserMenuListByUserId(@Param(value = "userId")Long userId);
}




