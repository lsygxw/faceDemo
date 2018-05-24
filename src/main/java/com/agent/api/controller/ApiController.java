/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ApiController
 * Author:   apple
 * Date:     2018/5/24 上午11:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.agent.api.controller;

import com.agent.api.util.InternetUtil;
import com.agent.api.util.Key;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author apple
 * @create 2018/5/24
 * @since 1.0.0
 */
@RestController
@RequestMapping("/renlian")
public class ApiController {

    @RequestMapping("/detection")
    @ResponseBody
    public JSONObject api_Detection(String Base) {
        String body = "{\"type\":1 , \"content\": \"" + Base + "\"}";
        // 返回一组Json
        try {
            JSONObject jsonObject = Key.jsonObject(InternetUtil.sendPost(Key.UrlDetect, body, Key.ak_id_Detect, Key.ak_secret_Detect));
            if (jsonObject != null) {
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject api_Verify(String original_Base,String contrastBase){
        // 返回一组对比数据
        String body = "{\"type\" : 1 , \"content_1\" :\"" + original_Base + "\",\"content_2\":\"" + contrastBase + "\"}";
        // 通过post处理 
        try {
            JSONObject jsonObject = Key.jsonObject(InternetUtil.sendPost(Key.UrlVerify, body, Key.ak_id_Verify, Key.ak_secret_Verify));
            if(jsonObject != null) {
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}