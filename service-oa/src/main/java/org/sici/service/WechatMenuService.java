package org.sici.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.wechat.Menu;
import org.sici.vo.wechat.MenuVo;
import java.util.List;

/**
* @author 20148
* @description 针对表【wechat_menu(菜单)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface WechatMenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
