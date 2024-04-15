package org.sici.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.sici.service.WechatMenuService;
import org.sici.mapper.WechatMenuMapper;
import org.sici.util.MenuHelper;
import org.sici.vo.wechat.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sici.model.wechat.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 20148
 * @description 针对表【wechat_menu(菜单)】的数据库操作Service实现
 * @createDate 2024-01-30 19:45:08
 */
@Service
public class WechatMenuServiceImpl extends ServiceImpl<WechatMenuMapper, Menu>
        implements WechatMenuService {
    @Autowired
    private WxMpService wxMpService;
    @Override
    public List<MenuVo> findMenuInfo() {
        List<Menu> menus = this.list();
        ArrayList<MenuVo> menuVos = new ArrayList<>();
        for (Menu menu : menus) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            menuVos.add(menuVo);
        }

        List<MenuVo> trees = MenuHelper.buildTreeOfMenuVo(menuVos);
        return trees;
    }

    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if (CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://sicioa.top:9090/#" + oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if (twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://sicioa.top:9090#" + twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        // 推送菜单
        try {
            wxMpService.getMenuService().menuCreate(button.toString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
