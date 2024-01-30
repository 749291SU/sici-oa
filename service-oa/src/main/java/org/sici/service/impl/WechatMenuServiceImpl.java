package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.service.WechatMenuService;
import org.sici.mapper.WechatMenuMapper;
import org.springframework.stereotype.Service;
import org.sici.model.wechat.Menu;

/**
* @author 20148
* @description 针对表【wechat_menu(菜单)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class WechatMenuServiceImpl extends ServiceImpl<WechatMenuMapper, Menu>
    implements WechatMenuService{

}




